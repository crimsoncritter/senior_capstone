package com.example.cartassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button logButton;
    private Button reqButton;
    private FirebaseUser user;
    private TextView numCarts;
    private boolean loggedIn;
    private DatabaseReference mDatabase;
    private FirebaseFirestore fireStoreDB;
    private Status status;
    private Button acBut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();
        numCarts = (TextView) findViewById(R.id.NumberOfCarts);
        logButton = (Button) findViewById(R.id.button);
        reqButton = (Button) findViewById(R.id.button2);
        acBut = (Button) findViewById(R.id.button7);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fireStoreDB = FirebaseFirestore.getInstance();
        status = Status.getInstance();



        mDatabase.child("Stores").child("1").child("available_carts").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    //Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    numCarts.setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });


        if(user != null){
            logButton.setText("Logout");
            loggedIn = true;
            status = Status.getInstance();
            if(status.getState() == "customer"){
                acBut.setVisibility(View.VISIBLE);
            }
            //logButton.setText(String.valueOf(status.isCarSelected()));
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
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("requests");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("manager");
                    //status = Status.getInstance();
                    if(status.getState() == "attendant") {
                        mDatabase.child("Stores").child("1").child("attendant_on_duty").setValue(false);
                    }
                    status.setCarSelected(false);
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


        acBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddActivity();
            }
        });

    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void openAddActivity(){
        Intent intent = new Intent(this, AddCar.class);
        startActivity(intent);
    }

    public void openChooseActivity(){
        Intent intent = new Intent(this, ChooseCar.class);
        startActivity(intent);
    }

    public void openReqActivity(){
        if(!loggedIn || status.isCarSelected() || status.getState() != "customer" ) {
            //reqButton.setText(String.valueOf(status.getState()));
            Intent intent = new Intent(this, CartRequest.class);
            startActivity(intent);
        }
        else{
            //reqButton.setText(String.valueOf(status.isCarSelected()));
            openChooseActivity();
        }
    }
}