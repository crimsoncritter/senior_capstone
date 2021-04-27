package com.example.cartassist;

public class User {
    public String email; //make, model, carColor;
    public Boolean isEmployee;
    public boolean multipleCars;

    public User(){

    }

    public User(String email, Boolean isEmployee, Boolean multipleCars){
        this.email = email;
        /*this.make = make;
        this.model = model;
        this.carColor = carColor;*/
        this.isEmployee = isEmployee;
        this.multipleCars = multipleCars;
    }
}
