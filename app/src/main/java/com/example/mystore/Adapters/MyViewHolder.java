package com.example.mystore.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystore.Models.OnItemClickListener;
import com.example.mystore.R;
import com.example.mystore.Models.UserData;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView avatar;
    TextView id;
    TextView firstName;
    TextView lastName;
    TextView email;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        avatar = itemView.findViewById(R.id.imageView);
        id = itemView.findViewById(R.id.idTxt);
        firstName = itemView.findViewById(R.id.firstName);
        lastName = itemView.findViewById(R.id.latstName);
        email = itemView.findViewById(R.id.email);
    }

    public void bind(final UserData user, final OnItemClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(user);
            }
        });

    }
}
