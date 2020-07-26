package com.dotdevs.tellus.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dotdevs.tellus.R;
import com.dotdevs.tellus.adapter.MissingPeopleListAdapter;
import com.dotdevs.tellus.model.People;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WaitingListFragment extends Fragment {

    @BindView(R.id.waitingList)
    RecyclerView waitingList;

    public WaitingListFragment() {
        // Required empty public constructor
    }

    private FirebaseFirestore mFirestore;
    private MissingPeopleListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_list, container, false);
        ButterKnife.bind(this, view);

        mFirestore = FirebaseFirestore.getInstance();

        waitingList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        getWaitingList();

        return view;
    }

    // Mengambil semua laporan yang belum diverifikasi oleh admin
    private void getWaitingList() {

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Query query = mFirestore.collection("missing")
                .whereEqualTo("uidReporter", mAuth.getUid())
                .whereEqualTo("found", false)
                .whereEqualTo("active", true)
                .whereEqualTo("verify", false);

        FirestoreRecyclerOptions<People> options =
                new FirestoreRecyclerOptions.Builder<People>().setQuery(query, People.class)
                        .build();

        mAdapter = new MissingPeopleListAdapter(options, getContext());

        waitingList.setAdapter(mAdapter);

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