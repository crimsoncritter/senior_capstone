package com.example.cartassist;

import com.example.cartassist.ApiService;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    String userType;
    Button loginBut;
    Spinner logSpinner;
    ApiService apiService = new ApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userType = "customer";
        /*when users login they will select whether they are a customer, attendant or manager and
        that value will be save in the userType variable as a String*/
        loginBut = (Button)findViewById(R.id.button3);
        logSpinner = (Spinner) findViewById(R.id.spinner);

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
                // Decide what happens when the login button is clicked
                try {
                    // apiService.login(email, password);
                } catch (Exception ex) {
                    // Maybe display a message on the UI here
                }
            }
        });
    }
}