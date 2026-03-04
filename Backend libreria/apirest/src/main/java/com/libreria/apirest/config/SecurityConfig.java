package com.libreria.apirest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

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
                        .requestMatchers(
                            "/auth/**",
                            "/uploads/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex ->
                    ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return  http.build();
    }
}
