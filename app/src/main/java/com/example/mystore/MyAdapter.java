package com.example.mystore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;

    List<UserData> users;
    private OnItemClickListener listener;

    public MyAdapter(Context context, List<UserData> users, OnItemClickListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.data_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserData user = users.get(position);
        holder.id.setText(user.getId() + "");
        holder.firstName.setText(user.getFirst_name());
        holder.lastName.setText(user.getLast_name());
        holder.email.setText(user.getEmail());
        String avatarUrl = user.getAvatar();

        Glide.with(holder.itemView.getContext())
                .load(avatarUrl)
                .into(holder.avatar);

        //  holder.avatar.setImageResource(users.get(position).getAvatar());

        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<UserData> users){
        this.users = users;
    }
}
