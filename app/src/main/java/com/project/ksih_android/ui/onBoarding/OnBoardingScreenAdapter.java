package com.project.ksih_android.ui.onBoarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ksih_android.R;

import java.util.List;

public class OnBoardingScreenAdapter extends RecyclerView.Adapter<OnBoardingScreenAdapter.OnBoardingScreenViewHolder> {

    private List<OnboardingModel> mOnBoardingModelList;

    public OnBoardingScreenAdapter(List<OnboardingModel> list) {
        mOnBoardingModelList = list;
    }


    @NonNull
    @Override
    public OnBoardingScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnBoardingScreenViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.on_boarding_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingScreenViewHolder holder, int position) {

        holder.onBoardingImageView.setImageResource(mOnBoardingModelList.get(position).onBoardingImage);
        holder.onBoardingTextTitle.setText(mOnBoardingModelList.get(position).onBoardingTextTitle);
        holder.getOnBoardingTextDescription.setText(mOnBoardingModelList.get(position).onBoardingTextDescription);

    }

    @Override
    public int getItemCount() {
        return mOnBoardingModelList.size();
    }

    class OnBoardingScreenViewHolder extends RecyclerView.ViewHolder {

        ImageView onBoardingImageView;
        TextView onBoardingTextTitle;
        TextView getOnBoardingTextDescription;

        OnBoardingScreenViewHolder(@NonNull View itemView) {
            super(itemView);

            onBoardingImageView = itemView.findViewById(R.id.onboarding_image);
            onBoardingTextTitle = itemView.findViewById(R.id.onboarding_textTitle);
            getOnBoardingTextDescription = itemView.findViewById(R.id.onboarding_textDescription);
        }

    }
}
