package com.example.cartassist;

public class Status {
    private String state;
    private boolean carSelected;

    private Status(){

    }

    private  static  Status instance;
    public static Status getInstance(){
        if(instance == null){
            instance = new Status();
        }
        return instance;
    }

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public boolean isCarSelected(){
        return carSelected;
    }

    public void setCarSelected(boolean carSelected){
        this.carSelected = carSelected;
    }
}
