package com.example.mystore;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;

    List<Data> users;

    public MyAdapter(Context context, List<Data> users) {
        this.context = context;
        this.users = users;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.data_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.id.setText(users.get(position).getId() + "");
        holder.firstName.setText(users.get(position).getFirst_name());
        holder.lastName.setText(users.get(position).getLast_name());
        holder.email.setText(users.get(position).getEmail());
        String avatarUrl = users.get(position).getAvatar();
        Glide.with(holder.itemView.getContext())
                .load(avatarUrl)
                .into(holder.avatar);
      //  holder.avatar.setImageResource(users.get(position).getAvatar());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateUsers(List<Data> newUsers) {
        users.addAll(newUsers);
        notifyDataSetChanged();
    }
}
