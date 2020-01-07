package com.project.ksih_android.ui.chat.friends;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ksih_android.R;
import com.project.ksih_android.ui.HomeActivity;
import com.project.ksih_android.ui.chat.models.Friends;

import java.util.NavigableMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    //initialize variables
    private Toolbar toolbar;
    private RecyclerView friend_list_view;

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


        return view;
    }

}
