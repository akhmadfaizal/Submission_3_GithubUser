package com.afi.bfaa.submission_3_githubuser.helper;

import android.database.Cursor;

import com.afi.bfaa.submission_3_githubuser.database.DatabaseContract;
import com.afi.bfaa.submission_3_githubuser.model.User;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<User> mapCursorToArrayList(Cursor userCursor) {
        ArrayList<User> userList = new ArrayList<>();

        while (userCursor.moveToNext()){
//            int id = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns._ID));
            int id = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_ID));
            String username = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_USERNAME));
            String avatarUrl = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_AVATAR_URL));
            String name = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_USERNAME));
            String type = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_TYPE));
            String company = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_COMPANY));
            String location = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_LOCATION));
            int repository = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_REPOSITORY));
            int followers = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_FOLLOWERS));
            int following = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_FOLLOWING));
            userList.add(new User(id, username, avatarUrl, name, type, company, location, repository, followers, following));
        }

        return userList;
    }

//    public static User mapCursorToObject(Cursor userCursor){
//        userCursor.moveToFirst();
//        int id = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns._ID));
//        String username = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_USERNAME));
//        String avatarUrl = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_AVATAR_URL));
//        String name = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_USERNAME));
//        String type = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_TYPE));
//
////        return new User(id, username, avatarUrl, name, type);
//    }
}
