package com.project.ksih_android.ui.startup;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

public class StartupViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Boolean> isButtonEnabled = new MutableLiveData<>();
    private StartUpField mStartUpField;
    private DatabaseReference ref;
    private MutableLiveData<List<StartUpField>> startupList = new MutableLiveData<>();
    private List<StartUpField> mList = new ArrayList<>();

    public StartupViewModel() {
        ref = FirebaseDatabase.getInstance().getReference("startups");
        mStartUpField = new StartUpField();
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
        isButtonEnabled.setValue(true);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Boolean> getIsButtonEnabled() {
        return isButtonEnabled;
    }

    public StartUpField getUrl() {
        return mStartUpField;
    }

    public LiveData<List<StartUpField>> getStartUps() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StartUpField field = snapshot.getValue(StartUpField.class);
                    if (field != null) {
                        mList.add(field);
                    }
                }
                startupList.setValue(mList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.d("Database Error: %s", databaseError.getDetails());
            }
        });
        return startupList;
    }
}