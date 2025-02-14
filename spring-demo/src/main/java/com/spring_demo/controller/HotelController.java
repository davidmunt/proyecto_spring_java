package com.spring_demo.controller;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import com.spring_demo.entities.Hotel;
import com.spring_demo.services.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Controlador de hoteles.
 * Funciones para crear, obtener, actualizar y eliminar hoteles.
 */

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

    @Autowired
    private HotelService hotelService;
    
    @Operation(summary = "Obtener hotel por ID", description = "Devuelve un hotel por su ID")
    @ApiResponse(responseCode = "200", description = "Hotel obtenido")
    @ApiResponse(responseCode = "404", description = "Hotel no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        logger.info("getHotelById endpoint llamado con ID: {}", id);
        return hotelService.getHotelById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo hotel", description = "Crea un nuevo hotel")
    @ApiResponse(responseCode = "201", description = "Hotel creado")
    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        logger.info("createHotel endpoint llamado con datos: {}", hotel);
        Hotel savedHotel = hotelService.saveHotel(hotel);
        return ResponseEntity.ok(savedHotel);
    }

    @Operation(summary = "Actualizar un hotel", description = "Actualiza los datos de un hotel")
    @ApiResponse(responseCode = "200", description = "Hotel actualizado")
    @ApiResponse(responseCode = "404", description = "Hotel no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotelDetails) {
        logger.info("updateHotel endpoint llamado con ID: {}", id);
        return hotelService.getHotelById(id).map(existingHotel -> {
            existingHotel.setName(hotelDetails.getName());
            existingHotel.setDirection(hotelDetails.getDirection());
            Hotel updatedHotel = hotelService.saveHotel(existingHotel);
            return ResponseEntity.ok(updatedHotel);
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un hotel", description = "Elimina un hotel")
    @ApiResponse(responseCode = "204", description = "Hotel eliminado")
    @ApiResponse(responseCode = "404", description = "Hotel no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        logger.info("deleteHotel endpoint llamado con ID: {}", id);
        return hotelService.getHotelById(id).map(hotel -> {
            hotelService.deleteHotel(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
