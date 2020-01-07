package com.project.ksih_android.ui.chat.search;


import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.makeramen.roundedimageview.RoundedImageView;
import com.project.ksih_android.R;
import com.project.ksih_android.ui.chat.models.ProfileInfo;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    //variables
    private EditText searchInput;
    private ImageView backButton;
    private TextView notFoundTv;
    private RecyclerView people_list;

    //firebase
    private DatabaseReference peoplesDatabaseReference;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchInput = view.findViewById(R.id.SearchInput);
        notFoundTv = view.findViewById(R.id.notFoundTv);
        backButton = view.findViewById(R.id.backButton);
        //setting text watcher for search
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        backButton.setOnClickListener(view1 -> Navigation.findNavController(view).navigate(R.id.nav_chats));

        //setup recycler view
        people_list = view.findViewById(R.id.SearchList);
        people_list.setHasFixedSize(true);
        people_list.setLayoutManager(new LinearLayoutManager(getContext()));
        //offline
        peoplesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        peoplesDatabaseReference.keepSynced(true);

        return view;
    }
    /**
     * UI Bindings for firebase
     */
    private void searchPeopleProfile(final String searchString){
        final Query searchQuery = peoplesDatabaseReference.orderByChild("search_name")
                .startAt(searchString).endAt(searchString + "\uf8ff");

        FirebaseRecyclerOptions<ProfileInfo> recyclerOptions = new FirebaseRecyclerOptions.Builder<ProfileInfo>()
                .setQuery(searchQuery, ProfileInfo.class)
                .build();

        FirebaseRecyclerAdapter<ProfileInfo, SearchPeopleVH> adapter = new FirebaseRecyclerAdapter<ProfileInfo,
                SearchPeopleVH>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull SearchPeopleVH holder, int position, @NonNull ProfileInfo model) {
                holder.name.setText(model.getUser_name());
                holder.status.setText(model.getUser_status());

                Picasso.get()
                        .load(model.getUser_image())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.default_profile_image)
                        .into(holder.profile_pic);

                holder.verified_icon.setVisibility(View.GONE);
                if (model.getVerified().contains("true"));
                    holder.verified_icon.setVisibility(View.VISIBLE);
                    holder.verified_icon.setVisibility(View.GONE);

                    //on list >> clicking item go to single user profile
                holder.itemView.setOnClickListener(view -> {
                    String visit_user_id = getRef(position).getKey();
                    /**
                     * @Todo send intent to profile fragment
                     */
                });
            }

            @NonNull
            @Override
            public SearchPeopleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_single_profile_display, parent, false);
                return new SearchPeopleVH(view);
            }
        };
        people_list.setAdapter(adapter);
        adapter.startListening();
    }

    public static class SearchPeopleVH extends RecyclerView.ViewHolder{

        TextView name, status;
        RoundedImageView profile_pic;
        ImageView verified_icon;

        public SearchPeopleVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.all_user_name);
            status = itemView.findViewById(R.id.all_user_status);
            profile_pic = itemView.findViewById(R.id.all_user_profile_img);
            verified_icon = itemView.findViewById(R.id.verifiedIcon);
        }
    }

}
