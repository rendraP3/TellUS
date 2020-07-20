package com.dotdevs.tellus.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dotdevs.tellus.R;
import com.dotdevs.tellus.adapter.MissingPeopleListAdapter;
import com.dotdevs.tellus.model.People;
import com.dotdevs.tellus.view.AddNewPeopleActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MissingListFragment extends Fragment {

    @BindView(R.id.missingPeopleList)
    RecyclerView missingList;

    @BindView(R.id.addNewButton)
    FloatingActionButton addNewPeople;

    public MissingListFragment() {
        // Required empty public constructor
    }

    private FirebaseFirestore mFirestore;
    private MissingPeopleListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_missing_list, container, false);
        ButterKnife.bind(this, view);

        mFirestore = FirebaseFirestore.getInstance();

        addNewPeople.setOnClickListener(v -> {
            startActivity(new Intent(this.getContext(), AddNewPeopleActivity.class));
        });

        missingList.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL,
                false));

        getAllMissingPeople();

        return view;
    }

    private void getAllMissingPeople() {
        final Query query =
                mFirestore.collection("missing")
                        .whereEqualTo("found", false)
                        .whereEqualTo("active", true)
                        .whereEqualTo("verify", true).orderBy("timeStamp"
                        , Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<People> options =
                new FirestoreRecyclerOptions.Builder<People>().setQuery(query, People.class)
                        .build();

        mAdapter = new MissingPeopleListAdapter(options, this.getContext());

        missingList.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }
}
