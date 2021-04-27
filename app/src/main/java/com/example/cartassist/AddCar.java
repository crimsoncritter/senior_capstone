package com.example.cartassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AddCar extends AppCompatActivity {

    private EditText makeED, modelED, carColorED;
    private ImageButton imBut;
    private Button button;
    private Status status;
    //String make, model, carColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        makeED = (EditText)findViewById(R.id.editTextMakeCR2);
        modelED = (EditText)findViewById(R.id.editTextModelCR2);
        carColorED = (EditText)findViewById(R.id.editTextColorCR2);
        imBut = (ImageButton)findViewById(R.id.imageButton5);
        button = (Button)findViewById(R.id.button8);
        status = Status.getInstance();




        imBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCarToUser();
            }
        });
    }

    private void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void addCarToUser(){
        String make = makeED.getText().toString().trim();
        String model = modelED.getText().toString().trim();
        String carColor = carColorED.getText().toString().trim();

        if(make.isEmpty()){
            makeED.setError("make is required");
            makeED.requestFocus();
            return;
        }

        if(model.isEmpty()){
            modelED.setError("model is required");
            modelED.requestFocus();
            return;
        }

        if(carColor.isEmpty()){
            carColorED.setError("color is required");
            carColorED.requestFocus();
            return;
        }

        Car userCar = new Car(make,model, carColor);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Cars").push().getKey();

        status.setCarSelected(false);

        database.getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Cars").child(key).setValue(userCar).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddCar.this,"Car added successfully!", Toast.LENGTH_LONG).show();
                    makeED.getText().clear();
                    modelED.getText().clear();
                    carColorED.getText().clear();
                    database.getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("multipleCars").setValue(true);

                }
                else{
                    Toast.makeText(AddCar.this,"Failed to register, try again",Toast.LENGTH_LONG);
                }
            }
        });
    }
}