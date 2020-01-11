package com.project.ksih_android.ui.chat.friends;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.project.ksih_android.ui.HomeActivity;
import com.project.ksih_android.ui.chat.chatMessage.ChatMessageFragment;
import com.project.ksih_android.ui.chat.models.Friends;
import com.project.ksih_android.ui.chat.profile.ProfileFragment;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.NavigableMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    //initialize variables
    private Toolbar toolbar;
    private RecyclerView friend_list_Rv;

    //firebase
    private DatabaseReference friendsDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth mAuth;
    String current_user_id;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        toolbar = view.findViewById(R.id.main_appbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        toolbar.setTitle("Friends");
        toolbar.setNavigationOnClickListener(view1 -> {
            Navigation.findNavController(view).navigate(R.id.nav_chats);
        });

        //firebase logic
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        friendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("friends").child(current_user_id);
        friendsDatabaseReference.keepSynced(true);
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        friendsDatabaseReference.keepSynced(true);

        //recycler view setup
        friend_list_Rv = view.findViewById(R.id.friendList);
        friend_list_Rv.setHasFixedSize(true);
        friend_list_Rv.setLayoutManager(new LinearLayoutManager(getContext()));

        showPeopleList();

        return view;
    }

    /**
     * UI Bindings for Firebase
     */
    private void showPeopleList() {
        FirebaseRecyclerOptions<Friends> recyclerOptions = new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(friendsDatabaseReference, Friends.class)
                .build();

        FirebaseRecyclerAdapter<Friends, FriendsVH> recyclerAdapter = new FirebaseRecyclerAdapter<Friends, FriendsVH>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull FriendsVH holder, int position, @NonNull Friends model) {
                holder.date.setText(getString(R.string.friendship_date) + model.getDate());

                final String userID = getRef(position).getKey();
                userDatabaseReference.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String userName = dataSnapshot.child("user_name").getValue().toString();
                        final String userThumbPhoto = dataSnapshot.child("user_thumb_image").getValue().toString();
                        String active_status = dataSnapshot.child("active_now").getValue().toString();

                        //online active status
                        holder.active_icon.setVisibility(View.GONE);
                        if (active_status.contains("active_now")){
                         holder.active_icon.setVisibility(View.VISIBLE);
                        }else {
                            holder.active_icon.setVisibility(View.GONE);
                        }

                        holder.name.setText(userName);
                        Picasso.get()
                                .load(userThumbPhoto)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.default_profile_image)
                                .into(holder.profile_thumb);

                        //click item, 2 options in a dialougue will appear
                        holder.itemView.setOnClickListener(view -> {
                            CharSequence options[] = new CharSequence[]{
                                    "Send Message", userName+"'s profile"
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setItems(options, (dialogInterface, which) -> {
                                if (which == 0){
                                    //user active validation status
                                    if (dataSnapshot.child("active_now").exists()){

                                        /**
                                         * @Todo send intent to Chat Message Fragment
                                         */
                                        ChatMessageFragment chatMessageFragment = new ChatMessageFragment();
                                        Bundle args = new Bundle();
                                        args.putString("visitUserId", userID);
                                        args.putString("userName", userName);
                                        chatMessageFragment.setArguments(args);
                                        Navigation.findNavController(view).navigate(R.id.friendList, args);
                                    }else {
                                        userDatabaseReference.child(userID).child("active_now")
                                                .setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                /**
                                                 * @Todo initialize intent to chat Message Fragment
                                                 */
                                                ChatMessageFragment chatMessageFragment = new ChatMessageFragment();
                                                Bundle args = new Bundle();
                                                args.putString("visitUserId", userID);
                                                args.putString("userName", userName);
                                                chatMessageFragment.setArguments(args);
                                                Navigation.findNavController(view).navigate(R.id.friendList, args);

                                            }
                                        });
                                    }
                                }

                                if (which == 1){
                                    /**
                                     * @Todo send intent to profile fragment
                                     */
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public FriendsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_single_profile_display, parent, false);
                return new FriendsVH(view);
            }
        };
        friend_list_Rv.setAdapter(recyclerAdapter);
        recyclerAdapter.startListening();
    }

    public static class FriendsVH extends RecyclerView.ViewHolder{

        public TextView name;
        TextView date;
        RoundedImageView profile_thumb;
        ImageView active_icon;

        public FriendsVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.all_user_name);
            date = itemView.findViewById(R.id.all_user_status);
            profile_thumb = itemView.findViewById(R.id.all_user_profile_img);
            active_icon = itemView.findViewById(R.id.activeIcon);
        }
    }

}
