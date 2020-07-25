package com.afi.bfaa.submission_3_githubuser.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.afi.bfaa.submission_3_githubuser";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static final class UserColumns implements BaseColumns {
        public static String TABLE_NAME = "user";
        public static String COLUMN_ID = "id";
        public static String COLUMN_USERNAME = "username";
        public static String COLUMN_AVATAR_URL = "avatar_url";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_TYPE = "type";
        public static String COLUMN_COMPANY = "company";
        public static String COLUMN_LOCATION = "location";
        public static String COLUMN_REPOSITORY = "repository";
        public static String COLUMN_FOLLOWERS = "followers";
        public static String COLUMN_FOLLOWING = "following";

        // URI content://com.afi.bfaa.submission_3_githubuser/user
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
