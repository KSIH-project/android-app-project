package com.project.ksih_android.ui.events;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import static com.project.ksih_android.utility.Constants.EVENTS_ITEM_KEY;


import androidx.annotation.Nullable;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.ksih_android.R;
import com.project.ksih_android.data.Events;
import com.project.ksih_android.databinding.FragmentEventDetailsBinding;


import static com.project.ksih_android.utility.Constants.EVENTS_FIREBASE_PATH;
import static com.project.ksih_android.utility.Methods.loadUrlWithChromeTab;

/**
 * Created by ChukwuwaUchenna
 */
public class EventDetailsFragment extends Fragment {
    private FragmentEventDetailsBinding binding;

    private Events mEvents;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_details, container, false);

        mEvents = getArguments().getParcelable(EVENTS_ITEM_KEY);

        binding.textEventEmailDetails.setText(mEvents.getEmail());
        binding.textEventsDescripDetails.setText(mEvents.getEventDescription());
        binding.textPhoneNumber.setText(mEvents.getPhoneNumber());
        binding.textEventDate.setText(mEvents.getDate());
        binding.textEventTime.setText(mEvents.getTime());
        binding.textEventsLinkDetails.setText(mEvents.getEventRSVP());
        binding.textEventsTypeDetails.setText(mEvents.getEventType());
        Glide.with(requireContext()).load(mEvents.getImageUrl()).into(binding.imageEventsDetails);
        setupToolBar(binding.eventsDetailsToolbar);


        return binding.getRoot();
    }

    private void setupToolBar(MaterialToolbar eventsDetailsToolbar) {
        eventsDetailsToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        eventsDetailsToolbar.setTitle(mEvents.getEventName());
        eventsDetailsToolbar.inflateMenu(R.menu.events_menu);
        eventsDetailsToolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        eventsDetailsToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.events_edit_menu)
                editEvent();
            else if (item.getItemId() == R.id.events_delete_menu)
                alertMessage();
            return true;
        });
    }

    private void alertMessage() {
        new MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle("Delete Event")
                .setMessage(getString(R.string.event_delete_alert_message))
                .setIcon(R.drawable.ksih_background)
                .setPositiveButton("Yes", (dialogInterface, i) -> deleteEvent(mEvents.getId()))
                .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void deleteEvent(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(EVENTS_FIREBASE_PATH);
        reference.child(id).removeValue().addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "Item removed!", Toast.LENGTH_SHORT).show();
                deleteImage(mEvents.getImageUrl());
                Navigation.findNavController(requireView()).navigate(R.id.action_eventDetailsFragment_to_navigation_event);
            } else {
                Toast.makeText(requireContext(), "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void deleteImage(String imageUrl) {
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        reference.delete().addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "Image Successfully Removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Image Delete Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editEvent() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("eventToEdit", mEvents);
        Navigation.findNavController(requireView()).navigate(R.id.action_eventDetailsFragment_to_eventAddFragment, bundle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = new Bundle();
        bundle.putString("eventsImage", mEvents.getImageUrl());
        binding.imageEventsDetails.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.zoomFragment, bundle));

    }
}
