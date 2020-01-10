package com.project.ksih_android.ui.chat.requestFragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.ui.chat.models.Requests;
import com.project.ksih_android.ui.chat.profile.ProfileFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

        //setup firebase recycler Adapter
        FirebaseRecyclerAdapter<Requests, RequestsVH> adapter = new FirebaseRecyclerAdapter<Requests, RequestsVH>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull RequestsVH holder, int position, @NonNull Requests model) {
               final String userID = getRef(position).getKey();
               //handling accept/cancel button
                DatabaseReference getTypeReference = getRef(position).child("request_type").getRef();
                getTypeReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String requestType = dataSnapshot.getValue().toString();
                            holder.verified_icon.setVisibility(View.GONE);

                            if (requestType.equals("received")){
                                holder.re_icon.setVisibility(View.VISIBLE);
                                holder.se_icon.setVisibility(View.GONE);
                                userDatabaseReference.child(userID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final String userName = dataSnapshot.child("user_name").getValue().toString();
                                        final String userVerified = dataSnapshot.child("verified").getValue().toString();
                                        final String userThumbPhoto = dataSnapshot.child("user_thumb_image").getValue().toString();
                                        final String user_status = dataSnapshot.child("user_status").getValue().toString();

                                        holder.name.setText(userName);
                                        holder.status.setText(user_status);

                                        // set image conditions
                                        if (!userThumbPhoto.equals("default_image")){
                                            Picasso.get()
                                                    .load(userThumbPhoto)
                                                    .networkPolicy(NetworkPolicy.OFFLINE)
                                                    .placeholder(R.drawable.default_profile_image)
                                                    .into(holder.user_photo, new Callback() {
                                                        @Override
                                                        public void onSuccess() {

                                                        }

                                                        @Override
                                                        public void onError(Exception e) {
                                                            Picasso.get()
                                                                    .load(userThumbPhoto)
                                                                    .placeholder(R.drawable.default_profile_image)
                                                                    .into(holder.user_photo);
                                                        }
                                                    });
                                        }

                                        if (userVerified.contains("true")){
                                            holder.verified_icon.setVisibility(View.VISIBLE);
                                        }

                                        holder.itemView.setOnClickListener(view -> {
                                            CharSequence options[] = new CharSequence[]{
                                              "Accept Request", "Cancel Request", userName+"'s profile"
                                            };
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {

                                                    if (which == 0){
                                                        Calendar myCalender = Calendar.getInstance();
                                                        SimpleDateFormat currentDate = new SimpleDateFormat("EEEE, dd MMM, yyyy");
                                                        final String friendshipDate = currentDate.format(myCalender.getTime());

                                                        friendsDatabaseReference.child(user_UId).child("date").setValue(friendshipDate)
                                                                .addOnCompleteListener(task -> {

                                                                    //check node and delete after friend request is added
                                                                    friendReqDatabaseReference.child(user_UId).child(userID).removeValue()
                                                                            .addOnCompleteListener(task12 -> {
                                                                                if (task12.isSuccessful()){
                                                                                    // delete from users friend_request node, receiver >> sender >> values
                                                                                    friendReqDatabaseReference.child(userID).child(user_UId).removeValue()
                                                                                            .addOnCompleteListener(task1 -> {
                                                                                                if (task1.isSuccessful()){
                                                                                                    //after deleting data
                                                                                                    Snackbar snackbar = Snackbar
                                                                                                            .make(view, "You are now friends", Snackbar.LENGTH_LONG);
                                                                                                    //customizing snackbar
                                                                                                    View sView = snackbar.getView();
                                                                                                    sView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_color_disabled));
                                                                                                    snackbar.setTextColor(Color.WHITE);
                                                                                                    snackbar.show();
                                                                                                }
                                                                                            });
                                                                                }
                                                                            });
                                                                });
                                                    }

                                                    //implement second condition
                                                    if (which == 1){
                                                        //for cancellation, delete data from user nodes
                                                        //delete from, sender >> receiver >. values
                                                        friendReqDatabaseReference.child(user_UId).child(userID).removeValue()
                                                                .addOnCompleteListener(task -> {
                                                                    if (task.isSuccessful()){
                                                                        //delete from receiver >> sender >> values
                                                                        friendReqDatabaseReference.child(userID).child(user_UId).removeValue()
                                                                                .addOnCompleteListener(task13 -> {
                                                                                    if (task13.isSuccessful()){
                                                                                        Snackbar snackbar = Snackbar
                                                                                                .make(view, "Cancel Request", Snackbar.LENGTH_LONG);
                                                                                        //customizing snackbar
                                                                                        View sView = snackbar.getView();
                                                                                        sView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_color_disabled));
                                                                                        snackbar.setTextColor(Color.WHITE);
                                                                                        snackbar.show();
                                                                                    }
                                                                                });
                                                                    }
                                                                });
                                                    }
                                                    if (which == 2){
                                                        /**
                                                         * @Todo add intent to profile
                                                         */
                                                        //on friend request accepted send intent to profile
                                                        ProfileFragment profileFragment = new ProfileFragment();
                                                        Bundle args = new Bundle();
                                                        args.putString("visitUserId", userID);
                                                        profileFragment.setArguments(args);
                                                        Navigation.findNavController(view).navigate(R.id.profile_settings, args);
                                                    }
                                                }
                                            });
                                            builder.show();
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            if (requestType.equals("sent")){
                                holder.re_icon.setVisibility(View.GONE);
                                holder.se_icon.setVisibility(View.VISIBLE);
                                userDatabaseReference.child(userID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final String userName = dataSnapshot.child("user_name").getValue().toString();
                                        final String userVerified = dataSnapshot.child("verified").getValue().toString();
                                        final String userThumbPhoto = dataSnapshot.child("user_thumb_image").getValue().toString();
                                        final String user_status = dataSnapshot.child("user_status").getValue().toString();

                                        holder.name.setText(userName);
                                        holder.status.setText(user_status);

                                        //load user photo
                                        if (!userThumbPhoto.equals("default_image")){
                                            Picasso.get()
                                                    .load(userThumbPhoto)
                                                    .networkPolicy(NetworkPolicy.OFFLINE)
                                                    .placeholder(R.drawable.default_profile_image)
                                                    .into(holder.user_photo, new Callback() {
                                                        @Override
                                                        public void onSuccess() {

                                                        }

                                                        @Override
                                                        public void onError(Exception e) {
                                                            Picasso.get()
                                                                    .load(userThumbPhoto)
                                                                    .placeholder(R.drawable.default_profile_image)
                                                                    .into(holder.user_photo);
                                                        }
                                                    });

                                            if (userVerified.contains("true")){
                                                holder.verified_icon.setVisibility(View.VISIBLE);
                                            }

                                            holder.itemView.setOnClickListener(view -> {
                                                CharSequence options[] = new CharSequence[]{
                                                        "Cancel Sent Request", userName+"'s profile"
                                                };
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setItems(options, (dialogInterface, which) -> {
                                                    if (which == 0){
                                                        //for cancellation, delete data from user node
                                                        //delete from sender >> receiver > values
                                                        friendReqDatabaseReference.child(user_UId).child(userID).removeValue()
                                                                .addOnCompleteListener(task -> {
                                                                    if (task.isSuccessful()){
                                                                        //delete from receiver >. sender > values
                                                                        friendReqDatabaseReference.child(userID).child(user_UId).removeValue()
                                                                                .addOnCompleteListener(task14 -> {
                                                                                    if (task14.isSuccessful()){
                                                                                        Snackbar snackbar = Snackbar
                                                                                                .make(view, "Cancel Sent Request", Snackbar.LENGTH_LONG);
                                                                                                //customize snack bar
                                                                                        View sView = snackbar.getView();
                                                                                        sView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.button_color_disabled));
                                                                                        snackbar.setTextColor(Color.WHITE);
                                                                                        snackbar.show();
                                                                                    }
                                                                                });
                                                                    }
                                                                });
                                                    }
                                                    if (which == 1){
                                                        /**
                                                         * @Todo add intent to profile
                                                         */
                                                        //send intent to Profile
                                                        ProfileFragment profileFragment = new ProfileFragment();
                                                        Bundle args = new Bundle();
                                                        args.putString("visitUserId", userID);
                                                        profileFragment.setArguments(args);
                                                        Navigation.findNavController(view).navigate(R.id.profile_settings, args);
                                                    }
                                                });
                                                builder.show();
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public RequestsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_single, parent, false);
                return new RequestsVH(view);
            }
        };
        request_list.setAdapter(adapter);
        adapter.startListening();
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
