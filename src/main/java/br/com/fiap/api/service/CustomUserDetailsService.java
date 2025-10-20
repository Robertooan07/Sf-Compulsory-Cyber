package br.com.fiap.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;

import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService; // só para buscar usuários do banco

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<br.com.fiap.api.model.User> optionalUser = userService.findByUsername(username);

        br.com.fiap.api.model.User user = optionalUser
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}
