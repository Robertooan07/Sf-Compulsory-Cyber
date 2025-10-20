package br.com.fiap.api.dto;

import br.com.fiap.api.vo.EmailVO;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UserUpdateDTO {

    private String clientName;
    private EmailVO email;
    @Min(value = 0)
    private Double betMaxValue;
    private String userPixKey;

    // Validação customizada
    public void validate() {
        if (clientName != null && !clientName.matches("^[a-zA-Z\\s]{3,50}$")) {
            throw new IllegalArgumentException("Nome do cliente inválido");
        }

        if (email != null && (email.getValue() == null || !email.getValue().matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$"))) {
            throw new IllegalArgumentException("Email inválido");
        }
    }
}
