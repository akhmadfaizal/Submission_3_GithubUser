package com.afi.bfaa.submission_3_githubuser.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afi.bfaa.submission_3_githubuser.R;
import com.afi.bfaa.submission_3_githubuser.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowingUserAdapter extends RecyclerView.Adapter<FollowingUserAdapter.FollowingViewHolder> {
    // Store a member variable for the user
    private ArrayList<User> mUser = new ArrayList<>();

    public void setFollowing(ArrayList<User> items){
        mUser.clear();
        mUser.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FollowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        // Return a new holder instance
        return new FollowingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingViewHolder holder, int position) {
        // Get the data model based on position
        final User user = mUser.get(position);
        holder.tvUserName.setText(user.getLogin());
        holder.tvUserType.setText(user.getType());
        Glide.with(holder.itemView.getContext())
                .load(user.getAvatarUrl())
                .apply(new RequestOptions().override(60, 60))
                .into(holder.ivUser);
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class FollowingViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserType;
        CircleImageView ivUser;
        public FollowingViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.iv_user);
            tvUserName = itemView.findViewById(R.id.tv_name_user);
            tvUserType = itemView.findViewById(R.id.tv_type_user);
        }
    }
}
