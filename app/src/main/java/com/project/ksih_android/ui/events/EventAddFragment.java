package com.project.ksih_android.ui.events;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.ksih_android.R;
import com.project.ksih_android.data.Events;
import com.project.ksih_android.databinding.FragmentEventAddBinding;

import static android.app.Activity.RESULT_OK;
import static com.project.ksih_android.utility.Constants.REQUEST_CODE;
import static com.project.ksih_android.utility.Constants.REQUEST_CODE_EVENTS_IMAGE;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventAddFragment extends Fragment {

    private EventViewModel viewModel;
    private Bitmap mBitmap;
    private String imagePath = "";
    private String imageUrl;
    private FragmentEventAddBinding binding;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_add, container, false);
        viewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        mBitmap = null;
        binding.imageViewAdd.setOnClickListener(view -> openGallery());
        binding.buttonAddEvents.setOnClickListener(v -> {
            addEvents();
            saveImage();
        });

        return binding.getRoot();
    }

    private void saveImage() {
        if (mBitmap != null)
            uploadImageToFireBaseStorage();
        else
            Toast.makeText(requireActivity(), "Select An Image", Toast.LENGTH_SHORT).show();
    }

    private void uploadImageToFireBaseStorage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
        byte[] data = outputStream.toByteArray();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getInstance().getReference("images/events_flyers/" + imagePath);
        UploadTask task = storageReference.putBytes(data);
        task.addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                task1.getResult().getStorage().getDownloadUrl().addOnCompleteListener(requireActivity(), task2 -> {
                    if (task2.isSuccessful()) {
                        imageUrl = task2.getResult().toString();
                        addEvents();
                        Toast.makeText(getActivity(), "Event added Successfully", Toast.LENGTH_SHORT).show();
                        navigateToEventsListFragment(binding.buttonAddEvents);
                    }
                });
            } else {
                Toast.makeText(getActivity(), task1.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_OK && requestCode == REQUEST_CODE_EVENTS_IMAGE) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    imagePath = uri.getLastPathSegment();
                    if (Build.VERSION.SDK_INT >= 29) {
                        ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), uri);
                        mBitmap = ImageDecoder.decodeBitmap(source);

                        Glide.with(requireContext()).load(mBitmap).into(binding.imageViewAdd);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Timber.d(e.getLocalizedMessage());
                }

            } else {
                Toast.makeText(requireActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_EVENTS_IMAGE);
    }

    private void addEvents() {
        String tittle = binding.textInputLayoutTittle.getEditText().getText().toString();
        String type = binding.textInputLayoutType.getEditText().getText().toString();
        String descp = binding.textInputLayoutDesc.getEditText().getText().toString();
        String contactsEmail = binding.textInputLayoutContactsEmail.getEditText().getText().toString();
        String contactsPhone = binding.textInputLayoutPhone.getEditText().getText().toString();
        String rsvp = binding.textInputLayoutRsvp.getEditText().getText().toString();
        Events mEvents = new Events(imageUrl, tittle, contactsEmail, contactsPhone, descp, type, rsvp);
        databaseReference.setValue(mEvents);
    }

    private void navigateToEventsListFragment(View v) {
        Navigation.findNavController(v).navigate(R.id.action_eventAddFragment_to_navigation_event);
    }

}
