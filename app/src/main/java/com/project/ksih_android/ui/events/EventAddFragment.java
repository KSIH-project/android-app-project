package com.project.ksih_android.ui.events;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.ksih_android.R;
import com.project.ksih_android.data.Events;
import com.project.ksih_android.databinding.FragmentEventAddBinding;

import static android.app.Activity.RESULT_OK;
import static com.project.ksih_android.utility.Constants.EVENTS_FIREBASE_PATH;
import static com.project.ksih_android.utility.Constants.EVENT_TO_EDIT;
import static com.project.ksih_android.utility.Constants.REQUEST_CODE_EVENTS_IMAGE;
import static com.project.ksih_android.utility.Methods.hideSoftKeyboard;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventAddFragment extends Fragment {

    private Events mEvents;
    private Bitmap mBitmap;
    private String imagePath = "";
    private String imageUrl;
    private FragmentEventAddBinding binding;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(EVENTS_FIREBASE_PATH);


    private static boolean hasText(TextInputLayout editText, String error_message) {

        String text = editText.getEditText().getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(error_message);
            return false;
        }
        return true;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_add, container, false);
        mBitmap = null;
        binding.imageViewAdd.setOnClickListener(view -> openGallery());


        binding.buttonAddEvents.setOnClickListener(v -> {


            if (validate() && mEvents == null) {
                disableViews();
                binding.progressBarEventsAddFragment.start();
                binding.buttonAddEvents.setEnabled(false);
                uploadImageToFireBaseStorage();
            } else if (validate() && getArguments() != null) {
                binding.progressBarEventsAddFragment.start();
                binding.buttonAddEvents.setEnabled(false);
//                saveEvents();
                uploadNewImageToFireBaseStorage();
            } else {
                Toast.makeText(requireContext(), "Please Fill in the Required Fields", Toast.LENGTH_SHORT).show();
            }
        });
        if (getArguments() != null) {
            binding.buttonAddEvents.setText("Save Events");
            mEvents = getArguments().getParcelable(EVENT_TO_EDIT);
            getEventsDetails();
        }


        return binding.getRoot();
    }

    private void getEventsDetails() {

        binding.textInputLayoutTittle.getEditText().setText(mEvents.getEventName());
        binding.textInputLayoutType.getEditText().setText(mEvents.getEventType());
        binding.textInputLayoutDesc.getEditText().setText(mEvents.getEventDescription());
        binding.textInputLayoutContactsEmail.getEditText().setText(mEvents.getEmail());
        binding.textInputLayoutPhone.getEditText().setText(mEvents.getPhoneNumber());
        binding.textInputLayoutRsvp.getEditText().setText(mEvents.getEventRSVP());
        binding.textInputLayoutTime.getEditText().setText(mEvents.getTime());
        binding.textInputLayoutDate.getEditText().setText(mEvents.getDate());
        Glide.with(requireContext()).load(mEvents.getImageUrl()).into(binding.imageViewAdd);
    }

    private void saveEvents() {
        saveImage();
        hideSoftKeyboard(requireActivity());
    }

    private void disableViews() {
        binding.buttonAddEvents.setEnabled(false);
        binding.textInputLayoutContactsEmail.setEnabled(false);
        binding.textInputLayoutDesc.setEnabled(false);
        binding.textInputLayoutPhone.setEnabled(false);
        binding.textInputLayoutRsvp.setEnabled(false);
        binding.textInputLayoutTittle.setEnabled(false);
        binding.textInputLayoutType.setEnabled(false);
        binding.textInputLayoutDate.setEnabled(false);
        binding.textInputLayoutTime.setEnabled(false);
    }

    private void saveImage() {
        if (mBitmap != null && mEvents.getImageUrl() != null) {
            binding.progressBarEventsAddFragment.start();
            disableViews();
            uploadImageToFireBaseStorage();
        } else if (mBitmap == null && mEvents.getImageUrl() != null) {
            uploadNewImageToFireBaseStorage();
        }
    }

    private void uploadImageToFireBaseStorage() {
        if (mBitmap != null) {
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
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Failed to add Events", Toast.LENGTH_SHORT).show();
                    enableViews();
                }
            });

        } else {
            Toast.makeText(requireContext(), "Please Select An Image to upload", Toast.LENGTH_SHORT).show();
            enableViews();
            binding.progressBarEventsAddFragment.stop();
        }


    }

    private void uploadNewImageToFireBaseStorage() {
        if (mBitmap == null) {
            editEvents();
        } else {


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
                            editEvents();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Failed to add Events", Toast.LENGTH_SHORT).show();
                    enableViews();
                }
            });
        }
    }



    private void editEvents() {
        String id = mEvents.getId();
        Timber.d("mField: %s", mEvents.getId());
        if (imageUrl == null) {
            Events nEvents = new Events();
            nEvents.setImageUrl(mEvents.getImageUrl());
            nEvents.setId(id);
            nEvents.setEventName(binding.textInputLayoutTittle.getEditText().getText().toString());
            nEvents.setEventType(binding.textInputLayoutType.getEditText().getText().toString());
            nEvents.setEventDescription(binding.textInputLayoutDesc.getEditText().getText().toString());
            nEvents.setEmail(binding.textInputLayoutContactsEmail.getEditText().getText().toString());
            nEvents.setPhoneNumber(binding.textInputLayoutPhone.getEditText().getText().toString());
            nEvents.setEventRSVP(binding.textInputLayoutRsvp.getEditText().getText().toString());
            nEvents.setDate(binding.textInputLayoutDate.getEditText().getText().toString());
            nEvents.setTime(binding.textInputLayoutTime.getEditText().getText().toString());
            if (id == null) {
                addEvents();
                Toast.makeText(requireContext(), "error detected", Toast.LENGTH_SHORT).show();
            } else {
                databaseReference.child(id).setValue(nEvents).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressBarEventsAddFragment.stop();
                        binding.buttonAddEvents.setEnabled(true);
                        navigateToEventsListFragment(binding.buttonAddEvents);
                        Toast.makeText(requireContext(), "Event Edited Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Unable to edit Startup", Toast.LENGTH_SHORT).show();
                        Timber.d("Database Write Error: %s", task.getException().getLocalizedMessage());
                        binding.progressBarEventsAddFragment.stop();
                        binding.buttonAddEvents.setEnabled(true);
                    }
                });
            }

        } else {
            deleteImage(mEvents.getImageUrl());
            Events nEvents = new Events();
            nEvents.setEventName(binding.textInputLayoutTittle.getEditText().getText().toString());
            nEvents.setEventType(binding.textInputLayoutType.getEditText().getText().toString());
            nEvents.setEventDescription(binding.textInputLayoutDesc.getEditText().getText().toString());
            nEvents.setEmail(binding.textInputLayoutContactsEmail.getEditText().getText().toString());
            nEvents.setPhoneNumber(binding.textInputLayoutPhone.getEditText().getText().toString());
            nEvents.setEventRSVP(binding.textInputLayoutRsvp.getEditText().getText().toString());
            nEvents.setId(id);
            nEvents.setImageUrl(imageUrl);
            nEvents.setDate(binding.textInputLayoutDate.getEditText().getText().toString());
            nEvents.setTime(binding.textInputLayoutTime.getEditText().getText().toString());
            databaseReference.child(id).setValue(nEvents).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    binding.progressBarEventsAddFragment.stop();
                    binding.buttonAddEvents.setEnabled(true);
                    navigateToEventsListFragment(requireView());
                    Toast.makeText(requireContext(), "Event Edited Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Unable to edit Startup", Toast.LENGTH_SHORT).show();
                    Timber.d("Database Write Error: %s", task.getException().getLocalizedMessage());
                    binding.progressBarEventsAddFragment.stop();
                    binding.buttonAddEvents.setEnabled(true);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EVENTS_IMAGE) {
            Uri uri = data.getData();
            if (uri != null) {
                Toast.makeText(requireContext(), "success uri", Toast.LENGTH_SHORT).show();
                try {
                    imagePath = uri.getLastPathSegment();
                    if (Build.VERSION.SDK_INT >= 29) {
                        ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), uri);
                        mBitmap = ImageDecoder.decodeBitmap(source);

                        Glide.with(requireContext()).load(uri).into(binding.imageViewAdd);
                    } else {
                        mBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                        // Load image using Glide
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

    private void enableViews() {
        binding.buttonAddEvents.setEnabled(true);
        binding.textInputLayoutContactsEmail.setEnabled(true);
        binding.textInputLayoutDesc.setEnabled(true);
        binding.textInputLayoutPhone.setEnabled(true);
        binding.textInputLayoutRsvp.setEnabled(true);
        binding.textInputLayoutTittle.setEnabled(true);
        binding.textInputLayoutType.setEnabled(true);
        binding.textInputLayoutDate.setEnabled(true);
        binding.textInputLayoutTime.setEnabled(true);

    }

    private boolean validate() {
        String error = getString(R.string.event_error_message);
        if (!hasText(binding.textInputLayoutTittle, error)) return false;
        if (!hasText(binding.textInputLayoutType, error)) return false;
        if (!hasText(binding.textInputLayoutDesc, error)) return false;
        if (!hasText(binding.textInputLayoutContactsEmail, error)) return false;
        if (!hasText(binding.textInputLayoutPhone, error)) return false;
        if (!hasText(binding.textInputLayoutDate, error)) return false;
        if (!hasText(binding.textInputLayoutTime, error)) return false;
        return hasText(binding.textInputLayoutRsvp, error);
    }

    private void addEvents() {
        String id = databaseReference.push().getKey();
        mEvents = new Events(id, imageUrl, binding.textInputLayoutTittle.getEditText().getText().toString(),
                binding.textInputLayoutContactsEmail.getEditText().getText().toString(),
                binding.textInputLayoutPhone.getEditText().getText().toString(),
                binding.textInputLayoutDate.getEditText().getText().toString(), binding.textInputLayoutTime.getEditText().getText().toString(),
                binding.textInputLayoutDesc.getEditText().getText().toString(), binding.textInputLayoutType.getEditText().getText().toString()
                , binding.textInputLayoutRsvp.getEditText().getText().toString());
        databaseReference.child(id).setValue(mEvents).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                navigateToEventsListFragment(binding.buttonAddEvents);
                binding.progressBarEventsAddFragment.stop();
                Toast.makeText(requireContext(), "Events Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToEventsListFragment(View v) {
        Navigation.findNavController(v).navigate(R.id.action_eventAddFragment_to_navigation_event);
    }

    private void deleteImage(String fullUrl) {
        if (fullUrl.contains("")) {
            Throwable e = new Throwable();
            Log.d("full url is null", "deleteImage: ", e.getCause());
        } else {


            StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(fullUrl);
            ref.delete().addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Old Photo Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Image Delete Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
