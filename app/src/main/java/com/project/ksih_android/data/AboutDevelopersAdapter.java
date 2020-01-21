package com.project.ksih_android.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;

import java.util.ArrayList;

public class AboutDevelopersAdapter extends RecyclerView.Adapter<AboutDevelopersAdapter.AboutDevelopersViewHolder> {

    ArrayList<AboutDevelopersData> mDevelopersAdapters;
    private DatabaseReference mDatabaseReference;

    @NonNull
    @Override
    public AboutDevelopersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_developers, parent, false);
        return new AboutDevelopersViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AboutDevelopersViewHolder holder, int position) {
        AboutDevelopersData data = mDevelopersAdapters.get(position);
        holder.bind(data);

    }

    @Override
    public int getItemCount() {
        return mDevelopersAdapters.size();
    }

    public class AboutDevelopersViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView mRoundedImageViewPerson;
        TextView mTextViewName;
        TextView mTextViewProfession;

        public AboutDevelopersViewHolder(@NonNull View itemView) {
            super(itemView);

            mRoundedImageViewPerson = itemView.findViewById(R.id.roundedImageViewPerson);
            mTextViewName = itemView.findViewById(R.id.textViewName);
            mTextViewProfession = mTextViewProfession.findViewById(R.id.textViewProfession);
        }

        public void bind(AboutDevelopersData data) {
            mRoundedImageViewPerson.setImageResource(data.getDevelopersImageUri());
            mTextViewName.setText(data.getTextName());
            mTextViewProfession.setText(data.getTextProfession());
        }
    }
}
