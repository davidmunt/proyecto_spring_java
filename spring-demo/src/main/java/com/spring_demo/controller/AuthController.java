package com.spring_demo.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring_demo.dto.JwtDto;
import com.spring_demo.dto.LoginUsuario;
import com.spring_demo.dto.Mensaje;
import com.spring_demo.dto.RespuestaDto;
import com.spring_demo.entities.User;
import com.spring_demo.security.jwt.JwtProvider;
import com.spring_demo.security.jwt.SecurityUtils;
import com.spring_demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador de autenticacion que gestiona el inicio de sesion y la generacion de tokens JWT.
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    @Operation(summary = "Iniciar sesion", description = "Permite a un usuario hacer login y obtener un token JWT")
    @ApiResponse(responseCode = "200", description = "Login correcto, devuelve el token JWT")
    @ApiResponse(responseCode = "400", description = "Usuario o contraseña incorrectos")
    @PostMapping("/login")
    public ResponseEntity<RespuestaDto> login(@RequestBody LoginUsuario loginUsuario, HttpServletRequest request) {
        String ip = SecurityUtils.getClientIP(request);
        String urlRequest = SecurityUtils.getFullURL(request);
        Optional<User> optUser = userService.getByUserName(loginUsuario.getNombreUsuario());
        if (optUser.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("Usuario o contraseña incorrectos."), HttpStatus.BAD_REQUEST);
        }
        // Autenticar usuario con el AuthenticationManager de Spring Security
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }
}
