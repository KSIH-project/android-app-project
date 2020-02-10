package com.project.ksih_android.ui.others;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.project.ksih_android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PolicyFragment extends Fragment {


    public PolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_policy, container, false);

        WebView policyWeb = root.findViewById(R.id.policies);
        policyWeb.setWebViewClient(new MyBrowser());

        String url = "https://flycricket.io/ksih/privacy.html";
        policyWeb.getSettings().setLoadsImagesAutomatically(true);
        policyWeb.getSettings().setJavaScriptEnabled(true);
        policyWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        policyWeb.loadUrl(url);



        return root;
    }

    private class MyBrowser extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }
    }

}
