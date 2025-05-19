package com.taskea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskeaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskeaApplication.class, args);
    }

}
