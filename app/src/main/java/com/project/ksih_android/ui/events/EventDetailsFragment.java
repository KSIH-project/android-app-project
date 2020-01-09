package com.project.ksih_android.ui.events;


import android.os.Bundle;

import static com.project.ksih_android.utility.Constants.EVENTS_ITEM_KEY;


import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.ksih_android.R;
import com.project.ksih_android.data.Events;
import com.project.ksih_android.databinding.FragmentEventDetailsBinding;
import com.project.ksih_android.storage.SharedPreferencesStorage;

import static com.project.ksih_android.utility.Constants.EVENTS_ITEM_KEY;

/**
 * Created by ChukwuwaUchenna
 */
public class EventDetailsFragment extends Fragment {
    private FragmentEventDetailsBinding binding;
    private SharedPreferencesStorage mStorage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_details, container, false);
        mStorage = new SharedPreferencesStorage(requireContext());
        Events events = mStorage.getEvents(EVENTS_ITEM_KEY);
        binding.textEventTittleDetails.setText(events.getEventName());
        binding.textEventEmailDetails.setText(events.getEmail());
        binding.textEventsDescripDetails.setText(events.getEventDescription());
        binding.textPhoneNumber.setText(events.getPhoneNumber());
        binding.textEventsTypeDetails.setText(events.getEventType());
        Glide.with(requireContext()).load(events.getImageUrl()).into(binding.imageEventsDetails);

        return binding.getRoot();
    }

}
