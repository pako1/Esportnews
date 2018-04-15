package com.example.kaelxin.esportnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLodaer extends AsyncTaskLoader<List<News>> {

    private String stringQuery;

    NewsLodaer(Context context, String stringQuery) {
        super(context);
        this.stringQuery = stringQuery;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {

        List<News> newsList = null;

        if (stringQuery != null) {
            newsList = QueryUtils.fetchData(stringQuery);
        }
        return newsList;
    }

}
