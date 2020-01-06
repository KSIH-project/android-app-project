package com.project.ksih_android.ui.startup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by SegunFrancis
 */

public class StartUpAdapter extends RecyclerView.Adapter<StartUpAdapter.StartUpViewHolder> {

    private List<StartUpField> mList = new ArrayList<>();

    public StartUpAdapter(List<StartUpField> list) {
        mList = list;
    }

    @NonNull
    @Override
    public StartUpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StartUpViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.startup_items_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StartUpViewHolder holder, int position) {
        StartUpField field = mList.get(position);
        Glide.with(holder.mContext).load(field.getImageUrl()).into(holder.roundLogo);
        holder.startupName.setText(field.getStartupName());
        holder.startupDescription.setText(field.getStartupDescription());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class StartUpViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView roundLogo;
        TextView startupName;
        TextView startupDescription;
        Context mContext;

        public StartUpViewHolder(@NonNull View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            roundLogo = itemView.findViewById(R.id.startup_icon_image);
            startupName = itemView.findViewById(R.id.startup_name_item);
            startupDescription = itemView.findViewById(R.id.startup_description_item);
        }
    }
}
