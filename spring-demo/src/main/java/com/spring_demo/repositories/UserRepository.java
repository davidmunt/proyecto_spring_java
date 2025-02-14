package com.spring_demo.repositories;

import com.spring_demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad usuario.
 * Proporciona metodos de acceso a la base de datos para gestionar usuarios.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUserName(String userName);
    
    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findUsersByRole(@Param("role") String role);

    @Query("SELECT u FROM User u WHERE u.userName LIKE %:userName%")
    List<User> findUsersByUserNameContaining(@Param("userName") String userName);
}
