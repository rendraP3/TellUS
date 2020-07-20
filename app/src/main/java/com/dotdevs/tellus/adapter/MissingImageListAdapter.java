package com.dotdevs.tellus.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dotdevs.tellus.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MissingImageListAdapter extends RecyclerView.Adapter<MissingImageListAdapter.ViewHolder> {

    private static final int FOOTER_VIEW = 1;

    private List<String> imageList;
    private Activity activity;

    public MissingImageListAdapter(List<String> imageList, Activity activity) {
        this.imageList = imageList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.image_list_item, parent,
                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (holder.getItemViewType() != FOOTER_VIEW) {
            holder.imageView.setImageURI(Uri.parse(imageList.get(position)));
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.fabRemove.setVisibility(View.VISIBLE);

            holder.wrapper.setOnClickListener(v -> {
                imageList.remove(position);
                notifyDataSetChanged();
            });

        }else {
            holder.imageView.setImageResource(R.drawable.ic_add_a_photo_black_24dp);

            holder.wrapper.setOnClickListener(v -> ImagePicker.Companion.with(activity)
                    .compress(1024)
                    .start());
        }
    }

    @Override
    public int getItemCount() {
        if (imageList == null) {
            return 0;
        }

        if (imageList.size() == 0) {
            return 1;
        }

        return imageList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == imageList.size()) {
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imageView)
        ImageView imageView;

        @BindView(R.id.wrapper)
        View wrapper;

        @BindView(R.id.fabRemove)
        ImageView fabRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
