package com.dotdevs.tellus.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.dotdevs.tellus.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txtEmailAddress)
    TextInputEditText txtEmailAddress;

    @BindView(R.id.btSendResetPassword)
    Button btSend;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        mAuth = FirebaseAuth.getInstance();

        btSend.setOnClickListener(v -> {
            if (txtEmailAddress.getText() != null ||
                    !txtEmailAddress.getText().toString().isEmpty()) {

                mAuth.sendPasswordResetEmail(txtEmailAddress.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this,
                                "Kami sudah mengirim email untuk mereset password anda",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                txtEmailAddress.setError("Email tidak boleh kosong");
            }
        });
    }
}
