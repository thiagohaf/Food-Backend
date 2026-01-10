package com.thiagoferreira.food_backend.infraestructure.security;

import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;
import com.thiagoferreira.food_backend.exceptions.ResourceNotFoundException;
import com.thiagoferreira.food_backend.infraestructure.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}

