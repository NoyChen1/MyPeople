package com.example.mystore.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mystore.Network.Client;
import com.example.mystore.Adapters.MyAdapter;
import com.example.mystore.Models.OnItemClickListener;
import com.example.mystore.R;
import com.example.mystore.DataBase.UserDao;
import com.example.mystore.Models.UserData;
import com.example.mystore.DataBase.UserDatabase;
import com.example.mystore.Models.UsersData;
import com.google.android.material.button.MaterialButton;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    final String BASE_URL = "https://reqres.in/";
    private List<UserData> users = new LinkedList<>();
    private MyAdapter adapter = new MyAdapter(MainActivity.this, users, this);
    private UserDatabase usersDB;
    private UserDao userDao;
    private MaterialButton addUserBtn;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private boolean loading = true;
    private int currentPage = 1;
    private Client client;

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

        usersDB = Room.databaseBuilder(getApplicationContext(),
                UserDatabase.class, "UsersDB").build();
        userDao = usersDB.getUserDao();

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        addUserBtn = findViewById(R.id.add_user_btn);

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();


        client = retrofit.create(Client.class);

        loadPage(currentPage);
        for(int i=0; i < 2; i++){
            getUsers(client, i+1);
        }

        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Add User", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                if(dy > 0 ){
                    if(loading){
                        if((visibleItemCount + pastVisibleItems) >= totalItemCount){
                            loading = false;
                            currentPage++;
                            loadPage(currentPage);
                        }
                    }
                }
            }
        });
    }

    private void loadPage(int currentPage) {
        Toast.makeText(MainActivity.this, "Loading Page " + currentPage, Toast.LENGTH_SHORT).show();
        getUsers(client, currentPage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            fetchAndUpdateUsers();
        });
    }

    public void getUsers (Client client, int page){
        client.getUsers(page).enqueue(new Callback<UsersData>() {
            @Override
            public void onResponse(Call<UsersData> call, Response<UsersData> response) {
                //saves the users localy
                if(response != null && response.isSuccessful()){
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(() -> {
                        List<UserData> newUsers = response.body().getData();
                        if(newUsers != null && !newUsers.isEmpty()){
                            userDao.addAllUsers(newUsers);
                            fetchAndUpdateUsers();
                            loading = true;
                        }else {
                            runOnUiThread(() -> Toast.makeText(MainActivity.this,
                                    "No more data", Toast.LENGTH_SHORT).show());
                        }
//                        for (UserData user : response.body().getData()) {
//                            userDao.addUser(user);
//                        }
                    });
                }else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this,
                            "Failed to load data", Toast.LENGTH_SHORT).show());
                }

            }
            @Override
            public void onFailure(Call<UsersData> call, Throwable throwable) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this,
                        "Failed to load data", Toast.LENGTH_SHORT).show());
            }
        });
    }
    private void fetchAndUpdateUsers() {
        users = userDao.getAllUsers();
        runOnUiThread(() ->{
            adapter.setUsers(users);
            adapter.notifyDataSetChanged();
        });
    }

    public void onItemClick(UserData user) {

        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("imageResId", user.getAvatar());
        intent.putExtra("id", user.getId());
        intent.putExtra("first name", user.getFirst_name());
        intent.putExtra("last name",user.getLast_name());
        intent.putExtra("email", user.getEmail());
        startActivity(intent);

        // Handle the click event here
        Toast.makeText(this, "Clicked: " + user.getFirst_name(), Toast.LENGTH_SHORT).show();
    }



}

