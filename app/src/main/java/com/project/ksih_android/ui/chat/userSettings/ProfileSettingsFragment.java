package com.project.ksih_android.ui.chat.userSettings;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.utility.Constants;

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
    private Constants mConstants;
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




        return  view;
    }

}
