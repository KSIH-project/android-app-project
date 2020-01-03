package com.project.ksih_android.utility;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ksih_android.data.Events;
import com.project.ksih_android.databinding.EventsItemsListBinding;

import java.util.ArrayList;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventRecyclerViewHolder> {
    ArrayList<Events> mEvents;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabasereference;
    private ChildEventListener mChildEventListner;
    private EventsItemsListBinding binding;

    public EventRecyclerAdapter() {
        mEvents = new ArrayList<>();
        mDatabasereference.addChildEventListener(mChildEventListner = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Events events = dataSnapshot.getValue(Events.class);
                events.setId(dataSnapshot.getKey());
                mEvents.add(events);
                notifyItemInserted(mEvents.size() - 1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @NonNull
    @Override
    public EventRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        binding = EventsItemsListBinding.inflate(layoutInflater, parent, false);

        return new EventRecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerViewHolder holder, int position) {
        Events events = mEvents.get(position);
        holder.bind(events);
    }

    @Override
    public int getItemCount() {
        return mEvents != null ? mEvents.size() : 0;
    }

    class EventRecyclerViewHolder extends RecyclerView.ViewHolder {

        EventRecyclerViewHolder(@NonNull EventsItemsListBinding eventBinding) {
            super(eventBinding.getRoot());

        }

        void bind(Events nEvent) {
            binding.textEventsTittle.setText(nEvent.getEventName());
            binding.textEventsDescription.setText(nEvent.getEventDescription());
            binding.textEventsType.setText(nEvent.getEventType());
        }
    }

}

