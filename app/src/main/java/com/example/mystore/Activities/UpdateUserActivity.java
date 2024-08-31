package com.example.mystore.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.mystore.R;
import com.example.mystore.Models.UserData;
import com.example.mystore.DataBase.UserDatabase;
import com.example.mystore.Utils.UserValidationUtil;
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
                        imageUri != null ? imageUri.toString() : getIntent().getStringExtra("imageResId"));

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null){
            imageUri = data.getData();

            //dispaly the selected image in the imageView
            userImage.setImageURI(imageUri);
        }
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
            if(userDB.getUserDao().getUser(updatedUser.getId()) != null && //no exists in DB
                    getIntent().getIntExtra("id", -1) != updatedUser.getId()){ //and this is the same user
                runOnUiThread(() -> {
                    Toast.makeText(this,
                            "User " + updatedUser.getId() + " is already exists !",
                            Toast.LENGTH_SHORT).show();
                });
            }else if (userDB.getUserDao().getUser(updatedUser.getEmail()) != null &&
                    !(getIntent().getStringExtra("email").equals(updatedUser.getEmail()))) {
                runOnUiThread(() -> {
                    Toast.makeText(UpdateUserActivity.this,
                            "Email is already exists !",
                            Toast.LENGTH_SHORT).show();
                });
            }else {
                userDB.getUserDao().updateUser(updatedUser);
                runOnUiThread(() -> {
                    Toast.makeText(this, "User " + updatedUser.getId() + " updated successfully!",
                            Toast.LENGTH_SHORT).show();

                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(UpdateUserActivity.this, UserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }, 200);  // 200ms delay

                        /*
                    Intent intent = new Intent(UpdateUserActivity.this, UserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                         */
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

      //  user = new UserData(id, email, firstName, lastName,
        //        Uri.parse(userImage.getTag().toString()) + "");

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
