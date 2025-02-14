package com.spring_demo.security.jwt;

import java.io.IOException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Punto de entrada de autenticacion para manejar errores de autenticación en solicitudes no autorizadas.
 * Implementa AuthenticationEntryPoint para personalizar las respuestas cuando se necesita autenticacion.
 */

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String urlRequest = SecurityUtils.getFullURL(request);
        String ip = SecurityUtils.getClientIP(request);
        if (authException instanceof BadCredentialsException || authException instanceof InternalAuthenticationServiceException) {
            System.err.println("Error de autenticación. IP: " + ip + ". Request URL: " + urlRequest);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Credenciales erróneas");
        } else {
            System.err.println("Error de petición. IP: " + ip + ". Request URL: " + urlRequest);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
        }
    }
}
