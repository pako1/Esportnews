package com.example.kaelxin.esportnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    NewsAdapter(@NonNull Context context, @NonNull List<News> newsList) {
        super(context, 0, newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listView = convertView;

        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        ViewHolder viewHolder = new ViewHolder();

        viewHolder.esportImage = listView.findViewById(R.id.Thumbnail);
        if (currentNews != null) {
            Picasso.get().load(currentNews.getmThumbnail()).into(viewHolder.esportImage);
        }

        viewHolder.headLineView = listView.findViewById(R.id.headline);
        if (currentNews != null) {
            viewHolder.headLineView.setText(currentNews.getHeadLine());
        }

        viewHolder.authorView = listView.findViewById(R.id.author);
        if (currentNews != null) {
            viewHolder.authorView.setText(currentNews.getAuthor());
        }

        viewHolder.dateView = listView.findViewById(R.id.date);
        if (currentNews != null) {
            viewHolder.dateView.setText(currentNews.getmPublicationDate());
        }

        viewHolder.sectionView = listView.findViewById(R.id.section);
        if (currentNews != null) {
            viewHolder.sectionView.setText(currentNews.getSection());
        }

        return listView;
    }


    private static class ViewHolder {

        private ImageView esportImage;
        private TextView headLineView;
        private TextView authorView;
        private TextView dateView;
        private TextView sectionView;

    }

}

