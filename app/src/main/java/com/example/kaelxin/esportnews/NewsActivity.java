package com.example.kaelxin.esportnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

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

        newsAdapter = new NewsAdapter(NewsActivity.this, new ArrayList<News>());

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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                swipe.setEnabled(firstVisibleItem == 0);
            }
        });


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loaderManager.restartLoader(0, null, NewsActivity.this);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent settingsIntent = new Intent(NewsActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String defaultStartTime = sharedPrefs.getString(
                getString(R.string.settings_start_time_key),
                getString(R.string.settings_start_time_default));

        String defaultEndTime = sharedPrefs.getString(
                getString(R.string.settings_time_key),
                getString(R.string.settings_time_default));

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("q", "sport/esports")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("show-fields","hasStoryPackage,headline,thumbnail")
                .appendQueryParameter("from-date", defaultStartTime)
                .appendQueryParameter("to-date", defaultEndTime)
                .appendQueryParameter("api-key","d2598eb2-7309-4b6f-aecc-ca94ab21ce9c");

        return new NewsLoader(NewsActivity.this,builder.toString());
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

    private void setup() {
        noNewsView = findViewById(R.id.no_internet);
        swipe = findViewById(R.id.swiperefresh);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.listView);
    }

    private void CheckInternet() {

        loaderManager = getLoaderManager();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        boolean isConnected = networkInfo != null && networkInfo.isConnected();

        if (isConnected) {
            loaderManager.initLoader(0, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            noNewsView.setText(R.string.no_internet);
        }
    }
}
