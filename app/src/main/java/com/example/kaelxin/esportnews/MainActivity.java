package com.example.kaelxin.esportnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout swipe;
    private TextView noNewsView;
    private ProgressBar progressBar;
    private ListView listView;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        setup();

        CheckInternet();

        newsAdapter = new NewsAdapter(MainActivity.this, 0, new ArrayList<News>());

        listView.setEmptyView(noNewsView);
        listView.setAdapter(newsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNewsObject = newsAdapter.getItem(position);
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                if (currentNewsObject != null) {
                    webIntent.setData(Uri.parse(currentNewsObject.getmUrl()));
                }
                startActivity(webIntent);
            }
        });

        swipe.setColorSchemeResources(R.color.colorAccent, R.color.myColor);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loaderManager.restartLoader(0, null, MainActivity.this);
            }
        });

    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(MainActivity.this, StaticClass.STRING_QUERY);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        progressBar.setVisibility(View.GONE);
        noNewsView.setText(R.string.no_news);

        swipe.setRefreshing(false);
        if (newsList != null) {
            newsAdapter.clear();
            newsAdapter.setNotifyOnChange(true);
            newsAdapter.addAll(newsList);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }

    public void setup() {
        noNewsView = findViewById(R.id.no_internet);
        swipe = findViewById(R.id.swiperefresh);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.listView);
    }

    private void CheckInternet(){

        loaderManager = getLoaderManager();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo= cm.getActiveNetworkInfo();

        boolean isConnected = networkInfo !=null && networkInfo.isConnected();

        if (isConnected){
            loaderManager.initLoader(0, null, this);
        }
        else{
            progressBar.setVisibility(View.GONE);
            noNewsView.setText(R.string.no_internet);
        }
    }

}
