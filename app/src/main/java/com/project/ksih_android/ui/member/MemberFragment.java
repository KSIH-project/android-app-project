package com.project.ksih_android.ui.member;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ksih_android.R;
import com.project.ksih_android.data.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.project.ksih_android.utility.Constants.PROFILE_FIREBASE_DATABASE_REFERENCE;

public class MemberFragment extends Fragment {

  //  private MemberViewModel mMemberViewModel;

    DatabaseReference mDatabaseReference;
    RecyclerView mRecyclerView;
    List<User> mUserDataClass;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_member, container, false);
        mRecyclerView = root.findViewById(R.id.recyclerView_Members);
        String uid = FirebaseAuth.getInstance().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(PROFILE_FIREBASE_DATABASE_REFERENCE).child(uid);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserDataClass = new ArrayList<>();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    mUserDataClass.add(user);
                }
                setUpRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;
    }

    private void setUpRecyclerView(){
        GridLayoutManager layoutManager = new GridLayoutManager(getParentFragment().getContext(),2 );
        MemberViewAdapter memberViewAdapter = new MemberViewAdapter(mUserDataClass, getParentFragment().getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(memberViewAdapter);
    }
}