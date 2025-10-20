package br.com.fiap.api.controller;

import br.com.fiap.api.config.JwtUtils;
import br.com.fiap.api.dto.LoginRequest;
import br.com.fiap.api.dto.ResetPasswordRequest;
import br.com.fiap.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            var userDetails = userService.authenticateUser(
                    loginRequest.getUsername(), loginRequest.getPassword());

            String token = jwtUtils.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Usuário ou senha inválidos");
        }
    }

    @PatchMapping("/{username}/password")
    public ResponseEntity<String> resetPassword(
            @PathVariable String username,
            @RequestBody ResetPasswordRequest request) {
        try {
            userService.resetPassword(username, request);
            return ResponseEntity.ok("Senha atualizada com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }
}
