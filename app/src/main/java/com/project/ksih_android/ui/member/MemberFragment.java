package com.project.ksih_android.ui.member;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ksih_android.R;
import com.victor.loading.rotate.RotateLoading;

public class MemberFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MemberViewModel memberViewModel = ViewModelProviders.of(this).get(MemberViewModel.class);
        View root = inflater.inflate(R.layout.fragment_member, container, false);
        RotateLoading membersProgressBar = root.findViewById(R.id.members_progress_bar);
        startProgressBar(membersProgressBar);
        memberViewModel.getMembers().observe(this, users -> {
            MembersRecyclerAdapter adapter = new MembersRecyclerAdapter(users, requireContext());
            RecyclerView recyclerView = root.findViewById(R.id.members_recycler_view);
            recyclerView.setAdapter(adapter);
            stopProgressBar(membersProgressBar);
        });
        return root;
    }

    private void startProgressBar(RotateLoading loading) {
        loading.start();
    }

    private void stopProgressBar(RotateLoading loading) {
        loading.stop();
    }
}