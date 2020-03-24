package com.project.ksih_android.ui.onBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.project.ksih_android.R;
import com.project.ksih_android.storage.SharedPreferencesStorage;
import com.project.ksih_android.utility.Constants;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by SegunFrancis
 */
public class OnBoardingFragment extends Fragment {

    private OnBoardingViewModel mOnBoardingViewModel;
    private SharedPreferencesStorage mStorage;
    private NavController mNavController;
    private ImageView mIndicator1;
    private ImageView mIndicator2;
    private ImageView mIndicator3;
    private ViewPager2 mOnBoardingViewPager;
    private AppCompatButton mPrevButton;
    private AppCompatButton mNextButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_on_boarding, container, false);
        mStorage = new SharedPreferencesStorage(root.getContext());
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //activity
        mOnBoardingViewPager = view.findViewById(R.id.onBoarding_viewPager);
        mPrevButton = view.findViewById(R.id.button_prev);
        mNextButton = view.findViewById(R.id.button_next);
        mIndicator1 = view.findViewById(R.id.indicator1);
        mIndicator2 = view.findViewById(R.id.indicator2);
        mIndicator3 = view.findViewById(R.id.indicator3);


        //nextButton = view.findViewById(R.id.button_next);
        mOnBoardingViewModel = ViewModelProviders.of(this).get(OnBoardingViewModel.class);
        mNavController = Navigation.findNavController(view);
        mOnBoardingViewModel.seenOnBoardingScreen(mStorage.hasSeenOnBoardingScreen(Constants.ON_BOARDING_KEY));
        mOnBoardingViewModel.mUserStatesMutableLiveData.observe(this, userStates -> {
            switch (userStates) {
                case SEEN_ON_BOARDING_SCREEN:
                    mNavController.navigate(R.id.navigation_member);
                    break;
                case NOT_SEEN_ON_BOARDING_SCREEN:
                    // Display OnBoarding Screen
                    notSeenOnBoarding();

                    break;
            }
        });


    }
    //Populating onBoarding screen
    private void notSeenOnBoarding() {
        List<OnboardingModel> modelList = new ArrayList<>();

        OnboardingModel onBoardingModel_one = new OnboardingModel(R.drawable.ksih,
                getResources().getString(R.string.on_boarding_text_title1)
                , getResources().getString(R.string.ken_quote), getResources().getString(R.string.ken_saro_wiwa));
        modelList.add(onBoardingModel_one);

        OnboardingModel onBoardingModel_two = new OnboardingModel(R.drawable.connect,
                getResources().getString(R.string.on_boarding_text_title2),
                getResources().getString(R.string.on_boarding_text_description2), "");
        modelList.add(onBoardingModel_two);

        OnboardingModel onBoardingModel_three = new OnboardingModel(R.drawable.inform,
                getResources().getString(R.string.on_boarding_text_title3),
                getResources().getString(R.string.on_boarding_text_description3), "");
        modelList.add(onBoardingModel_three);


        OnBoardingScreenAdapter adapter = new OnBoardingScreenAdapter(modelList);
        mOnBoardingViewPager.setAdapter(adapter);


        mPrevButton.setOnClickListener(View -> mOnBoardingViewPager.setCurrentItem
                (mOnBoardingViewPager.getCurrentItem() - 1, true));

        mNextButton.setOnClickListener(View -> {
            if (mOnBoardingViewPager.getCurrentItem() < 2)
                mOnBoardingViewPager.setCurrentItem(mOnBoardingViewPager.getCurrentItem() + 1, true);
            else {
                mNavController.navigate(R.id.navigation_member);
                    mOnBoardingViewModel.seenOnBoardingScreen(true);
                    mStorage.setSeenOnBoardingScreen(Constants.ON_BOARDING_KEY, true);
            }
        });


        mOnBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Timber.tag("Displayed Item Position" + position);

                switch (position) {

                    case 0:
                        hideButton(mPrevButton);
                        mNextButton.setText(R.string.next_onboarding_text);
                        indicatorSelector(true, false, false);
                        break;
                    case 1:
                        showButton(mPrevButton);
                        showButton(mNextButton);
                        mNextButton.setText(R.string.next_onboarding_text);
                        indicatorSelector(false, true, false);
                        break;
                    case 2:
                        showButton(mPrevButton);
                        mNextButton.setText(R.string.start_onboarding_text);
                        indicatorSelector(false, false, true);
                        break;
                }
            }
        });
    }

    private void showButton(AppCompatButton appCompatButton) {
        appCompatButton.setVisibility(View.VISIBLE);
    }

    private void hideButton(AppCompatButton appCompatButton) {
        appCompatButton.setVisibility(View.GONE);
    }

    private void indicatorSelector(boolean jan, boolean feb, boolean march) {
        mIndicator1.setSelected(jan);
        mIndicator2.setSelected(feb);
        mIndicator3.setSelected(march);
    }
}
