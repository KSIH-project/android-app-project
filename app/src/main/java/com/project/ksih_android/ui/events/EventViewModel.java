package com.project.ksih_android.ui.events;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.ksih_android.data.Events;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import timber.log.Timber;

import static com.project.ksih_android.utility.Constants.EVENTS_FIREBASE_PATH;

/**
 * Created by ChukwuwaUchenna
 */

public class EventViewModel extends ViewModel implements ValueEventListener {

    private MutableLiveData<String> mEventTopic;
    MutableLiveData<Boolean> isButtonEnabled = new MutableLiveData<>();
    private Events mEvents;
    private Query reference;
    private List<Events> mList = new ArrayList<>();
    private MutableLiveData<List<Events>> eventsList = new MutableLiveData<>();

    public EventViewModel() {
        reference = FirebaseDatabase.getInstance()
                .getReference(EVENTS_FIREBASE_PATH)
                .orderByKey(); /* Sort events by latest */
        reference.addValueEventListener(this);
        mEvents = new Events();
        isButtonEnabled.setValue(true);

    }


    public LiveData<String> getText() {
        return mEventTopic;
    }

    public LiveData<List<Events>> getEvents() {
        return eventsList;
    }

    public void removeListeners() {
        reference.removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Events events = snapshot.getValue(Events.class);
            if (events != null) {
                mList.add(events);
            }
        }
        eventsList.setValue(mList);
    }


    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Timber.d("Database Error: %s", databaseError.getDetails());
    }
}

