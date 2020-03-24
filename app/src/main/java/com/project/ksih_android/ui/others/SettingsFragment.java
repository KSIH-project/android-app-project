package com.project.ksih_android.ui.others;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.project.ksih_android.R;
import com.project.ksih_android.storage.SharedPreferencesStorage;
import com.project.ksih_android.ui.sharedViewModel.SharedViewModel;

import static com.project.ksih_android.utility.Constants.EMAIL_KEY;
import static com.project.ksih_android.utility.Constants.PROFILE_PHOTO_KEY;
import static com.project.ksih_android.utility.Constants.USERNAME_KEY;

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
                Snackbar.make(root.findViewById(R.id.constraintLayout),
                        "Please Sign In", Snackbar.LENGTH_LONG)
                        .setAction("Sign In", v1 ->
                                Navigation.findNavController(v).navigate(R.id.action_nav_settings_to_loginFragment)).show();

            }
        });

        TextView textInviteFriend = root.findViewById(R.id.invie_friend);
        textInviteFriend.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.project.ksih_android");
            intent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(intent, null);
            startActivity(shareIntent);
        });

        TextView textViewPrivacy = root.findViewById(R.id.privacy_policies);
        textViewPrivacy.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.privacy_policies));

        TextView textHelp = root.findViewById(R.id.help);
        textHelp.setOnClickListener(v -> {
            HelpDialogFragment helpDialogFragment = new HelpDialogFragment();
            helpDialogFragment.show(getFragmentManager(), "help");
        });

        return root;
    }

    private void userSignOut() {
        mFirebaseAuth.signOut();
        // Clear ViewModel and sharedPreference data
        clearData();
        Toast.makeText(getActivity(), "Sign Out Successful", Toast.LENGTH_SHORT).show();
    }

    private void clearData() {
        // Clear ViewModel and sharedPreference data
        SharedViewModel sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        SharedPreferencesStorage storage = new SharedPreferencesStorage(requireContext());
        sharedViewModel.userEmail.setValue("");
        sharedViewModel.username.setValue("");
        sharedViewModel.userProfilePhotoUrl.setValue("");
        storage.setUserName(USERNAME_KEY, "");
        storage.setUserEmail(EMAIL_KEY, "");
        storage.setProfilePhotoUrl(PROFILE_PHOTO_KEY, "");
    }

    private void dialogQuestion() {
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Sign out!")
                .setIcon(R.drawable.ksih_background)
                .setMessage("Are you sure you want to Sign out?")
                .setPositiveButton("Yes", (dialog, which) -> userSignOut())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
}
