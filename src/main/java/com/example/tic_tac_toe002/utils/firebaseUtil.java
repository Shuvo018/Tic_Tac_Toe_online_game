package com.example.tic_tac_toe002.utils;

import androidx.annotation.NonNull;

import com.example.tic_tac_toe002.model.userModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class firebaseUtil {
    public static String gameRoom;

    public static String getGameRoom() {
        return gameRoom;
    }

    public static void setGameRoom(String gameR) {
        gameRoom = gameR;
    }
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public static DatabaseReference playersList(){
        return FirebaseDatabase.getInstance().getReference("playersList").child(getGameRoom());
    }
    public static DatabaseReference UserDetails(String uid){
        return FirebaseDatabase.getInstance().getReference("users").child(uid);
    }
    public static DatabaseReference wins(String uid){
        return FirebaseDatabase.getInstance().getReference("wins").child(uid);
    }

}
