package com.example.tic_tac_toe002;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tic_tac_toe002.model.userModel;
import com.example.tic_tac_toe002.utils.firebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class OnlineCodeGeneratorActivity extends AppCompatActivity {
    EditText codeEditText;
    Button generateCode, createCodeBtn, joinCodeBtn;
    String GNcode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_code_generator);

        generateCode = findViewById(R.id.idBtngenerateCode);
        codeEditText = findViewById(R.id.idEditCode);
        createCodeBtn = findViewById(R.id.idBtnCreate);
        joinCodeBtn = findViewById(R.id.idBtnJoin);

        generateCode.setOnClickListener(v -> {
            Random random = new Random();
            for (int i=0; i<5; i++){
                GNcode += Integer.toString(random.nextInt(9));
            }
            codeEditText.setText(GNcode);
            generateCode.setVisibility(View.GONE);
            createCodeBtn.setVisibility(View.VISIBLE);
        });
        createCodeBtn.setOnClickListener(v -> {
            userModel.firstTurn = true;
            firebaseUtil.setGameRoom(GNcode);
            if(!GNcode.isEmpty()){
                FirebaseDatabase.getInstance().getReference("gameRooms").child(firebaseUtil.currentUserId()).setValue(GNcode).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        firebaseUtil.playersList().push().setValue(firebaseUtil.currentUserId()).addOnCompleteListener(task1 -> { //this line for get active user id
                        });
                        startActivity(new Intent(OnlineCodeGeneratorActivity.this, OnlineMultiPlayerGameActivity.class));
                    }
                });
            }else{
                Toast.makeText(this, "Enter some numbers", Toast.LENGTH_SHORT).show();
            }
        });

        joinCodeBtn.setOnClickListener(v -> {
            userModel.firstTurn = false;
            String code = codeEditText.getText().toString();
            firebaseUtil.setGameRoom(code);
            if(!code.isEmpty()){
                FirebaseDatabase.getInstance().getReference("gameRooms").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        boolean isValid = isValid(dataSnapshot, code);
                        if(isValid){
                            //this line insert user id
                            firebaseUtil.playersList().push().setValue(firebaseUtil.currentUserId()).addOnCompleteListener(task1 -> {
                            });
                            startActivity(new Intent(OnlineCodeGeneratorActivity.this, OnlineMultiPlayerGameActivity.class));
                        }else{
                            Toast.makeText(OnlineCodeGeneratorActivity.this, "code not matched", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private boolean isValid(DataSnapshot snapshot, String code) {
//        Toast.makeText(OnlineCodeGeneratorActivity.this, snapshot.getChildren().toString(), Toast.LENGTH_SHORT).show();
        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//            Toast.makeText(OnlineCodeGeneratorActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
            if(code.equals(dataSnapshot.getValue().toString()))
                return true;
        }
        return false;
    }
}

