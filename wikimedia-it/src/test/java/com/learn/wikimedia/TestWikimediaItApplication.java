package com.learn.wikimedia;

import org.springframework.boot.SpringApplication;

public class TestWikimediaItApplication {

    public static void main(String[] args) {
        SpringApplication.from(WikimediaItApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
