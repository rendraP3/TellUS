package com.dotdevs.tellus.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dotdevs.tellus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MissingPeopleDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.peopleImage)
    ImageView ivImage;

    @BindView(R.id.peopleName)
    TextView txtName;

    @BindView(R.id.peopleAge)
    TextView txtAge;

    @BindView(R.id.peopleAddress)
    TextView txtAddress;

    @BindView(R.id.peopleGender)
    TextView txtGender;

    @BindView(R.id.peopleDescription)
    TextView txtDescription;

    @BindView(R.id.peopleReligion)
    TextView txtReligion;

    @BindView(R.id.peopleLastLocation)
    TextView txtLastLocation;

    @BindView(R.id.btReport)
    Button btReport;

    @BindView(R.id.btSeeDetail)
    Button btSeeDetail;

    @BindView(R.id.btFounded)
    Button btFounded;

    @BindView(R.id.btCancel)
    Button btCancel;

    private FirebaseFirestore mFirestore;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_people_detail);
        ButterKnife.bind(this);

        mFirestore = FirebaseFirestore.getInstance();

        toolbar.setTitle("Detail Orang Hilang");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        getData();

        btReport.setOnClickListener(this);
        btFounded.setOnClickListener(this);
        btSeeDetail.setOnClickListener(this);
        btCancel.setOnClickListener(this);
    }

    private void getData() {
        Intent intent = getIntent();
        boolean isReported = intent.getBooleanExtra("isReported", false);
        boolean isFound = intent.getBooleanExtra("isFound", false);
        uid = intent.getStringExtra("uid");

        txtName.setText(intent.getStringExtra("name"));
        txtAge.setText(intent.getStringExtra("age"));
        txtAddress.setText(intent.getStringExtra("address"));
        txtDescription.setText(intent.getStringExtra("description"));
        txtGender.setText(intent.getStringExtra("gender"));
        txtReligion.setText(intent.getStringExtra("religion"));
        txtLastLocation.setText(intent.getStringExtra("lastLocation"));

        Glide.with(this).load(intent.getStringExtra("image")).into(ivImage);
        ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ivImage.setOnClickListener(v -> {
            Intent imageDetailIntent = new Intent(this, ImageDetailActivity.class);
            imageDetailIntent.putExtra("image", intent.getStringExtra("image"));

            startActivity(imageDetailIntent);
        });

        if (intent.getStringExtra("uidReporter").equals(FirebaseAuth.getInstance().getUid())) {
            btReport.setVisibility(View.GONE);
            btCancel.setVisibility(View.VISIBLE);

            if (isReported) {
                btCancel.setVisibility(View.GONE);
                btSeeDetail.setVisibility(View.VISIBLE);
                btFounded.setVisibility(View.VISIBLE);
            }

            if (isFound) {
                btCancel.setVisibility(View.GONE);
                btSeeDetail.setVisibility(View.GONE);
                btFounded.setVisibility(View.GONE);
            }
        }

        if (isReported) {
            btReport.setText("Sudah dilaporkan");
            btReport.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btReport : {
                Intent intent = new Intent(this, AddReportActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
            break;
            case R.id.btCancel : {
                mFirestore.collection("missing").document(uid).update("active", false)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Laporan anda dibatalkan", Toast.LENGTH_SHORT)
                                        .show();
                                finish();
                            } else {
                                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
            break;
            case R.id.btSeeDetail : {
                Intent intent = new Intent(this, ReportDetailActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
            break;
            case R.id.btFounded : {
                mFirestore.collection("missing").document(uid).update("found", true)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Laporan anda sudah diupdate",
                                        Toast.LENGTH_SHORT)
                                        .show();

                                finish();
                            } else {
                                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
            break;
        }
    }
}
