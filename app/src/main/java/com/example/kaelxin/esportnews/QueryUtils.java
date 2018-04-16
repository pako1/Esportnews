package com.example.kaelxin.esportnews;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class QueryUtils {

    private QueryUtils() {
    }

    public static List<News> fetchData(String requestURL) {

        String jSonResponse = null;

        URL url = createUrl(requestURL);

        try {
            jSonResponse = makeHttpConnection(url);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return fetchJson(jSonResponse);
    }

    private static List<News> fetchJson(String response) {

        List<News> newsList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject responseOjbect = jsonObject.getJSONObject(StaticClass.RESPONSE);
            JSONArray jsonResult = responseOjbect.getJSONArray(StaticClass.RESULTS);
            for (int x = 0; x < jsonResult.length(); x++) {

                JSONObject resultObject = jsonResult.getJSONObject(x);
                String date = resultObject.getString(StaticClass.PUBLICATION_DATE);
                date = formatDate(date);
                String url = resultObject.getString(StaticClass.WEB_URL);
                String section = resultObject.getString(StaticClass.SECTION_NAME);
                JSONObject fieldObject = resultObject.getJSONObject(StaticClass.FIELDS);
                String headLine = fieldObject.getString(StaticClass.HEADLINE);

                String thumbnailUrl = fieldObject.getString(StaticClass.THUMBNAIL);

                JSONArray tagArray = resultObject.getJSONArray(StaticClass.TAGS);

                StringBuilder author = new StringBuilder();

                if (tagArray.length() == 0) {
                    author.append("");
                } else {
                    for (int i = 0; i < tagArray.length(); i++) {
                        JSONObject tagObject = tagArray.getJSONObject(i);
                        author.append(tagObject.getString(StaticClass.WEB_TITLE));
                    }
                }

                News news = new News(thumbnailUrl, headLine, date, author.toString(), url, section);

                newsList.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsList;
    }

    private static URL createUrl(String stringUrl) {

        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static String makeHttpConnection(URL url) throws IOException {

        String JsonResponse = "";

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        if (url == null) {
            return JsonResponse;
        }

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setRequestMethod(StaticClass.GET_WEBSERVICE);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                JsonResponse = readFromStream(inputStream);
            } else {
                Log.e(StaticClass.TAG_QUERY, "Error response code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return JsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(StaticClass.UTF8));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private static String formatDate(String rawDate) {

        SimpleDateFormat jsonFormatter = new SimpleDateFormat(StaticClass.JSON_DATE_FORMAT, Locale.US);
        try {
            Date parsedJsonDate = jsonFormatter.parse(rawDate);
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(StaticClass.DATE_FORMAT, Locale.US);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e(StaticClass.TAG_QUERY, "Error parsing JSON date: ", e);
            return "";
        }
    }
}
