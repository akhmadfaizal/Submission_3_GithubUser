package com.afi.bfaa.consumerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvList;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvList = findViewById(R.id.rv_list);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FavoriteList().execute();
            }
        });

        new FavoriteList().execute();

    }

    @SuppressLint("StaticFieldLeak")
    private class FavoriteList extends AsyncTask<Void, Void, Cursor>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getApplicationContext().getContentResolver().query(DatabaseContract.UserColumns.CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            FavoriteListAdapter favoriteListAdapter = new FavoriteListAdapter(getApplicationContext());
            favoriteListAdapter.setCursor(cursor);
            favoriteListAdapter.notifyDataSetChanged();
            rvList.setAdapter(favoriteListAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
