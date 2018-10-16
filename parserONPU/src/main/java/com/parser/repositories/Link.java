package com.parser.repositories;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class Link {
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    String link;

    public Link(String link) {
        this.link = link;
    }
}
