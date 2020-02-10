package com.project.ksih_android.ui.others;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.project.ksih_android.R;

import static com.project.ksih_android.utility.Constants.ZOOM_IMAGE_GENERAL_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpDialogFragment extends DialogFragment {

    String[] addresses;

    public HelpDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_help_dialog, null);
        builder.setView(view);

        TextView emailSent = view.findViewById(R.id.email_sent);
        emailSent.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "ginowinel@gmail.com", null
            ));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
            startActivity(Intent.createChooser(emailIntent, "send email..."));
        });

        TextView callNow = view.findViewById(R.id.call_now);
        callNow.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+2348073336523"));
            startActivity(callIntent);
        });

        return builder.create();
    }

}
