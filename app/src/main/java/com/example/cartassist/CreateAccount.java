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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {

    private EditText emailED, passwordED, passwordReED, makeED, modelED, carColorED;
    private Button registerBut;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();

        emailED = (EditText)findViewById(R.id.editTextEnterEmail);
        passwordED = (EditText)findViewById(R.id.editTextEnterPass);
        passwordReED = (EditText)findViewById(R.id.editTextReEnterPass);
        makeED = (EditText)findViewById(R.id.editTextCarMake);
        modelED = (EditText)findViewById(R.id.editTextCarModel);
        carColorED = (EditText)findViewById(R.id.editTextCarColor);

        registerBut = (Button)findViewById(R.id.button5);
        registerBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser(){
        String email = emailED.getText().toString().trim();
        String password = passwordED.getText().toString().trim();
        String passwordRe = passwordReED.getText().toString().trim();
        String make = makeED.getText().toString().trim();
        String model = modelED.getText().toString().trim();
        String carColor = carColorED.getText().toString().trim();

        if(email.isEmpty()){
            emailED.setError("email is required");
            emailED.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailED.setError("Please provide valid email");
            emailED.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordED.setError("password is required");
            passwordED.requestFocus();
            return;
        }

        if(passwordRe.isEmpty()){
            passwordReED.setError("password is required");
            passwordReED.requestFocus();
            return;
        }

        if(password.length()<6){
            passwordED.setError("password must be a minimum of 6 characters");
            passwordED.requestFocus();
            return;
        }

        if(!password.equals(passwordRe)){
            passwordED.setError("passwords do not match");
            passwordED.requestFocus();
            passwordReED.requestFocus();
            return;
        }

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

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(email,make,model,carColor);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(CreateAccount.this,"User has been registered successfully!", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(CreateAccount.this,"Failed to register, try again",Toast.LENGTH_LONG);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(CreateAccount.this,"Failed to register, try again",Toast.LENGTH_LONG);
                        }
                    }
                });
    }
}