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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

/**
 * Created by SegunFrancis
 */

public class StartUpAdapter extends RecyclerView.Adapter<StartUpAdapter.StartUpViewHolder> {

    private List<StartUpField> mList = new ArrayList<>();
    //private SharedPreferencesStorage mStorage;
    private Context mContext;
    //private StartupViewModel mViewModel;

    public StartUpAdapter(List<StartUpField> list, Context context) {
        mList = list;
        mContext = context.getApplicationContext();
        //mStorage = new SharedPreferencesStorage(mContext);
        //mViewModel = ViewModelProviders.of(homeActivity).get(StartupViewModel.class);
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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class StartUpViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView roundLogo;
        TextView startupName;
        TextView startupDescription;

        public StartUpViewHolder(@NonNull View itemView) {
            super(itemView);

            roundLogo = itemView.findViewById(R.id.startup_icon_image);
            startupName = itemView.findViewById(R.id.startup_name_item);
            startupDescription = itemView.findViewById(R.id.startup_description_item);

            itemView.setOnClickListener(view -> {
                StartUpField field = mList.get(getAdapterPosition());
                //mStorage.setStartupField(STARTUP_ITEM_KEY, field);
                //mViewModel.select(mList.get(getAdapterPosition()));
                Bundle bundle = new Bundle();
                bundle.putSerializable("startup_details", mList.get(getAdapterPosition()));
                Navigation.findNavController(view).navigate(R.id.action_navigation_startup_to_startUpDetailsFragment, bundle);
                Timber.d("Field: %s", field);
            });
        }
    }
}
