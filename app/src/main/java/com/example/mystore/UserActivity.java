package com.example.mystore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserActivity extends AppCompatActivity {

    private ImageView userImage;
    private TextView userId;
    private TextView userName;
    private TextView userEmail;
    private MaterialButton backBtn;
    private MaterialButton deleteBtn;
    private MaterialButton updateBtn;

    private UserDatabase usersDB;
    private UserDao userDao;

    private UserData user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);

        initialize();
        // Get data from Intent
        getDataFromIntent();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, UpdateUserActivity.class);
                intent.putExtra("imageResId", user.getAvatar());
                intent.putExtra("id", user.getId());
                intent.putExtra("first name", user.getFirst_name());
                intent.putExtra("last name",user.getLast_name());
                intent.putExtra("email", user.getEmail());
                startActivity(intent);
            }
        });
    }

    private void initialize() {
        userImage = findViewById(R.id.image_view);
        userId = findViewById(R.id.id_txt);
        userName = findViewById(R.id.name_txt);
        userEmail = findViewById(R.id.email_txt);

        backBtn = findViewById(R.id.back_btn);
        deleteBtn = findViewById(R.id.delete_btn);
        updateBtn = findViewById(R.id.update_btn);

        usersDB = Room.databaseBuilder(getApplicationContext(),
                UserDatabase.class, "UsersDB").build();
        userDao = usersDB.getUserDao();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        builder.setMessage("Are you sure you want to delete this user?")
                .setTitle("Confirmation");
        // Add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Yes button
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {
                    userDao.deleteUser(user);
                    runOnUiThread(() -> {
                        Toast.makeText(UserActivity.this, "Action confirmed", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked No button
                dialog.dismiss();
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getDataFromIntent(){
        String imageResId = getIntent().getStringExtra("imageResId");
        int id = getIntent().getIntExtra("id", -1);
        String firstName = getIntent().getStringExtra("first name");
        String lastName = getIntent().getStringExtra("last name");
        String name = firstName + " " + lastName;
        String email = getIntent().getStringExtra("email");

        user = new UserData(id, email, firstName, lastName, imageResId);

        // Set data to views
        Glide.with(this)
                .load(imageResId)
                .into(userImage);
        userId.setText(id + "");
        userName.setText(name);
        userEmail.setText(email);
    }
}
