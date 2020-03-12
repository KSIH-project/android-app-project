package com.project.ksih_android.ui.appInfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ksih_android.R;
import com.project.ksih_android.data.AboutDevelopersAdapter;
import com.project.ksih_android.data.AboutDevelopersData;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class KsihAndroidTeam extends Fragment {

    private static final String TAG = "KsihAndroidTeam";
    
    MaterialToolbar mToolbar;
    DatabaseReference mDatabaseReference;
    private List<AboutDevelopersData> mList = new ArrayList<>();
    private RecyclerView mRecyclerViewAboutDevelopers;
    private RotateLoading mRotateLoadingAboutDevelopers;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_ksih_android_team, container, false);
        mToolbar = mView.findViewById(R.id.toolbar_about_developers);
        mRecyclerViewAboutDevelopers = mView.findViewById(R.id.Recycler_developer_data);
        mRotateLoadingAboutDevelopers = mView.findViewById(R.id.progress_bar_about_developers);

        mRotateLoadingAboutDevelopers.start();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("developer_profile");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<AboutDevelopersData> data = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AboutDevelopersData mDatam = snapshot.getValue(AboutDevelopersData.class);
                    Timber.d("onDataChange: %s", mList.toString());
                    data.add(mDatam);
                }
                mList = data;
                setupRecyclerView();
                Timber.d( "afterDataChanged: %s", data.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mToolbar.setNavigationOnClickListener(v ->
                Navigation.findNavController(v).navigateUp());

        return mView;
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getParentFragment().getContext(),2);
        AboutDevelopersAdapter mAda = new AboutDevelopersAdapter(mList, getParentFragment().getContext());
        Timber.d( "setupRecyclerView: %s", mList.toString());
        mRecyclerViewAboutDevelopers.setAdapter(mAda);
        mRecyclerViewAboutDevelopers.setLayoutManager(gridLayoutManager);
        mRotateLoadingAboutDevelopers.stop();

    }

}
