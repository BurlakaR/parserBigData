package com.parser.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LinkRepository extends MongoRepository<Link, Long> {
    public Link findByLink(String link);
}
