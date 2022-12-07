package com.projectmanager.teamup.Fragments_Screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.projectmanager.teamup.Activity_Screen.CardView;
import com.projectmanager.teamup.Adapter.CardAdapter;
import com.projectmanager.teamup.Modal.CardModal;
import com.projectmanager.teamup.R;

import java.util.ArrayList;
import java.util.List;


public class HomePageFragment extends Fragment {

    private FirebaseAuth mAuth;
    Button btnCreate;
    SearchView idSearch;
    private DatabaseReference mIncomeDatabase;
    FloatingActionButton floatingActionButton;
    private CardAdapter adapter;
    private ArrayList<CardModal> arrayList;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    TextView textView;

    public HomePageFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        if(mAuth!=null) {
            String uid = mUser.getUid();

            mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        }

        idSearch = view.findViewById(R.id.idSearch);
        idSearch.clearFocus();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.loading);
        floatingActionButton = view.findViewById(R.id.btnCreate);
        arrayList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.container);
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();

//            list.add(new CardModal("C++ Language"));
//            list.add(new CardModal("C++ Language"));
//            list.add(new CardModal("C++ Language"));
//            list.add(new CardModal("C++ Language"));


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //creating recyclerview adapter
        adapter = new CardAdapter(arrayList, getContext());
        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        db.collection("TeamUp").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.VISIBLE);
                if (!queryDocumentSnapshots.isEmpty()) {
//                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        // after getting this list we are passing
                        // that list to our object class.
                        CardModal c = d.toObject(CardModal.class);
//                        CardModal c = d.toObject(CardModal.class);

                        // and we will pass this object class
                        // inside our arraylist which we have
                        // created for recycler view.
                        arrayList.add(c);
                        progressBar.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "no Data Found in Database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = new CreateProjectFragment();
                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.container, f).commit();
            }
        });
        idSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText);
                return true;
            }
        });
        return view;
    }

    private void filterList(String newText) {
        ArrayList<CardModal> list = new ArrayList<>();
        for (CardModal cardModal : arrayList) {
            if (cardModal.getTVTitle().toLowerCase().contains(newText.toLowerCase())) {
                list.add(cardModal);

            }

        }
        if (list.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearch(list);
        }
    }
}