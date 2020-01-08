package com.project.ksih_android.ui.events;


import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.ksih_android.R;
import com.project.ksih_android.data.Events;
import com.project.ksih_android.databinding.FragmentEventBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by ChukwuwaUchenna
 */
public class EventFragment extends Fragment {

    private EventViewModel eventViewModel;
    private FragmentEventBinding mFragmentEventBinding;
    public EventFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        // Inflate the layout for this fragment
        mFragmentEventBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_event, container, false);
        mFragmentEventBinding.progressBar.start();
        mFragmentEventBinding.floatingActionButton.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_navigation_event_to_eventDetailsFragment));
        setupRecyclerView();
        return mFragmentEventBinding.getRoot();
    }

    private void setupRecyclerView() {
        mFragmentEventBinding.recyclerEvents.setLayoutManager(new LinearLayoutManager(requireActivity()));
        eventViewModel.getEvents().observe(this, new Observer<List<Events>>() {
            @Override
            public void onChanged(List<Events> events) {
                EventRecyclerAdapter adapter = new EventRecyclerAdapter(events, requireContext());
                mFragmentEventBinding.recyclerEvents.setAdapter(adapter);
                mFragmentEventBinding.progressBar.stop();

            }
        });

    }

}
