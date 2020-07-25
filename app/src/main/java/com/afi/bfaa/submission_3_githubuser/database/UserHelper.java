package com.afi.bfaa.submission_3_githubuser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import com.afi.bfaa.submission_3_githubuser.model.User;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class UserHelper {
    private static final String DATABASE_TABLE = DatabaseContract.UserColumns.TABLE_NAME;
    private static DatabaseHelper dataBaseHelper;
    private static UserHelper INSTANCE;

    private static SQLiteDatabase database;

    private UserHelper(Context context){
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static UserHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new UserHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }
    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    // metode untuk melakukan proses CRUD-nya, metode pertama adalah untuk mengambil data.
    /**
     * Ambil data dari semua note yang ada di dalam database
     *
     * @return cursor hasil queryAll
     */
    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                DatabaseContract.UserColumns.COLUMN_ID + " ASC");
    }

    // metode untuk mengambal data dengan id tertentu.
    /**
     * Ambil data dari note berdasarakan parameter id
     *
     * @param id id note yang dicari
     * @return cursor hasil queryAll
     */
    public Cursor queryById(String id) {
        return database.query(DATABASE_TABLE, null
                , DatabaseContract.UserColumns.COLUMN_ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public ArrayList<User> getAllUser(){
        ArrayList<User> listUser = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                DatabaseContract.UserColumns.COLUMN_ID + " ASC");
        cursor.moveToFirst();
        User user;
        if (cursor.getCount() > 0){
            do {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_ID)));
                user.setLogin(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_USERNAME)));
                user.setAvatarUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_AVATAR_URL)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_NAME)));
                user.setType(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_TYPE)));
                user.setCompany(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_COMPANY)));
                user.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_LOCATION)));
                user.setPublicRepos(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_REPOSITORY)));
                user.setFollowers(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_FOLLOWERS)));
                user.setFollowing(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.COLUMN_FOLLOWING)));

                listUser.add(user);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return listUser;
    }

    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, DatabaseContract.UserColumns.COLUMN_ID + " = ?", new String[]{id});
    }
}
