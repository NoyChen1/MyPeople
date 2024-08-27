package com.example.mystore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateUserActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private MaterialButton uploadImageBtn;
    private MaterialButton updateUserBtn;
    private EditText idTxt;
    private EditText firstNameTxt;
    private EditText lastNameTxt;
    private EditText emailTxt;
    private ImageView userImage;
    private UserData updatedUser;
    private UserDatabase userDB;
    private Uri imageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user);

        initialize();
        getDataFromIntent();
        updateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedUser = new UserData(Integer.parseInt(idTxt.getText() + ""),
                        emailTxt.getText() + "", firstNameTxt.getText() + "",
                        lastNameTxt.getText() + "",
                        imageUri != null ? imageUri.toString() : userImage.toString());

                if(UserValidationUtil.isUserDataValid(updatedUser, UpdateUserActivity.this)){
                    updateUserInDB();
                }
            }
        });

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void updateUserInDB(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() ->{
            if(userDB.getUserDao().getUser(updatedUser.getId()) != null &&
                    getIntent().getIntExtra("id", -1) != updatedUser.getId()){
                runOnUiThread(() -> {
                    Toast.makeText(UpdateUserActivity.this,
                            "User " + updatedUser.getId() + " is already exists !",
                            Toast.LENGTH_SHORT).show();
                });
            }else {
                userDB.getUserDao().updateUser(updatedUser);
                runOnUiThread(() -> {
                    Toast.makeText(UpdateUserActivity.this, "User updated successfully!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }
    private void getDataFromIntent() {
        String imageResId = getIntent().getStringExtra("imageResId");
        int id = getIntent().getIntExtra("id", -1);
        String firstName = getIntent().getStringExtra("first name");
        String lastName = getIntent().getStringExtra("last name");
        String email = getIntent().getStringExtra("email");

        Glide.with(this)
                .load(imageResId)
                .into(userImage);
        idTxt.setText(id + "");
        firstNameTxt.setText(firstName);
        lastNameTxt.setText(lastName);
        emailTxt.setText(email);
    }

    private void initialize() {

        uploadImageBtn = findViewById(R.id.upload_image_btn);
        updateUserBtn = findViewById(R.id.update_user_btn);
        idTxt = findViewById(R.id.idu_txt);
        firstNameTxt = findViewById(R.id.first_nameu_txt);
        lastNameTxt = findViewById(R.id.last_nameu_txt);
        emailTxt = findViewById(R.id.emailu_txt);
        userImage = findViewById(R.id.image_viewu);

        userDB = Room.databaseBuilder(getApplicationContext(),
                UserDatabase.class, "UsersDB").build();

    }
}
