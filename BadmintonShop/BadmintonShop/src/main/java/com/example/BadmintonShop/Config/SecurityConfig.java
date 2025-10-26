package com.example.BadmintonShop.Config;

import com.example.BadmintonShop.Auth.JwtAuthenticationFilter;
import com.example.BadmintonShop.Auth.JwtUtils;
import com.example.BadmintonShop.Service.RedisService;
import com.example.BadmintonShop.Service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtils jwtUtils,
                                                           UserService userService,
                                                           RedisService redisService){
        return new JwtAuthenticationFilter(jwtUtils, userService, redisService);
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter,
                                                   UserService userService) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sess-> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/public/**").permitAll()
                                .requestMatchers("/users/**").hasRole("USER")
                                .anyRequest().authenticated()
                        )
//                .formLogin(Customizer.withDefaults())
//                .oauth2Login(
//                        oauth2 -> oauth2.userInfoEndpoint(
//                                        userInfo-> userInfo.userService(oauth2UserService())
//                                )
//                                .successHandler((request, response, authentication) ->{
//                                    CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
//                                    User user = userService.processOAuthPostLogin(customOAuth2User.getEmail());
//                                    String token = jwtUtils.generateToken(user);
//                                    String refreshToken = jwtUtils.generateRefreshToken(user);
//
//                                    response.setContentType("application/json");
//                                    response.getWriter().write("{\"token\": \"" + token + "\"}");
//                                    response.getWriter().write("{\"refresh token\": \"" + refreshToken + "\"}");
//                                } )
//                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
