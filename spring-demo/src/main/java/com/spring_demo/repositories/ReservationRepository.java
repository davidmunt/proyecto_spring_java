package com.spring_demo.repositories;
import com.spring_demo.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la entidad reserva.
 * Proporciona metodos de acceso a la base de datos para gestionar reservas.
 */

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserUserId(Long userId);
    List<Reservation> findByHotelId(Long hotelId);
}
