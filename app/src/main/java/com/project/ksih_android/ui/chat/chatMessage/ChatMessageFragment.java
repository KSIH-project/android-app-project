package com.project.ksih_android.ui.chat.chatMessage;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.icu.util.ValueIterator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.ui.chat.ChatHolderFragment;
import com.project.ksih_android.ui.chat.adapters.MessageAdapter;
import com.project.ksih_android.ui.chat.models.Message;
import com.project.ksih_android.utility.Constants;
import com.project.ksih_android.utility.UserLastSeenTime;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatMessageFragment extends Fragment {

    //variables
    private String messageReceiverID;
    private String messageReceiverName;
    private LinearLayout chatNavigateBack;
    private TextView chatUserName;
    private TextView chatUserActiveStatus, ChatConnectionTV;
    private RoundedImageView chatUserImageView;

    private DatabaseReference rootReference;

    //sending message
    private ImageView send_message, send_image;
    private EditText input_user_message;
    private FirebaseAuth mAuth;
    private String messageSenderId, download_url;

    private RecyclerView messageList_ReCyVw;
    private final List<Message> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;

    private StorageReference imageMessageStorageRef;
    private ConnectivityReceiver connectivityReceiver;

    public ChatMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_message, container, false);

        //firebase initialization
        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();

        //receive user details intent
        /**
         * Todo receive intent for message receiver details
         */

        imageMessageStorageRef = FirebaseStorage.getInstance().getReference().child("messages_image");

        chatNavigateBack = view.findViewById(R.id.chat_navigate_back);
        chatNavigateBack.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.nav_chats));


        //set views
        ChatConnectionTV = view.findViewById(R.id.ChatConnectionTV);
        chatUserName = view.findViewById(R.id.chat_user_name);
        chatUserActiveStatus = view.findViewById(R.id.chat_active_status);
        chatUserImageView = view.findViewById(R.id.chat_profile_image);
        send_message = view.findViewById(R.id.c_send_message_BTN);
        send_image = view.findViewById(R.id.c_send_image_BTN);
        input_user_message = view.findViewById(R.id.c_input_message);

        //setup for showing message
        messageAdapter = new MessageAdapter(messageList);
        messageList_ReCyVw = view.findViewById(R.id.message_list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        messageList_ReCyVw.setLayoutManager(linearLayoutManager);
        messageList_ReCyVw.setHasFixedSize(true);
        messageList_ReCyVw.setAdapter(messageAdapter);

        fetchMessage();

        chatUserName.setText(messageReceiverName);
        rootReference.child("users").child(messageReceiverID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String active_status = dataSnapshot.child("active_now").getValue().toString();
                        final String thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();

                        rootReference.child("active_now").setValue(ServerValue.TIMESTAMP);
                        //show image on appBar
                        Picasso.get()
                                .load(thumb_image)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.default_profile_image)
                                .into(chatUserImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get()
                                                .load(thumb_image)
                                                .placeholder(R.drawable.default_profile_image)
                                                .into(chatUserImageView);
                                    }
                                });
                        //active status
                        if (active_status.contains("true")){
                            chatUserActiveStatus.setText("Active_now");
                        }else {
                            UserLastSeenTime lastSeenTime = new UserLastSeenTime();
                            long last_seen = Long.parseLong(active_status);

                            String lastSeenOnScreenTime = UserLastSeenTime.getTimeAgo(last_seen, getContext());
                            Timber.d("lastSeenTime%s", lastSeenOnScreenTime);

                            if (lastSeenOnScreenTime != null){
                                chatUserActiveStatus.setText(lastSeenOnScreenTime);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        //send text message button
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        //send image message button
        send_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent().setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Constants.GALLERY_PICK_CODE_MESSAGE);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        connectivityReceiver = new ConnectivityReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireContext().registerReceiver(connectivityReceiver, intentFilter);
    }

    public class ConnectivityReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = ((ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE));
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            ChatConnectionTV.setVisibility(View.GONE);
            if (networkInfo != null && networkInfo.isConnected()){
                ChatConnectionTV.setText("Intenet connected");
                ChatConnectionTV.setTextColor(Color.WHITE);
                ChatConnectionTV.setVisibility(View.VISIBLE);

                //set timer
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               ChatConnectionTV.setVisibility(View.GONE);
                           }
                       });
                    }
                }, 1200);
            }else {
                ChatConnectionTV.setText("No internet connection! ");
                ChatConnectionTV.setTextColor(Color.WHITE);
                ChatConnectionTV.setBackgroundColor(Color.RED);
                ChatConnectionTV.setVisibility(View.VISIBLE);
            }
        }
    }

    private void sendMessage() {
    }

    private void fetchMessage() {
    }

}
