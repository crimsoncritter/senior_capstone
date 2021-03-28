package com.example.cartassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPass extends AppCompatActivity {
    private EditText emailED;
    private Button resetBut;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        emailED = (EditText)findViewById(R.id.editTextEmailReset);
        resetBut = (Button)findViewById(R.id.button10);
        auth = FirebaseAuth.getInstance();

        resetBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        String email = emailED.getText().toString().trim();
        if(email.isEmpty()){
            emailED.setError("Email is required");
            emailED.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailED.setError("Please enter a valid email");
            emailED.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPass.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ResetPass.this, "Try again. Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}