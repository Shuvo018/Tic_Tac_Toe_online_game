package com.example.tic_tac_toe002;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tic_tac_toe002.model.userModel;
import com.example.tic_tac_toe002.utils.firebaseUtil;

import java.util.ArrayList;
import java.util.Random;

public class singlePlayer extends AppCompatActivity {
    TextView player1TV, player2TV;

    Button box1Btn, box2Btn, box3Btn, box4Btn, box5Btn, box6Btn, box7Btn, box8Btn, box9Btn, rematchBtn;

    int player1Count = 0, player2Count = 0;

    ArrayList<Integer> player1 = new ArrayList<>();
    ArrayList<Integer> player2 = new ArrayList<>();
    boolean player1Turn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

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
        rematchBtn = findViewById(R.id.idBtnRematch);
        rematchBtn.setOnClickListener(v ->{ reMatch(); });
        player1TV.setText("human : "+player1Count);
        player2TV.setText("robot : "+player2Count);
        random();
    }

    private void alertDialog(String player) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(player+"\n Do you want to play again");
        builder.setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reMatch();
//                        Toast.makeText(singlePlayer.this, "Yes clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
//                        Toast.makeText(singlePlayer.this, "No clicked", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Tic Tac Toe");
        alertDialog.show();
    }
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
        random();
    }
    void random(){
        Random ran = new Random();
        int r = ran.nextInt(2);
        player1Turn = true;
        if(r == 1){
            player2TV.setBackgroundColor(Color.parseColor("#FFBF00"));//orange
            player1TV.setBackgroundColor(Color.parseColor("#32CD32"));//green
        }else{
            player1TV.setBackgroundColor(Color.parseColor("#FFBF00"));//orange
            player2TV.setBackgroundColor(Color.parseColor("#32CD32"));//green
            new Handler().postDelayed(() -> { if(userModel.singlePlayer){robot(); }else{player1Turn = false;} }, 1000);
        }
    }

    public void buttonClick(View v) {
        MediaPlayer mp = MediaPlayer.create(this,R.raw.button_sound);
        mp.start();
            int cell = -1;
            if(v.getId() == box1Btn.getId()){
                cell = 0;
            } else if (v.getId() == box2Btn.getId()){
                cell = 1;
            } else if (v.getId() == box3Btn.getId()){
                cell = 2;
            } else if (v.getId() == box4Btn.getId()){
                cell = 3;
            } else if (v.getId() == box5Btn.getId()){
                cell = 4;
            } else if (v.getId() == box6Btn.getId()){
                cell = 5;
            } else if (v.getId() == box7Btn.getId()){
                cell = 6;
            } else if (v.getId() == box8Btn.getId()){
                cell = 7;
            } else if (v.getId() == box9Btn.getId()){
                cell = 8;
            } else{
                cell = -1;
            }
//        Log.d("human : ", cell+" ");
        Button button = (Button) v;
        if(player1Turn){
            player1.add(cell);
            button.setText("X");
            button.setEnabled(false);
            button.setTextColor(Color.parseColor("#EC0C0C"));

            if(checkWinner()) return;
            new Handler().postDelayed(() -> {
                if(userModel.singlePlayer) {mp.release();robot();}
                else{mp.release();player1Turn = false;}
                }, 1000);
            player1TV.setBackgroundColor(Color.parseColor("#FFBF00"));//orange
            player2TV.setBackgroundColor(Color.parseColor("#32CD32"));//green

        }else{
            player2.add(cell);
            button.setText("O");
            button.setEnabled(false);
            button.setTextColor(Color.parseColor("#EC0C0C"));

            if(checkWinner()) return;
            new Handler().postDelayed(() -> {mp.release();player1Turn = true;}, 1000);
            player2TV.setBackgroundColor(Color.parseColor("#FFBF00"));//orange
            player1TV.setBackgroundColor(Color.parseColor("#32CD32"));//green
        }
    }

    private void robot() {

        Random random = new Random();
        int cell = random.nextInt(9);
        if(player1.contains(cell) || player2.contains(cell)){
            robot();
        }else{
            MediaPlayer mp = MediaPlayer.create(this, R.raw.button_sound);
            mp.start();

            Button button;
            switch (cell){
                case 0 : button = box1Btn; break;
                case 1 : button = box2Btn; break;
                case 2 : button = box3Btn; break;
                case 3: button = box4Btn; break;
                case 4 : button = box5Btn; break;
                case 5 : button = box6Btn; break;
                case 6 : button = box7Btn; break;
                case 7 : button = box8Btn; break;
                case 8 : button = box9Btn; break;
                default:
                    button = box1Btn;
            }
//            Log.d("robot : ", cell+" ");
//            Toast.makeText(this, cell+"", Toast.LENGTH_SHORT).show();
            player2.add(cell);
            button.setText("O");
            button.setEnabled(false);
            button.setTextColor(Color.parseColor("#EC0C0C"));
            new Handler().postDelayed(() -> {mp.release();}, 1000);
            if(checkWinner()) return;
            player2TV.setBackgroundColor(Color.parseColor("#FFBF00"));//orange
            player1TV.setBackgroundColor(Color.parseColor("#32CD32"));//green
        }

    }

    boolean checkWinner(){
        MediaPlayer mp = MediaPlayer.create(this,R.raw.winning_sound);
        if ((player1.contains(0) && player1.contains(1) && player1.contains(2)) ||
                (player1.contains(0) && player1.contains(3) && player1.contains(6)) ||
                (player1.contains(2) && player1.contains(5) && player1.contains(8)) ||
                (player1.contains(6) && player1.contains(7) && player1.contains(8)) ||
                (player1.contains(3) && player1.contains(4) && player1.contains(5)) ||
                (player1.contains(0) && player1.contains(4) && player1.contains(8)) ||
                (player1.contains(2) && player1.contains(4) && player1.contains(6)) ||
                (player1.contains(1) && player1.contains(4) && player1.contains(7))){
            mp.start();
            player1Count += 5;
            disableAllBtn();
            alertDialog("human win");
            player1TV.setText("human : "+player1Count);
            new Handler().postDelayed(() -> { mp.release();}, 1000);
//            Toast.makeText(this, "player 1 win", Toast.LENGTH_SHORT).show();
            return true;

        }else if((player2.contains(0) && player2.contains(1) && player2.contains(2)) ||
                (player2.contains(0) && player2.contains(3) && player2.contains(6)) ||
                (player2.contains(2) && player2.contains(5) && player2.contains(8)) ||
                (player2.contains(6) && player2.contains(7) && player2.contains(8)) ||
                (player2.contains(3) && player2.contains(4) && player2.contains(5)) ||
                (player2.contains(0) && player2.contains(4) && player2.contains(8)) ||
                (player2.contains(2) && player2.contains(4) && player2.contains(6)) ||
                (player2.contains(1) && player2.contains(4) && player2.contains(7))){
            mp.start();
            player2Count += 5;
            disableAllBtn();
            alertDialog("robot win");
            player2TV.setText("robot : "+player2Count);
            new Handler().postDelayed(() -> { mp.release();}, 1000);
//            Toast.makeText(this, "player 2 win", Toast.LENGTH_SHORT).show();
            return true;

        } else if ( (player1.size()+player2.size()) - 9 == 0 ) {
            mp.start();
            disableAllBtn();
            alertDialog("draw");
            new Handler().postDelayed(() -> { mp.release();}, 1000);
//            Toast.makeText(this, "draw", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
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