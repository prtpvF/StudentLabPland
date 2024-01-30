package com.poland.student.StudentLab.Configuration;

import com.poland.student.StudentLab.Services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/login","/auth/registration","/error","/css/*",
                                "/org/login", "/about",
                                "/", "/tour/all",
                                "/offer", "/all",
                                "/image/**", "/js/**",
                                "/tour/info/**").permitAll()
                        .anyRequest().authenticated()).formLogin(form->form.loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .failureForwardUrl("/auth/login?error")
                        .defaultSuccessUrl("/home", true).permitAll());
        return http.build();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(@Autowired PersonService personService){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(personService);
        return authenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}