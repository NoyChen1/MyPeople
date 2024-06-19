package com.example.mystore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

public class UserActivity extends AppCompatActivity {

    private ImageView userImage;
    private TextView userId;
    private TextView userName;
    private TextView userEmail;

    private MaterialButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);

        userImage = findViewById(R.id.image_view);
        userId = findViewById(R.id.id_txt);
        userName = findViewById(R.id.name_txt);
        userEmail = findViewById(R.id.email_txt);
        backBtn = findViewById(R.id.back_btn);

        // Get data from Intent
        String imageResId = getIntent().getStringExtra("imageResId");
        int id = getIntent().getIntExtra("id", -1);
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");

        // Set data to views
        Glide.with(this)
                .load(imageResId)
                .into(userImage);
        userId.setText(id+ "");
        userName.setText(name);
        userEmail.setText(email);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();            }
        });
    }

}
