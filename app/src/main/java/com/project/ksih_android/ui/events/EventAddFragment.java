package com.project.ksih_android.ui.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.project.ksih_android.utility.Constants.EDIT_EVENT_DETAILS_KEY;
import static com.project.ksih_android.utility.Constants.EVENTS_FIREBASE_PATH;
import static com.project.ksih_android.utility.Constants.EVENT_FIREBASE_STORAGE_REFRENCE_PATH;
import static com.project.ksih_android.utility.Constants.REQUEST_CODE_EVENTS_IMAGE;
import static com.project.ksih_android.utility.Constants.SAVE_EVENTS_BUTTON_TEXT;
import static com.project.ksih_android.utility.Methods.hideSoftKeyboard;
/**
 * A simple {@link Fragment} subclass.
 */
public class EventAddFragment extends Fragment {
    private DatePickerDialog.OnDateSetListener mDateDialog;
    private TimePickerDialog.OnTimeSetListener mTimeDialogListner;
    private Events mEvents;
    private Bitmap mBitmap;
    private String imagePath = "";
    private String imageUrl;
    private FragmentEventAddBinding binding;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(EVENTS_FIREBASE_PATH);



    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_add, container, false);
        mBitmap = null;
        binding.imageViewAdd.setOnClickListener(view -> openGallery());
        binding.textInputLayoutTime.setOnClickListener(v -> {
            Calendar time = Calendar.getInstance();
            int currentHour = time.get(Calendar.HOUR_OF_DAY);
            int currentMinute = time.get(Calendar.MINUTE);
            TimePickerDialog timeDialog = new TimePickerDialog(getParentFragment().getContext(),
                    mTimeDialogListner,
                    currentHour, currentMinute, false);
            timeDialog.show();
        });
        mTimeDialogListner = (view, hourOfDay, minute) -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar1.set(Calendar.MINUTE, minute);
            CharSequence time = DateFormat.format("hh:mm a", calendar1);
            binding.textInputLayoutTime.setText(time);
        };
        binding.textInputLayoutDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog pickerDialog = new DatePickerDialog(getParentFragment().getContext()
                    , mDateDialog,
                    year, month, day);
            pickerDialog.show();
        });
        mDateDialog = (view, year, month, dayOfMonth) -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.YEAR, year);
            calendar1.set(Calendar.MONTH, month);
            calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            CharSequence date = DateFormat.format("MMM d, yyyy", calendar1);
            binding.textInputLayoutDate.setText(date);
        };
        binding.buttonAddEvents.setOnClickListener(v -> {


            if (!validate() && mEvents == null) {
                disableViews();
                binding.progressBarEventsAddFragment.start();
                binding.buttonAddEvents.setEnabled(false);
                hideSoftKeyboard(requireActivity());
                uploadImageToFireBaseStorage();
            } else if (!validate() && getArguments() != null) {
                disableViews();
                binding.progressBarEventsAddFragment.start();
                binding.buttonAddEvents.setEnabled(false);
                hideSoftKeyboard(requireActivity());
                uploadNewImageToFireBaseStorage();
            } else {
                Toast.makeText(getParentFragment().getContext(), "Please Fill in the Required Fields", Toast.LENGTH_SHORT).show();
            }
        });
        if (getArguments() != null) {
            binding.buttonAddEvents.setText(SAVE_EVENTS_BUTTON_TEXT);
            mEvents = getArguments().getParcelable(EDIT_EVENT_DETAILS_KEY);
            getEventsDetails();
        }
        return binding.getRoot();
    }
    private void getEventsDetails() {

        binding.textInputLayoutTittle.getEditText().setText(mEvents.getEventName());
        binding.textInputLayoutLocation.getEditText().setText(mEvents.getEventLocation());
        binding.textInputLayoutDesc.getEditText().setText(mEvents.getEventDescription());
        binding.textInputLayoutContactsEmail.getEditText().setText(mEvents.getEmail());
        binding.textInputLayoutPhone.getEditText().setText(mEvents.getPhoneNumber());
        binding.textInputLayoutRsvp.getEditText().setText(mEvents.getEventRSVP());
        binding.textInputLayoutTime.setText(mEvents.getTime());
        binding.textInputLayoutDate.setText(mEvents.getDate());
        Glide.with(requireContext()).load(mEvents.getImageUrl()).into(binding.imageViewAdd);
    }
    private void disableViews() {
        binding.buttonAddEvents.setEnabled(false);
        binding.textInputLayoutContactsEmail.setEnabled(false);
        binding.textInputLayoutDesc.setEnabled(false);
        binding.textInputLayoutPhone.setEnabled(false);
        binding.textInputLayoutRsvp.setEnabled(false);
        binding.textInputLayoutTittle.setEnabled(false);
        binding.textInputLayoutLocation.setEnabled(false);
    }

    private static boolean hasText(TextInputLayout editText, String error_message) {

        String text = editText.getEditText().getText().toString().trim();
        editText.setError(null);
        if (text.length() == 0) {
            editText.setError(error_message);
            return false;
        }
        return true;
    }
    private void uploadImageToFireBaseStorage() {
        if (mBitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            byte[] data = outputStream.toByteArray();
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getInstance().getReference(EVENT_FIREBASE_STORAGE_REFRENCE_PATH + imagePath);
            UploadTask task = storageReference.putBytes(data);
            task.addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    if (getView() != null) {
                        navigateToEventsListFragment(requireView());
                    }
                    task1.getResult().getStorage().getDownloadUrl().addOnCompleteListener(getParentFragment().getActivity(), task2 -> {
                        if (task2.isSuccessful()) {
                            imageUrl = task2.getResult().toString();
                            addEvents();
                        } else {
                            Timber.d("Download URL error: %s", task2.getException().getLocalizedMessage());
                            Toast.makeText(getParentFragment().getContext(), task2.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            binding.progressBarEventsAddFragment.stop();
                            binding.buttonAddEvents.setEnabled(false);
                        }
                    });
                } else {
                    Timber.d("Event save error: %s", task1.getException().getLocalizedMessage());
                    Toast.makeText(getParentFragment().getContext(), "Failed to add Events", Toast.LENGTH_SHORT).show();
                    binding.progressBarEventsAddFragment.stop();
                    enableViews();
                }
            });
        } else {
            Toast.makeText(getParentFragment().getContext(), "Please Select An Image to upload", Toast.LENGTH_SHORT).show();
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
            StorageReference storageReference = firebaseStorage.getInstance().getReference(EVENT_FIREBASE_STORAGE_REFRENCE_PATH + imagePath);
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
                    Toast.makeText(getParentFragment().getContext(), "Failed to add Events", Toast.LENGTH_SHORT).show();
                    binding.progressBarEventsAddFragment.stop();
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
            nEvents.setEventName(binding.textInputLayoutTittle.getEditText().getText().toString().trim());
            nEvents.setEventLocation(binding.textInputLayoutLocation.getEditText().getText().toString().trim());
            nEvents.setEventDescription(binding.textInputLayoutDesc.getEditText().getText().toString().trim());
            nEvents.setEmail(binding.textInputLayoutContactsEmail.getEditText().getText().toString().trim());
            nEvents.setPhoneNumber(binding.textInputLayoutPhone.getEditText().getText().toString().trim());
            nEvents.setEventRSVP(binding.textInputLayoutRsvp.getEditText().getText().toString().trim());
            nEvents.setDate(binding.textInputLayoutDate.getText().toString());
            nEvents.setTime(binding.textInputLayoutTime.getText().toString());
            if (id == null) {
                addEvents();
                Toast.makeText(getParentFragment().getContext(), "error detected", Toast.LENGTH_SHORT).show();
            } else {
                databaseReference.child(id).setValue(nEvents).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressBarEventsAddFragment.stop();
                        binding.buttonAddEvents.setEnabled(true);
                        navigateToEventsListFragment(getParentFragment().getView());
                        Toast.makeText(getParentFragment().getContext(), "Event Edited Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getParentFragment().getContext(), "Unable to edit Event", Toast.LENGTH_SHORT).show();
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
            nEvents.setEventLocation(binding.textInputLayoutLocation.getEditText().getText().toString());
            nEvents.setEventDescription(binding.textInputLayoutDesc.getEditText().getText().toString());
            nEvents.setEmail(binding.textInputLayoutContactsEmail.getEditText().getText().toString());
            nEvents.setPhoneNumber(binding.textInputLayoutPhone.getEditText().getText().toString());
            nEvents.setEventRSVP(binding.textInputLayoutRsvp.getEditText().getText().toString());
            nEvents.setId(id);
            nEvents.setImageUrl(imageUrl);
            nEvents.setDate(binding.textInputLayoutDate.getText().toString());
            nEvents.setTime(binding.textInputLayoutTime.getText().toString());
            databaseReference.child(id).setValue(nEvents).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    binding.progressBarEventsAddFragment.stop();
                    binding.buttonAddEvents.setEnabled(true);
                    navigateToEventsListFragment(getParentFragment().getView());
                    Toast.makeText(getParentFragment().getContext(), "Event Edited Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Unable to edit Event", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getParentFragment().getContext(), "Image Selected", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getParentFragment().getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
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
        binding.textInputLayoutLocation.setEnabled(true);
    }
    private boolean validate() {
        String error = getString(R.string.event_error_message);
        if (!hasText(binding.textInputLayoutTittle, error)) return false;
        if (!hasText(binding.textInputLayoutLocation, error)) return false;
        if (!hasText(binding.textInputLayoutDesc, error)) return false;
        if (!hasText(binding.textInputLayoutContactsEmail, error)) return false;
        if (!hasText(binding.textInputLayoutPhone, error)) return false;
        if (binding.textInputLayoutDate == null) return false;
        if (binding.textInputLayoutTime == null) return false;
        return false;
    }
    private void addEvents() {
        String id = databaseReference.push().getKey();
        mEvents = new Events(id, imageUrl, binding.textInputLayoutTittle.getEditText().getText().toString().trim(), binding.textInputLayoutLocation.getEditText().getText().toString().trim(),
                binding.textInputLayoutContactsEmail.getEditText().getText().toString().trim(),
                binding.textInputLayoutPhone.getEditText().getText().toString().trim(),
                binding.textInputLayoutDate.getText().toString(), binding.textInputLayoutTime.getText().toString(),
                binding.textInputLayoutDesc.getEditText().getText().toString().trim()
                , binding.textInputLayoutRsvp.getEditText().getText().toString().trim());
        databaseReference.child(id).setValue(mEvents).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                navigateToEventsListFragment(getParentFragment().getView());
                binding.progressBarEventsAddFragment.stop();
                Toast.makeText(getParentFragment().getContext(), "Events Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void navigateToEventsListFragment(View v) {
        NavController navController = Navigation.findNavController(getParentFragment().getActivity(), R.id.nav_host_fragment);
        if (navController.getCurrentDestination().getId() == R.id.eventAddFragment) {
            Navigation.findNavController(v).navigate(R.id.action_eventAddFragment_to_navigation_event);
        }

    }

    private void deleteImage(String fullUrl) {
        if (fullUrl.contains("")) {
            Throwable e = new Throwable();
            Timber.d(e.getCause());
        } else {
            StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(fullUrl);
            ref.delete().addOnCompleteListener(requireActivity(), task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getParentFragment().getContext(), "Old Photo Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getParentFragment().getContext(), "Image Delete Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}