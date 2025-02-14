package com.spring_demo.dto;

/**
 * DTO para enviar mensajes de respuesta en las solicitudes.
 * Se usa principalmente para devolver mensajes de exito o error.
 */

public class Mensaje implements RespuestaDto {

    private String mensaje;

    public Mensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
