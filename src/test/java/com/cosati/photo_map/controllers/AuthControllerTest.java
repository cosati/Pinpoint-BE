package com.cosati.photo_map.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cosati.photo_map.dto.LoginRequest;
import com.cosati.photo_map.dto.LoginResult;
import com.cosati.photo_map.dto.RegisterRequest;
import com.cosati.photo_map.dto.UserDTO;
import com.cosati.photo_map.exceptions.EmailAlreadyRegisteredException;
import com.cosati.photo_map.exceptions.UsernameAlreadyTakenException;
import com.cosati.photo_map.repository.UserRepository;
import com.cosati.photo_map.security.JwtService;
import com.cosati.photo_map.service.AuthService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private AuthService authService;

  @MockBean private UserRepository userRepository;

  @MockBean private PasswordEncoder passwordEncoder;

  @MockBean private JwtService jwtService;

  @Test
  void register_withValidRequest_shouldReturnCreatedUser() throws Exception {
    RegisterRequest req = new RegisterRequest("user@example.com", "password123", "Jane Doe");
    UserDTO created = new UserDTO(UUID.randomUUID(), "Jane Doe");
    when(authService.registerNewUser(any(RegisterRequest.class))).thenReturn(created);

    mockMvc
        .perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(created.getId().toString()))
        .andExpect(jsonPath("$.username").value("Jane Doe"));

    verify(authService).registerNewUser(any(RegisterRequest.class));
  }

  @Test
  void register_withBlankFields_shouldReturnBadRequest() throws Exception {
    RegisterRequest req = new RegisterRequest("", "short", "");

    mockMvc
        .perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void register_withExistingEmail_shouldReturnConflict() throws Exception {
    RegisterRequest req = new RegisterRequest("user@example.com", "password123", "Jane Doe");
    when(authService.registerNewUser(any(RegisterRequest.class)))
        .thenThrow(new EmailAlreadyRegisteredException("Email already registered"));

    mockMvc
        .perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Email already registered"));
  }

  @Test
  void register_withExistingDisplayName_shouldReturnConflict() throws Exception {
    RegisterRequest req = new RegisterRequest("user@example.com", "password123", "Jane Doe");
    when(authService.registerNewUser(any(RegisterRequest.class)))
        .thenThrow(new UsernameAlreadyTakenException("Username already taken"));

    mockMvc
        .perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Username already taken"));
  }

  @Test
  void login_withValidCredentials_shouldSetCookieAndReturnUser() throws Exception {
    LoginRequest req = new LoginRequest("user@example.com", "password123");
    UserDTO user = new UserDTO(UUID.randomUUID(), "Jane Doe");
    when(authService.login(any(LoginRequest.class))).thenReturn(new LoginResult("jwt-token", user));
    when(jwtService.getExpiration()).thenReturn(Duration.ofDays(7));

    mockMvc
        .perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(user.getId().toString()))
        .andExpect(jsonPath("$.username").value("Jane Doe"))
        .andExpect(cookie().value("auth_token", "jwt-token"))
        .andExpect(cookie().httpOnly("auth_token", true))
        .andExpect(cookie().maxAge("auth_token", (int) Duration.ofDays(7).getSeconds()));
  }

  @Test
  void login_withInvalidCredentials_shouldReturnUnauthorized() throws Exception {
    LoginRequest req = new LoginRequest("user@example.com", "wrong-password");
    when(authService.login(any(LoginRequest.class)))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    mockMvc
        .perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void logout_shouldClearCookieAndReturnNoContent() throws Exception {
    mockMvc
        .perform(post("/auth/logout"))
        .andExpect(status().isNoContent())
        .andExpect(cookie().maxAge("auth_token", 0))
        .andExpect(content().string(""));
  }
}
