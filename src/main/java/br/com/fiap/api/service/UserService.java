package br.com.fiap.api.service;

import br.com.fiap.api.dto.UserCreateDTO;
import br.com.fiap.api.dto.UserUpdateDTO;
import br.com.fiap.api.dto.ResetPasswordRequest;
import br.com.fiap.api.model.User;
import br.com.fiap.api.repository.UserRepository;
import br.com.fiap.api.config.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------- MÉTODOS DE AUTENTICAÇÃO ----------
    public CustomUserDetails authenticateUser(String usernameOrEmail, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(usernameOrEmail);
        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByEmail(usernameOrEmail);
        }

        User user = optionalUser.orElseThrow(() -> new BadCredentialsException("Usuário não encontrado"));

        if (password != null && !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Senha inválida");
        }

        return new CustomUserDetails(user);
    }

    public void resetPassword(String usernameOrEmail, ResetPasswordRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(usernameOrEmail);
        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByEmail(usernameOrEmail);
        }

        User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    // ---------- CRUD DE USUÁRIOS ----------
    public List<User> listAll() {
        return userRepository.findAll();
    }

    public User searchForId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public User createUser(UserCreateDTO dto) {
        dto.validate(); // chama validação do DTO

        User user = User.builder()
                .clientName(dto.getClientName())
                .username(dto.getUsername().getValue())
                .email(dto.getEmail().getValue())
                .password(passwordEncoder.encode(dto.getPassword().getValue()))
                .userPixKey(dto.getUserPixKey())
                .betMaxValue(dto.getBetMaxValue())
                .role("USER") // ou "ADMIN", se for o caso
                .build();

        return userRepository.save(user);
    }

    public User updateUser(Long id, UserUpdateDTO dto) {
        dto.validate(); // chama validação do DTO

        User user = searchForId(id);

        if (dto.getClientName() != null) user.setClientName(dto.getClientName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail().getValue());
        if (dto.getUserPixKey() != null) user.setUserPixKey(dto.getUserPixKey());
        if (dto.getBetMaxValue() != null) user.setBetMaxValue(dto.getBetMaxValue());

        return userRepository.save(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }
}
