package com.dotdevs.tellus.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dotdevs.tellus.R;
import com.dotdevs.tellus.model.People;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MissingPeopleDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

    @BindView(R.id.btCall)
    Button btCall;

    @BindView(R.id.imageSlider)
    CarouselView imageSlider;

    private FirebaseFirestore mFirestore;
    private String uid;
    private People model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_people_detail);
        ButterKnife.bind(this);

        mFirestore = FirebaseFirestore.getInstance();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Orang Hilang");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        getData();

        btReport.setOnClickListener(this);
        btFounded.setOnClickListener(this);
        btSeeDetail.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btCall.setOnClickListener(this);
    }

    private void getData() {
        model = getIntent().getParcelableExtra("people");
        assert model != null;
        boolean isReported = model.isReported();
        boolean isFound = model.isFound();
        uid = model.getUid();

        txtName.setText(model.getName());
        txtAge.setText(model.getAge());
        txtAddress.setText(model.getAddress());
        txtDescription.setText(model.getDescription());
        txtGender.setText(model.getGender());
        txtLastLocation.setText(model.getLastLocation());

        imageSlider.setPageCount(model.getImagesUrl().size());
        imageSlider.setImageListener(imageListener);
        imageSlider.setImageClickListener(position -> {
            Intent intent = new Intent(this, ImageDetailActivity.class);
            intent.putExtra("image", model.getImagesUrl().get(position));
            startActivity(intent);
        });

        if (model.getUidReporter().equals(FirebaseAuth.getInstance().getUid())) {
            btReport.setVisibility(View.GONE);
            btCancel.setVisibility(View.VISIBLE);
            btCall.setVisibility(View.GONE);

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

    private ImageListener imageListener = (position, imageView) -> {
        StorageReference mStorageRef =
                FirebaseStorage.getInstance()
                        .getReferenceFromUrl(model.getImagesUrl().get(position).getLocation());

        Glide.with(this).load(mStorageRef).into(imageView);
    };

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
            case R.id.btCall: {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("tel:" + model.getPhoneNumberReporter()));
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!FirebaseAuth.getInstance().getUid().equals(uid)) {
            menu.findItem(R.id.menu_maps).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maps_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_maps) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(
                    "https://www.google.com/maps/search/?api=1&query=" + model.getLatitude() + "," +
                            model.getLongitude()));

            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
