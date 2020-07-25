package com.afi.bfaa.submission_3_githubuser;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.afi.bfaa.submission_3_githubuser.adapter.SectionsPagerAdapter;
import com.afi.bfaa.submission_3_githubuser.database.DatabaseContract;
import com.afi.bfaa.submission_3_githubuser.database.UserHelper;
import com.afi.bfaa.submission_3_githubuser.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "extra_user";
    private Menu menu;
    private User user;
    private UserHelper userHelper;
    private boolean statusFavorite = false;
//    private Uri uriWithId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Detail Github User");
        toolbar.setElevation(0);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        }

        CircleImageView cvDetImage = findViewById(R.id.cv_det_user);
        TextView tvDetUsername = findViewById(R.id.tv_det_username_user);
        TextView tvDetName = findViewById(R.id.tv_det_name_user);
        TextView tvDetCompany = findViewById(R.id.tv_det_company);
        TextView tvDetLocation = findViewById(R.id.tv_det_location);
        TextView tvDetRepository = findViewById(R.id.tv_det_repository);
        TextView tvDetFollowers = findViewById(R.id.tv_det_followers);
        TextView tvDetFollowing = findViewById(R.id.tv_det_following);

        user = getIntent().getParcelableExtra(EXTRA_USER);

        Glide.with(this)
                .load(user.getAvatarUrl())
                .apply(new RequestOptions().override(150,150))
                .into(cvDetImage);

        String usernameDet = user.getLogin();
        String nameDet = user.getName();
        String companyDet = user.getCompany();
        String locationDet = user.getLocation();
        int repositoryDet = user.getPublicRepos();
        int followersDet = user.getFollowers();
        int followingDet = user.getFollowing();

        tvDetUsername.setText(usernameDet);
        if (nameDet.equals("null")) {
            tvDetName.setText("No Name");
        } else {
            tvDetName.setText(nameDet);
        }
        if (companyDet.equals("null")){
            tvDetCompany.setText(" No Company, ");
        } else {
            tvDetCompany.setText(" " + companyDet + ", ");
        }
        if (locationDet.equals("null")) {
            tvDetLocation.setText(" No Location");
        } else {
            tvDetLocation.setText(" " + locationDet);
        }
//        tvDetRepository.setText(Integer.toString(repositoryDet));
//        tvDetFollowers.setText(Integer.toString(followersDet));
//        tvDetFollowing.setText(Integer.toString(followingDet));
        tvDetRepository.setText(" : " + repositoryDet + ", ");
        tvDetFollowers.setText(" : " + followersDet + ", ");
        tvDetFollowing.setText(" : " + followingDet + " ");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        // ambil data username
        sectionsPagerAdapter.setUsername(usernameDet);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        userHelper = UserHelper.getInstance(getApplicationContext());
        userHelper.open();

        checkUsername();
    }

    private void checkUsername(){
        ArrayList<User> checkUsernameDb = userHelper.getAllUser();
        for (User user: checkUsernameDb){
            if (this.user.getId().equals(user.getId())){
                statusFavorite = !statusFavorite;
            }
            if (statusFavorite){
                break;
            }
        }

    }

    // Menu
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite_menu, menu);
        this.menu = menu;
        setStatusFavorite(statusFavorite);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

            case R.id.favorite:
                // Gunakan contentvalues untuk menampung data
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.UserColumns.COLUMN_ID, user.getId());
                values.put(DatabaseContract.UserColumns.COLUMN_USERNAME, user.getLogin());
                values.put(DatabaseContract.UserColumns.COLUMN_AVATAR_URL, user.getAvatarUrl());
                values.put(DatabaseContract.UserColumns.COLUMN_NAME, user.getName());
                values.put(DatabaseContract.UserColumns.COLUMN_TYPE, user.getType());
                values.put(DatabaseContract.UserColumns.COLUMN_COMPANY, user.getCompany());
                values.put(DatabaseContract.UserColumns.COLUMN_LOCATION, user.getLocation());
                values.put(DatabaseContract.UserColumns.COLUMN_REPOSITORY, user.getPublicRepos());
                values.put(DatabaseContract.UserColumns.COLUMN_FOLLOWERS, user.getFollowers());
                values.put(DatabaseContract.UserColumns.COLUMN_FOLLOWING, user.getFollowing());

                if (statusFavorite){
                    userHelper.deleteById(String.valueOf(user.getId()));
                    Toast.makeText(this,"Data dihapus", Toast.LENGTH_SHORT).show();
                    // Gunakan uriWithId untuk delete
                    // content://com.dicoding.picodiploma.mynotesapp/note/id
//                    uriWithId = Uri.parse(DatabaseContract.UserColumns.CONTENT_URI + "/" + user.getId());
//                    getContentResolver().delete(uriWithId, null, null);
//                    Toast.makeText(DetailActivity.this, "Satu item berhasil dihapus", Toast.LENGTH_SHORT).show();
                } else {
                    userHelper.insert(values);
                    Toast.makeText(this,"Data Masuk", Toast.LENGTH_SHORT).show();
                    // Gunakan content uri untuk insert
                    // content://com.dicoding.picodiploma.mynotesapp/note/
//                    getContentResolver().insert(DatabaseContract.UserColumns.CONTENT_URI, values);
//                    Toast.makeText(DetailActivity.this, "Satu item berhasil disimpan", Toast.LENGTH_SHORT).show();
                }
                statusFavorite = !statusFavorite;
                setStatusFavorite(statusFavorite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setStatusFavorite(Boolean statusFavorite){
        if (statusFavorite){
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
        } else {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        userHelper.close();
//    }
}
