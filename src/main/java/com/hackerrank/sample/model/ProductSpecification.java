package com.hackerrank.sample.model;

import lombok.Data;

@Data
public class ProductSpecification {
    private String key; // internal reference key for the specification, useful for future implementations
    private String label; // display label shown on the frontend
    private String value; // value of this specification
    private String unit; // measurement unit
}
