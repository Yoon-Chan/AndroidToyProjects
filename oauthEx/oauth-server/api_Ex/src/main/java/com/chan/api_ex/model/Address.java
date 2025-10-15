package com.chan.api_ex.model;

import lombok.Data;

import java.util.List;

@Data
public class Address {
    String city;
    String street;
    List<Integer> counts;
    List<Number> numbers;
    GRADLE gradle;
    Person person;
}

@Data
class Number {
    int value;
}

@Data
class Person {
    String name;
    String age;
}

enum GRADLE {
    A,B,C,D
}
