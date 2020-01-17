package com.project.ksih_android.ui.startup;

import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.ksih_android.data.StartUpField;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

import static com.project.ksih_android.utility.Constants.STARTUP_FIREBASE_DATABASE_REFERENCE;

public class StartupViewModel extends ViewModel implements ValueEventListener {

    private Query ref;
    private MutableLiveData<List<StartUpField>> startupList = new MutableLiveData<>();
    private List<StartUpField> mList = new ArrayList<>();
    private StartupValidationField mField;

    public StartupViewModel() {
        mField = new StartupValidationField();
        ref = FirebaseDatabase.getInstance()
                .getReference(STARTUP_FIREBASE_DATABASE_REFERENCE)
                .orderByChild("startupName"); /* Sort start ups by name */
        ref.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            StartUpField field = snapshot.getValue(StartUpField.class);
            if (field != null) {
                mList.add(field);
            }
        }
        startupList.setValue(mList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Timber.d("Database Error: %s", databaseError.getDetails());
    }

    LiveData<List<StartUpField>> getStartUps() {
        return startupList;
    }

    void removeListeners() {
        ref.removeEventListener(this);
    }

    public StartupValidationField getField() {
        return mField;
    }

    public View.OnFocusChangeListener focusChangeStartupName() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                TextInputEditText editText = (TextInputEditText) view;
                if (editText.getText().toString().length() > 0 && !b) {
                    mField.isStartupNameValid();
                }
            }
        };
    }

    public View.OnFocusChangeListener focusChangeStartupFounder() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                TextInputEditText editText = (TextInputEditText) view;
                if (editText.getText().toString().length() > 0 && !b) {
                    mField.isStartupFounderValid();
                }
            }
        };
    }

    public View.OnFocusChangeListener focusChangeStartupDescription() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                TextInputEditText editText = (TextInputEditText) view;
                if (editText.getText().toString().length() > 0 && !b) {
                    mField.isStartupDescriptionValid();
                }
            }
        };
    }

    public View.OnFocusChangeListener focusChangeStartupEmail() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                TextInputEditText editText = (TextInputEditText) view;
                if (editText.getText().toString().length() > 0 && !b) {
                    mField.isStartupEmailValid(true);
                }
            }
        };
    }

    public View.OnFocusChangeListener focusChangeStartupPhone() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                TextInputEditText editText = (TextInputEditText) view;
                if (editText.getText().toString().length() > 0 && !b) {
                    mField.isStartupPhoneValid();
                }
            }
        };
    }

    public View.OnFocusChangeListener focusChangeStartupWebsite() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                TextInputEditText editText = (TextInputEditText) view;
                if (editText.getText().toString().length() > 0 && !b) {
                    mField.isWebsiteValid();
                }
            }
        };
    }

    public View.OnFocusChangeListener focusChangeStartupFacebook() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                TextInputEditText editText = (TextInputEditText) view;
                if (editText.getText().toString().length() > 0 && !b) {
                    mField.isFacebookValid();
                }
            }
        };
    }

    public View.OnFocusChangeListener focusChangeStartupTwitter() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                TextInputEditText editText = (TextInputEditText) view;
                if (editText.getText().toString().length() > 0 && !b) {
                    mField.isTwitterValid();
                }
            }
        };
    }

    @BindingAdapter("onFocus")
    public static void startUpBindFocusChange(TextInputEditText editText, View.OnFocusChangeListener onFocusChangeListener) {
        if (editText.getOnFocusChangeListener() == null) {
            editText.setOnFocusChangeListener(onFocusChangeListener);
        }
    }

    @BindingAdapter("error")
    public static void startUpSetError(TextInputLayout inputLayout, String errorMessage) {
        inputLayout.setError(errorMessage);
    }
}