package com.tma.techday.foodnutrientfact.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.tma.techday.foodnutrientfact.R;

/**
 * Show web cooky.vn through web view
 */
public class WebViewCookingRecipe extends AppCompatActivity {

    private WebView webView;

    /**
     * Declare param and set url for web view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_cooking_recipe);

        webView = findViewById(R.id.webViewCookingRecipe);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getString(R.string.url_web_view));
    }
}
