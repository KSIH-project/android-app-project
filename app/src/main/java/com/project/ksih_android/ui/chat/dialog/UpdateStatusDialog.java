package com.project.ksih_android.ui.chat.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ksih_android.R;
import com.victor.loading.rotate.RotateLoading;

public class UpdateStatusDialog extends DialogFragment {
    private EditText status_from_input;
    private RotateLoading loading;
    TextView confirmStatus;

    //firebase
    private DatabaseReference statusDatabaseReference;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_update_status, container, false);


        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        statusDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

        status_from_input = view.findViewById(R.id.input_status);
        confirmStatus = view.findViewById(R.id.profile_update_confirm);
        confirmStatus.setOnClickListener(view1 -> {
            String new_status = status_from_input.getText().toString();
            changeProfileStatus(new_status);
        });

        /**
         * Todo retrieve previous profile status from settings Fragment
         */

        return view;
    }

    private void changeProfileStatus(String new_status){
        if (TextUtils.isEmpty(new_status)){
            Toast.makeText(getContext(), "Please write", Toast.LENGTH_SHORT).show();
        }else {
            loading.start();

            statusDatabaseReference.child("user_status").setValue(new_status)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            loading.stop();
                            //navigate backward
                        }else {
                            Toast.makeText(getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                        }

                    });
        }
    }
}
