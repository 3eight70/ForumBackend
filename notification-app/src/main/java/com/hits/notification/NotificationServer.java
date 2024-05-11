package com.hits.notification;

import com.hits.security.Rest.Client.ForumAppClient;
import com.hits.security.Rest.Client.UserAppClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(clients = UserAppClient.class)
@ComponentScan(basePackages = {"com.hits.notification", "com.hits.security"})
public class NotificationServer {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServer.class, args);
    }
}