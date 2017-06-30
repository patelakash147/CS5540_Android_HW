package com.example.akash.news_app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "mainactivity";

    private RecyclerView recycleview;
    private NewsAdapter mNewsAdapter;
    private EditText searchbox;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycleview = (RecyclerView) findViewById(R.id.recycler_news);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleview.setLayoutManager(layoutManager);
        recycleview.setHasFixedSize(true);

        searchbox = (EditText) findViewById(R.id.searchbox);
        spinner = (ProgressBar) findViewById(R.id.loading_indicator);

        loadNewsData();


    }

    private void loadNewsData() {
        new FetchNewsTask("").execute();
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
                String s = searchbox.getText().toString();
                FetchNewsTask task = new FetchNewsTask(s);
                task.execute();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    class FetchNewsTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> implements NewsAdapter.ItemClickListener {
        String query;
        ArrayList<NewsItem> data;

        FetchNewsTask(String s) {
            query = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<NewsItem> doInBackground(URL... params) {
            ArrayList<NewsItem> result = null;
            URL newsRequestURL = NetworkUtils.buildUrl();

            try {
                String json = NetworkUtils.getResponseFromHttpUrl(newsRequestURL);
                result = NetworkUtils.parseJSON(json);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<NewsItem> newsData) {
            this.data = newsData;
            super.onPostExecute(data);
            spinner.setVisibility(View.INVISIBLE);

            if (data != null) {
                // 13. Pass in this as the ItemClickListener to the NewsAdapter constructor
                NewsAdapter adapter = new NewsAdapter(data, this);
                recycleview.setAdapter(adapter);
            }
        }


        @Override
        public void onListItemClick(int clickedItemIndex) {

            openWebPage(data.get(clickedItemIndex).getUrl());
        }

        public void openWebPage(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}
