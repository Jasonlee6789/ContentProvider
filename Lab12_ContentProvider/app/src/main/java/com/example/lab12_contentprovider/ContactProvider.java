package com.example.lab12_contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class ContactProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.lab12_contentprovider.ContactProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/cpcontacts";
    static final Uri CONTENT_URL = Uri.parse(URL);

    // create a Uri macther
    static final int uriCode = 1;
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "cpcontacts", uriCode);
    }

    static final String id = "id";
    static final String name = "name";

    // create the database
    private SQLiteDatabase sqlDB;
    static final String DATABASE_NAME = "myContacts";
    static final String TABLE_NAME = "names";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME +
            " ( id INTERGER PRIMARY KEY AUTOINCREMENT , " +
            " name TEXT NOT NULL) ;";


    // this will handle the database key and value
    private static HashMap<String, String> values;


    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        sqlDB = dbHelper.getWritableDatabase();
        if (sqlDB != null) {
            return true;
        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)){
            case uriCode:
            queryBuilder.setProjectionMap(values);
            break;

            default:
               throw new IllegalArgumentException("Unknown URI"+ uri);
        }

        Cursor cursor = queryBuilder.query(sqlDB, projection, selection, selectionArgs, null,null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case uriCode:
                return "vnd.android.cursor.dir/cpcontacts";

                default:
                    throw new IllegalArgumentException("Unknown URI"+ uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowID = sqlDB.insert(TABLE_NAME, null, contentValues);

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URL, rowID);
            getContext().getContentResolver().notifyChange(_uri,null);

        return _uri;
        }
        Toast.makeText(getContext(),"Row Insret Failed",Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDelete = 0;
        switch (uriMatcher.match(uri)){
            case uriCode:
            rowsDelete = sqlDB.delete(TABLE_NAME, selection, selectionArgs);
            break;

            default:
                throw new IllegalArgumentException("Unknown URI"+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDelete;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        int rowsUpdated = 0;
        switch (uriMatcher.match(uri)){
            case uriCode:
                rowsUpdated = sqlDB.update(TABLE_NAME, contentValues,selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI"+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdated;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqlDB.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqlDB.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqlDB);
        }
    }
}
