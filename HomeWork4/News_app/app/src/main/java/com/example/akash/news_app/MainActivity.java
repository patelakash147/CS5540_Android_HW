package com.example.akash.news_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.akash.news_app.data.Contract;
import com.example.akash.news_app.data.DBHelper;
import com.example.akash.news_app.data.DBUtils;

// for loadmanager
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, NewsAdapter.ItemClickListener{
    static final String TAG = "mainactivity";

    private RecyclerView recycleview;
    private NewsAdapter mNewsAdapter;
    private ProgressBar spinner;

    // this field for sqldatabase , object of cursor and field for loader
    private SQLiteDatabase databases;
    private Cursor cursor;
    private static final int NEWS_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this will display the recycle view depends whats in database
        recycleview = (RecyclerView) findViewById(R.id.recycler_news);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleview.setLayoutManager(layoutManager);
        recycleview.setHasFixedSize(true);
        spinner = (ProgressBar) findViewById(R.id.loading_indicator);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); // it will check application is installed if not then load data from database
        boolean isFirst = prefs.getBoolean("isfirst", true);

        if (isFirst) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isfirst", false);
            editor.commit();
        }
        ScheduleUtils.scheduleRefresh(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databases = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DBUtils.getAll(databases);
        mNewsAdapter = new NewsAdapter(cursor, this);
        recycleview.setAdapter(mNewsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();

        switch (itemNumber) {
            case R.id.action_refresh:
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {


        return new AsyncTaskLoader<Void>(this) {

            // its for showing loading indicator
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            public Void loadInBackground() {
                //refresh method
                NewsJob.refreshArticles(MainActivity.this);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {

        spinner.setVisibility(View.INVISIBLE); // hide loading indicator

        // get form database and reset data in recycleview
        databases = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DBUtils.getAll(databases);
        mNewsAdapter = new NewsAdapter(cursor, this);
        recycleview.setAdapter(mNewsAdapter);
        mNewsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {}

    //  update clicked items
    @Override
    public void onListItemClick(Cursor cursor, int clickedItemIndex) {
        cursor.moveToPosition(clickedItemIndex);
        String url = cursor.getString(cursor.getColumnIndex(Contract.NewsItem.URL));
        Log.d(TAG, String.format("Url %s", url));

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}