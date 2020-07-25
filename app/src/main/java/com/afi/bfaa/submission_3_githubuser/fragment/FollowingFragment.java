package com.afi.bfaa.submission_3_githubuser.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afi.bfaa.submission_3_githubuser.R;
import com.afi.bfaa.submission_3_githubuser.adapter.FollowingUserAdapter;
import com.afi.bfaa.submission_3_githubuser.model.User;
import com.afi.bfaa.submission_3_githubuser.viewmodel.FollowingViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {
    private static final String EXTRA_USERNAME = "extra_username";
    private FollowingUserAdapter adapter;
    private FollowingViewModel followingViewModel;

    public static FollowingFragment newInstance(String username) {

        Bundle args = new Bundle();
        args.putString(EXTRA_USERNAME, username);
        FollowingFragment fragment = new FollowingFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public FollowingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String username = getArguments().getString(EXTRA_USERNAME);
        showRecyclerList(view);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);

        viewModelProvider(username, progressBar);
        subscribeLiveData(adapter, progressBar);
    }

    public void showRecyclerList(View view){
        RecyclerView recyclerView = view.findViewById(R.id.rv_list_following);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FollowingUserAdapter();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    /**
     * ViewModelProviders
     * Menghubungkan ViewModel dengan activity/fragment
     */
    public void viewModelProvider(String username, final ProgressBar progressBar){
        followingViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel.class);
        followingViewModel.setFollowing(username, progressBar);
    }

    /**
     * Subscribe
     * Cara mendapatkan value dari LiveData yang ada pada kelas ViewModel
     */
    public void subscribeLiveData(final FollowingUserAdapter adapter, final ProgressBar progressBar) {
        followingViewModel.getFollowing().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                if (users != null) {
                    adapter.setFollowing(users);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
