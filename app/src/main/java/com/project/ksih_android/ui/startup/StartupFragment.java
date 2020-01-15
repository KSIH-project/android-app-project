package com.project.ksih_android.ui.startup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.ksih_android.R;
import com.project.ksih_android.databinding.FragmentStartupBinding;

public class StartupFragment extends Fragment {

    private StartupViewModel mStartupViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mStartupViewModel =
                ViewModelProviders.of(this).get(StartupViewModel.class);
        return setUpBinding(inflater, container);
    }

    private View setUpBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentStartupBinding startupBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_startup, container, false);
        startupBinding.rotateLoading.start();
        setUpRecyclerView(startupBinding);
        startupBinding.tempFab.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_navigation_startup_to_addStartUpFragment));
        return startupBinding.getRoot();
    }

    private void setUpRecyclerView(FragmentStartupBinding startupBinding) {
        startupBinding.startUpRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        startupBinding.startUpRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        mStartupViewModel.getStartUps().observe(this, startUpFields -> {
            StartUpAdapter adapter = new StartUpAdapter(startUpFields, requireContext());
            startupBinding.startUpRecyclerView.setAdapter(adapter);
            // Toggle empty list sign
            if (startUpFields.size() < 1) {
                displayEmptyListImage(startupBinding.emptyListGroup);
            } else {
                hideEmptyListImage(startupBinding.emptyListGroup);
            }
            startupBinding.rotateLoading.stop();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStartupViewModel.removeListeners();
    }

    private void displayEmptyListImage(Group emptyListGroup) {
        emptyListGroup.setVisibility(View.VISIBLE);
    }

    private void hideEmptyListImage(Group emptyListGroup) {
        emptyListGroup.setVisibility(View.INVISIBLE);
    }
}