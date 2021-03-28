package com.example.cartassist;

public class User {
    public String email, make, model, carColor;

    public User(){

    }

    public User(String email, String make, String model, String carColor){
        this.email = email;
        this.make = make;
        this.model = model;
        this.carColor = carColor;
    }
}
