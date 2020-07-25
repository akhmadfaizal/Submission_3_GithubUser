package com.afi.bfaa.submission_3_githubuser;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afi.bfaa.submission_3_githubuser.adapter.ListUserAdapter;
import com.afi.bfaa.submission_3_githubuser.model.User;
import com.afi.bfaa.submission_3_githubuser.viewmodel.UserViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListUserAdapter adapter;
    private ProgressBar progressBar;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Github User");
        toolbar.setElevation(4);
        setSupportActionBar(toolbar);

        showRecyclerList();
        viewModelProvider();
        subscribeLiveData(adapter);
    }

    public void showRecyclerList(){
        // inisialisasi adapter
        adapter = new ListUserAdapter();
        // inisialisasi recylerView
        RecyclerView rvListUser = findViewById(R.id.rv_list_user);
        rvListUser.setHasFixedSize(true);
        // Set layout manager to position the items
        rvListUser.setLayoutManager(new LinearLayoutManager(this));
        // setAdapter ke recyclerView
        rvListUser.setAdapter(adapter);
    }

    /**
     * ViewModelProviders
     * Menghubungkan ViewModel dengan activity
     */
    public void viewModelProvider(){
        userViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(UserViewModel.class);
        userViewModel.setUsers();
    }

    /**
     * Subscribe
     * Cara mendapatkan value dari LiveData yang ada pada kelas ViewModel
     */
    public void subscribeLiveData(final ListUserAdapter adapter) {
        userViewModel.getUsers().observe(this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                if (users != null) {
                    adapter.setUser(users);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        // SearchView
        SearchManager searchManager = (SearchManager)  getSystemService(Context.SEARCH_SERVICE);

        if(searchManager != null){
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    showRecyclerList();
                    userViewModel.getSearchDataApi(newText);
                    subscribeLiveData(adapter);
                    return true;
                }
            });
        }
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting_language:
                Intent intentSetting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.favorite_detail:
                Intent intentFavorite = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intentFavorite);
        }
        return super.onOptionsItemSelected(item);
    }
}
