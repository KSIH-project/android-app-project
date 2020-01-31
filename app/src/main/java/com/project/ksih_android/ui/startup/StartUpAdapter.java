package com.project.ksih_android.ui.startup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.data.StartUpField;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

import static com.project.ksih_android.utility.Constants.STARTUP_DETAILS_BUNDLE_KEY;

/**
 * Created by SegunFrancis
 */

public class StartUpAdapter extends RecyclerView.Adapter<StartUpAdapter.StartUpViewHolder> {

    private List<StartUpField> mList;
    private Context mContext;

    StartUpAdapter(List<StartUpField> list, Context context) {
        mList = list;
        mContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    public StartUpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StartUpViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.startup_items_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StartUpViewHolder holder, int position) {
        StartUpField field = mList.get(position);
        Glide.with(mContext).load(field.getImageUrl()).into(holder.roundLogo);
        holder.startupName.setText(field.getStartupName());
        holder.startupDescription.setText(field.getStartupDescription());
        holder.startupFounder.setText(field.getStartupFounder());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class StartUpViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView roundLogo;
        TextView startupName, startupDescription, startupFounder;

        StartUpViewHolder(@NonNull View itemView) {
            super(itemView);

            roundLogo = itemView.findViewById(R.id.startup_icon_image);
            startupName = itemView.findViewById(R.id.startup_name_item);
            startupDescription = itemView.findViewById(R.id.startup_description_item);
            startupFounder = itemView.findViewById(R.id.startup_founder_item);

            itemView.setOnClickListener(view -> {
                StartUpField field = mList.get(getAdapterPosition());
                Bundle bundle = new Bundle();
                bundle.putSerializable(STARTUP_DETAILS_BUNDLE_KEY, mList.get(getAdapterPosition()));
                Navigation.findNavController(view).navigate(R.id.action_navigation_startup_to_startUpDetailsFragment, bundle);
                Timber.d("Field: %s", field);
            });
        }
    }
}
