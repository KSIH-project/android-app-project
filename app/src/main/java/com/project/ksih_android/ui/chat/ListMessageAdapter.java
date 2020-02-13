package com.project.ksih_android.ui.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.data.ChatMessage;
import com.project.ksih_android.data.User;
import com.project.ksih_android.ui.zoom.ZoomFragment;
import com.project.ksih_android.utility.Constants;

import java.util.List;

import timber.log.Timber;

public class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<ChatMessage> chatMessage;
    private FirebaseAuth mAuth;
    private List<User> mUserList;
    private DatabaseReference mRef;
    private User mUser;

    public ListMessageAdapter(Context context, List<ChatMessage> chatMessage){
        this.context = context;
        this.chatMessage = chatMessage;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ChatFragment.VIEW_TYPE_FRIEND_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_friend, parent, false);
            return new ItemMessageFriendHolder(view);
        } else if (viewType == ChatFragment.VIEW_TYPE_USER_MESSAGE) {
            View view = LayoutInflater.from(context).inflate(R.layout.rc_item_message_user, parent, false);
            return new ItemMessageUserHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //for friend chat
        if (chatMessage.get(position).getText() != null){
            if (holder instanceof ItemMessageFriendHolder){
                ((ItemMessageFriendHolder)holder).txtContent.setVisibility(View.VISIBLE);
                ((ItemMessageFriendHolder)holder).chatTimeFriend.setVisibility(View.VISIBLE);
                ((ItemMessageFriendHolder)holder).messageImageView.setVisibility(View.GONE);
                ((ItemMessageFriendHolder)holder).chatTimeFriendImage.setVisibility(View.GONE);
                ((ItemMessageFriendHolder)holder).txtContent.setText(chatMessage.get(position).getText());
                ((ItemMessageFriendHolder)holder).messengerTextView.setText(chatMessage.get(position).getName());
                ((ItemMessageFriendHolder)holder).chatTimeFriend.setText(chatMessage.get(position).getTime());

                if (chatMessage.get(position).getPhotoUrl() ==null){
                    ((ItemMessageFriendHolder)holder).messangerImageView.setImageDrawable(ContextCompat
                            .getDrawable(context, R.drawable.ic_profile_photo));
                }else {
                    Glide.with(context)
                            .load(chatMessage.get(position).getPhotoUrl())
                            .into(((ItemMessageFriendHolder)holder).messangerImageView);
                }

                ((ItemMessageFriendHolder)holder).messangerImageView.setOnClickListener(v -> {
                    ZoomFragment zoomFragment = new ZoomFragment();
                    Bundle args = new Bundle();
                    args.putString("photo_url", chatMessage.get(position).getPhotoUrl() );
                    zoomFragment.setArguments(args);
                    Navigation.findNavController(v).navigate(R.id.editPhotoFragment, args);
                });


            }else if (holder instanceof ItemMessageUserHolder){
                ((ItemMessageUserHolder)holder).txtContent.setVisibility(View.VISIBLE);
                ((ItemMessageUserHolder)holder).chatTimeUser.setVisibility(View.VISIBLE);
                ((ItemMessageUserHolder)holder).messageImageView.setVisibility(View.GONE);
                ((ItemMessageUserHolder)holder).chatTimeUserImage.setVisibility(View.GONE);
                ((ItemMessageUserHolder)holder).txtContent.setText(chatMessage.get(position).getText());
                ((ItemMessageUserHolder)holder).chatTimeUser.setText(chatMessage.get(position).getTime());

                if (chatMessage.get(position).getPhotoUrl() == null){
                    ((ItemMessageUserHolder)holder).messengerImageView.setImageDrawable(ContextCompat
                    .getDrawable(context, R.drawable.ic_profile_photo));
                }else {
                    Glide.with(context)
                            .load(chatMessage.get(position).getPhotoUrl())
                            .into(((ItemMessageUserHolder)holder).messengerImageView);
                }
            }
        }else if (chatMessage.get(position).getImageUrl() != null){
            if (holder instanceof ItemMessageFriendHolder){
                ((ItemMessageFriendHolder)holder).txtContent.setVisibility(View.INVISIBLE);
                ((ItemMessageFriendHolder)holder).chatTimeFriend.setVisibility(View.INVISIBLE);
                ((ItemMessageFriendHolder)holder).messageImageView.setVisibility(View.VISIBLE);
                ((ItemMessageFriendHolder)holder).chatTimeFriendImage.setVisibility(View.VISIBLE);

                ((ItemMessageFriendHolder)holder).messengerTextView.setText(chatMessage.get(position).getName());
                ((ItemMessageFriendHolder)holder).chatTimeFriendImage.setText(chatMessage.get(position).getTime());

                String imageUrl = chatMessage.get(position).getImageUrl();
                if (imageUrl.startsWith("gs://")) {
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReferenceFromUrl(imageUrl);
                    storageReference.getDownloadUrl().addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    String downloadUrl = task.getResult().toString();
                                    Glide.with(context)
                                            .load(downloadUrl)
                                            .into(((ItemMessageFriendHolder)holder).messageImageView);

                                } else {
                                    Timber.d( "Getting download url was not successful."+ task.getException());
                                }
                            });
                }else {
                    Glide.with(context)
                            .load(chatMessage.get(position).getImageUrl())
                            .into(((ItemMessageFriendHolder)holder).messageImageView);
                }

                if (chatMessage.get(position).getPhotoUrl() ==null){
                    ((ItemMessageFriendHolder)holder).messangerImageView.setImageDrawable(ContextCompat
                            .getDrawable(context, R.drawable.ic_profile_photo));
                }else {
                    Glide.with(context)
                            .load(chatMessage.get(position).getPhotoUrl())
                            .into(((ItemMessageFriendHolder)holder).messangerImageView);
                }

                ((ItemMessageFriendHolder)holder).messageImageView.setOnClickListener(v -> {
                    ZoomFragment zoomFragment = new ZoomFragment();
                    Bundle args = new Bundle();
                    args.putString(Constants.ZOOM_IMAGE_GENERAL_KEY, chatMessage.get(position).getImageUrl() );
                    zoomFragment.setArguments(args);
                    Navigation.findNavController(v).navigate(R.id.zoomFragment, args);
                });

                ((ItemMessageFriendHolder)holder).messangerImageView.setOnClickListener(v -> {
                    ZoomFragment zoomFragment = new ZoomFragment();
                    Bundle args = new Bundle();
                    args.putString("photo_url", chatMessage.get(position).getPhotoUrl() );
                    zoomFragment.setArguments(args);
                    Navigation.findNavController(v).navigate(R.id.editPhotoFragment, args);
                });


            }if (holder instanceof ItemMessageUserHolder){
                ((ItemMessageUserHolder)holder).txtContent.setVisibility(View.INVISIBLE);
                ((ItemMessageUserHolder)holder).chatTimeUser.setVisibility(View.INVISIBLE);
                ((ItemMessageUserHolder)holder).messageImageView.setVisibility(View.VISIBLE);
                ((ItemMessageUserHolder)holder).chatTimeUserImage.setVisibility(View.VISIBLE);

                ((ItemMessageUserHolder)holder).chatTimeUserImage.setText(chatMessage.get(position).getTime());

                String imageUrl = chatMessage.get(position).getImageUrl();
                if (imageUrl.startsWith("gs://")) {
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReferenceFromUrl(imageUrl);
                    storageReference.getDownloadUrl().addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    String downloadUrl = task.getResult().toString();
                                    Glide.with(context)
                                            .load(downloadUrl)
                                            .into(((ItemMessageUserHolder)holder).messageImageView);

                                } else {
                                    Timber.d( "Getting download url was not successful."+ task.getException());
                                }
                            });
                }else {
                    Glide.with(context)
                            .load(chatMessage.get(position).getImageUrl())
                            .into(((ItemMessageUserHolder)holder).messageImageView);
                }


                if (chatMessage.get(position).getPhotoUrl() == null){
                    ((ItemMessageUserHolder)holder).messengerImageView.setImageDrawable(ContextCompat
                            .getDrawable(context, R.drawable.ic_profile_photo));
                }else {
                    Glide.with(context)
                            .load(chatMessage.get(position).getPhotoUrl())
                            .into(((ItemMessageUserHolder)holder).messengerImageView);
                }

                ((ItemMessageUserHolder)holder).messageImageView.setOnClickListener(v -> {
                    ZoomFragment zoomFragment = new ZoomFragment();
                    Bundle args = new Bundle();
                    args.putString(Constants.ZOOM_IMAGE_GENERAL_KEY, chatMessage.get(position).getImageUrl());
                    zoomFragment.setArguments(args);
                    Navigation.findNavController(v).navigate(R.id.zoomFragment, args);
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return chatMessage.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
       FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String senderID = mAuth.getCurrentUser().getUid();
        return chatMessage.get(position).getFrom().equals(senderID)
                ? ChatFragment.VIEW_TYPE_USER_MESSAGE : ChatFragment.VIEW_TYPE_FRIEND_MESSAGE;
    }

    class ItemMessageUserHolder extends RecyclerView.ViewHolder {

        TextView txtContent;
        RoundedImageView messageImageView;
        RoundedImageView messengerImageView;
        TextView chatTimeUser, chatTimeUserImage;


        public ItemMessageUserHolder(@NonNull View itemView) {
            super(itemView);

            //sender message details
            messageImageView = itemView.findViewById(R.id.messageUserImage);
            messengerImageView = itemView.findViewById(R.id.messangerImageView);
            chatTimeUser = itemView.findViewById(R.id.chat_time_user);
            chatTimeUserImage = itemView.findViewById(R.id.chat_time_user_image);
            //receiver message details
            txtContent = itemView.findViewById(R.id.textContentUser);
        }
    }

    class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
        public TextView txtContent;
        public RoundedImageView messageImageView;
        public RoundedImageView messangerImageView;
        public TextView messengerTextView;
        public TextView chatTimeFriend, chatTimeFriendImage;

        public ItemMessageFriendHolder(View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.textContentFriend);
            messangerImageView = itemView.findViewById(R.id.imageView3);
            messageImageView = itemView.findViewById(R.id.messageFriendImage);
            messengerTextView = itemView.findViewById(R.id.messangerTextView);
            chatTimeFriend = itemView.findViewById(R.id.chat_time_friend);
            chatTimeFriendImage = itemView.findViewById(R.id.chat_time_friend_image);
        }
    }

}
