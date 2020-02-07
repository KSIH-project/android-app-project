package com.project.ksih_android.ui.others;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.project.ksih_android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView textViewAppinfo = root.findViewById(R.id.app_info);
        textViewAppinfo.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_settings_to_appInfo));

        TextView textViewAboutKsih = root.findViewById(R.id.text_about_ksih);
        textViewAboutKsih.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_settings_to_aboutKsihFragment));

        TextView textViewSignOut = root.findViewById(R.id.text_signout);
        textViewSignOut.setOnClickListener(v -> {
            if (mFirebaseAuth.getCurrentUser() != null) {
                dialogQuestion();
            } else {
               // Toast.makeText(getActivity(), "Please Sign in!", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(root.findViewById(R.id.settingsLayout),"Please Sign in!",Snackbar.LENGTH_LONG)
                        .setAction("Sign In", v1 -> {
                            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_nav_settings_to_loginFragment);
                        });
                snackbar.show();
            }
        });
        return root;
    }

    private void userSignOut() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            mFirebaseAuth.signOut();
            Toast.makeText(getActivity(), "SignOut Successfully", Toast.LENGTH_SHORT).show();
        }/* else {
            Toast.makeText(getActivity(), "Please Sign in!", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void dialogQuestion() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity());
        materialAlertDialogBuilder.setTitle("Sign out!");
        materialAlertDialogBuilder.setIcon(R.drawable.ksih_background);
        materialAlertDialogBuilder.setMessage("Are you sure you want to Sign out?");
        materialAlertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> userSignOut());
        materialAlertDialogBuilder.setNegativeButton("No", (dialog, which) -> {
        });
        materialAlertDialogBuilder.create().show();
    }
}
