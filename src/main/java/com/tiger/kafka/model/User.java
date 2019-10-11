package com.tiger.kafka.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = -3107335660896097458L;

    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
