package com.example.cartassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartRequest extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private EditText makeED, modelED, colorED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_request);

        makeED = (EditText)findViewById(R.id.editTextMakeCR);
        modelED = (EditText)findViewById(R.id.editTextModelCR);
        colorED = (EditText)findViewById(R.id.editTextColorCR);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();
            reference.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        makeED.setText(String.valueOf(task.getResult().child("make").getValue()));
                        modelED.setText(String.valueOf(task.getResult().child("model").getValue()));
                        colorED.setText(String.valueOf(task.getResult().child("carColor").getValue()));
                    }
                }
            });
        }
    }
}