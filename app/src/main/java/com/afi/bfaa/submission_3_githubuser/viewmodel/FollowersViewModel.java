package com.afi.bfaa.submission_3_githubuser.viewmodel;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.afi.bfaa.submission_3_githubuser.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FollowersViewModel extends ViewModel {

    private MutableLiveData<ArrayList<User>> listUsers = new MutableLiveData<>();
    ArrayList<User> listItems = new ArrayList<>();

    public void setFollowers(String username, final ProgressBar progressBar){
        AsyncHttpClient client = new AsyncHttpClient();
        String url ="https://api.github.com/users/" + username +"/followers";
        client.addHeader("Authorization", "token 96012dddfcef72602ca662f51b41d5f97b29cbe3");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.GONE);
                String result = new String(responseBody);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject responseObject = jsonArray.getJSONObject(i);
                        String userName = responseObject.getString("login");
                        String avatar_url = responseObject.getString("avatar_url");
                        String type = responseObject.getString("type");

                        User user = new User();
                        user.setLogin(userName);
                        user.setAvatarUrl(avatar_url);
                        user.setType(type);
                        listItems.add(user);

                    }
//                    adapter.setUser(listItems);
                    listUsers.postValue(listItems);
                }catch (Exception e){
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Jika koneksi gagal
                progressBar.setVisibility(View.GONE);
                String errorMessage;

                switch (statusCode){
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;

                    case 403:
                        errorMessage = statusCode + " : Forbidden";
                        break;

                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;

                    default:
                        errorMessage = statusCode + " : " + error.getMessage();
                        break;
                }
                Log.d("FollowersViewModel ", errorMessage);
                error.printStackTrace();
            }
        });
    }

    public LiveData<ArrayList<User>> getFollowers(){return listUsers;}
}
