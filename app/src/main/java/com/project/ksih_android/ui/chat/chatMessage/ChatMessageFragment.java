package com.project.ksih_android.ui.chat.chatMessage;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.icu.util.ValueIterator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatMessageFragment extends Fragment {

    //variables
    private String messageReceiverID;
    private String messageReceiverName;
    private FrameLayout chatNavigateBack;
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
        rootReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();

        //receive user details intent
        /**
         * Todo receive intent from chat fragment for message receiver details
         */
            messageReceiverID = getArguments().getString("visitUserId");
            messageReceiverName = getArguments().getString("userName");

        imageMessageStorageRef = FirebaseStorage.getInstance().getReference().child("messages_image");

        chatNavigateBack = view.findViewById(R.id.chat_navigate_back);
        chatNavigateBack.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.action_friendList_to_nav_chats2));


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
        send_message.setOnClickListener(view12 -> sendMessage());

        //send image message button
        send_image.setOnClickListener(view13 -> {
            Intent galleryIntent = new Intent().setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, Constants.GALLERY_PICK_CODE_MESSAGE);
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

    @Override
    public void onStop() {
        super.onStop();
        requireContext().unregisterReceiver(connectivityReceiver);
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
                       getActivity().runOnUiThread(() -> ChatConnectionTV.setVisibility(View.GONE));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GALLERY_PICK_CODE_MESSAGE && resultCode == RESULT_OK && data!= null
        && data.getData() != null){
            Uri imageUri = data.getData();

            final String message_sender_reference = "messages/" + messageSenderId + "/" + messageReceiverID;
            final String message_receiver_reference = "messages/" + messageReceiverID + '/' + messageSenderId;

            DatabaseReference user_message_key = rootReference.child("messages").child(messageSenderId).child(messageReceiverID).push();
            final String message_push_id = user_message_key.getKey();

            final StorageReference file_path = imageMessageStorageRef.child(message_push_id + ".jpg");

            UploadTask uploadTask = file_path.putFile(imageUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()){
                    Toast.makeText(getContext(), "Upload fail", Toast.LENGTH_SHORT).show();
                }
                download_url = file_path.getDownloadUrl().toString();
                return file_path.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    download_url = task.getResult().toString();

                    HashMap<String, Object> message_text_body = new HashMap<>();
                    message_text_body.put("message", download_url);
                    message_text_body.put("seen", false);
                    message_text_body.put("type", "image");
                    message_text_body.put("time", ServerValue.TIMESTAMP);
                    message_text_body.put("from", messageSenderId);

                    HashMap<String, Object> messageBodyDetails = new HashMap<>();
                    messageBodyDetails.put(message_sender_reference + '/' + message_push_id, message_text_body);
                    messageBodyDetails.put(message_receiver_reference + "/" + message_push_id, message_text_body);

                    rootReference.updateChildren(messageBodyDetails, (databaseError, databaseReference) -> {
                        if (databaseError != null){
                            Timber.d("from_image_chat %s", databaseError.getMessage());
                        }
                        input_user_message.setText("");
                    });
                    Timber.d("Image sent Successfully");
                }else {
                    Toast.makeText(getContext(), "Failed to send image", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendMessage() {
        String message = input_user_message.getText().toString();
        if (TextUtils.isEmpty(message)){
            Toast.makeText(getContext(), "Please type a message", Toast.LENGTH_SHORT).show();
        }else {
            String message_sender_reference = "messages/" + messageSenderId + "/" + messageReceiverID;
            String message_receiver_reference = "messages/" + messageReceiverID + "/" + messageSenderId;

            DatabaseReference user_message_key = rootReference.child("messages").child(messageSenderId).child(messageReceiverID).push();
            String message_push_id = user_message_key.getKey();

            HashMap<String, Object> message_text_body = new HashMap<>();
            message_text_body.put("message", message);
            message_text_body.put("seen", false);
            message_text_body.put("type", "text");
            message_text_body.put("time", ServerValue.TIMESTAMP);
            message_text_body.put("from", messageSenderId);

            HashMap<String, Object> messageBodyDetails = new HashMap<>();
            messageBodyDetails.put(message_sender_reference + "/" + message_push_id, message_text_body);
            messageBodyDetails.put(message_receiver_reference + "/" + message_push_id, message_text_body);

            rootReference.updateChildren(messageBodyDetails, (databaseError, databaseReference) -> {
                if (databaseError != null){
                    Timber.d("Sending message%s", databaseError.getMessage());
                }
                input_user_message.setText("");
            });
        }
    }

    private void fetchMessage() {
        rootReference.child("messages").child(messageSenderId).child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (dataSnapshot.exists()){
                            Message message = dataSnapshot.getValue(Message.class);
                            messageList.add(message);
                            messageAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
