package com.project.ksih_android.ui.events;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ksih_android.R;
import com.project.ksih_android.data.Events;
import com.project.ksih_android.databinding.FragmentEventAddBinding;

import org.jetbrains.annotations.NotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventAddFragment extends Fragment {

    private FragmentEventAddBinding binding;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_add, container, false);
        binding.buttonAddEvents.setOnClickListener(v -> {
            addEvents();
        });

        return binding.getRoot();
    }

    private void addEvents() {
        String tittle = binding.textInputLayoutTittle.getEditText().getText().toString();
        String type = binding.textInputLayoutType.getEditText().getText().toString();
        String descp = binding.textInputLayoutDesc.getEditText().getText().toString();
        String contactsEmail = binding.textInputLayoutContactsEmail.getEditText().getText().toString();
        String contactsPhone = binding.textInputLayoutPhone.getEditText().getText().toString();
        String rsvp = binding.textInputLayoutRsvp.getEditText().getText().toString();
        Events mEvents = new Events("", tittle, contactsEmail, contactsPhone, descp, type, rsvp);
        databaseReference.setValue(mEvents).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Event Added", Toast.LENGTH_SHORT).show();
                navigateToEventsListFragment(binding.buttonAddEvents);
            } else {
                Toast.makeText(getContext(), "Try Again", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void navigateToEventsListFragment(View v) {
        Navigation.findNavController(v).navigate(R.id.action_eventAddFragment_to_navigation_event);
    }

}
