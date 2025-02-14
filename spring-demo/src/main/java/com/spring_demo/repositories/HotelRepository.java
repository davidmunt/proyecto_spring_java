package com.spring_demo.repositories;
import com.spring_demo.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Repositorio para la entidad Hotel.
 * Proporciona metodos de acceso a la base de datos para los hoteles.
 */

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
    @Query("SELECT h FROM Hotel h WHERE h.name LIKE %:name%")
    List<Hotel> findHotelsByName(@Param("name") String name);
}
