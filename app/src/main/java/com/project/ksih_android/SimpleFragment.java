package com.project.ksih_android;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ksih_android.data.AboutDevelopersData;


/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleFragment extends Fragment {

    Button mButton;
    private EditText name, email, stack, mediumUrl, twitterUrl, facebookUrl, githubUrl, phoneNumber, linkedInUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simple, container, false);
        name = view.findViewById(R.id.editText_name);
        email = view.findViewById(R.id.editText_email);
        stack = view.findViewById(R.id.editText_stack);
        mediumUrl = view.findViewById(R.id.editText_meduimUri);
        twitterUrl = view.findViewById(R.id.editText_twitterUri);
        facebookUrl = view.findViewById(R.id.editText_facebookUri);
        githubUrl = view.findViewById(R.id.editText_githubUri);
        phoneNumber = view.findViewById(R.id.editText_phoneNumber);
        linkedInUrl = view.findViewById(R.id.editText_linkedinUri);
        mButton = view.findViewById(R.id.button_jesil_save);

        mButton.setOnClickListener(v -> {
            String userName = name.getText().toString();
            String userEmail = email.getText().toString();
            String userStack = stack.getText().toString();
            String userMediumUrl = mediumUrl.getText().toString();
            String userTwitterUrl = twitterUrl.getText().toString();
            String userFacebookUrl = facebookUrl.getText().toString();
            String userGithubUrl = githubUrl.getText().toString();
            String userPhoneNumber = phoneNumber.getText().toString();
            String userLinkedinUrl = linkedInUrl.getText().toString();

            AboutDevelopersData developersData = new AboutDevelopersData(
                    userName, userStack, userEmail, userPhoneNumber, userFacebookUrl, userTwitterUrl,
                    userGithubUrl, userLinkedinUrl, userMediumUrl);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("developer_profile");
            ref.push().setValue(developersData).addOnCompleteListener(task -> Toast.makeText(getParentFragment().requireContext(), "Saved", Toast.LENGTH_SHORT).show());
        });
        return view;
    }

}
