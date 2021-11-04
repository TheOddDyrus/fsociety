package com.thomax.mockito.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomObject {

    private int id;

    private String name;

    private BigDecimal value;

    public CustomObject(int id, String name, BigDecimal value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

}
