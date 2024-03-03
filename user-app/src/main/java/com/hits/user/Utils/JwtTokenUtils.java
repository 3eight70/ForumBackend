package com.hits.user.Utils;

<<<<<<< HEAD
import com.hits.user.Models.Entity.User;
=======

import com.hits.user.Models.Entities.User;
>>>>>>> 652e6b5cc00632fb43cd0fa859c1d48e64471d8d
import com.hits.user.Repositories.RedisRepository;
import com.hits.user.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    private final RedisRepository redisRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Duration lifetime;

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        claims.put("roles", rolesList);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + lifetime.toMillis());
        UUID tokenId = UUID.randomUUID();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .claim("userId", user.getId().toString())
                .setId(tokenId.toString())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    public String getUserEmail(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public void saveToken(String key, String value) {
        redisRepository.save(key, value, lifetime.toMillis());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean validateToken(String token){
        return redisRepository.checkToken(getIdFromToken(token));
    }

    public String getIdFromToken(String token) {
        return getAllClaimsFromToken(token).getId();
    }
}
