package com.rcelik.jaxrs.annotation.formparams;

public class Person {

    private String name;
    private String surName;

    public Person(String name, String surName){
        this.name = name;
        this.surName = surName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + surName +
                '}';
    }
}
