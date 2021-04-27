package com.example.cartassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Iterables;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseCar extends AppCompatActivity {
    private Button button;
    private Spinner spinner;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String userID;
    private String testString;
    private EditText parkingED;
    private String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_car);

        button = (Button) findViewById(R.id.button11);
        spinner = findViewById(R.id.spinner2);
        testString = "";


        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("Cars");



        ArrayList<String> arrayList = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot car : snapshot.getChildren()) {
                    testString += car.child("make").getValue().toString() + " ";
                    testString += car.child("model").getValue().toString() + " ";
                    testString += car.child("carColor").getValue().toString();
                    arrayList.add(testString);
                    testString = "";
                }

               ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChooseCar.this, android.R.layout.simple_spinner_item, arrayList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selection = parent.getItemAtPosition(position).toString();
                        Toast.makeText(parent.getContext(), "Selected: " + selection,Toast.LENGTH_LONG).show();
                        choice = selection;
                    }
                    @Override
                    public void onNothingSelected(AdapterView <?> parent) {
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReqActivity();
            }
        });
    }

    private void openReqActivity(){
        Intent intent = new Intent(this, CartRequest.class);
        intent.putExtra("carChoice", choice);
        startActivity(intent);
    }
}