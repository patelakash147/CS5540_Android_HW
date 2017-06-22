package com.example.akash.news_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView newsview;
    private EditText searchbox;
    private ProgressBar loading ;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsview = (TextView) findViewById(R.id.news);
        searchbox = (EditText) findViewById(R.id.searchbox);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        loading = (ProgressBar) findViewById(R.id.loading_indicator);

     //   loadNewsData();
    }@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.forecast, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh){
            loadNewsData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadNewsData() {

        URL githubSearchUrl = NetworkUtils.buildUrl();
        spinner.setVisibility(View.VISIBLE);


        new FetchNewsTask().execute();
    }

    public class FetchNewsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            URL newsRequestURL = NetworkUtils.buildUrl();

            try {
                return NetworkUtils.getResponseFromHttpUrl(newsRequestURL);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String newsData) {
            loading.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.GONE);

            if (newsData != null) {
                newsview.setText(newsData);
            }
        }
    }
}
