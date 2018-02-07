package ru.jxt.testtaskunit6.data;

public class User {

    private String firstName;
    private String secondName;
    private int age;
    private String address;

    public User(String firstName, String secondName, int age, String address) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }
}
