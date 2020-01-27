package com.project.ksih_android.ui.member;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.data.User;

import java.util.List;

public class MemberViewAdapter extends RecyclerView.Adapter<MemberViewAdapter.MemberViewHolder>  {

    Context mContext;
    private List<User> mUserList;

    public MemberViewAdapter(List<User> mUser, Context context) {
        mUserList = mUser;
        mContext = context.getApplicationContext();
    }


    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.members_cardview, parent, false);
        return new MemberViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        User data = mUserList.get(position);
        holder.bind(data);
        holder.itemView.findViewById(R.id.member_card).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("UserData", data);
            Navigation.findNavController(holder.mRoundedImageViewMemberUser).navigate(R.id.action_navigation_member_to_profileFragment, bundle);
        });


    }


    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView mRoundedImageViewMemberUser;
        TextView mTextViewMemberName;
        TextView mTextViewMemberStack;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            mRoundedImageViewMemberUser = itemView.findViewById(R.id.members_photo);
            mTextViewMemberName = itemView.findViewById(R.id.members_name);
            mTextViewMemberStack = itemView.findViewById(R.id.members_stack);
        }

        public void bind(User data) {
           Glide.with(mContext)
                   .load(data.user_image)
                   .into(mRoundedImageViewMemberUser);
            mTextViewMemberName.setText(data.user_firstName + data.user_lastName);
            mTextViewMemberStack.setText(data.user_stack);
        }
    }
}
