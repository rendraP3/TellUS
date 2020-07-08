package com.dotdevs.tellus.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dotdevs.tellus.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener {

    @BindView(R.id.txtEmailAddress)
    TextInputEditText txtEmailAddress;

    @BindView(R.id.txtPassword)
    TextInputEditText txtPassword;

    @BindView(R.id.txtEmailAddressWrapper)
    TextInputLayout txtEmailAddressWrapper;

    @BindView(R.id.txtPasswordWrapper)
    TextInputLayout txtPasswordWrapper;

    @BindView(R.id.btForgotPassword)
    Button btForgotPassword;

    @BindView(R.id.btRegister)
    Button btRegister;

    @BindView(R.id.btLogin)
    Button btLogin;

    @BindView(R.id.progressBarWrapper)
    View progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        btForgotPassword.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        btRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btLogin: {
                if (checkInput()){
                    login();
                }
            }
            break;
            case R.id.btForgotPassword: {

            }
            break;
            case R.id.btRegister: {
                startActivity(new Intent(this, RegisterActivity.class));
            }
            break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void login(){
        String email = Objects.requireNonNull(txtEmailAddress.getText()).toString();
        String password = Objects.requireNonNull(txtPassword.getText()).toString();

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
           if (task.isSuccessful()){
               Toast.makeText(this, "Selamat datang, " + Objects
                               .requireNonNull(mAuth.getCurrentUser()).getDisplayName(),
                       Toast.LENGTH_SHORT).show();

               startActivity(new Intent(this, MainActivity.class));
           }else{
               Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
               Log.e(this.getPackageName(), task.getException().toString());
           }
        });
    }

    private boolean checkInput(){
        if (txtEmailAddress.getText() == null || txtEmailAddress.getText().toString().isEmpty()){
            txtEmailAddress.setError("Alamat email tidak boleh kosong");
            return false;
        }

        if (txtPassword.getText() == null || txtPassword.getText().toString().isEmpty()){
            txtPassword.setError("Password tidak boleh kosong");
            return false;
        }

        return true;
    }
}
