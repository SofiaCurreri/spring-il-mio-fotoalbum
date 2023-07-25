package org.java.lessons.springilmiofotoalbum.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //serve per reindirizzare l' utente a cio che c'è dentro fotoalbum-frontend, ovvero homepage.html nel caso di log out
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/fotoalbum-frontend/**")
                .addResourceLocations("classpath:/fotoalbum-frontend/");
    }
}
