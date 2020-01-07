package com.project.ksih_android.ui.events;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentEventBinding;
import com.project.ksih_android.utility.EventRecyclerAdapter;

import org.jetbrains.annotations.NotNull;

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

        setupRecyclerView();
        return mFragmentEventBinding.getRoot();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = mFragmentEventBinding.recyclerEvents;

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        EventRecyclerAdapter adapter = new EventRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

}
