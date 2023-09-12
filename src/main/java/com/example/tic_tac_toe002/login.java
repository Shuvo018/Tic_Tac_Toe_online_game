package com.example.tic_tac_toe002;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tic_tac_toe002.model.userModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity {
    EditText nameET, emailET, passwordET;

    Button signIn;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameET = findViewById(R.id.idNameEditText);
        emailET = findViewById(R.id.idEmailEditText);
        passwordET = findViewById(R.id.idPasswordEditText);
        signIn = findViewById(R.id.idBtnSignIn);

        signIn.setOnClickListener(v -> {
            String name = nameET.getText().toString();
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();
            if(name.isEmpty()){
                nameET.setError("Empty");
            }
            if(email.isEmpty()){
                emailET.setError("Empty");
            }
            if(password.isEmpty()){
                passwordET.setError("Empty");
            }
//            Toast.makeText(this," sign in clicked", Toast.LENGTH_SHORT).show()

            saveFireBase(name, email, password);
        });

    }
    private void saveFireBase(String name, String email, String password) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                                    Toast.makeText(login.this, "successed", Toast.LENGTH_SHORT).show();
                            String uid = FirebaseAuth.getInstance().getUid().toString();
                            userModel model = new userModel(name, password);
                            FirebaseDatabase.getInstance().getReference("users").child(uid).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(login.this, MainActivity.class));
                                        finish();
                                    }
//                                    Toast.makeText(login.this, "successed", Toast.LENGTH_SHORT).show();
//                                    return null;
                                }
                            });
//                            startActivity(Intent(this, MainActivity2::class.java))
                        }
                    }
                });

    }
}