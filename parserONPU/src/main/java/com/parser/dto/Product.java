package com.parser.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "docs")
public class Product {

    @Getter (AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) private String url;
    @Getter (AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC)private  String name;
    @Getter (AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC)private String price;
    @Getter (AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC)private String img;
    @Getter (AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC)private String description;

    public Product(String url, String name, String price, String img, String description) {
        this.url = url;
        this.name = name;
        this.price = price;
        this.img = img;
        this.description = description;
    }
}


