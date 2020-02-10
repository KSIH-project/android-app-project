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
import androidx.navigation.NavController;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ksih_android.R;
import com.project.ksih_android.data.ChatMessage;
import com.project.ksih_android.utility.Constants;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

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
    private AutoCompleteTextView mMessageEditText;
    private ImageView mAddMessageImageView;
    LinearLayoutManager mLinearLayoutManager;



    //for message
    public static final int VIEW_TYPE_USER_MESSAGE = 0;
    public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
    public ArrayList<ChatMessage> chatMessages =new ArrayList<>();

    //firebase utils
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabaseReference;
    public FirebaseUser currentUser;
    private ListMessageAdapter  mFirebaseAdapter;
    private ArrayList<String> newList;
    private LinkedHashSet<String> linkedHashSet;


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
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(getParentFragment().getContext(), "SignIn to use chat", Toast.LENGTH_SHORT).show();

            NavController controller = Navigation.findNavController(getParentFragment().getView());
            controller.popBackStack(R.id.nav_chats, true);
            controller.navigate(R.id.loginFragment);
        }else {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            mUserName = currentUser.getDisplayName();
            if (currentUser.getPhotoUrl() != null){
                mPhotoUrl = currentUser.getPhotoUrl().toString();
            }
        }

        //setting views
        mMessageRecyclerView = root.findViewById(R.id.zoomFragment);

        //initialLIZING LAYOUTS
        mLinearLayoutManager = new LinearLayoutManager(requireContext());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //get messages back
        FirebaseDatabase.getInstance().getReference().child(Constants.MESSAGES_CHILD)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessages.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                        ChatMessage chatMessage = dataSnapshot1.getValue(ChatMessage.class);
                        chatMessages.add(chatMessage);
                    }
                }

                Timber.d(chatMessages.toString());
                mFirebaseAdapter = new ListMessageAdapter(getContext(), chatMessages);
                mMessageRecyclerView.setAdapter(mFirebaseAdapter);
                mFirebaseAdapter.notifyDataSetChanged();

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //initialize edittext logic
        mSendButton = root.findViewById(R.id.sendButton);
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
        mSendButton.setOnClickListener(view -> {


            if (mMessageEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "write something", Toast.LENGTH_SHORT).show();
            }else{
                String user_uID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                FirebaseDatabase.getInstance().getReference().child("users").child(user_uID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String userName = dataSnapshot.child("user_name").getValue().toString();
                                String userPic = dataSnapshot.child("user_image").getValue().toString();

                                DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
                                String date = dfDate.format(Calendar.getInstance().getTime());
                                DateFormat dfTime = new SimpleDateFormat("HH:mm");
                                String time = dfTime.format(Calendar.getInstance().getTime());
                                String setTime = date + " " + time;

                                //checking profile image and sending chat
                                if (!userPic.equals("default image")){
                                    if (!mMessageEditText.getText().toString().trim().isEmpty()) {
                                        ChatMessage friendlyMessage = new
                                                ChatMessage(mMessageEditText.getText().toString().trim(),
                                                userName,
                                                userPic,
                                                null /* no image */, user_uID, setTime);
                                        mFirebaseDatabaseReference.child(Constants.MESSAGES_CHILD)
                                                .push().setValue(friendlyMessage);
                                        mMessageEditText.setText("");

                                    }

                                }else {

                                    if (!mMessageEditText.getText().toString().trim().isEmpty()) {
                                        ChatMessage friendlyMessage = new
                                                ChatMessage(mMessageEditText.getText().toString().trim(),
                                                userName,
                                                mPhotoUrl,
                                                null /* no image */, user_uID, setTime);
                                        mFirebaseDatabaseReference.child(Constants.MESSAGES_CHILD)
                                                .push().setValue(friendlyMessage);
                                        mMessageEditText.setText("");

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }

        });

        mAddMessageImageView = root.findViewById(R.id.addMessageImageView);
        mAddMessageImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, Constants.REQUEST_IMAGE);
        });

//        displayChatUsers();

        return root;
    }


    private void displayChatUsers(){

        FirebaseDatabase.getInstance().getReference().child(Constants.MESSAGES_CHILD)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()){

                            String suggestion = suggestionSnapshot.child("name").getValue(String.class);
//
//                           Set<String> set = new HashSet<String>(arrayList);
                            newList = new ArrayList<>();
//                            newList.add(suggestion);

                            linkedHashSet = new LinkedHashSet<>();
                            linkedHashSet.add(suggestion);
                            newList.addAll(linkedHashSet);


                            final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(getParentFragment().getContext(),
                                    android.R.layout.simple_list_item_1, newList);

                            mMessageEditText.setAdapter(autoComplete);
                            Timber.d(autoComplete.toString());

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Register connectivity BroadcastReceiver
        connectivityReceiver = new ConnectivityReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(connectivityReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        requireContext().unregisterReceiver(connectivityReceiver);

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.profileFragment) {
            navigateToProfileFragment(getParentFragment().getView());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToProfileFragment(View view) {
        Navigation.findNavController(view).navigate(R.id.action_nav_chats_to_profileFragment);
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

                    String user_uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final Uri uri = data.getData();

                    FirebaseDatabase.getInstance().getReference().child("users").child(user_uID)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String userName = dataSnapshot.child("user_name").getValue().toString();
                                    String userPic = dataSnapshot.child("user_image").getValue().toString();


                                    DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
                                    String date = dfDate.format(Calendar.getInstance().getTime());
                                    DateFormat dfTime = new SimpleDateFormat("HH:mm");
                                    String time = dfTime.format(Calendar.getInstance().getTime());
                                    String setTime = date + " " + time;

                                    if (userPic.equals("default image")) {

                                        ChatMessage tempMessage = new ChatMessage(null, userName, mPhotoUrl,
                                                "", user_uID, setTime);

                                        mFirebaseDatabaseReference.child(Constants.MESSAGES_CHILD).push()
                                                .setValue(tempMessage, (databaseError, databaseReference) -> {
                                                    if (databaseError == null) {
                                                        String key = databaseReference.getKey();
                                                        StorageReference storageReference = FirebaseStorage.getInstance()
                                                                .getReference("images/chat_image/")
                                                                .child(key)
                                                                .child(uri.getLastPathSegment());

                                                        putImageInStorage(storageReference, uri, key);

                                                    } else {
                                                        Timber.d("unable to write to %s", databaseError.toException());
                                                    }
                                                });
                                    }else {

                                        ChatMessage tempMessage = new ChatMessage(null, userName, userPic,
                                                "", user_uID, setTime);

                                        mFirebaseDatabaseReference.child(Constants.MESSAGES_CHILD).push()
                                                .setValue(tempMessage, (databaseError, databaseReference) -> {
                                                    if (databaseError == null) {
                                                        String key = databaseReference.getKey();
                                                        StorageReference storageReference = FirebaseStorage.getInstance()
                                                                .getReference("images/chat_image/")
                                                                .child(key)
                                                                .child(uri.getLastPathSegment());

                                                        putImageInStorage(storageReference, uri, key);

                                                    } else {
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

                                String user_uID = FirebaseAuth.getInstance().getCurrentUser().getUid();


                                FirebaseDatabase.getInstance().getReference().child("users").child(user_uID)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                String userName = dataSnapshot.child("user_name").getValue().toString();
                                                String userPic = dataSnapshot.child("user_image").getValue().toString();

                                                DateFormat dfDate = new SimpleDateFormat("yyyy/MM/dd");
                                                String date = dfDate.format(Calendar.getInstance().getTime());
                                                DateFormat dfTime = new SimpleDateFormat("HH:mm");
                                                String time = dfTime.format(Calendar.getInstance().getTime());
                                                String setTime = date + " " + time;

                                                if (userPic.equals("default image")) {
                                                    ChatMessage chatMessage = new ChatMessage(
                                                            null, userName, mPhotoUrl, task1.getResult().toString(),
                                                            user_uID, setTime);
                                                    mFirebaseDatabaseReference.child(Constants.MESSAGES_CHILD).child(key)
                                                            .setValue(chatMessage);
                                                }else {
                                                    ChatMessage chatMessage = new ChatMessage(
                                                            null, userName, userPic, task1.getResult().toString(),
                                                            user_uID, setTime);
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
