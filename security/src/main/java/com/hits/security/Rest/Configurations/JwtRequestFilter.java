package com.hits.security.Rest.Configurations;

import com.hits.common.Core.User.DTO.UserDto;
import com.hits.common.Core.Utils.JwtUtils;
import com.hits.security.Rest.Client.UserAppClient;
import feign.FeignException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
@Import(JwtUtils.class)
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtils jwtTokenUtils;
    @Qualifier("userController")
    private final UserAppClient userAppClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String login = null;
        String jwt = null;
        boolean tokenInRedis = false;
        UserDto userDto = null;

        response.setHeader("Access-Control-Allow-Origin", "http://localhost");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, X-Auth-Token");

        if (authHeader != null && authHeader.equals("Bearer null")) {
            authHeader = null;
        }
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);

                tokenInRedis = userAppClient.validateToken(jwt);

                if (tokenInRedis) {
                    login = jwtTokenUtils.getUserLogin(jwt);
                }
            }

            if (login != null && !login.isEmpty()) {
                userDto = userAppClient.getUser(login);
            }

            if (login != null && SecurityContextHolder.getContext().getAuthentication() == null && tokenInRedis
                    && userDto != null && userDto.getIsConfirmed() && !userDto.getIsBanned()) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDto,
                        jwt,
                        userDto.getAuthorities()
                                .stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                );
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        catch (UsernameNotFoundException | ExpiredJwtException | SignatureException
               | FeignException.Unauthorized | FeignException.ServiceUnavailable
                | MalformedJwtException e){

        }
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
