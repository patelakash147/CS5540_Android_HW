package com.example.akash.news_app.data;

import android.provider.BaseColumns;

/**
 * Created by akash on 7/27/2017.
 */

//  this class is implements basecolumns
public class Contract {

    public static final class NewsItem implements BaseColumns {

        public static final String TITLE = "title";
        public static final String TABLE_NAME = "news_items";
        public static final String PUBLISHED_AT = "published_at";
        public static final String SOURCE = "source";
        public static final String URL = "url";
        public static final String URL_TO_IMAGE = "urlToImage";
        public static final String DESCRIPTION = "description";
        public static final String AUTHOR = "author";




    }

}

