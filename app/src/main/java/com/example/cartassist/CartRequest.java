package com.example.cartassist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Iterables;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CartRequest extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private EditText makeED, modelED, colorED, parkingED;
    private Button button;
    private ImageButton imBut;
    private RequestQueue rq;
    private String url = "https://fcm.googleapis.com/fcm/send";
    private DatabaseReference mDatabase;
    private String testString;
    private String topic;
    private int carNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_request);

        imBut = (ImageButton)findViewById(R.id.imageButton);
        makeED = (EditText)findViewById(R.id.editTextMakeCR);
        modelED = (EditText)findViewById(R.id.editTextModelCR);
        colorED = (EditText)findViewById(R.id.editTextColorCR);
        parkingED = (EditText)findViewById(R.id.editTextParkingCR);
        button = (Button)findViewById(R.id.button6);
        rq = Volley.newRequestQueue(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();



        /*testString = "yup";
        reference = FirebaseDatabase.getInstance().getReference("Stores");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                testString += " " + String.valueOf(Iterables.size(snapshot.getChildren()));
                for(DataSnapshot store : snapshot.getChildren()) {
                    testString += " " + store.child("available_carts").getValue().toString();

                }
                parkingED.setText(testString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                testString+= "cancelled";
                parkingED.setText(testString);
            }
        });*/
        //mDatabase.child("Users").child("yr254zxUDFMOQHVaXe2JYS8S3r83").child("raccoon").child("monkey").setValue("26");




        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Intent intent = getIntent();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String carSelect = extras.getString("carChoice");
                String[] split = carSelect.split(" ");
                makeED.setText(split[0]);
                modelED.setText(split[1]);
                colorED.setText(split[2]);
                //The key argument here must match that used in the other activity
            }
            else{
                reference = FirebaseDatabase.getInstance().getReference("Users");
                userID = user.getUid();
                reference.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!(Boolean) task.getResult().child("isEmployee").getValue()) {
                                makeED.setText(String.valueOf(task.getResult().child("Cars").child("car" + String.valueOf(carNumber)).child("make").getValue()));
                                modelED.setText(String.valueOf(task.getResult().child("Cars").child("car" + String.valueOf(carNumber)).child("model").getValue()));
                                colorED.setText(String.valueOf(task.getResult().child("Cars").child("car" + String.valueOf(carNumber)).child("carColor").getValue()));

                            }
                        }
                    }
                });
            }
        }

        mDatabase.child("Stores").child("1").child("attendant_on_duty").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if((boolean)task.getResult().getValue()){
                        topic = "requests";

                    }
                    else{
                        topic = "manager";
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        imBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    private void sendNotification(){
        JSONObject mainObj = new JSONObject();

        try {
            mDatabase.child("Stores").child("1").child("attendant_on_duty").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        if((boolean)task.getResult().getValue()){
                            topic = "requests";

                        }
                        else{
                            topic = "manager";
                        }
                    }
                }
            });

            mainObj.put("to", "/topics/" + topic);
            JSONObject notifObj = new JSONObject();
            notifObj.put("title", "Cart Request at " + parkingED.getText().toString().trim());
            notifObj.put("body", colorED.getText().toString().trim() + " " + makeED.getText().toString().trim() + " " + modelED.getText().toString().trim());
            mainObj.put("notification", notifObj);

            JsonObjectRequest jreq = new JsonObjectRequest(Request.Method.POST, url, mainObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(CartRequest.this, "Cart Request Sent",Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAmOKNa58:APA91bFLeEc28225c54wHhLOBaicocFCXYe5jrwH0AJHkbyT322_jUMUcChhc8AyDQkpRhhNHos_Wyv-6u0j58HTiPtI5YOrvFDDwGEs0q3A1ehJ4LecpNscHcvxfprH_Wac2WeweCMK");
                    return header;
                }
            };

            rq.add(jreq);
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    private void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}