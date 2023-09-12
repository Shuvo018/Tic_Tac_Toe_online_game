package com.example.tic_tac_toe002.model;

public class userModel {
    public static boolean firstTurn = false;
    public static boolean singlePlayer = false;

    String name, password;

    public userModel() {
    }

    public userModel(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
