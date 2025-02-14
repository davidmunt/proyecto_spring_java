package com.spring_demo.controller;
import java.util.List;
import com.spring_demo.services.UserService;
import com.spring_demo.services.HotelService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import com.spring_demo.entities.Hotel;
import com.spring_demo.entities.Reservation;
import com.spring_demo.entities.User;
import com.spring_demo.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Controlador de reservas.
 * Funciones para crear, obtener, actualizar y eliminar reservas.
 */

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private UserService userService; 
    
    @Autowired
    private HotelService hotelService;

    @Operation(summary = "Crear una nueva reserva", description = "Crea una nueva reserva")
    @ApiResponse(responseCode = "201", description = "Reserva creada")
    @ApiResponse(responseCode = "400", description = "Solicitud invalida")
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        logger.info("createReservation endpoint llamado con datos: {}", reservation);
        User user = userService.getUserById(reservation.getUser().getUserId()).orElse(null);
        Hotel hotel = hotelService.getHotelById(reservation.getHotel().getId()).orElse(null);
        reservation.setUser(user);
        reservation.setHotel(hotel);
        Reservation savedReservation = reservationService.saveReservation(reservation);
        return ResponseEntity.ok(savedReservation);
    }
    
    @Operation(summary = "Actualizar una reserva", description = "Actualiza los datos de una reserva")
    @ApiResponse(responseCode = "200", description = "Reserva actualizada")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservationDetails) {
        logger.info("updateReservation endpoint llamado con ID: {}", id);
        return reservationService.getReservationById(id).map(existingReservation -> {
            existingReservation.setName(reservationDetails.getName());
            Reservation updatedReservation = reservationService.saveReservation(existingReservation);
            return ResponseEntity.ok(updatedReservation);
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Obtener una reserva por ID", description = "Devuelve una reserva por su ID")
    @ApiResponse(responseCode = "200", description = "Reserva obtenida")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        logger.info("getReservationById endpoint llamado con ID: {}", id);
        return reservationService.getReservationById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener reservas por usuario", description = "Devuelve todas las reservas de un usuario")
    @ApiResponse(responseCode = "200", description = "Reservas obtenidas")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Long userId) {
        logger.info("getReservationsByUser endpoint llamado con userId: {}", userId);
        return ResponseEntity.ok(reservationService.getReservationsByUser(userId));
    }

    @Operation(summary = "Obtener reservas por hotel", description = "Devuelve todas las reservas de un hotel")
    @ApiResponse(responseCode = "200", description = "Reservas obtenidas")
    @ApiResponse(responseCode = "404", description = "Hotel no encontrado")
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Reservation>> getReservationsByHotel(@PathVariable Long hotelId) {
        logger.info("getReservationsByHotel endpoint llamado con hotelId: {}", hotelId);
        return ResponseEntity.ok(reservationService.getReservationsByHotel(hotelId));
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar una reserva", description = "Elimina una reserva")
    @ApiResponse(responseCode = "204", description = "Reserva eliminada con éxito")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        logger.info("deleteReservation endpoint llamado con ID: {}", id);
        return reservationService.getReservationById(id).map(reservation -> {
            reservationService.deleteReservation(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
