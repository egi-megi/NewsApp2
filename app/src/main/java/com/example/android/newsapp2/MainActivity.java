package com.example.android.newsapp2;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    // Constant value for the article loader ID
    private static final int ARTICLE_LOADER_ID = 1;

    public static final String USGS_REQUEST_URL =
            "https://content.guardianapis.com/search?";

    /**
     * Adapter for the list of articles
     */
    private ArticleAdapter mAdapter;

    public ImageView emptyPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find a reference to the {@link ListView} in the layout
        ListView articleListView = (ListView) findViewById(R.id.list);

        // Find a reference to the Image View in empty page in the layout
        emptyPage = (ImageView) findViewById(R.id.empty_text_view);
        articleListView.setEmptyView(findViewById(R.id.empty_text_view));

        // Create a new {@link ArrayAdapter} of articles
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(mAdapter);


        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current article that was clicked on
                Article currentArticle = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri articleUri = Uri.parse(currentArticle.getWebUrl());

                // Create a new intent to view the article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            View progressBar = findViewById(R.id.loading_progress_bar);
            progressBar.setVisibility(View.GONE);
            emptyPage.setImageResource(R.drawable.no_connection);

        }
    }


    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        Date today = Calendar.getInstance().getTime();
        String todayDateString = formatDate(today);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String fromDate = sharedPrefs.getString(
                getString(R.string.settings_from_date_key),
                todayDateString);
        //Set today's current date for fromDate if user don't set different date
        if ("today".equalsIgnoreCase(fromDate)){
            fromDate=todayDateString;
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(getString(R.string.settings_from_date_key), fromDate);
            editor.commit();
        }
        String toDate = sharedPrefs.getString(
                getString(R.string.settings_to_date_key),
                todayDateString);
        //Set today's current date for toDate if user don't set different date
        if (toDate.equalsIgnoreCase("today")) {
            toDate=todayDateString;
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(getString(R.string.settings_to_date_key), toDate);
            editor.commit();
        }

        String topic = sharedPrefs.getString(
                getString(R.string.settings_topic_key),
                getString(R.string.settings_topic_default)
        );

        String language = sharedPrefs.getString(
                getString(R.string.settings_language_key),
                getString(R.string.settings_language_default)
        );

        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter("q", topic);
        if (!fromDate.isEmpty()) {
             uriBuilder.appendQueryParameter("from-date", fromDate);
        }
        if (!toDate.isEmpty()) {
            uriBuilder.appendQueryParameter("to-date", toDate);
        }
        uriBuilder.appendQueryParameter("api-key", "21bb4e65-d7b8-4e81-ae28-632e388ed476");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("lang", language);
        uriBuilder.appendQueryParameter("order-by", orderBy);

        // Return the completed uri `https://content.guardianapis.com/search?q=topic&from-date=fromDate&to-date=toDate&api-key=test&show-tags=contributor&lang=language&order-by=orderBy
        Log.e("url",uriBuilder.toString());
        return new ArticleLoader(this, uriBuilder.toString());
    }

    //Set properly format of date
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dateObject);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        View progressBar = findViewById(R.id.loading_progress_bar);
        progressBar.setVisibility(View.GONE);

        emptyPage.setImageResource(R.drawable.no_article);
        // Clear the adapter of previous article
        mAdapter.clear();

        // If there is a valid list of {@link Article}s, then add them to the adapter's
        // articles set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }


    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings_action) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

