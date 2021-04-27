package com.example.cartassist;

public class Store {
    public String available_carts, empty_carts, total_carts;
    public boolean attendant_on_duty;

    public Store(){

    }

    public Store(boolean attendant_on_duty, String available_carts, String empty_carts, String total_carts){
        this.attendant_on_duty = attendant_on_duty;
        this.available_carts= available_carts;
        this.empty_carts = empty_carts;
        this.total_carts = total_carts;

    }
}
