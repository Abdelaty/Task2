package com.example.task2.View;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.task2.API.NEWS_API;
import com.example.task2.API.NewsRetrofitClientInstance;
import com.example.task2.Adapters.NewsAdapter;
import com.example.task2.POJO.News.Article;
import com.example.task2.POJO.News.NewsExample;
import com.example.task2.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class MainActivity extends AppCompatActivity {
    NewsAdapter newsAdapter;
    ArrayList<Article> articleArrayList;

    @BindView(R.id.news_rv)
    RecyclerView newsList_rv;

    @BindView(R.id.placeholder_img)
    ImageView plaveHolderImg;
    private View parentLayout;
    String newsCountry = "fr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        try {
            generateNewsNetworkCall(newsCountry);
        } catch (Exception e) {
            Log.v(getLocalClassName(), e.getMessage().toString());
            showPlaceHolder();
        }

    }

    private void showPlaceHolder() {
        newsList_rv.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "Can't get news!", Toast.LENGTH_LONG).show();

        plaveHolderImg.setVisibility(View.VISIBLE);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                generateNewsNetworkCall(newsCountry);
            }
        }, 0, 1000);
    }

    void generateNewsNetworkCall(String newsCountry) {
        NEWS_API service1 = NewsRetrofitClientInstance.getNewsRetrofitInstance().create(NEWS_API.class);
        Call<NewsExample> call = service1.getNews(newsCountry);
        call.enqueue(new Callback<NewsExample>() {
            @Override
            public void onResponse(@NonNull Call<NewsExample> call, @NonNull Response<NewsExample> response) {
                plaveHolderImg.setVisibility(View.INVISIBLE);
                newsList_rv.setVisibility(View.VISIBLE);
                if (articleArrayList == null) {
                    assert response.body() != null;
                    articleArrayList = (ArrayList<Article>) response.body().getArticles();
                    generateNewsList(articleArrayList);
                } else {
                    articleArrayList.clear();
                    newsList_rv.removeAllViewsInLayout();
                    assert response.body() != null;
                    articleArrayList = (ArrayList<Article>) response.body().getArticles();
                    generateNewsList(articleArrayList);
                    newsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsExample> call, @NonNull Throwable t) {
                Log.v(getLocalClassName(), t.getMessage());
                showPlaceHolder();
            }
        });
    }

    public void checkConnection() {

        if (isNetworkAvailable()) {
            Snackbar snackbar = Snackbar
                    .make(parentLayout,
                            "Connected to internet!",
                            Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {

            Snackbar snackbar = Snackbar
                    .make(parentLayout,
                            "Check connection!",
                            Snackbar.LENGTH_LONG);
            snackbar.show();

        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            generateNewsNetworkCall(newsCountry);
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }

    private void generateNewsList(ArrayList<Article> newsExampleArrayList) {
        newsAdapter = new NewsAdapter(newsExampleArrayList, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(VERTICAL);
        newsList_rv.setLayoutManager(layoutManager);
        newsList_rv.setAdapter(newsAdapter);
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Egypt News", Toast.LENGTH_LONG).show();
                newsList_rv.removeAllViewsInLayout();
                generateNewsNetworkCall("eg");
                newsAdapter.notifyDataSetChanged();
                return true;
            case R.id.item2:
                Toast.makeText(getApplicationContext(), "USA News", Toast.LENGTH_LONG).show();
                newsList_rv.removeAllViewsInLayout();

                generateNewsNetworkCall("us");
                newsAdapter.notifyDataSetChanged();

                return true;
            case R.id.item3:
                Toast.makeText(getApplicationContext(), "France News", Toast.LENGTH_LONG).show();
                newsList_rv.removeAllViewsInLayout();

                generateNewsNetworkCall("fr");
                newsAdapter.notifyDataSetChanged();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
