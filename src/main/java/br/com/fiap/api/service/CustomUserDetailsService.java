package br.com.fiap.api.service;

import br.com.fiap.api.config.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        try {
            // Usa o método correto do UserService
            return userService.authenticateUser(usernameOrEmail, null); 
            // Passamos null para a senha porque aqui só queremos o UserDetails
        } catch (Exception e) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + usernameOrEmail);
        }
    }
}
