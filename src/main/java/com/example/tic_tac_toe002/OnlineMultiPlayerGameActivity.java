package com.example.tic_tac_toe002;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tic_tac_toe002.model.userModel;
import com.example.tic_tac_toe002.utils.firebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class OnlineMultiPlayerGameActivity extends AppCompatActivity {
    TextView player1TV, player2TV;

    Button box1Btn, box2Btn, box3Btn, box4Btn, box5Btn, box6Btn, box7Btn, box8Btn, box9Btn;

    int player1Count = 10, player2Count = 10;

    ArrayList<Integer> player1 = new ArrayList<>();
    ArrayList<Integer> player2 = new ArrayList<>();
    boolean turnPlayer1 = userModel.firstTurn;
    String otherPlayerUID ;
    String P1N, P2N;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_multi_player_game);

        player1TV = findViewById(R.id.idTVPlayer1);
        player2TV = findViewById(R.id.idTVPlayer2);
        box1Btn = findViewById(R.id.idBtnBox1);
        box2Btn = findViewById(R.id.idBtnBox2);
        box3Btn = findViewById(R.id.idBtnBox3);
        box4Btn = findViewById(R.id.idBtnBox4);
        box5Btn = findViewById(R.id.idBtnBox5);
        box6Btn = findViewById(R.id.idBtnBox6);
        box7Btn = findViewById(R.id.idBtnBox7);
        box8Btn = findViewById(R.id.idBtnBox8);
        box9Btn = findViewById(R.id.idBtnBox9);

// insert playsers name
        firebaseUtil.playersList().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getValue().toString().equals(firebaseUtil.currentUserId())){
                        firebaseUtil.UserDetails(firebaseUtil.currentUserId()).addValueEventListener(new ValueEventListener() { // this line is for get user name
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                userModel model = new userModel();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    model = snapshot.getValue(userModel.class);
                                }
                                player1TV.setText(model.getName());
                                P1N = model.getName();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else{
                        otherPlayerUID = dataSnapshot.getValue().toString();
                        firebaseUtil.UserDetails(dataSnapshot.getValue().toString()).addValueEventListener(new ValueEventListener() { // this line is for get user name
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                userModel model = new userModel();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    model = snapshot.getValue(userModel.class);
                                }
                                player2TV.setText(model.getName());
                                P2N = model.getName();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("data").child(firebaseUtil.getGameRoom()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                 String dataSnapshot = snapshot.getValue().toString();
                if(turnPlayer1){
                    turnPlayer1 = false;
                }else{
                    turnPlayer1 = true;
                    onlineMove(dataSnapshot);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (turnPlayer1) {
            player1TV.setBackgroundColor(Color.parseColor("#32CD32"));//green
            player2TV.setBackgroundColor(Color.parseColor("#FFBF00"));//ore
        }else{
            player2TV.setBackgroundColor(Color.parseColor("#32CD32"));//green
            player1TV.setBackgroundColor(Color.parseColor("#FFBF00"));//ore
        }
    }

//    rematch doesn't properly
    private void reMatch() {

        for (int i=0; i<9; i++){
            switch (i){
                case 0 : box1Btn.setText(""); break;
                case 1 : box2Btn.setText(""); break;
                case 2 : box3Btn.setText(""); break;
                case 3: box4Btn.setText(""); break;
                case 4 : box5Btn.setText(""); break;
                case 5 : box6Btn.setText(""); break;
                case 6 : box7Btn.setText(""); break;
                case 7 : box8Btn.setText(""); break;
                case 8 : box9Btn.setText(""); break;
                default:
                    box1Btn.setText("");
            }
        }
        enableAllBtn();
        Random random = new Random();
        int r = random.nextInt(2);
        if(r==1){
            turnPlayer1 = true;
            player1TV.setBackgroundColor(Color.parseColor("#32CD32"));//green
            player2TV.setBackgroundColor(Color.parseColor("#FFBF00"));//ore
        }else{
            turnPlayer1 = false;
            player2TV.setBackgroundColor(Color.parseColor("#32CD32"));//green
            player1TV.setBackgroundColor(Color.parseColor("#FFBF00"));//ore
        }
        FirebaseDatabase.getInstance().getReference("data").child(firebaseUtil.getGameRoom()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    FirebaseDatabase.getInstance().getReference("data").child(firebaseUtil.getGameRoom()).child(snapshot.getKey()).removeValue();
                }
            }
        });

    }

    private void onlineMove(String value) {
        MediaPlayer mp = MediaPlayer.create(this,R.raw.button_sound);
        mp.start();
        int moveCell=Integer.parseInt(value);
        Button selectedButton;
        switch (moveCell){
            case 1: selectedButton = box1Btn;
                    break;
            case 2: selectedButton = box2Btn;
                break;
            case 3: selectedButton = box3Btn;
                break;
            case 4: selectedButton = box4Btn;
                break;
            case 5: selectedButton = box5Btn;
                break;
            case 6: selectedButton = box6Btn;
                break;
            case 7: selectedButton = box7Btn;
                break;
            case 8: selectedButton = box8Btn;
                break;
            case 9: selectedButton = box9Btn;
                break;
            default:
                selectedButton = box1Btn;
        }
//        Toast.makeText(this, moveCell+"", Toast.LENGTH_SHORT).show();
        player2.add(moveCell);
        selectedButton.setText("O");
        selectedButton.setEnabled(false);
        selectedButton.setTextColor(Color.parseColor("#EC0C0C"));
        checkWinner();
        new Handler().postDelayed(() -> mp.release(),1000);
//        turnTv.setText(player1TV.getText());
        player2TV.setBackgroundColor(Color.parseColor("#FFBF00"));
        player1TV.setBackgroundColor(Color.parseColor("#32CD32"));
    }
    public void buttonClick(View v) {
        MediaPlayer mp = MediaPlayer.create(this,R.raw.button_sound);
            if(turnPlayer1){
                mp.start();
                int cell = 0;
                if(v.getId() == box1Btn.getId()){
                    cell = 1;
                } else if (v.getId() == box2Btn.getId()){
                    cell = 2;
                } else if (v.getId() == box3Btn.getId()){
                    cell = 3;
                } else if (v.getId() == box4Btn.getId()){
                    cell = 4;
                } else if (v.getId() == box5Btn.getId()){
                    cell = 5;
                } else if (v.getId() == box6Btn.getId()){
                    cell = 6;
                } else if (v.getId() == box7Btn.getId()){
                    cell = 7;
                } else if (v.getId() == box8Btn.getId()){
                    cell = 8;
                } else if (v.getId() == box9Btn.getId()){
                    cell = 9;
                } else{
                    cell = 0;
                }
//                Toast.makeText(this, cell+"", Toast.LENGTH_SHORT).show();
                player1.add(cell);
                Button button = (Button) v;
                button.setText("X");
                button.setEnabled(false);
                button.setTextColor(Color.parseColor("#EC0C0C"));
                updateFirebase(cell);
                checkWinner();
//                turnTv.setText(player2TV.getText());
                player1TV.setBackgroundColor(Color.parseColor("#FFBF00"));//orange
                player2TV.setBackgroundColor(Color.parseColor("#32CD32"));//green
                new Handler().postDelayed(() -> mp.release(),1000);

            }else {
                Toast.makeText(this, "your oppenent turn", Toast.LENGTH_SHORT).show();

            }
    }

    private void updateFirebase(int cell) {
        FirebaseDatabase.getInstance().getReference("data").child(firebaseUtil.getGameRoom()).push().setValue(cell).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
    boolean checkWinner(){
        MediaPlayer mp = MediaPlayer.create(this,R.raw.winning_sound);
        if ((player1.contains(1) && player1.contains(2) && player1.contains(3)) ||
                (player1.contains(1) && player1.contains(4) && player1.contains(7)) ||
                (player1.contains(3) && player1.contains(6) && player1.contains(9)) ||
                (player1.contains(7) && player1.contains(8) && player1.contains(9)) ||
                (player1.contains(4) && player1.contains(5) && player1.contains(6)) ||
                (player1.contains(1) && player1.contains(5) && player1.contains(9)) ||
                (player1.contains(3) && player1.contains(5) && player1.contains(7)) ||
                (player1.contains(2) && player1.contains(5) && player1.contains(8))){

                mp.start();
                player1Count += 5;
                disableAllBtn();
                winerUpdate(player1Count, firebaseUtil.currentUserId());
                alertDialog(P1N+" win");
            new Handler().postDelayed(() -> { mp.release();}, 1000);
//            Toast.makeText(this, "player 1 win", Toast.LENGTH_SHORT).show();
             return true;

        }else if((player2.contains(1) && player2.contains(2) && player2.contains(3)) ||
                (player2.contains(1) && player2.contains(4) && player2.contains(7)) ||
                (player2.contains(3) && player2.contains(6) && player2.contains(9)) ||
                (player2.contains(7) && player2.contains(8) && player2.contains(9)) ||
                (player2.contains(4) && player2.contains(5) && player2.contains(6)) ||
                (player2.contains(1) && player2.contains(5) && player2.contains(9)) ||
                (player2.contains(3) && player2.contains(5) && player2.contains(7)) ||
                (player2.contains(2) && player2.contains(5) && player2.contains(8))){
            mp.start();
            player2Count += 5;
            disableAllBtn();
            alertDialog(P2N+" win");
            winerUpdate(player2Count, otherPlayerUID);
            new Handler().postDelayed(() -> { mp.release();}, 1000);
//            Toast.makeText(this, "player 2 win", Toast.LENGTH_SHORT).show();
            return true;

        } else if ( (player2.size()+player1.size()) - 9 == 0 ) {
            mp.start();
            disableAllBtn();
            alertDialog("draw");
//            Toast.makeText(this, "draw", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> { mp.release();}, 1000);
            return true;
        }
        return false;
    }

    private void alertDialog(String player) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(player+"\n Do you want to play again");
        builder.setCancelable(false)
                .setPositiveButton("yes", (dialog, which) -> {
                    onBackPressed();
                    finish();
//                    reMatch();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    onBackPressed();
                    finish();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Tic Tac Toe");
        alertDialog.show();
    }

    private void winerUpdate(int count, String userId) {
        final int[] sum = {0};
        firebaseUtil.wins(userId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                sum[0] = Integer.parseInt(task.getResult().getValue().toString());
            }
        });
        firebaseUtil.wins(firebaseUtil.currentUserId()).setValue(count+sum[0]).addOnCompleteListener(t -> {
        });
    }

    void disableAllBtn(){
        player1.clear();
        player2.clear();
        box1Btn.setEnabled(false);
        box2Btn.setEnabled(false);
        box3Btn.setEnabled(false);
        box4Btn.setEnabled(false);
        box5Btn.setEnabled(false);
        box6Btn.setEnabled(false);
        box7Btn.setEnabled(false);
        box8Btn.setEnabled(false);
        box9Btn.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        FirebaseDatabase.getInstance().getReference("data").child(firebaseUtil.getGameRoom()).removeValue();
        FirebaseDatabase.getInstance().getReference("gameRooms").child(firebaseUtil.currentUserId()).removeValue();
        firebaseUtil.playersList().removeValue();
        super.onBackPressed();
    }

    void enableAllBtn(){
        player1.clear();
        player2.clear();
        box1Btn.setEnabled(true);
        box2Btn.setEnabled(true);
        box3Btn.setEnabled(true);
        box4Btn.setEnabled(true);
        box5Btn.setEnabled(true);
        box6Btn.setEnabled(true);
        box7Btn.setEnabled(true);
        box8Btn.setEnabled(true);
        box9Btn.setEnabled(true);
    }

}