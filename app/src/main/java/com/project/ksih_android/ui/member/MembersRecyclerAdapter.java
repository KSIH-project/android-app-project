package com.project.ksih_android.ui.member;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.data.User;

import java.util.List;

/**
 * Created by SegunFrancis
 */

public class MembersRecyclerAdapter extends RecyclerView.Adapter<MembersRecyclerAdapter.MembersViewHolder> {

    private List<User> membersList;
    private Context mContext;

    MembersRecyclerAdapter(List<User> membersList, Context context) {
        this.membersList = membersList;
        mContext = context;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MembersViewHolder(LayoutInflater.from(mContext).inflate(R.layout.members_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {
        String name = membersList.get(position).user_firstName + " " + membersList.get(position).user_lastName;
        holder.membersName.setText(name);
        holder.itemView.findViewById(R.id.memebersCardView).setVisibility(View.VISIBLE);
        holder.membersStack.setText(membersList.get(position).user_stack);
        Glide.with(mContext)
                .load(membersList.get(position).user_image)
                .placeholder(R.drawable.default_profile_image)
                .error(R.drawable.default_profile_image)
                .into(holder.membersImage);
    }

    @Override
    public int getItemCount() {
        return membersList.size();
    }

    class MembersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView membersName, membersStack;
        RoundedImageView membersImage;

        MembersViewHolder(@NonNull View itemView) {
            super(itemView);

            membersName = itemView.findViewById(R.id.members_name_textView);
            membersStack = itemView.findViewById(R.id.members_stack_textView);
            membersImage = itemView.findViewById(R.id.members_image_ImageView);

            itemView.setOnClickListener(this);
            membersImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == itemView) {
                Bundle membersBundle = new Bundle();
                membersBundle.putSerializable("members_bundle", membersList.get(getAdapterPosition()));
                Navigation.findNavController(view).navigate(R.id.action_navigation_member_to_profileFragment, membersBundle);
            } else if (view == membersImage) {
                Bundle imageBundle = new Bundle();
                imageBundle.putSerializable("photo_url", membersList.get(getAdapterPosition()));
                Navigation.findNavController(view).navigate(R.id.action_navigation_member_to_editPhotoFragment, imageBundle);
            }
        }
    }
}
