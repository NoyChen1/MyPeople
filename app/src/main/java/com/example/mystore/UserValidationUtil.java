package com.example.mystore;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class UserValidationUtil extends AppCompatActivity {

    public static boolean isUserDataValid(UserData user, AppCompatActivity activity){

        if(user.getId() == -1){
            //activity.showToast("User ID is not set.");
            Toast.makeText(activity, "User ID is not set.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isEmailValid(user.getEmail())){
//            activity.showToast("Invalid email address !");
            Toast.makeText(activity, "Invalid email address !", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(user.getFirst_name()) || TextUtils.isEmpty(user.getLast_name())){
//            activity.showToast("first and/or last name cannot be empty !");
            Toast.makeText(activity, "first and/or last name cannot be empty !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private static boolean isEmailValid(String email) {
        //email pattern
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailPattern);

    }
}