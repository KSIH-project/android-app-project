package com.project.ksih_android.ui.profile;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.project.ksih_android.R;
import com.project.ksih_android.data.User;

import static com.project.ksih_android.utility.Constants.ZOOM_IMAGE_GENERAL_KEY;

/**
 * A simple {@link Fragment} subclass.
 */

public class EditPhotoDialogFragment extends DialogFragment {

    private User mUser;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_photo, null);
        builder.setView(view);
        String photoUrl = null;
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable("photo_url");
            photoUrl = mUser.user_image;
        }
        ImageView imageView = view.findViewById(R.id.profile_imageView);
        ImageView imageViewInfo = view.findViewById(R.id.member_info_imageView);
        ImageView imageViewPhone = view.findViewById(R.id.member_phone_imageView);
        Glide.with(view.getContext())
                .load(photoUrl)
                .error(R.drawable.ic_profile_photo)
                .into(imageView);

        imageView.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            mUser = (User) getArguments().getSerializable("photo_url");
            bundle.putString(ZOOM_IMAGE_GENERAL_KEY, mUser.user_image);
            Navigation.findNavController(getParentFragment().getActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_editPhotoFragment_to_messageRecyclerView, bundle);
        });
        Bundle memberInfoBundle = new Bundle();
        memberInfoBundle.putSerializable("members_bundle", getArguments().getSerializable("photo_url"));
        imageViewInfo.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_editPhotoFragment_to_profileFragment, memberInfoBundle));
        imageViewPhone.setOnClickListener(v1 -> dialIntent(requireContext(), mUser.user_mobile));
        return builder.create();
    }

    private void dialIntent(Context context, String telephoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        Uri uri = Uri.parse("tel:" + telephoneNumber);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
