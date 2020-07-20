package com.dotdevs.tellus.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dotdevs.tellus.R;
import com.dotdevs.tellus.adapter.MissingImageListAdapter;
import com.dotdevs.tellus.model.MissingImage;
import com.dotdevs.tellus.model.People;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewPeopleActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private final int REQUEST_CODE_AUTOCOMPLETE = 101;

    @BindView(R.id.imageList)
    RecyclerView rvImage;

    @BindView(R.id.txtName)
    EditText txtName;

    @BindView(R.id.txtAge)
    EditText txtAge;

    @BindView(R.id.spinnerGender)
    Spinner spGender;

    @BindView(R.id.txtPhoneNumber)
    EditText txtPhoneNumber;

    @BindView(R.id.txtAddress)
    EditText txtAddress;

    @BindView(R.id.txtLastLocation)
    EditText txtLastLocation;

    @BindView(R.id.txtDescription)
    EditText txtDescription;

    @BindView(R.id.btSave)
    Button btSave;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressBarWrapper)
    View progressBar;
    private String gender;

    private FirebaseFirestore mFirestore;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;

    private double latitude, longitude;
    private List<String> imageList;
    private List<MissingImage> missingImageList;
    private MissingImageListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_token));
        setContentView(R.layout.activity_add_new_people);
        ButterKnife.bind(this);

        imageList = new ArrayList<>();
        missingImageList = new ArrayList<>();
        adapter = new MissingImageListAdapter(imageList, this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("Form Data Orang Hilang");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(v -> finish());

        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btSave.setOnClickListener(this);
        spGender.setOnItemSelectedListener(this);
        
        setupSpinner();
        setupImageList();

        txtLastLocation.setOnClickListener(v -> {
            Intent intent = new PlacePicker.IntentBuilder()
                    .accessToken(getString(R.string.mapbox_token))
                    .placeOptions(PlacePickerOptions.builder()
                            .statingCameraPosition(new CameraPosition.Builder()
                                    .target(new LatLng(-5.407355, 105.270548)).zoom(14).build())
                            .build())
                    .build(this);

            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        });
    }

    private void setupImageList() {
        rvImage.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
        rvImage.setAdapter(adapter);
    }

    private void setupSpinner() {
        List<String> genderList = new ArrayList<>();
        genderList.add("Laki - Laki");
        genderList.add("Perempuan");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, genderList);

        spGender.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSave : {
                if (checkInput()) {
                    addNewData();
                }
            }
            break;
        }
    }

    private void addNewData(){
        String name = txtName.getText().toString();
        String age = txtAge.getText().toString();
        String phoneNumber = txtPhoneNumber.getText().toString();
        String address = txtAddress.getText().toString();
        String lastLocation = txtLastLocation.getText().toString();
        String description = txtDescription.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        for (int i = 0; i < imageList.size(); i++) {
            Uri imageUri = Uri.parse(imageList.get(i));

            StorageReference mStoreRef = mStorage.getReference().child("people")
                    .child(name + "/" + imageUri.getLastPathSegment());

            UploadTask uploadTask = mStoreRef.putFile(imageUri);

            uploadTask.continueWithTask(uploadImage -> {
                if (!uploadImage.isSuccessful()) {
                    throw Objects.requireNonNull(uploadImage.getException());
                }

                return mStoreRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                }
            });

            missingImageList.add(
                    new MissingImage(
                            imageUri.getLastPathSegment(),
                            "gs://tell-us-cae0f.appspot.com/people/" + name + "/" +
                                    imageUri.getLastPathSegment()
                    )
            );
        }

        if (missingImageList.size() != 0) {
            final DocumentReference mDocRef = mFirestore.collection("missing").document();

            final People people = new People(
                    mDocRef.getId(),
                    mAuth.getUid(),
                    name,
                    age,
                    gender,
                    phoneNumber,
                    address,
                    lastLocation,
                    description,
                    missingImageList,
                    new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()),
                    false,
                    true,
                    false,
                    false,
                    latitude,
                    longitude,
                    Timestamp.now()
            );

            mDocRef.set(people).addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Laporan berhasil dibuat", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature feature = PlacePicker.getPlace(data);

            txtLastLocation.setText(feature.placeName());
            latitude = feature.center().latitude();
            longitude = feature.center().longitude();
        }

        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            assert data != null;
            imageList.add(data.getData().toString());

            adapter.notifyDataSetChanged();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkInput(){
        if (txtName.getText() == null || txtName.getText().toString().isEmpty()) {
            txtName.setError("Nama tidak boleh kosong");

            return false;
        }

        if (txtAge.getText() == null || txtAge.getText().toString().isEmpty()) {
            txtAge.setError("Umur tidak boleh kosong");

            return false;
        }

        if (txtPhoneNumber.getText() == null || txtPhoneNumber.getText().toString().isEmpty()) {
            txtPhoneNumber.setError("Agama tidak boleh kosong");

            return false;
        }

        if (txtAddress.getText() == null || txtAddress.getText().toString().isEmpty()) {
            txtAddress.setError("Alamat tidak boleh kosong");

            return false;
        }

        if (txtLastLocation.getText() == null || txtLastLocation.getText().toString().isEmpty()) {
            txtLastLocation.setError("Lokasi terakhir tidak boleh kosong");

            return false;
        }

        if (txtDescription.getText() == null || txtDescription.getText().toString().isEmpty()) {
            txtDescription.setError("Deskripsi tidak boleh kosong");

            return false;
        }

        if (imageList.isEmpty()) {
            Toast.makeText(this, "Gambar tidak boleh kosong", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (gender.isEmpty()) {
            Toast.makeText(this, "Jenis Kelamin tidak boleh kosong", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gender = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
