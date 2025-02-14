package com.spring_demo.controller;

import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import com.spring_demo.entities.User;
import com.spring_demo.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

/**
 * Controlador de usuarios.
 * Funciones para crear, obtener, actualizar y eliminar usuarios.
 */

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Usuarios obtenidos")
    @ApiResponse(responseCode = "404", description = "No se encontraron usuarios")
    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> usuarios = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }
    
    @Operation(summary = "Obtener usuarios por rol", description = "Devuelve una lista de usuarios por su rol")
    @ApiResponse(responseCode = "200", description = "Usuarios obtenidos")
    @ApiResponse(responseCode = "404", description = "No se encontraron usuarios con ese rol")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        logger.info("getUsersByRole endpoint llamado con rol: {}", role);
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }
    
    @Operation(summary = "Obtener un usuario por ID", description = "Devuelve un usuario por su ID")
    @ApiResponse(responseCode = "200", description = "Usuario obtenido")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("getUserById endpoint llamado con ID: {}", id);
        return userService.getUserById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado")
    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        logger.info("createUser endpoint llamado con datos: {}", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @Operation(summary = "Actualizar un usuario", description = "Actualiza los datos de un usuario")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        logger.info("updateUser endpoint llamado con ID: {}", id);
        return userService.getUserById(id).map(existingUser -> {
            existingUser.setUserName(userDetails.getUserName());
            if (!userDetails.getPassword().equals(existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
            existingUser.setRole(userDetails.getRole());
            User updatedUser = userService.saveUser(existingUser);
            return ResponseEntity.ok(updatedUser);
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario por su ID")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado con éxito")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("deleteUser endpoint llamado con ID: {}", id);
        return userService.getUserById(id).map(user -> {
            userService.deleteUser(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
