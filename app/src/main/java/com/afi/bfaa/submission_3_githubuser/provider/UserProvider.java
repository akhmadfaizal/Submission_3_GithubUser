package com.afi.bfaa.submission_3_githubuser.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.afi.bfaa.submission_3_githubuser.database.DatabaseContract;
import com.afi.bfaa.submission_3_githubuser.database.UserHelper;

public class UserProvider extends ContentProvider {
    private static final int USER = 1;
    private static final int USER_ID = 2;
    private UserHelper userHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        // content://com.afi.bfaa.submission_3_githubuser/user
        sUriMatcher.addURI(DatabaseContract.AUTHORITY, DatabaseContract.UserColumns.TABLE_NAME, USER);

        sUriMatcher.addURI(DatabaseContract.AUTHORITY,
                DatabaseContract.UserColumns.TABLE_NAME + "/#",
                USER_ID);
    }
    public UserProvider() {
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        userHelper = UserHelper.getInstance(getContext());
        userHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case USER:
                cursor = userHelper.queryAll();
                break;
            case USER_ID:
                cursor = userHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long added;
        switch (sUriMatcher.match(uri)){
            case USER:
                added = userHelper.insert(values);
                break;
            default:
                added = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(DatabaseContract.UserColumns.CONTENT_URI, null);

        return Uri.parse(DatabaseContract.UserColumns.CONTENT_URI + "/" + added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int deleted;
        switch (sUriMatcher.match(uri)){
            case USER_ID:
                deleted = userHelper.deleteById(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(DatabaseContract.UserColumns.CONTENT_URI, null);
        return deleted;
    }



}
