package com.spring_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring_demo.entities.User;
import com.spring_demo.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Controlador que maneja las solicitudes accesibles sin autenticacion.
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida")
    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> usuarios = userService.getAllUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarios);
    }

    @Operation(summary = "Crear un usuario aleatorio", description = "Genera un nuevo usuario con datos aleatorios")
    @ApiResponse(responseCode = "201", description = "Usuario aleatorio creado")
    @GetMapping("/newrandomuser")
    public ResponseEntity<List<User>> newRandomUser() {
        User user = new User();
        user.setUserName("user" + (int) (Math.random() * 1000));
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole("USER");

        userService.saveUser(user);

        List<User> usuarios = userService.getAllUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarios);
    }
}

