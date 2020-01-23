package com.project.ksih_android.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;


public class AboutDevelopersAdapter extends RecyclerView.Adapter<AboutDevelopersAdapter.AboutDevelopersViewHolder> {

    private List<AboutDevelopersData> mDevelopersDataList;

    public AboutDevelopersAdapter(List<AboutDevelopersData> developersDataList) {
        mDevelopersDataList = developersDataList;
    }



    @NonNull
    @Override
    public AboutDevelopersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_developers, parent, false);
        return new AboutDevelopersViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AboutDevelopersViewHolder holder, int position) {
        AboutDevelopersData data = mDevelopersDataList.get(position);
        holder.bind(data);

    }

    @Override
    public int getItemCount() {
        return mDevelopersDataList.size();
    }

    public class AboutDevelopersViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView mRoundedImageViewPerson;
        TextView mTextViewName;
        TextView mTextViewProfession;

        public AboutDevelopersViewHolder(@NonNull View itemView) {
            super(itemView);

            mRoundedImageViewPerson = itemView.findViewById(R.id.roundedImageViewPerson);
            mTextViewName = itemView.findViewById(R.id.textViewName);
            mTextViewProfession = itemView.findViewById(R.id.textViewProfession);
        }

        public void bind(AboutDevelopersData data) {
            mRoundedImageViewPerson.setImageResource(data.getDevelopersImageUri());
            mTextViewName.setText(data.getTextName());
            mTextViewProfession.setText(data.getTextProfession());
        }
    }
}
