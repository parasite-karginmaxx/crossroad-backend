package com.example.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdditionRequest {
    private String name;
    private BigDecimal price;
}
