package com.project.ksih_android.ui.chat;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ksih_android.R;
import com.project.ksih_android.data.ChatMessage;
import com.project.ksih_android.ui.zoom.ZoomFragment;
import com.project.ksih_android.utility.Constants;
import com.victor.loading.rotate.RotateLoading;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.project.ksih_android.utility.Constants.ANONYMOUS;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    //Initialize variables
    public ConnectivityReceiver connectivityReceiver;
    private String mUserName;
    private String mPhotoUrl;
    private ImageView mSendButton;
    private RecyclerView mMessageRecyclerView;
    private RotateLoading loading;
    private EditText mMessageEditText;
    private ImageView mAddMessageImageView;
    LinearLayoutManager mLinearLayoutManager;

    //firebase utils
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabaseReference;
    public FirebaseUser currentUser;
    private FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder> mFirebaseAdapter;


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chat, container, false);


        //check and get current user data
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Navigation.findNavController(root).navigate(R.id.nav_signIn);
        }else {
            mUserName = currentUser.getDisplayName();
            if (currentUser.getPhotoUrl() != null){
                mPhotoUrl = currentUser.getPhotoUrl().toString();
            }
        }

        //setting views
        loading = root.findViewById(R.id.loading);
        mMessageRecyclerView = root.findViewById(R.id.messageRecyclerView);

        //initialLIZING LAYOUTS
        mLinearLayoutManager = new LinearLayoutManager(requireContext());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<ChatMessage> parser = snapshot -> {
            ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
            if (chatMessage != null){
                chatMessage.setId(snapshot.getKey());
            }
            return chatMessage;
        };

        DatabaseReference messageRef = mFirebaseDatabaseReference.child(Constants.MESSAGES_CHILD);
        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(messageRef, parser)
                .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>(options) {
            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.item_chat_message, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final MessageViewHolder viewHolder,
                                            int position,
                                            ChatMessage friendlyMessage) {
                loading.stop();
                if (friendlyMessage.getText() != null) {
                    viewHolder.messageTextView.setText(friendlyMessage.getText());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageImageView.setVisibility(ImageView.GONE);

                } else if (friendlyMessage.getImageUrl() != null) {
                    String imageUrl = friendlyMessage.getImageUrl();
                    if (imageUrl.startsWith("gs://")) {
                        StorageReference storageReference = FirebaseStorage.getInstance()
                                .getReferenceFromUrl(imageUrl);
                        storageReference.getDownloadUrl().addOnCompleteListener(
                                task -> {
                                    if (task.isSuccessful()) {
                                        String downloadUrl = task.getResult().toString();
                                        Glide.with(viewHolder.messageImageView.getContext())
                                                .load(downloadUrl)
                                                .into(viewHolder.messageImageView);
                                    } else {
                                        Timber.d( "Getting download url was not successful."+ task.getException());
                                    }
                                });
                    } else {
                        Glide.with(viewHolder.messageImageView.getContext())
                                .load(friendlyMessage.getImageUrl())
                                .into(viewHolder.messageImageView);
                    }
                    viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                    viewHolder.messageTextView.setVisibility(TextView.GONE);
                }
                    viewHolder.messengerTextView.setText(friendlyMessage.getName());
                if (friendlyMessage.getPhotoUrl() == null) {
                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(getContext(),
                            R.drawable.default_profile_image));
                } else {
                    Glide.with(getContext())
                            .load(friendlyMessage.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }

                viewHolder.messageImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ZoomFragment zoomFragment = new ZoomFragment();
                            Bundle bundle = new Bundle();
                            zoomFragment.setArguments(bundle);
                            bundle.putString("eventsImage", friendlyMessage.getImageUrl());
                            


                    }
                });
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });


        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

        //initialize edittext logic
        mMessageEditText = root.findViewById(R.id.messageEditText);
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

            //send message with sender details
        mSendButton = root.findViewById(R.id.sendButton);
        mSendButton.setOnClickListener(view -> {

            String user_uID = mAuth.getCurrentUser().getUid();
            DatabaseReference firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(user_uID);
            firebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String userName = dataSnapshot.child("user_name").getValue().toString();
                    String userImage = dataSnapshot.child("user_image").getValue().toString();

                    if (!userImage.equals("default image")){
                        ChatMessage friendlyMessage = new
                                ChatMessage(mMessageEditText.getText().toString(),
                                userName,
                                userImage,
                                null /* no image */);
                        mFirebaseDatabaseReference.child(Constants.MESSAGES_CHILD)
                                .push().setValue(friendlyMessage);
                        mMessageEditText.setText("");

                    }else {
                        ChatMessage friendlyMessage = new
                                ChatMessage(mMessageEditText.getText().toString(),
                                userName,
                                mPhotoUrl,
                                null /* no image */);
                        mFirebaseDatabaseReference.child(Constants.MESSAGES_CHILD)
                                .push().setValue(friendlyMessage);
                        mMessageEditText.setText("");
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        });

        mAddMessageImageView = root.findViewById(R.id.addMessageImageView);
        mAddMessageImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, Constants.REQUEST_IMAGE);
        });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Login to use chat session", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getParentFragment().getView()).navigate(R.id.nav_signIn);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Register connectivity BroadcastReceiver
        connectivityReceiver = new ConnectivityReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(connectivityReceiver, intentFilter);
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        requireContext().unregisterReceiver(connectivityReceiver);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public class ConnectivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

            } else {
                Snackbar snackbar = Snackbar
                        .make(getParentFragment().getView(), "No internet Connection! ", Snackbar.LENGTH_LONG)
                        .setAction("Check settings", view -> {
                            Intent settings = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                            context.startActivity(settings);
                        });
                // customizing snackbar
                snackbar.setActionTextColor(Color.BLACK);
                View view = snackbar.getView();
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.button_color_disabled));
                snackbar.setText("Check network");
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Timber.d("onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == Constants.REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null){

                    String user_uID = mAuth.getCurrentUser().getUid();
                    DatabaseReference firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference()
                            .child("users").child(user_uID);
                    firebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String userName = dataSnapshot.child("user_name").getValue().toString();
                            String userImage = dataSnapshot.child("user_image").getValue().toString();

                            if (!userImage.equals("default image")){
                                final Uri uri = data.getData();
                                ChatMessage tempMessage = new ChatMessage(null, userName, userImage, "");

                                mFirebaseDatabaseReference.child(Constants.MESSAGES_CHILD).push()
                                        .setValue(tempMessage, (databaseError, databaseReference) -> {
                                            if (databaseError == null){
                                                String key = databaseReference.getKey();
                                                StorageReference storageReference = FirebaseStorage.getInstance()
                                                        .getReference(currentUser.getUid())
                                                        .child(key)
                                                        .child(uri.getLastPathSegment());

                                                putImageInStorage(storageReference, uri, key);

                                            }else {
                                                Timber.d("unable to write to %s", databaseError.toException());
                                            }
                                        });
                            }else {
                                final Uri uri = data.getData();
                                ChatMessage tempMessage = new ChatMessage(null, userName, mPhotoUrl, "");

                                mFirebaseDatabaseReference.child(Constants.MESSAGES_CHILD).push()
                                        .setValue(tempMessage, (databaseError, databaseReference) -> {
                                            if (databaseError == null){
                                                String key = databaseReference.getKey();
                                                StorageReference storageReference = FirebaseStorage.getInstance()
                                                        .getReference(currentUser.getUid())
                                                        .child(key)
                                                        .child(uri.getLastPathSegment());

                                                putImageInStorage(storageReference, uri, key);

                                            }else {
                                                Timber.d("unable to write to %s", databaseError.toException());
                                            }
                                        });
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        }

    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key){
        storageReference.putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                task.getResult().getMetadata().getReference().getDownloadUrl()
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()){

                                String user_uID = mAuth.getCurrentUser().getUid();
                                DatabaseReference firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(user_uID);
                                firebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String userName = dataSnapshot.child("user_name").getValue().toString();
                                        String userImage = dataSnapshot.child("user_image").getValue().toString();

                                        if (!userImage.equals("default image")){
                                            ChatMessage chatMessage = new ChatMessage(
                                                    null, userName, userImage, task1.getResult().toString());
                                            mFirebaseDatabaseReference.child(Constants.MESSAGES_CHILD).child(key)
                                                    .setValue(chatMessage);
                                        }else {
                                            ChatMessage chatMessage = new ChatMessage(
                                                    null, userName, mPhotoUrl, task1.getResult().toString());
                                            mFirebaseDatabaseReference.child(Constants.MESSAGES_CHILD).child(key)
                                                    .setValue(chatMessage);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
            }else {
                Timber.d("Image upload task was not successful." + task.getException());
            }
        });
    }
}
