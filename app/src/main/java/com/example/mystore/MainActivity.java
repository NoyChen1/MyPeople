package com.example.mystore;


import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    final String BASE_URL = "https://reqres.in/";

    private List<Data> users = new ArrayList<>();
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
        MyAdapter adapter = new MyAdapter(MainActivity.this, users);
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();


        Client client = retrofit.create(Client.class);
//        client.getUser("3").enqueue(new Callback<UserData>() {
//            @Override
//            public void onResponse(Call<UserData> call, Response<UserData> response) {
//                textView.setText(response.body().getData().getEmail());
//            }
//
//            @Override
//            public void onFailure(Call<UserData> call, Throwable throwable) {
//                textView.setText(throwable.getMessage());
//            }
//        });
        client.getUsers(1).enqueue(new Callback<UsersData>() {
            @Override
            public void onResponse(Call<UsersData> call, Response<UsersData> response) {
                if(response.isSuccessful() && response.body() != null){
                    users.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<UsersData> call, Throwable throwable) {
            }
        });
        client.getUsers(2).enqueue(new Callback<UsersData>() {
            @Override
            public void onResponse(Call<UsersData> call, Response<UsersData> response) {
                users.addAll(response.body().getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UsersData> call, Throwable throwable) {
            }
        });



    }

}