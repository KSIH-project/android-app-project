package com.project.ksih_android.ui.chat.userSettings;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

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
import com.project.ksih_android.utility.Constants;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileSettingsFragment extends Fragment {

    //variables
    private RoundedImageView profile_settings_image;
    private TextView display_status, updatedMsg, recheckGender;
    private ImageView editPhotoIcon, editStatusBtn;
    private EditText display_name, display_email, user_phone, user_profession, user_nickname;
    private RadioButton maleRB, femaleRB;

    private Button saveInfoBtn;

    //firebase
    private DatabaseReference getUserDatabaseReference;
    private FirebaseAuth mAuth;
    private StorageReference mProfileStorageRef;
    private StorageReference thumb_image_ref;

    Bitmap thumb_Bitmap = null;
    private ProgressDialog progressDialog;
    private String selectedGender = "", profile_download_url, profile_thumb_download_url;


    public ProfileSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_settings, container, false);

        Toolbar toolbar = view.findViewById(R.id.edit_profile_appbar);
        toolbar.setTitle("Edit Profile");


        //firebase
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        getUserDatabaseReference.keepSynced(true);

        mProfileStorageRef = FirebaseStorage.getInstance().getReference().child("profile_image");
        thumb_image_ref = FirebaseStorage.getInstance().getReference().child("thumb_image");

        //initializing views
        profile_settings_image = view.findViewById(R.id.profile_img);
        display_name = view.findViewById(R.id.user_display_name);
        user_nickname = view.findViewById(R.id.user_nickname);
        user_profession = view.findViewById(R.id.profession);
        display_email = view.findViewById(R.id.userEmail);
        user_phone = view.findViewById(R.id.phone);
        display_status = view.findViewById(R.id.userProfileStatus);
        editPhotoIcon = view.findViewById(R.id.editPhotoIcon);
        saveInfoBtn = view.findViewById(R.id.saveInfoBtn);
        editStatusBtn = view.findViewById(R.id.statusEdit);
        updatedMsg = view.findViewById(R.id.updateMsg);
        recheckGender = view.findViewById(R.id.recheckGender);
        recheckGender.setVisibility(View.VISIBLE);

        maleRB = view.findViewById(R.id.maleRB);
        femaleRB = view.findViewById(R.id.femaleRB);

        progressDialog = new ProgressDialog(getContext());

        //Retrieve data from firebase
        getUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("user_name").getValue().toString();
                String nickname = dataSnapshot.child("user_nickname").getValue().toString();
                String profession = dataSnapshot.child("user_profession").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                String email = dataSnapshot.child("user_email").getValue().toString();
                String phone = dataSnapshot.child("user_mobile").getValue().toString();
                String gender = dataSnapshot.child("user_gender").getValue().toString();
                final String image = dataSnapshot.child("user_image").getValue().toString();
                String thumbImage = dataSnapshot.child("user_thumb_image").getValue().toString();
                
                //setting parameters for display
                display_status.setText(status);
                display_name.setText(name);
                display_name.setSelection(display_name.getText().length());
                user_nickname.setText(nickname);
                user_nickname.setSelection(user_nickname.getText().length());
                user_profession.setText(profession);
                user_profession.setSelection(user_profession.getText().length());
                user_phone.setText(phone);
                user_phone.setSelection(user_phone.getText().length());
                display_email.setText(email);
                
                //condition for image
                if (!image.equals("default_image")){
                    Picasso.get()
                            .load(image)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_profile_image)
                            .error(R.drawable.default_profile_image)
                            .into(profile_settings_image);
                }
                
                if (gender.equals("Male")){
                    maleRB.setChecked(true);
                }else if (gender.equals("Female")){
                    femaleRB.setChecked(true);
                }else {
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /**
         * change profile photo from gallery
         */
        editPhotoIcon.setOnClickListener(view1 -> {
            Intent gallaeryIntent = new Intent();
            gallaeryIntent.setType("image/*");
            gallaeryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(gallaeryIntent, Constants.GALLERY_PICK_CODE);
        });
        
        //Edit information
        saveInfoBtn.setOnClickListener(view12 -> {
            String uName = display_name.getText().toString();
            String uNickname = user_nickname.getText().toString();
            String uPhone = user_phone.getText().toString();
            String uProfession = user_profession.getText().toString();
            
            saveInformation(uName, uNickname, uPhone, uProfession, selectedGender);
        });

        //Edit status
        editStatusBtn.setOnClickListener(view13 -> {
            String previous_status = display_status.getText().toString();

            /**
             * Todo send Intent to Status Dialog
             */
        });

        //hide soft Keyboard
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        selectedGenderRB(view);

        return  view;
    }

    private void selectedGenderRB(View view){
        boolean checked = ((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.maleRB:
                if (checked){
                    selectedGender = "Male";
                    recheckGender.setVisibility(View.GONE);
                    break;
                }
            case R.id.femaleRB:
                if (checked){
                    selectedGender = "Female";
                    recheckGender.setVisibility(View.GONE);
                    break;
                }
        }
    }

    private void saveInformation(String uName, String uNickname, String uPhone, String uProfession,
                                 String uGender) {
            if (uGender.length()<1){
                recheckGender.setTextColor(Color.RED);
            }
    }

}
