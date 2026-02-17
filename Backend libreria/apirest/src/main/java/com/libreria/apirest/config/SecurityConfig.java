package com.libreria.apirest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    // realizar peticiones a auth permitiendo crear usuario sin pedir token
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception
    {
        http
                // DESACTIVAR LA PROTECCION
                .csrf(csrf -> csrf.disable())
                // AUTORIZAR REQUEST
                .authorizeHttpRequests(auth -> auth
                        // SE ELIMINO EL METODO POST PARA PERMITIR, EN CASO FALTA ESO TODOS LOS METODOS SON PERMITIDOS
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return  http.build();
    }
}
