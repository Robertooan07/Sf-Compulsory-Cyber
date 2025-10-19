package br.com.fiap.api.controller;

import br.com.fiap.api.dto.LoginRequest;
import br.com.fiap.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest {

    @Test
    void shouldReturn200WhenLoginIsValid() {
        UserService mockService = Mockito.mock(UserService.class);
        Mockito.when(mockService.autenticateUser("user", "123456")).thenReturn(true);

        AuthController controller = new AuthController();
        controller.userService = mockService;

        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("123456");

        ResponseEntity<String> response = controller.login(request);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldReturn401WhenInvalidCredentials() {
        UserService mockService = Mockito.mock(UserService.class);
        Mockito.when(mockService.autenticateUser("user", "wrong")).thenReturn(false);

        AuthController controller = new AuthController();
        controller.userService = mockService;

        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("wrong");

        ResponseEntity<String> response = controller.login(request);
        assertEquals(401, response.getStatusCodeValue());
    }
}
