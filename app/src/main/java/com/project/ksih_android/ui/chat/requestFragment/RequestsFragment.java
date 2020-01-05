package com.project.ksih_android.ui.chat.requestFragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.ui.chat.models.Requests;

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
        view = inflater.inflate(R.layout.fragment_requests, container, false);

        request_list = view.findViewById(R.id.requestList);
        request_list.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        user_UId = mAuth.getCurrentUser().getUid();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("friend_requests").child(user_UId);

        friendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("friends");
        friendReqDatabaseReference = FirebaseDatabase.getInstance().getReference().child("friend_requests");

        //initializing layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        request_list.setHasFixedSize(true);
        request_list.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Requests> recyclerOptions = new FirebaseRecyclerOptions.Builder<Requests>()
                .setQuery(databaseReference, Requests.class)
                .build();
    }

    public static class RequestsVH extends RecyclerView.ViewHolder{
        TextView name, status;
        RoundedImageView user_photo;
        ImageView re_icon, se_icon, verified_icon;

        public RequestsVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.r_profileName);
            user_photo = itemView.findViewById(R.id.r_profileImage);
            status = itemView.findViewById(R.id.r_profileStatus);
            re_icon = itemView.findViewById(R.id.receivedIcon);
            se_icon = itemView.findViewById(R.id.sentIcon);
            verified_icon = itemView.findViewById(R.id.verifiedIcon);

        }
    }
}
