package com.parser.repositories;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Service;

@Log
@Service
@EnableMongoRepositories(basePackages = "com.parser")
public class DBController {

    private final static String[] BAN = {"comments", "#full_review", "#demo_goods", "#example" , "#"};
    private final static String BASE = "rozetka.com.ua";


    @Autowired
    private LinkRepository repository;


    public boolean ban(String link) {
        for (int i = 0; i < BAN.length; i++) {
            if (link.contains(BAN[i])) return false;
        }
        return true;
    }

    public void DBSave(String link) {
        repository.save(new Link(link));
    }

    public boolean BDCheck(String link) {
        Link test = repository.findByLink(link);
        if (test == null) {
            if (link.contains(BASE) && ban(link)) {
                DBSave(link);
                return true;
            }
        }
        return false;
    }
}
