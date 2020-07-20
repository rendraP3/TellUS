package com.dotdevs.tellus.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.dotdevs.tellus.R;
import com.dotdevs.tellus.model.MissingImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jsibbold.zoomage.ZoomageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.peopleImage)
    ZoomageView zvImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        MissingImage missingImage = getIntent().getParcelableExtra("image");

        StorageReference mStorageRef =
                FirebaseStorage.getInstance().getReferenceFromUrl(missingImage.getLocation());

        Glide.with(this).load(mStorageRef).into(zvImage);
    }
}
