package com.hits.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.hits.file", "com.hits.user"})
public class FileServer {
    public static void main(String[] args) {
        SpringApplication.run(FileServer.class, args);
    }
}