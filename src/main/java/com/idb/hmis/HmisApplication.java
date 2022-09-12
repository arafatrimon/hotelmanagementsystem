package com.idb.hmis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HmisApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmisApplication.class, args);    
        System.out.println("Initiated");
    }

}
