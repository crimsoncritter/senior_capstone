package com.example.cartassist;

import com.example.cartassist.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {
    String userType;
    Button loginBut;
    Button regBut;
    Spinner logSpinner;
    private EditText emailED, passwordED;
    private TextView forgotPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userType = "customer";
        /*when users login they will select whether they are a customer, attendant or manager and
        that value will be save in the userType variable as a String*/
        loginBut = (Button)findViewById(R.id.button3);
        regBut = (Button)findViewById(R.id.button4);
        logSpinner = (Spinner) findViewById(R.id.spinner);

        emailED = (EditText)findViewById(R.id.editTextEmailAddressL);
        passwordED = (EditText)findViewById(R.id.editTextPasswordL);
        forgotPassword = (TextView)findViewById(R.id.fp);

        mAuth = FirebaseAuth.getInstance();

        ArrayAdapter<String> logAdapter = new ArrayAdapter<String>(Login.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.login_array));
        logAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        logSpinner.setAdapter(logAdapter);
        logSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        userType = "customer";
                        break;
                    case 1:
                        userType = "attendant";
                        break;
                    case 2:
                        userType = "manager";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        regBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAccountActivity();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResetPass();
            }
        });
    }

    public void openResetPass(){
        Intent intent = new Intent(this, ResetPass.class);
        startActivity(intent);
    }

    public void openCreateAccountActivity(){
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }

    private void userLogin(){
        String email = emailED.getText().toString().trim();
        String password = passwordED.getText().toString().trim();

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
            passwordED.setError("email is required");
            passwordED.requestFocus();
            return;
        }

        if(password.length()<6){
            passwordED.setError("password must be a minimum of 6 characters");
            passwordED.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    startActivity(new Intent(Login.this,MainActivity.class));
                }else{
                    Toast.makeText(Login.this, "Failed to login, please check your credentials",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}