package br.com.fiap.api.dto;

import br.com.fiap.api.vo.EmailVO;
import br.com.fiap.api.vo.PasswordVO;
import br.com.fiap.api.vo.UsernameVO;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UserUpdateDTO {

    private String clientName;
    private EmailVO email;
    private UsernameVO username;
    private PasswordVO password;
    @Min(value = 0)
    private Double betMaxValue;
    private String userPixKey;
    private String role;

    public void validate() {
        if (clientName != null && !clientName.matches("^[a-zA-Z\\s]{3,50}$")) {
            throw new IllegalArgumentException("Nome do cliente inválido");
        }
        if (email != null && (email.getValue() == null || !email.getValue().matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$"))) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (username != null && (username.getValue() == null || !username.getValue().matches("^[a-zA-Z0-9._-]{3,20}$"))) {
            throw new IllegalArgumentException("Username inválido");
        }
        if (password != null && (password.getValue() == null || password.getValue().length() < 6)) {
            throw new IllegalArgumentException("Senha deve ter ao menos 6 caracteres");
        }
    }
}
