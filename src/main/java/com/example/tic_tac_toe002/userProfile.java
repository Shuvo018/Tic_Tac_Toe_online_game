package com.example.tic_tac_toe002;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tic_tac_toe002.model.userModel;
import com.example.tic_tac_toe002.utils.firebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userProfile extends AppCompatActivity {
    ImageView userimage;
    TextView userName, wins, rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userimage = findViewById(R.id.idUserImageView);
        userName = findViewById(R.id.idUserNameTextView);
        wins = findViewById(R.id.idUserWinsTextView);

        firebaseUtil.UserDetails(firebaseUtil.currentUserId()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                userName.setText(userName.getText().toString()+""+task.getResult().child("name").getValue().toString());
            }
        });
        firebaseUtil.wins(firebaseUtil.currentUserId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                wins.setText(wins.getText().toString()+""+task.getResult().getValue().toString()); ;
            }
        });

    }
}