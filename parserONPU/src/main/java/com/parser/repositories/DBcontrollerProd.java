package com.parser.repositories;


import com.parser.dto.Product;
import com.parser.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

@Service
@EnableMongoRepositories(basePackages = "com.parser")
public class DBcontrollerProd {
    @Autowired
    private ProductRepository repository;


    public Product save(Product a){
        return repository.save(a);
    }
}
