package com.example.task2.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.task2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsNews extends AppCompatActivity {
    @BindView(R.id.web_view)
    WebView webview;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    String newsTitle, newsUrl;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_news);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        newsUrl = intent.getStringExtra("NEWS_URL");
        newsTitle = intent.getStringExtra("NEWS_TITLE");

        setTitle(newsTitle);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl(newsUrl);
        fab.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, newsUrl);
            sendIntent.setType("text/html");
            startActivity(sendIntent);
        });

    }
}
