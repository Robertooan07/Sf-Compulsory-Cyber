package br.com.fiap.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import br.com.fiap.api.dto.LoginRequest;
import br.com.fiap.api.dto.ResetPasswordRequest;
import br.com.fiap.api.security.JwtUtil;
import br.com.fiap.api.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    public UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginUserRequest) {
        boolean authenticated = userService.autenticateUser(
            loginUserRequest.getUsername().trim(),
            loginUserRequest.getPassword().trim()
        );

        if (authenticated) {
            String token = JwtUtil.generateToken(loginUserRequest.getUsername());
            return ResponseEntity.ok("Bearer " + token);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PatchMapping("/{username}/password")
    public ResponseEntity<String> resetPassword(
            @PathVariable String username,
            @RequestBody ResetPasswordRequest request) {

        boolean updatedPassword = userService.resetPassword(username, request);

        if (updatedPassword) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.status(401).body("Not authorized to perform this action.");
        }
    }
}
