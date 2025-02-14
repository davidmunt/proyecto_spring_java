package com.spring_demo.services;
import com.spring_demo.entities.Reservation;
import com.spring_demo.repositories.ReservationRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation saveReservation(@Valid Reservation reservation) {
        logger.info("Guardando reserva: {}", reservation.getName());
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        logger.info("Buscando reservas por usuario con ID: {}", userId);
        return reservationRepository.findByUserUserId(userId);
    }

    public List<Reservation> getReservationsByHotel(Long hotelId) {
        logger.info("Buscando reservas por hotel con ID: {}", hotelId);
        return reservationRepository.findByHotelId(hotelId);
    }
    
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public void deleteReservation(Long id) {
        logger.info("Eliminando reserva con ID: {}", id);
        reservationRepository.deleteById(id);
    }

}
