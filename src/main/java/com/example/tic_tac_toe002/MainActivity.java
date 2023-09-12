package com.example.tic_tac_toe002;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tic_tac_toe002.model.userModel;
import com.example.tic_tac_toe002.utils.firebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class MainActivity extends AppCompatActivity {
    TextView userName ;

    ImageView userProfileMV;
    Button singlePlayer;
    Button multiPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.idMainPageUserNameTV);
        userProfileMV = findViewById(R.id.idUserProfileImageView);
        singlePlayer = findViewById(R.id.idBtnSinglePlayer);
        multiPlayer = findViewById(R.id.idBtnMultiPlayer);

        firebaseUtil.UserDetails(firebaseUtil.currentUserId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    userName.setText(task.getResult().child("name").getValue().toString());
                }
            }
        });
        userProfileMV.setOnClickListener(v -> {
            startActivity(new Intent(this,userProfile.class));
        });
        singlePlayer.setOnClickListener(v ->{
            userModel.singlePlayer = true;
            startActivity(new Intent(this, singlePlayer.class));
        });

        multiPlayer.setOnClickListener(v->{
            startActivity(new Intent(this, multiPlayerGameSelectionActivity.class));
        });
    }
}