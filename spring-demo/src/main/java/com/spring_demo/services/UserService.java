package com.spring_demo.services;

import com.spring_demo.entities.User;
import com.spring_demo.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections; // <-- IMPORT FALTANTE
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new org.springframework.security.core.userdetails.User(
            user.getUserName(),
            user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
    
    public User saveUser(@Valid User user) {
        try {
            logger.info("Guardando usuario: {}", user.getUserName());
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error al guardar el usuario: {}", e.getMessage());
            throw new RuntimeException("No se pudo guardar el usuario. Intenta de nuevo.");
        }
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public List<User> getUsersByRole(String role) {
        logger.info("Buscando usuarios con rol: {}", role);
        return userRepository.findUsersByRole(role);
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        logger.info("Eliminando usuario con ID: {}", id);
        userRepository.deleteById(id);
    }
}
