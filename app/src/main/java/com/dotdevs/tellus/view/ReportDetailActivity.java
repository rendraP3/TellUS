package com.dotdevs.tellus.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dotdevs.tellus.R;
import com.dotdevs.tellus.model.Report;
import com.dotdevs.tellus.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.reporterName)
    TextView tvName;

    @BindView(R.id.reporterEmail)
    TextView tvEmail;

    @BindView(R.id.reporterPhoneNumber)
    TextView tvPhoneNumber;

    @BindView(R.id.reportDescription)
    TextView tvDescription;

    @BindView(R.id.reportLocation)
    TextView tvLocation;

    @BindView(R.id.btLocation)
    Button btLocation;

    @BindView(R.id.btCall)
    Button btCall;

    private FirebaseFirestore mFirestore;
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        ButterKnife.bind(this);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Detail Laporan");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Inisialisasi Firebase Firestore
        mFirestore = FirebaseFirestore.getInstance();

        btLocation.setOnClickListener(this);
        btCall.setOnClickListener(this);

        getReportDetail();

    }

    // Fungsi untuk mengambil detail dari laporan dari firestore
    private void getReportDetail() {
        String uid = getIntent().getStringExtra("uid");

        mFirestore.collection("missing").document(uid).collection("report").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();

                        if (snapshot.exists()) {
                            report = snapshot.toObject(Report.class);

                            assert report != null;
                            tvName.setText(report.getUser().getFullName());
                            tvEmail.setText(report.getUser().getEmail());
                            tvPhoneNumber.setText(report.getUser().getPhoneNumber());
                            tvDescription.setText(report.getDescription());
                            tvLocation.setText(report.getLocation());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btLocation: {
                // Navigasi dari apps ke google maps menggunakan navigation
                Uri gmmIntentUri = Uri.parse("google.navigation:q="
                        + report.getLatitude() + ","
                        + report.getLongitude());
                Intent navigateIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(navigateIntent);
            }
            break;

            case R.id.btCall: {
                // Untuk menggunakan fungsi call
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + report.getUser().getPhoneNumber()));
                startActivity(intent);
            }
            break;
        }
    }
}
