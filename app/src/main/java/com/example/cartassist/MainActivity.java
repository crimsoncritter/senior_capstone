package com.example.cartassist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button logButton;
    private Button reqButton;
    private FirebaseUser user;
    private boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();
        logButton = (Button) findViewById(R.id.button);
        reqButton = (Button) findViewById(R.id.button2);

        if(user != null){
            logButton.setText("Logout");
            loggedIn = true;
        }
        else{
            logButton.setText("Login");
            loggedIn = false;
        }
        //logButton.setVisibility(View.GONE);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loggedIn) {
                    openLoginActivity();
                }
                else{
                    FirebaseAuth.getInstance().signOut();
                    logButton.setText("Login");
                    loggedIn = false;
                }
            }
        });
        reqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReqActivity();
            }
        });

    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void openReqActivity(){
        Intent intent = new Intent(this, CartRequest.class);
        startActivity(intent);
    }
}