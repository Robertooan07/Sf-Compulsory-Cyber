package br.com.fiap.api.controller;

import br.com.fiap.api.dto.UserCreateDTO;
import br.com.fiap.api.dto.UserUpdateDTO;
import br.com.fiap.api.model.User;
import br.com.fiap.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> listAllUsers() {
        return userService.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> searchUserForId(@PathVariable Long id) {
        try {
            User user = userService.searchForId(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateDTO dto) {
        try {
            User savedUser = userService.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO dto) {
        try {
            User updatedUser = userService.updateUser(id, dto);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserField(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        return updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    // ✅ Endpoints para portabilidade e exclusão automatizados (LGPD)
    @DeleteMapping("/lgpd/{username}")
    public ResponseEntity<String> deleteUserLGPD(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.ok("Usuário removido conforme LGPD");
    }

    @GetMapping("/lgpd/{username}/export")
    public ResponseEntity<UserCreateDTO> exportUserLGPD(@PathVariable String username) {
        User user = userService.findByUsername(username);
        UserCreateDTO dto = new UserCreateDTO();
        dto.setClientName(user.getClientName());
        dto.setEmail(user.getEmail() != null ? user.getEmail() : null);
        dto.setUsername(user.getUsername() != null ? user.getUsername() : null);
        dto.setBetMaxValue(user.getBetMaxValue());
        return ResponseEntity.ok(dto);
    }
}
