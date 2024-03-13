package com.hits.security.Client;

import com.hits.common.Models.User.UserDto;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.hits.common.Consts.*;

@FeignClient(name = "USER-SERVICE")
public interface UserAppClient {
    @GetMapping(VALIDATE_TOKEN)
    Boolean validateToken(@RequestParam("token") String token);

    @GetMapping(GET_USER)
    ResponseEntity<UserDto> getUser(@RequestParam("login") String login);
}