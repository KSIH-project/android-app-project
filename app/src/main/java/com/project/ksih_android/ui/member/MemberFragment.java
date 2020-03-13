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
import com.project.ksih_android.data.User;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

public class MemberFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MemberViewModel memberViewModel = ViewModelProviders.of(this).get(MemberViewModel.class);
        View root = inflater.inflate(R.layout.fragment_member, container, false);
        ArrayList userList = new ArrayList();
        RotateLoading membersProgressBar = root.findViewById(R.id.members_progress_bar);
        startProgressBar(membersProgressBar);
        memberViewModel.getMembers().observe(this, users -> {
            userList.clear();
            for (User user : users) {
                if (!user.user_stack.isEmpty()) {

                    userList.add(user);
                }
            }
            MembersRecyclerAdapter adapter = new MembersRecyclerAdapter(userList, requireContext());
            RecyclerView recyclerView = root.findViewById(R.id.members_recycler_view);
//            adapter. notifyItemRangeChanged(recyclerView.getChildAdapterPosition(recyclerView.getFocusedChild()),users.size());
//            adapter.notifyDataSetChanged();
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