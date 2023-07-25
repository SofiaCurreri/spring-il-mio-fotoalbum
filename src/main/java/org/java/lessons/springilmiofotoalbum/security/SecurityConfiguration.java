package org.java.lessons.springilmiofotoalbum.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {

    //x definire un AuthenticationProvider ho bisogno di:
    //- uno UserDetailsService
    //- un PasswordEncoder


    //questo è lo UserDetailsService
    @Bean
    DatabaseUserDetailsService userDetailsService() {
        return new DatabaseUserDetailsService();
    }

    //questo è PasswordEncoder che deduce algoritmo di encoding da una stringa nella password stessa
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        //creo AuthenticationProvider
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //gli setto PasswordEncoder
        provider.setPasswordEncoder(passwordEncoder());
        //gli setto UserDetailsService
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }


    /*
    /categories solo ADMIN
    /photos, /photos/{id} ADMIN e USER
    /photos/create solo ADMIN
    /photos/edit/** solo ADMIN
    /photos/delete/{id} solo ADMIN (è una post)
     */

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //definisco la catena di filtri
        http.authorizeHttpRequests()
                .requestMatchers("/categories").hasAuthority("ADMIN")
                .requestMatchers("/photos/edit/**").hasAuthority("ADMIN")
                .requestMatchers("/photos/create").hasAuthority("ADMIN")
                .requestMatchers("/photos/delete/**").hasAuthority("ADMIN")
                .requestMatchers("/photos/**").hasAnyAuthority("ADMIN", "USER")
                //per dire che tutte le PostMapping sono solo x admin
                .requestMatchers(HttpMethod.POST, "/photos/**").hasAuthority("ADMIN")
                .requestMatchers("/**").permitAll()
                .and().formLogin()
                .and().logout()

                //serve per reindirizzare l' utente a cio che c'è dentro fotoalbum-frontend, ovvero homepage.html nel caso di log out
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessHandler((request, response, authentication) -> {
                    String targetUrl = "/fotoalbum-frontend/homepage.html";
                    response.sendRedirect(request.getContextPath() + targetUrl);
                })
                .permitAll(); // Consentire a tutti di accedere al logout

        //disabilitiamo csrf per invocare api da Postman
        http.csrf().disable();
        return http.build();
    }

}
