package com.spring_demo.services;
import com.spring_demo.entities.Hotel;
import com.spring_demo.repositories.HotelRepository;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class HotelService {
    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);

    @Autowired
    private HotelRepository hotelRepository;

    public Hotel saveHotel(@Valid Hotel hotel) {
        logger.info("Guardando hotel: {}", hotel.getName());
        return hotelRepository.save(hotel);
    }

    public Optional<Hotel> getHotelById(Long id) {
        logger.info("Buscando hotel por ID: {}", id);
        return hotelRepository.findById(id);
    }
    
    public void deleteHotel(Long id) {
        logger.info("Eliminando hotel con ID: {}", id);
        hotelRepository.deleteById(id);
    }

}

