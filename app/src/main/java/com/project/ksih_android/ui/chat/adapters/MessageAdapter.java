package com.project.ksih_android.ui.chat.adapters;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.ui.chat.models.Message;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    //intializing message list
    private List<Message> messageList;

    //initializing firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    //constructor
    public MessageAdapter(List<Message> messageList){
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_layout,
                parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        //declare message views
        String sender_UID = mAuth.getCurrentUser().getUid();
        Message message = messageList.get(position);

        //declare message type and sender
        String from_user_ID = message.getFrom();
        String from_message_TYPE = message.getType();

        //firebase for offline
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users")
                .child(from_user_ID);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String userName = dataSnapshot.child("user_name").getValue().toString();
                    String userProfileImage = dataSnapshot.child("user_thumb_image").getValue().toString();

                    //Load profile with picasso
                    Picasso.get()
                            .load(userProfileImage)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_profile_image)
                            .into(holder.user_profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //when message type is Text
        if (from_message_TYPE.equals("text")){
            holder.receiver_text_message.setVisibility(View.VISIBLE);
            holder.user_profile_image.setVisibility(View.VISIBLE);

            //when message is Text, Image views are gone
            holder.senderImageMsg.setVisibility(View.GONE);
            holder.receiverImageMsg.setVisibility(View.GONE);

            //checking message sender
            if (from_user_ID.equals(sender_UID)){
                holder.sender_text_message.setBackgroundResource
                        (R.drawable.single_message_text_another_background);
                holder.sender_text_message.setTextColor(Color.BLACK);
                holder.sender_text_message.setGravity(Gravity.START);
                holder.sender_text_message.setText(message.getMessage());
            }else {
                holder.sender_text_message.setVisibility(View.INVISIBLE);
                holder.receiver_text_message.setVisibility(View.VISIBLE);
                holder.user_profile_image.setVisibility(View.VISIBLE);

                holder.receiver_text_message.setBackgroundResource
                        (R.drawable.single_message_text_background);
                holder.receiver_text_message.setTextColor(Color.WHITE);
                holder.receiver_text_message.setGravity(Gravity.START);
                holder.receiver_text_message.setText(message.getMessage());
            }
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        //initializing views
        TextView sender_text_message, receiver_text_message;
        RoundedImageView user_profile_image;
        RoundedImageView senderImageMsg, receiverImageMsg;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            sender_text_message = itemView.findViewById(R.id.senderMessageText);
            receiver_text_message = itemView.findViewById(R.id.receiverMessageText);
            user_profile_image = itemView.findViewById(R.id.messageUserImage);

            senderImageMsg = itemView.findViewById(R.id.messageImageVsender);
            receiverImageMsg = itemView.findViewById(R.id.messageImageVreceiver);
        }
    }
}
