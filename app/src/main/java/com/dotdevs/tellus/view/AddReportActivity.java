package com.dotdevs.tellus.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dotdevs.tellus.R;
import com.dotdevs.tellus.model.Report;
import com.dotdevs.tellus.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddReportActivity extends AppCompatActivity {

    private final int REQUEST_CODE_AUTOCOMPLETE = 101;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.reportDescription)
    EditText reportDescription;

    @BindView(R.id.reportLocation)
    EditText reportLocation;

    @BindView(R.id.btReport)
    Button btReport;

    @BindView(R.id.progressBarWrapper)
    View proggressBar;

    private FirebaseFirestore mFirestore;
    private double latitude, longitude;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_token));
        setContentView(R.layout.activity_add_report);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v->finish());
        toolbar.setTitle("Buat Laporan");
        toolbar.setTitleTextColor(Color.WHITE);

        mFirestore = FirebaseFirestore.getInstance();

        reportLocation.setOnClickListener(v -> {
            Intent intent = new PlacePicker.IntentBuilder()
                    .accessToken(getString(R.string.mapbox_token))
                    .placeOptions(PlacePickerOptions.builder()
                            .statingCameraPosition(new CameraPosition.Builder()
                                    .target(new LatLng(-5.407355, 105.270548)).zoom(14).build())
                            .build())
                    .build(this);

            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        });

        btReport.setOnClickListener(v ->{
            if (checkInput()) {
                addReport();
            }
        });

        getUserDetail();
    }


    private void getUserDetail() {
        mFirestore.collection("users").document(
                FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    user = snapshot.toObject(User.class);
                }
            }
        });
    }

    private void addReport() {
        DocumentReference mDocRef =
                mFirestore.collection("missing").document(getIntent().getStringExtra("uid")).collection(
                        "report").document(getIntent().getStringExtra("uid"));

        final Report report = new Report(
                mDocRef.getId(),
                reportDescription.getText().toString(),
                reportLocation.getText().toString(),
                latitude,
                longitude,
                user
        );

        proggressBar.setVisibility(View.VISIBLE);
        mDocRef.set(report).addOnCompleteListener(task -> {
            proggressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                mFirestore.collection("missing").document(getIntent().getStringExtra("uid")).update("reported", true).addOnCompleteListener(update -> {
                    if (update.isSuccessful()) {
                        Toast.makeText(this, "Laporan anda berhasil dibuat", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkInput() {
        if (reportDescription.getText() == null ||
                reportDescription.getText().toString().isEmpty()) {

            reportDescription.setError("Deskripsi tidak boleh kosong");

            return false;
        }

        if (reportLocation.getText() == null || reportLocation.getText().toString().isEmpty()) {
            reportLocation.setError("Lokasi tidak boleh kosong");

            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature feature = PlacePicker.getPlace(data);

            reportLocation.setText(feature.placeName());
            latitude = feature.center().latitude();
            longitude = feature.center().longitude();
        }
    }
}
