package com.project.ksih_android.ui.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;

class MessageViewHolder extends RecyclerView.ViewHolder {

    TextView messageTextView;
    ImageView messageImageView;
    TextView messengerTextView;
    RoundedImageView messengerImageView;


    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        messageTextView = itemView.findViewById(R.id.messageTextView);
        messageImageView = itemView.findViewById(R.id.messageImageView);
        messengerTextView = itemView.findViewById(R.id.messengerTextView);
        messengerImageView = itemView.findViewById(R.id.messengerImageView);
    }
}
