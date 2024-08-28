package com.example.mystore;

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

import com.google.android.material.button.MaterialButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddUserActivity extends AppCompatActivity {

    private static final String NO_PROFILE_IMAGE = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg?sz=50";
    private static final int PICK_IMAGE_REQUEST = 1;
    private MaterialButton uploadImageBtn;
    private MaterialButton addUserBtn;
    private EditText idTxt;
    private EditText firstNameTxt;
    private EditText lastNameTxt;
    private EditText emailTxt;
    private ImageView avatarImage;
    private UserData newUser;
    private Uri imageUri;


    private UserDatabase userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        userDB = Room.databaseBuilder(getApplicationContext(),
                UserDatabase.class, "UsersDB").build();


        uploadImageBtn = findViewById(R.id.upload_image_btn);
        addUserBtn = findViewById(R.id.add_new_user_btn);
        idTxt = findViewById(R.id.id_txt);
        firstNameTxt = findViewById(R.id.first_name_txt);
        lastNameTxt = findViewById(R.id.last_name_txt);
        emailTxt = findViewById(R.id.email_txt);
        avatarImage = findViewById(R.id.image_viewa);

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUser = new UserData(Integer.parseInt(idTxt.getText() + ""),
                       emailTxt.getText() + "", firstNameTxt.getText() + "",
                        lastNameTxt.getText() + "",
                        imageUri != null ? imageUri.toString() : NO_PROFILE_IMAGE);

                if(UserValidationUtil.isUserDataValid(newUser, AddUserActivity.this)){
                    addUserToDB();
                }
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
            avatarImage.setImageURI(imageUri);
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void addUserToDB() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() ->{
            if(userDB.getUserDao().getUser(newUser.getId()) != null){
                runOnUiThread(() -> {
                    Toast.makeText(AddUserActivity.this,
                            "User " + newUser.getId() + " is already exists !",
                            Toast.LENGTH_SHORT).show();
                });
            }else{
                userDB.getUserDao().addUser(newUser);
                runOnUiThread(() -> {
                    Toast.makeText(AddUserActivity.this, "User " + newUser.getId() + " added successfully!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }
}
