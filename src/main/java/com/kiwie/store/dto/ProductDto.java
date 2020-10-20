package com.kiwie.store.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDto implements Serializable {

    private Long id;

    private String name;

    private Integer quantity;

    private String category;

    private List<String> images = new ArrayList<>();
}
