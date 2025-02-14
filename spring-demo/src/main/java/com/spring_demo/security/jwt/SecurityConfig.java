package com.spring_demo.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuracion de seguridad para la aplicacion con Spring Security y JWT.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtEntryPoint jwtEntryPoint;


	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
	    return new JwtAuthenticationFilter(jwtProvider, userDetailsService);
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers("/api/public/**").permitAll()
            	    .requestMatchers("/api/auth/**", "/api/users").permitAll()
            	    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui.html").permitAll()
            	    .requestMatchers(HttpMethod.DELETE, "/api/hotels/**", "/api/reservations/**", "/api/users/**").hasRole("ADMIN") // Solo usuarios con el rol ADMIN puede eliminar
            	    .anyRequest().authenticated()
            	)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtEntryPoint)
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
