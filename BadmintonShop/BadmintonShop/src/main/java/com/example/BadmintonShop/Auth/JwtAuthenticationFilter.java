package com.example.BadmintonShop.Auth;

import com.example.BadmintonShop.Model.User;
import com.example.BadmintonShop.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils;
    private UserService userService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
       final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
       String token = null;
       String username = null;
       try {
           if(authHeader != null && authHeader.startsWith("Bearer ")){
               token = authHeader.substring(7);
                username = jwtUtils.extractUsername(token);
           }

           if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
               User user =(User) userService.loadUserByUsername(username);
               if(jwtUtils.validateToken(token, user)){
                   List<String> roles = jwtUtils.extractRoles(token);
                   List<SimpleGrantedAuthority> authorities =
                           roles.stream().map(SimpleGrantedAuthority::new).toList();
                   UsernamePasswordAuthenticationToken authToken =
                           new UsernamePasswordAuthenticationToken(username, null, authorities);
                   authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(authToken);
               }
           }
           filterChain.doFilter(request, response);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }
}
