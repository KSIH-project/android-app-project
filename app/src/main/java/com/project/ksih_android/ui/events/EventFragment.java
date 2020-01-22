package com.project.ksih_android.ui.events;


import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.ksih_android.R;

import com.project.ksih_android.databinding.FragmentEventBinding;
import com.project.ksih_android.storage.SharedPreferencesStorage;

import org.jetbrains.annotations.NotNull;


/**
 * Created by ChukwuwaUchenna
 */
public class EventFragment extends Fragment {

    private EventViewModel eventViewModel;
    private FragmentEventBinding mFragmentEventBinding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        // Inflate the layout for this fragment
        mFragmentEventBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_event, container, false);
        mFragmentEventBinding.progressBar.start();
        mFragmentEventBinding.floatingActionButton.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_navigation_event_to_eventAddFragment));
        setupRecyclerView();
        return mFragmentEventBinding.getRoot();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, true);
        mFragmentEventBinding.recyclerEvents.setLayoutManager(layoutManager);
        eventViewModel.getEvents().observe(this, events -> {
            if (events.size() == 0) {
                mFragmentEventBinding.imageViewIllustration.setVisibility(View.VISIBLE);
            } else {
                EventRecyclerAdapter adapter = new EventRecyclerAdapter(events, requireContext());
                mFragmentEventBinding.recyclerEvents.setHasFixedSize(true);
                adapter.setHasStableIds(true);
                layoutManager.smoothScrollToPosition(mFragmentEventBinding.recyclerEvents, null, adapter.getItemCount());
                mFragmentEventBinding.recyclerEvents.setAdapter(adapter);
                mFragmentEventBinding.progressBar.stop();
            }


        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventViewModel.removeListeners();
    }
}
