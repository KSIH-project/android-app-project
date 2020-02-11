package com.project.ksih_android;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager2.widget.ViewPager2;

import com.project.ksih_android.adapter.OnBoardingScreenAdapter;
import com.project.ksih_android.model.OnboardingModel;
import com.project.ksih_android.ui.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends AppCompatActivity {

    private final static String TAG = OnBoardingActivity.class.getSimpleName();
    private ImageView mIndicator1;
    private ImageView mIndicator2;
    private ImageView mIndicator3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        ViewPager2 onBoardingViewPager = findViewById(R.id.onBoarding_viewPager);
        AppCompatButton prevButton = findViewById(R.id.button_prev);
        AppCompatButton nextButton = findViewById(R.id.button_next);
        mIndicator1 = findViewById(R.id.indicator1);
        mIndicator2 = findViewById(R.id.indicator2);
        mIndicator3 = findViewById(R.id.indicator3);

        //Populating onBoarding screen

        List<OnboardingModel> modelList = new ArrayList<>();

        OnboardingModel onboardingModel_one = new OnboardingModel(R.drawable.ksih,
                getResources().getString(R.string.on_boarding_text_title1)
                , getResources().getString(R.string.on_boarding_text_description1));
        modelList.add(onboardingModel_one);

        OnboardingModel onboardingModel_two = new OnboardingModel(R.drawable.connect,
                getResources().getString(R.string.on_boarding_text_title2),
                getResources().getString(R.string.on_boarding_text_description2));
        modelList.add(onboardingModel_two);

        OnboardingModel onboardingModel_three = new OnboardingModel(R.drawable.inform,
                getResources().getString(R.string.on_boarding_text_title3),
                getResources().getString(R.string.on_boarding_text_description3));
        modelList.add(onboardingModel_three);


        OnBoardingScreenAdapter adapter = new OnBoardingScreenAdapter(modelList);
        onBoardingViewPager.setAdapter(adapter);


        prevButton.setOnClickListener(view -> onBoardingViewPager.setCurrentItem
                (onBoardingViewPager.getCurrentItem() - 1, true));

        nextButton.setOnClickListener(view -> {
            if (onBoardingViewPager.getCurrentItem() < 2)
                onBoardingViewPager.setCurrentItem(onBoardingViewPager.getCurrentItem() + 1, true);
            else {
                startActivity(new Intent(OnBoardingActivity.this, HomeActivity.class));
                finish();
            }
        });

        onBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "Displayed Ite m Position" + position);

                switch (position) {

                    case 0:
                        hideButton(prevButton);
                        nextButton.setText(R.string.next_onboarding_text);
                        indicatorSelector(true, false, false);
                        break;
                    case 1:
                        showButton(prevButton);
                        showButton(nextButton);
                        nextButton.setText(R.string.next_onboarding_text);
                        indicatorSelector(false, true, false);
                        break;
                    case 2:
                        showButton(prevButton);
                        nextButton.setText(R.string.start_onboarding_text);
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
