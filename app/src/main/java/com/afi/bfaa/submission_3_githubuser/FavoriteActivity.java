package com.afi.bfaa.submission_3_githubuser;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afi.bfaa.submission_3_githubuser.adapter.FavoriteListAdapter;
import com.afi.bfaa.submission_3_githubuser.database.DatabaseContract;
import com.afi.bfaa.submission_3_githubuser.database.UserHelper;
import com.afi.bfaa.submission_3_githubuser.helper.MappingHelper;
import com.afi.bfaa.submission_3_githubuser.model.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity implements LoadUserCallback {
    private FavoriteListAdapter favoriteListAdapter;
    private UserHelper userHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Favorite User");
        toolbar.setElevation(4);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }

        RecyclerView rvFavorite = findViewById(R.id.rv_list_user);
        rvFavorite.setLayoutManager(new LinearLayoutManager(this));
        rvFavorite.setHasFixedSize(true);
        favoriteListAdapter = new FavoriteListAdapter();
        rvFavorite.setAdapter(favoriteListAdapter);

        userHelper = UserHelper.getInstance(getApplicationContext());
        userHelper.open();
//        mUser = userHelper.getAllUser();

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(DatabaseContract.UserColumns.CONTENT_URI, true, myObserver);

        new LoadUserAsync(this, this).execute();
    }

    // Back
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<User> mUser = userHelper.getAllUser();
        favoriteListAdapter.setFavorite(mUser);
    }

    @Override
    public void preExecute() {
        Log.d("FavoriteActivity", "preExecute: Start");
    }

    @Override
    public void postExecute(ArrayList<User> user) {
        if (user.size() > 0){
            favoriteListAdapter.setFavorite(user);
        }else {
            favoriteListAdapter.setFavorite(new ArrayList<User>());
        }
    }

    private static class LoadUserAsync extends AsyncTask<Void, Void, ArrayList<User>>{

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadUserCallback> loadUserCallbackWeakReference;

        private LoadUserAsync(Context context, LoadUserCallback callback){
            weakContext = new WeakReference<>(context);
            loadUserCallbackWeakReference = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadUserCallbackWeakReference.get().preExecute();
        }

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.UserColumns.CONTENT_URI, null, null, null, null);
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);
            loadUserCallbackWeakReference.get().postExecute(users);
        }
    }

    public static class DataObserver extends ContentObserver {
        Context context;

        public DataObserver(Handler handler, Context context){
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadUserAsync(context, (LoadUserCallback) context).execute();
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        userHelper.close();
//    }
}
interface LoadUserCallback{
    void preExecute();
    void postExecute(ArrayList<User> user);
}
