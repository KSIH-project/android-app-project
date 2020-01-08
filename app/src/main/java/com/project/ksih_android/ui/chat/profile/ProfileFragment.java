package com.project.ksih_android.ui.chat.profile;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ksih_android.R;
import com.squareup.picasso.Picasso;

/**
 * By david
 */
public class ProfileFragment extends Fragment {

    private Toolbar mToolbar;
    private Button sendFriendRequest_Button, declineFriendRequest_Button;
    private TextView profileName, profileStatus, u_work, go_my_profile;
    private ImageView profileImage, verified_icon;

    //firebase
    private DatabaseReference userDatabaseReference;
    private DatabaseReference friendRequestReference;
    private DatabaseReference friendsDatabaseReference;
    private DatabaseReference notificationDatabaseReference;
    private FirebaseAuth mAuth;
    private String CURRENT_STATE;

    //visit profile id
    public String receiver_userID;
    public String senderID;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //set toolbar
        mToolbar = view.findViewById(R.id.profile_appbar);
            mToolbar.setNavigationIcon(R.drawable.ic_back_button);
            mToolbar.setTitle("Profile");
            mToolbar.setNavigationOnClickListener(view1 -> {
                Navigation.findNavController(view).navigate(R.id.nav_chats);
            });

            //set firebase
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        friendRequestReference = FirebaseDatabase.getInstance().getReference().child("friend_request");
        friendRequestReference.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        senderID = mAuth.getCurrentUser().getUid();
        friendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("friends");
        friendsDatabaseReference.keepSynced(true);

        //notification
        notificationDatabaseReference = FirebaseDatabase.getInstance().getReference().child("notifications");
        notificationDatabaseReference.keepSynced(true);

        /**
         * @Todo receive intent from visitUserId
         */

        //views
        sendFriendRequest_Button = view.findViewById(R.id.visitUserFrndRqstSendButton);
        declineFriendRequest_Button = view.findViewById(R.id.visitUserFrndRqstDeclineButton);
        profileName = view.findViewById(R.id.visitUserProfileName);
        profileStatus = view.findViewById(R.id.visitUserProfileStatus);
        verified_icon = view.findViewById(R.id.visit_verified_icon);
        profileImage = view.findViewById(R.id.visit_user_profile_image);
        go_my_profile = view.findViewById(R.id.go_my_profile);
        u_work = view.findViewById(R.id.visit_work);

        verified_icon.setVisibility(View.INVISIBLE);
        CURRENT_STATE = "not_friends";

        /**
         * Load every single users data
         */
        userDatabaseReference.child(receiver_userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("user_name").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                String nickname = dataSnapshot.child("user_nickname").getValue().toString();
                String profession = dataSnapshot.child("user_profession").getValue().toString();
                String image = dataSnapshot.child("user_image").getValue().toString();
                String verified = dataSnapshot.child("veridied").getValue().toString();

                //setting logics
                if (nickname.isEmpty()){
                    profileName.setText(name);
                }else {
                    String full_name = name +" ("+nickname+")";
                    profileName.setText(full_name);
                }

                if (profession.length() > 2){
                    u_work.setText(" " + profession);
                }if (profession.equals("")){
                    u_work.setText(" No Profession");
                }

                profileStatus.setText(status);
                Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.default_profile_image)
                        .into(profileImage);

                if (verified.contains("true")){
                    verified_icon.setVisibility(View.VISIBLE);
                }

                //for fixing dynamic cancel / firend / unfriend / accept button
                friendRequestReference.child(senderID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //if database has these data, then execute the condition below
                                if (dataSnapshot.hasChild(receiver_userID)){
                                    String requestType = dataSnapshot.child(receiver_userID)
                                            .child("request_type").getValue().toString();

                                    if (requestType.equals("sent")){
                                        CURRENT_STATE = "request_sent";
                                        sendFriendRequest_Button.setText("Cancel Friend Request");

                                        declineFriendRequest_Button.setVisibility(View.VISIBLE);
                                        declineFriendRequest_Button.setEnabled(false);
                                    }else if (requestType.equals("received")){
                                        CURRENT_STATE = "request_received";
                                        sendFriendRequest_Button.setText("Accept Friend Request");

                                        declineFriendRequest_Button.setVisibility(View.VISIBLE);
                                        declineFriendRequest_Button.setEnabled(true);

                                        declineFriendRequest_Button.setOnClickListener(view12 -> {
                                            declineFriendRequest();
                                        });
                                    }else {
                                         friendsDatabaseReference.child(senderID)
                                                 .addListenerForSingleValueEvent(new ValueEventListener() {
                                                     @Override
                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                         if (dataSnapshot.exists()){
                                                             if (dataSnapshot.hasChild(receiver_userID)){
                                                                 CURRENT_STATE = "friends";
                                                                 sendFriendRequest_Button.setText("Unfriend This Person");

                                                                 declineFriendRequest_Button.setVisibility(View.INVISIBLE);
                                                                 declineFriendRequest_Button.setEnabled(false);
                                                             }
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        declineFriendRequest_Button.setVisibility(View.GONE);
        declineFriendRequest_Button.setEnabled(false);

        // send / cancel / Accept / Unfriend / request Mechanism
        // condition for current owner
        if (!senderID.equals(receiver_userID)){
            sendFriendRequest_Button.setOnClickListener(view13 -> {
                sendFriendRequest_Button.setEnabled(false);
                
                if (CURRENT_STATE.equals("not_friends")){
                    sendFriendRequest();
                }else if (CURRENT_STATE.equals("request_sent")){
                    cancelFriendRequest();
                }
            });
        }

        return view;
    }

    private void cancelFriendRequest() {
    }

    private void sendFriendRequest() {
    }

    private void declineFriendRequest() {
    }

}
