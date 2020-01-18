package com.project.ksih_android.ui.startup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.ksih_android.data.StartUpField;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

import static com.project.ksih_android.utility.Constants.STARTUP_FIREBASE_DATABASE_REFERENCE;

public class StartupViewModel extends ViewModel implements ValueEventListener {

    private Query ref;
    private MutableLiveData<List<StartUpField>> startupList = new MutableLiveData<>();
    private List<StartUpField> mList = new ArrayList<>();

    public StartupViewModel() {
        ref = FirebaseDatabase.getInstance()
                .getReference(STARTUP_FIREBASE_DATABASE_REFERENCE)
                .orderByChild("startupName"); /* Sort start ups by name */
        ref.addValueEventListener(this);
    }

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

    LiveData<List<StartUpField>> getStartUps() {
        return startupList;
    }

    void removeListeners() {
        ref.removeEventListener(this);
    }
}