package br.com.fiap.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.api.config.JwtUtils;
import br.com.fiap.api.dto.LoginRequest;
import br.com.fiap.api.dto.ResetPasswordRequest;
import br.com.fiap.api.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        boolean authenticated = userService.autenticateUser(
                loginRequest.getUsername(), loginRequest.getPassword());

        if (!authenticated) {
            return ResponseEntity.status(401).body("Usuário ou senha inválidos");
        }

        String token = jwtUtils.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(token);
    }

    @PatchMapping("/{username}/password")
    public ResponseEntity<String> resetPassword(
            @PathVariable String username,
            @RequestBody ResetPasswordRequest request) {

        boolean updated = userService.resetPassword(username, request);
        if (updated) {
            return ResponseEntity.ok("Senha atualizada com sucesso");
        } else {
            return ResponseEntity.status(401).body("Não autorizado");
        }
    }
}
