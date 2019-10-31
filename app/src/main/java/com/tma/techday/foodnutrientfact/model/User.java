package com.tma.techday.foodnutrientfact.model;


import com.tma.techday.foodnutrientfact.enums.Gender;

/**
 * Contain and save User from DB
 */

public class User {
    private String userName;
    private double height;
    private double weight;
    private double bmi;
    private int age;
    private Gender gender;

    public static User of(String userName, double height, double weight, double bmi, int age, Gender gender){
        return new User(userName, height, weight, bmi, age, gender);
    }

    public User(String userName, double height, double weight, double bmi, int age, Gender gender) {
        this.userName = userName;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.age = age;
        this.gender = gender;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
