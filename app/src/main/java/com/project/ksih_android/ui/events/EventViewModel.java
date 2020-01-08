package com.project.ksih_android.ui.events;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ksih_android.data.Events;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChukwuwaUchenna
 */

public class EventViewModel extends ViewModel {

    private MutableLiveData<String> mEventTopic;
    private MutableLiveData<Boolean> isButtonEnabled;
    private Events mEvents;
    private DatabaseReference reference;
    private List<Events> mList = new ArrayList<>();
    private MutableLiveData<List<Events>> eventsList = new MutableLiveData<>();

    public EventViewModel() {
        reference = FirebaseDatabase.getInstance().getReference("events");
        mEvents = new Events();
        isButtonEnabled.setValue(true);

    }

    public Events getUrl() {
        return mEvents;
    }

    public LiveData<String> getText() {
        return mEventTopic;
    }

    public MutableLiveData<Boolean> getIsButtonEnabled() {
        return isButtonEnabled;
    }

    public LiveData<List<Events>> getEvents() {
        reference.addValueEventListener(new ValueEventListener() {
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

            }
        });
        return eventsList;
    }

}

