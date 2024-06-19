package com.example.mystore;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    final String BASE_URL = "https://reqres.in/";

    private List<Data> users = new LinkedList<>();
    private MyAdapter adapter = new MyAdapter(MainActivity.this, users, this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();


        Client client = retrofit.create(Client.class);

        for(int i=0; i < 2; i++){
            getUsers(client, i+1);
        }


    }

    public void getUsers (Client client, int page){
        client.getUsers(page).enqueue(new Callback<UsersData>() {
            @Override
            public void onResponse(Call<UsersData> call, Response<UsersData> response) {
                users.addAll(response.body().getData());
                users.sort((o1, o2) -> {
                    int compare = Integer.compare(o1.getId(), o2.getId());
                    return compare;
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UsersData> call, Throwable throwable) {
            }
        });
    }

    public void onItemClick(Data user) {

        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("imageResId", user.getAvatar());
        intent.putExtra("id", user.getId());
        intent.putExtra("name", user.getFirst_name() + " " + user.getLast_name());
        intent.putExtra("email", user.getEmail());
        startActivity(intent);

        // Handle the click event here
        Toast.makeText(this, "Clicked: " + user.getFirst_name(), Toast.LENGTH_SHORT).show();
    }


}

