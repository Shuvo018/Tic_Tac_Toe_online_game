package com.example.tic_tac_toe002;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.tic_tac_toe002.model.userModel;

public class multiPlayerGameSelectionActivity extends AppCompatActivity {
    Button onlineBtn, offlineBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player_game_selection);

        onlineBtn = findViewById(R.id.idBtnOnline);
        offlineBtn = findViewById(R.id.idBtnOffline);
        onlineBtn.setOnClickListener(v -> {
//            Toast.makeText(this, " online multiplayer clicked", Toast.LENGTH_SHORT).show()
            startActivity(new Intent(this, OnlineCodeGeneratorActivity.class));
        });
        offlineBtn.setOnClickListener(v -> {
//            Toast.makeText(this, "offline comming soon...", Toast.LENGTH_SHORT).show();
            userModel.singlePlayer = false;
            startActivity(new Intent(this, singlePlayer.class));
        });
    }
}