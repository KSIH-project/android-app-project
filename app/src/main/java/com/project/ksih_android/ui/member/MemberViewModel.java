package com.project.ksih_android.ui.member;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ksih_android.data.User;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MemberViewModel extends ViewModel {

    private MutableLiveData<List<User>> membersList;
    private List<User> tempList;

    public MemberViewModel() {
        tempList = new ArrayList<>();
        membersList = new MutableLiveData<>();
    }

    LiveData<List<User>> getMembers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("development/users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tempList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    tempList.add(user);
                }
                membersList.setValue(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.d("Database Error: %s", databaseError.getDetails());
            }
        });
        return membersList;
    }
}