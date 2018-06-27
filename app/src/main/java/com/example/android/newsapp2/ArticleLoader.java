package com.example.android.newsapp2;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by egi-megi on 07.06.18.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Create URL object
        // Perform the HTTP request for article data and process the response.
        List<Article> article = QueryUtilis.fetchArticleData(mUrl);
        // Update the information displayed to the user.
        return article;
    }
}
