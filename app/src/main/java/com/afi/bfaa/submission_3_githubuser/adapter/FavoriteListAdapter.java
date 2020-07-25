package com.afi.bfaa.submission_3_githubuser.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afi.bfaa.submission_3_githubuser.DetailActivity;
import com.afi.bfaa.submission_3_githubuser.R;
import com.afi.bfaa.submission_3_githubuser.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHolder> {
    // Store a member variable for the user
    private ArrayList<User> mUser = new ArrayList<>();

    public void setFavorite(ArrayList<User> items){
        if (mUser.size() > 0) {
            mUser.clear();
        }
        mUser.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        // Return a new holder instance
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, final int position) {
        // Set item views based on your views and data model
        holder.tvUserName.setText(mUser.get(position).getLogin());
        holder.tvUserType.setText(mUser.get(position).getType());
        Glide.with(holder.itemView.getContext())
                .load(mUser.get(position).getAvatarUrl())
                .apply(new RequestOptions().override(60, 60))
                .into(holder.ivUser);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_USER, mUser.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView tvUserName, tvUserType;
        CircleImageView ivUser;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUser = itemView.findViewById(R.id.iv_user);
            tvUserName = itemView.findViewById(R.id.tv_name_user);
            tvUserType = itemView.findViewById(R.id.tv_type_user);
        }
    }
}
