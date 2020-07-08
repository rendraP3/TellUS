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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.dotdevs.tellus.R;
import com.dotdevs.tellus.model.People;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

    @BindView(R.id.ivImage)
    ImageView ivImage;

    @BindView(R.id.txtName)
    EditText txtName;

    @BindView(R.id.txtAge)
    EditText txtAge;

    @BindView(R.id.spinnerGender)
    Spinner spGender;

    @BindView(R.id.txtReligion)
    EditText txtReligion;

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

    private Uri imageUri;
    private String gender;

    private FirebaseFirestore mFirestore;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_people);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("Form Data Orang Hilang");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(v -> finish());

        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        ivImage.setOnClickListener(this);
        btSave.setOnClickListener(this);
        spGender.setOnItemSelectedListener(this);
        
        setupSpinner();
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
            case R.id.ivImage : {
                ImagePicker.Companion
                        .with(this)
                        .compress(1024)
                        .galleryOnly()
                        .start();
            }
            break;
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
        String religion = txtReligion.getText().toString();
        String address = txtAddress.getText().toString();
        String lastLocation = txtLastLocation.getText().toString();
        String description = txtDescription.getText().toString();

        final StorageReference mStorageRef =
                mStorage.getReference().child("people").child(name + ".jpg");

        UploadTask uploadTask = mStorageRef.putFile(imageUri);

        progressBar.setVisibility(View.VISIBLE);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw new Exception();
            }

            return mStorageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String imageUrl = Objects.requireNonNull(task.getResult()).toString();

                final DocumentReference mDocRef = mFirestore.collection("missing").document();

                final People people = new People(
                        mDocRef.getId(),
                        mAuth.getUid(),
                        name,
                        age,
                        gender,
                        religion,
                        address,
                        lastLocation,
                        description,
                        imageUrl,
                        new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()),
                        false,
                        true,
                        false,
                        Timestamp.now()
                );

                mDocRef.set(people).addOnCompleteListener(upload -> {
                    progressBar.setVisibility(View.GONE);
                    if (upload.isSuccessful()) {
                        Toast.makeText(this, "Data berhasil ditambah", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            assert data != null;
            imageUri = data.getData();

            ivImage.setImageURI(data.getData());
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

        if (txtReligion.getText() == null || txtReligion.getText().toString().isEmpty()) {
            txtReligion.setError("Agama tidak boleh kosong");

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

        if (imageUri == null) {
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
