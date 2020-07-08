package com.dotdevs.tellus.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dotdevs.tellus.R;
import com.dotdevs.tellus.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txtFullName)
    TextInputEditText txtFullName;

    @BindView(R.id.txtIdNumber)
    TextInputEditText txtIdNumber;

    @BindView(R.id.txtPhoneNumber)
    TextInputEditText txtPhoneNumber;

    @BindView(R.id.txtEmailAddress)
    TextInputEditText txtEmailAddress;

    @BindView(R.id.txtPassword)
    TextInputEditText txtPassword;

    @BindView(R.id.btRegister)
    Button btRegister;

    @BindView(R.id.progressBarWrapper)
    View progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseDatabase mFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mFirebase = FirebaseDatabase.getInstance();

        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        btRegister.setOnClickListener(v -> {
            if (checkInput()){
                register();
            }
        });
    }

    private void register() {
        String fullName = Objects.requireNonNull(txtFullName.getText()).toString();
        String idNumber = Objects.requireNonNull(txtIdNumber.getText()).toString();
        String phoneNumber = Objects.requireNonNull(txtPhoneNumber.getText()).toString();
        String emailAddress = Objects.requireNonNull(txtEmailAddress.getText()).toString();
        String password = Objects.requireNonNull(txtPassword.getText()).toString();

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build();

                User mUser = new User(
                        mAuth.getUid(),
                        fullName,
                        idNumber,
                        phoneNumber,
                        emailAddress
                );

                Objects.requireNonNull(mAuth.getCurrentUser())
                        .updateProfile(changeRequest).addOnSuccessListener(onSuccess -> {
                    mFirestore.collection("users").document(Objects.requireNonNull(mAuth.getUid())).set(mUser).addOnCompleteListener(upload -> {
                        if (upload.isSuccessful()){
                            mFirebase.getReference("users").child(mAuth.getUid()).child(
                                    "id_number").setValue(idNumber).addOnCompleteListener(uploadId -> {

                                progressBar.setVisibility(View.GONE);
                                if (uploadId.isSuccessful()){

                                    Toast.makeText(this, "Akun anda berhasil dibuat", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, MainActivity.class));
                                }else{
                                    Toast.makeText(this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                });
            }
        });
    }

    private boolean checkInput(){
        if(txtFullName.getText() == null || txtFullName.getText().toString().isEmpty()){
            txtFullName.setError("Nama tidak boleh kosong");

            return false;
        }

        if (txtIdNumber.getText() == null || txtIdNumber.getText().toString().isEmpty()){
            txtIdNumber.setError("NIK tidak boleh kosong");

            return false;
        }

        if (txtPhoneNumber.getText() == null || txtPhoneNumber.getText().toString().isEmpty()) {
            txtPhoneNumber.setError("Nomor telepon tidak boleh kosong");

            return false;
        }

        if (txtEmailAddress.getText() == null || txtEmailAddress.getText().toString().isEmpty()) {
            txtEmailAddress.setError("Alamat email tidak boleh kosong");

            return false;
        }

        if (txtPassword.getText() == null || txtPassword.getText().toString().isEmpty()){
            txtPassword.setError("Password tidak boleh kosong");

            return false;
        }

        if (txtIdNumber.getText().length() < 16){
            txtIdNumber.setError("NIK tidak boleh kurang dari 16 digit");

            return false;
        }

        return true;
    }
}
