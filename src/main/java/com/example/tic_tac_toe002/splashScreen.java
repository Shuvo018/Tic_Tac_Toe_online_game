package com.example.tic_tac_toe002;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getUid() != null){
                    startActivity(new Intent(splashScreen.this, MainActivity.class));
                }else{
                    startActivity(new Intent(splashScreen.this, login.class));
                }
                finish();
            }
        }, 3000);
    }
}