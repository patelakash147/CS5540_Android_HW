package com.example.akash.news_app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by akash on 7/27/2017.
 */

// this class is extends SQLiteOpenHelper
public class DBHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "news.db";
    private static final String TAG = "dbhelper";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NEWSITEM_TABLE = "CREATE TABLE " + Contract.NewsItem.TABLE_NAME + " (" +
                Contract.NewsItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Contract.NewsItem.SOURCE + " TEXT NOT NULL, " +
                Contract.NewsItem.AUTHOR + " TEXT NOT NULL, " +
                Contract.NewsItem.TITLE + " TEXT NOT NULL, " +
                Contract.NewsItem.DESCRIPTION + " TEXT NOT NULL, " +
                Contract.NewsItem.URL + " TEXT NOT NULL, " +
                Contract.NewsItem.URL_TO_IMAGE + " TEXT NOT NULL, " +
                Contract.NewsItem.PUBLISHED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        Log.d(TAG, "Create SQL: " + SQL_CREATE_NEWSITEM_TABLE);
        db.execSQL(SQL_CREATE_NEWSITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Contract.NewsItem.TABLE_NAME);
        onCreate(db);
    }
}
