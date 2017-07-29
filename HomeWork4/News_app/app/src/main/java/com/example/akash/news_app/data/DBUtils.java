package com.example.akash.news_app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.example.akash.news_app.data.Contract.NewsItem.*;
import static com.example.akash.news_app.data.Contract.NewsItem.DESCRIPTION;
import static com.example.akash.news_app.data.Contract.NewsItem.PUBLISHED_AT;
import static com.example.akash.news_app.data.Contract.NewsItem.SOURCE;
import static com.example.akash.news_app.data.Contract.NewsItem.TITLE;
import static com.example.akash.news_app.data.Contract.NewsItem.URL;
import static com.example.akash.news_app.data.Contract.NewsItem.URL_TO_IMAGE;
import static com.example.akash.news_app.data.Contract.NewsItem.TABLE_NAME;

/**
 * Created by akash on 7/27/2017.
 */

// this class is inserting and deleting values in database
public class DBUtils {

    public static Cursor getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                PUBLISHED_AT + " DESC"
        );
        return cursor;
    }


    public static void bulkInsert(SQLiteDatabase db, ArrayList<NewsItem> newsItems) {

        db.beginTransaction();
        try {

            for (NewsItem a : newsItems) {
                ContentValues cv = new ContentValues();
                cv.put(SOURCE, a.getSource());
                cv.put(AUTHOR, a.getAuthor());
                cv.put(TITLE, a.getTitle());
                cv.put(DESCRIPTION, a.getDescription());
                cv.put(URL, a.getUrl());
                cv.put(URL_TO_IMAGE, a.getUrlToImage());
                cv.put(PUBLISHED_AT, a.getPublishedAt());
                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }
}