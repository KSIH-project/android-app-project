package com.project.ksih_android.ui.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.ui.zoom.ZoomFragment;

class MessageViewHolder extends RecyclerView.ViewHolder {

    TextView messageTextView, receiverTextView;
    RoundedImageView messageImageView, receiverImageView;
    TextView messengerTextView;
    RoundedImageView messengerImageView;


    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        //sender message details
        messageTextView = itemView.findViewById(R.id.senderMessageText);
        messageImageView = itemView.findViewById(R.id.messageImageVsender);

        //user views
        messengerTextView = itemView.findViewById(R.id.messengerTextView);
        messengerImageView = itemView.findViewById(R.id.messageUserImage);

        //receiver message details
        receiverTextView = itemView.findViewById(R.id.receiverMessageText);
        receiverImageView = itemView.findViewById(R.id.messageImageVreceiver);
    }
}
