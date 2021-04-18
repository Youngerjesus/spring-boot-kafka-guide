package com.example.springkafkademo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public User() {
        super();
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
