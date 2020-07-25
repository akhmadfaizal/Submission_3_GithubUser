package com.afi.bfaa.consumerapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHolder> {
    private Cursor cursor;
    private Context context;

    public FavoriteListAdapter(Context context) {
        this.context = context;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    private User getItemData(int position){
        if (!cursor.moveToPosition(position)){
            throw new IllegalStateException("INVALID");
        }
        return new User(cursor);
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
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        final User user = getItemData(position);
        holder.tvUserName.setText(user.getLogin());
        holder.tvUserType.setText(user.getType());
        Glide.with(holder.itemView.getContext())
                .load(user.getAvatarUrl())
                .apply(new RequestOptions().override(60, 60))
                .into(holder.ivUser);
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
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
