package com.project.ksih_android.ui.events;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.ksih_android.R;
import com.project.ksih_android.data.Events;
import com.project.ksih_android.databinding.EventsItemsListBinding;

import static com.project.ksih_android.utility.Constants.EVENTS_ITEM_KEY;



import java.util.List;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventRecyclerViewHolder> {
    private List<Events> mEvents;
    private EventsItemsListBinding binding;
    private Context mContext;

    public EventRecyclerAdapter(List<Events> events, Context context) {
        mEvents = events;
        mContext = context.getApplicationContext();


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
        holder.itemView.setOnClickListener(v -> {
            Events events1 = mEvents.get(position);
            Bundle bundle = new Bundle();
            bundle.putParcelable(EVENTS_ITEM_KEY, events1);
            Navigation.findNavController(v).navigate(R.id.action_navigation_event_to_eventDetailsFragment, bundle);
        });

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
            Glide.with(mContext).load(nEvent.getImageUrl()).into(binding.imageEventsList);
            binding.textEventsDate.setText(nEvent.getDate());
            binding.textEventsTime.setText(nEvent.getTime());
            binding.textEventsTittle.setText(nEvent.getEventName());
            binding.textEventsDescription.setText(nEvent.getEventDescription());
            binding.textEventsType.setText(nEvent.getEventType());
        }
    }

}

