package com.dotdevs.tellus.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dotdevs.tellus.R;
import com.dotdevs.tellus.model.MissingImage;
import com.dotdevs.tellus.model.People;
import com.dotdevs.tellus.view.MissingPeopleDetailActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MissingPeopleListAdapter extends FirestoreRecyclerAdapter<People,
        MissingPeopleListAdapter.ViewHolder> {

    private Context context;

    public MissingPeopleListAdapter(@NonNull FirestoreRecyclerOptions<People> options,
                                    Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position,
                                    @NonNull People model) {
        holder.name.setText(model.getName());
        holder.address.setText(model.getAddress());

        StorageReference mStorageRef =
                FirebaseStorage.getInstance().getReferenceFromUrl(model.getImagesUrl().get(0).getLocation());

        Glide.with(context).load(mStorageRef).into(holder.image);

        SimpleDateFormat formatFrom = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat formatTo = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        try {
            Date dateFrom = formatFrom.parse(model.getDate());
            assert dateFrom != null;
            holder.date.setText(formatTo.format(dateFrom));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.wrapper.setOnClickListener(v -> {
            Intent intent = new Intent(context, MissingPeopleDetailActivity.class);

            intent.putExtra("people", model);

            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.missing_people_list_item,
                parent, false);

        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.peopleImage)
        ImageView image;

        @BindView(R.id.peopleName)
        TextView name;

        @BindView(R.id.missingDate)
        TextView date;

        @BindView(R.id.peopleAddress)
        TextView address;

        @BindView(R.id.wrapper)
                View wrapper;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
