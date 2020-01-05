package com.project.ksih_android.ui.chat.requestFragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.project.ksih_android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {
    //initialize variables
    private View view;
    private RecyclerView request_list;
    private Context context;

    //firebase
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    String user_UId;
    private DatabaseReference userDatabaseReference;

    //for accept and cancel request
    private DatabaseReference friendsDatabaseReference;
    private DatabaseReference friendReqDatabaseReference;


    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

}
