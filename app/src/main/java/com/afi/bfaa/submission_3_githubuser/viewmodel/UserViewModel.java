package com.afi.bfaa.submission_3_githubuser.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.afi.bfaa.submission_3_githubuser.model.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class UserViewModel extends ViewModel {
    // MutableLiveData bisa kita ubah value-nya
    private MutableLiveData<ArrayList<User>> listUsers = new MutableLiveData<>();
    ArrayList<User> listItems = new ArrayList<>();

    // setter
    public void setUsers(){
        // AsyncHttpClient, berarti kita akan menggunakan client yang bertanggung jawab untuk koneksi data dan sifatnya adalah asynchronous.
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/users";
        client.addHeader("Authorization", "token 96012dddfcef72602ca662f51b41d5f97b29cbe3");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            // Callback onSuccess() dipanggil ketika response memberikan kode status 200, yang artinya koneksi berhasil.
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Jika koneksi berhasil
                String response = new String(responseBody);
                Log.d("JSON", response);
                try {
                    JSONArray dataArray  = new JSONArray(response);
                    for (int i = 0; i < dataArray.length(); i++){
                        JSONObject dataObject = dataArray.getJSONObject(i);
                        String username = dataObject.getString("login");

                        // set data to model
                        User user = new User();
                        user.setLogin(username);

                        // get username for endpoint detail
                        getDetailUserApi(username);

                    }
                }catch (JSONException e){
                    Log.e("json", "unexpected JSON exception", e);
                }
            }

            // Callback onFailure() dipanggil ketika response memberikan kode status 4xx (seperti 401, 403, 404, dll), yang artinya koneksi gagal.
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Jika koneksi gagal
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
                Log.d("UserViewModel ", errorMessage);
                error.printStackTrace();
            }
        });
    }

    public void getDetailUserApi(String username){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/users/" + username;
        client.addHeader("Authorization", "token 96012dddfcef72602ca662f51b41d5f97b29cbe3");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d("UserViewModel Detail", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int id = jsonObject.getInt("id");
                    String username = jsonObject.getString("login");
                    String name = jsonObject.getString("name");
                    String avatarUrl = jsonObject.getString("avatar_url");
                    String type = jsonObject.getString("type");
                    String location = jsonObject.getString("location");
                    String company = jsonObject.getString("company");
                    int repository = jsonObject.getInt("public_repos");
                    int followers = jsonObject.getInt("followers");
                    int following = jsonObject.getInt("following");

                    User user = new User();
                    user.setId(id);
                    user.setLogin(username);
                    user.setName(name);
                    user.setAvatarUrl(avatarUrl);
                    user.setType(type);
                    user.setLocation(location);
                    user.setCompany(company);
                    user.setPublicRepos(repository);
                    user.setFollowers(followers);
                    user.setFollowing(following);

                    // initialize User
                    listItems.add(user);
                    listUsers.postValue(listItems);
                } catch (Exception e) {
                    Log.e("UserViewModel Detail",  "Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Jika koneksi gagal
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
                Log.d("UserViewModel Detail", errorMessage);
                error.printStackTrace();
            }
        });
    }

    public void getSearchDataApi(String username){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.github.com/search/users?q=" + username;
        client.addHeader("Authorization", "token 96012dddfcef72602ca662f51b41d5f97b29cbe3");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d("UserViewModel Search", result);
                listItems.clear();
                try {
                    JSONObject dataObject = new JSONObject(result);
                    JSONArray items = dataObject.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++){
                        Log.d("UserViewModel Search", "onSuccess: "+items);
                        JSONObject item = items.getJSONObject(i);
                        String username = item.getString("login");

                        User user = new User();
                        user.setLogin(username);
                        getDetailUserApi(username);
                    }
                }catch (Exception e){
                    Log.d("UserViewModel Search" ,"Error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Jika koneksi gagal
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
                Log.d("UserViewModel Search", errorMessage);
                error.printStackTrace();
            }
        });
    }

    // getter
    // sedangkan LiveData bersifat read-only.
    public LiveData<ArrayList<User>> getUsers(){
        return listUsers;
    }
}
