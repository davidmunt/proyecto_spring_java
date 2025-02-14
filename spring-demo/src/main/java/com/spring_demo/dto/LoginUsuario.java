package com.spring_demo.dto;

/**
 * DTO para la autenticacion de usuarios.
 * Tiene el nombre de usuario y la contraseña que se envian en la solicitud de inicio de sesion.
 */

public class LoginUsuario {

    private String nombreUsuario;

    private String password;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
