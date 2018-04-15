package com.example.kaelxin.esportnews;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);

        newsAdapter = new NewsAdapter(MainActivity.this, 0, new ArrayList<News>());

        ListView listView = findViewById(R.id.listView);
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

    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLodaer(MainActivity.this, StaticClass.STRING_QUERY);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {

        newsAdapter.clear();

        if (newsList != null && !newsList.isEmpty()) {
            newsAdapter.addAll(newsList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }
}
