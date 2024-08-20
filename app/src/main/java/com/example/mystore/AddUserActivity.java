package com.example.mystore;

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

    private MaterialButton uploadImageBtn;
    private MaterialButton addUserBtn;
    private EditText idTxt;
    private EditText firstNameTxt;
    private EditText lastNameTxt;
    private EditText emailTxt;
    private UserData newUser;

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


        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUser = new UserData(Integer.parseInt(idTxt.getText() + ""),
                       emailTxt.getText() + "", firstNameTxt.getText() + "",
                        lastNameTxt.getText() + "",
                        "https://reqres.in/img/faces/2-image.jpg" );

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() ->{
                    userDB.getUserDao().addUser(newUser);
                    runOnUiThread(() -> {
                        Toast.makeText(AddUserActivity.this, "User added successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                });
            }

        });
    }
}
