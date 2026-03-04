package com.libreria.apirest.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

    // Guardar las imagenes en uploads
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        

        // acceso a la carpeta uploads
        //* Ruta Publica
        registry
            .addResourceHandler("/uploads/**")
            .addResourceLocations("file:uploads/");
    }

    
}
