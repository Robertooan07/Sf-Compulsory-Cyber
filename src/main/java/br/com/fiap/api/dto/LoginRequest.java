package br.com.fiap.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username; // ou email
    private String password;
}
