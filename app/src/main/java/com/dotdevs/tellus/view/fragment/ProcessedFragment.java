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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProcessedFragment extends Fragment {

    @BindView(R.id.processedList)
    RecyclerView processdList;

    public ProcessedFragment() {
        // Required empty public constructor
    }

    private FirebaseFirestore mFirestore;
    private MissingPeopleListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_processed, container, false);
        ButterKnife.bind(this, view);

        mFirestore = FirebaseFirestore.getInstance();

        processdList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        getProcessedList();

        return view;
    }

    // Fungsi untuk mengambil seluruh laporan yang sedang di proses
    private void getProcessedList() {

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Query query = mFirestore.collection("missing")
                .whereEqualTo("uidReporter", mAuth.getUid())
                .whereEqualTo("found", false)
                .whereEqualTo("active", true)
                .whereEqualTo("verify", true);

        FirestoreRecyclerOptions<People> options =
                new FirestoreRecyclerOptions.Builder<People>().setQuery(query, People.class)
                        .build();

        mAdapter = new MissingPeopleListAdapter(options, getContext());

        processdList.setAdapter(mAdapter);

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
