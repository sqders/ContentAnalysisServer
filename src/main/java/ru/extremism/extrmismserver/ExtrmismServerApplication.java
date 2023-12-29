package ru.extremism.extrmismserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class ExtrmismServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExtrmismServerApplication.class, args);
    }

}
