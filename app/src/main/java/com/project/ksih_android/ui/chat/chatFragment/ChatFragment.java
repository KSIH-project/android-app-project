package com.project.ksih_android.ui.chat.chatFragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.ui.chat.models.Friends;
import com.project.ksih_android.utility.UserLastSeenTime;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private ChatViewModel chatViewModel;
    String current_user_id;

    //initialize variables
    private View view;
    private RecyclerView chat_list;

    //firebase variables
    private DatabaseReference friendsDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth mAuth;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        chatViewModel.getText().observe(this, s -> {

        });

        //initializing variables for recycler view
        chat_list = root.findViewById(R.id.chatList);
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        friendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("friends")
                .child(current_user_id);
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        chat_list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        chat_list.setLayoutManager(linearLayoutManager);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Friends> recyclerOptions = new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(friendsDatabaseReference, Friends.class)
                .build();

        //Firebase adapter initialization
        FirebaseRecyclerAdapter<Friends, ChatsVH> adapter = new FirebaseRecyclerAdapter<Friends, ChatsVH>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ChatsVH holder, int position, @NonNull Friends model) {

                final String userID = getRef(position).getKey();
                userDatabaseReference.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            final String userName = dataSnapshot.child("user_name").getValue().toString();
                            final String userPresence = dataSnapshot.child("active_now").getValue().toString();
                            final String userThumbPhoto = dataSnapshot.child("user_thumb_image").getValue().toString();

                            // setting image for new user
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
                                                //load default image on error
                                                Picasso.get()
                                                        .load(userThumbPhoto)
                                                        .placeholder(R.drawable.default_profile_image)
                                                        .into(holder.user_photo);
                                            }
                                        });
                            }
                            holder.user_name.setText(userName);

                            //setting active status
                            holder.active_icon.setVisibility(View.GONE);
                            if (userPresence.contains("true")){
                                holder.user_presence.setText(R.string.active_now);
                                holder.active_icon.setVisibility(View.VISIBLE);
                            }else {
                                holder.active_icon.setVisibility(View.GONE);
                                long last_seen = Long.parseLong(userPresence);
                                String lastSeenOnScreenTime = UserLastSeenTime.getTimeAgo(last_seen,
                                        getContext());
                                Timber.d("lastSeenTime%s", lastSeenOnScreenTime);
                                holder.user_presence.setText(lastSeenOnScreenTime);
                            }
                        }

                        //implement on click listener for items
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //user active status validation
                                if (dataSnapshot.child("active_now").exists()){

                                    //call intent to chat section

                                }else {
                                    userDatabaseReference.child(userID).child("active_now")
                                            .setValue(ServerValue.TIMESTAMP).addOnSuccessListener(
                                            new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //call intent to chat section

                                                }
                                            });
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ChatsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_single_profile_display, parent, false);
                return new ChatsVH(view);
            }
        };

        chat_list.setAdapter(adapter);
        adapter.startListening();
    }

    //chat view holder
    public static class ChatsVH extends RecyclerView.ViewHolder{

        //initialize views
        TextView user_name, user_presence;
        RoundedImageView user_photo;
        ImageView active_icon;

        public ChatsVH(@NonNull View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.all_user_name);
            user_photo = itemView.findViewById(R.id.all_user_profile_img);
            user_presence = itemView.findViewById(R.id.all_user_status);
            active_icon = itemView.findViewById(R.id.activeIcon);
        }
    }
}


