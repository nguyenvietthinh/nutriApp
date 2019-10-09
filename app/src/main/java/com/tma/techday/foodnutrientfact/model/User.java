package com.tma.techday.foodnutrientfact.model;

public class User {
    private String userName;
    private double height;
    private double weight;
    private double bmi;

    public static User of(String userName, double height, double weight, double bmi){
        return new User(userName, height, weight, bmi);
    }

    public User(String userName, double height, double weight, double bmi) {
        this.userName = userName;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }
}
