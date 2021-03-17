package com.example.cartassist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button logButton;
    private Button reqButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logButton = (Button) findViewById(R.id.button);
        reqButton = (Button) findViewById(R.id.button2);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
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