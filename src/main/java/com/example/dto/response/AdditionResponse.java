package com.example.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AdditionResponse {
    private String name;
    private BigDecimal price;
}
