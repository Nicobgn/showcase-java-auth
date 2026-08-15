package dev.nicobgn.showcase.java.auth_demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nicobgn.showcase.java.auth_demo.model.AuthResponse;
import dev.nicobgn.showcase.java.auth_demo.model.RefreshRequest;
import dev.nicobgn.showcase.java.auth_demo.model.SigninRequest;
import dev.nicobgn.showcase.java.auth_demo.model.SignupRequest;
import dev.nicobgn.showcase.java.auth_demo.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        private ObjectMapper objectMapper = new ObjectMapper();

        @MockitoBean
        private AuthService authService;

        @Test
        @DisplayName("Debe registrar un usuario correctamente y retornar Access y Refresh Tokens")
        void signup_ShouldReturnTokens_WhenRequestIsValid() throws Exception {
                // Arrange
                SignupRequest request = new SignupRequest("usuarioTest", "password123");
                AuthResponse responseMock = AuthResponse.builder()
                                .accessToken("fake-access-token")
                                .refreshToken("fake-refresh-token")
                                .build();

                when(authService.signup(any(SignupRequest.class))).thenReturn(responseMock);

                // Act & Assert
                mockMvc.perform(post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.accessToken").value("fake-access-token"))
                                .andExpect(jsonPath("$.refreshToken").value("fake-refresh-token"));
        }

        @Test
        @DisplayName("Debe iniciar sesión correctamente y retornar Access y Refresh Tokens")
        void signin_ShouldReturnTokens_WhenCredentialsAreValid() throws Exception {
                // Arrange
                SigninRequest request = new SigninRequest("usuarioTest", "password123");
                AuthResponse responseMock = AuthResponse.builder()
                                .accessToken("fake-access-token")
                                .refreshToken("fake-refresh-token")
                                .build();

                when(authService.signin(any(SigninRequest.class))).thenReturn(responseMock);
                // Act & Assert
                mockMvc.perform(post("/api/v1/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.accessToken").value("fake-access-token"))
                                .andExpect(jsonPath("$.refreshToken").value("fake-refresh-token"));
        }

        @Test
        @DisplayName("Debe renovar el token de acceso correctamente y retornar nuevos Access y Refresh Tokens")
        void refresh_ShouldReturnNewTokens_WhenRefreshTokenIsValid() throws Exception {
                // Arrange
                RefreshRequest refreshToken = new RefreshRequest("fake-refresh-token");
                AuthResponse responseMock = AuthResponse.builder()
                                .accessToken("fake-new-access-token")
                                .refreshToken("fake-refresh-token")
                                .build();

                when(authService.refresh(refreshToken)).thenReturn(responseMock);

                // Act & Assert
                mockMvc.perform(post("/api/v1/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(refreshToken)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.accessToken").value("fake-new-access-token"))
                                .andExpect(jsonPath("$.refreshToken").value("fake-refresh-token"));
        }
}