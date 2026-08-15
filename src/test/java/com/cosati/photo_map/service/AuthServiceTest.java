package com.cosati.photo_map.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.cosati.photo_map.domain.User;
import com.cosati.photo_map.dto.LoginRequest;
import com.cosati.photo_map.dto.LoginResult;
import com.cosati.photo_map.dto.RegisterRequest;
import com.cosati.photo_map.dto.UserDTO;
import com.cosati.photo_map.exceptions.EmailAlreadyRegisteredException;
import com.cosati.photo_map.exceptions.UsernameAlreadyTakenException;
import com.cosati.photo_map.repository.UserRepository;
import com.cosati.photo_map.security.JwtService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  private static final UUID USER_ID = UUID.randomUUID();
  private static final String EMAIL = "user@example.com";
  private static final String PASSWORD = "password123";
  private static final String PASSWORD_HASH = "hashed-password";
  private static final String DISPLAY_NAME = "Jane Doe";

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private JwtService jwtService;

  @InjectMocks private AuthService authService;

  private User existingUser;

  @BeforeEach
  void setup() {
    existingUser =
        User.builder()
            .id(USER_ID)
            .email(EMAIL)
            .passwordHash(PASSWORD_HASH)
            .displayName(DISPLAY_NAME)
            .build();
  }

  @Test
  void registerNewUser_withNewEmail_shouldSaveAndReturnUserDTO() {
    RegisterRequest req = new RegisterRequest(EMAIL, PASSWORD, DISPLAY_NAME);
    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
    when(userRepository.findByDisplayName(DISPLAY_NAME)).thenReturn(Optional.empty());
    when(passwordEncoder.encode(PASSWORD)).thenReturn(PASSWORD_HASH);
    when(userRepository.save(any(User.class))).thenReturn(existingUser);

    UserDTO result = authService.registerNewUser(req);

    assertThat(result.getId()).isEqualTo(USER_ID);
    assertThat(result.getUsername()).isEqualTo(DISPLAY_NAME);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(captor.capture());
    User savedUser = captor.getValue();
    assertThat(savedUser.getEmail()).isEqualTo(EMAIL);
    assertThat(savedUser.getPasswordHash()).isEqualTo(PASSWORD_HASH);
    assertThat(savedUser.getDisplayName()).isEqualTo(DISPLAY_NAME);
  }

  @Test
  void registerNewUser_withExistingEmail_shouldThrowAndNotSave() {
    RegisterRequest req = new RegisterRequest(EMAIL, PASSWORD, DISPLAY_NAME);
    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(existingUser));

    assertThatThrownBy(() -> authService.registerNewUser(req))
        .isInstanceOf(EmailAlreadyRegisteredException.class)
        .hasMessage("Email already registered");

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void registerNewUser_withExistingDisplayName_shouldThrowAndNotSave() {
    RegisterRequest req = new RegisterRequest(EMAIL, PASSWORD, DISPLAY_NAME);
    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
    when(userRepository.findByDisplayName(DISPLAY_NAME)).thenReturn(Optional.of(existingUser));

    assertThatThrownBy(() -> authService.registerNewUser(req))
        .isInstanceOf(UsernameAlreadyTakenException.class)
        .hasMessage("Username already taken");

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void login_withValidCredentials_shouldReturnTokenAndUser() {
    LoginRequest req = new LoginRequest(EMAIL, PASSWORD);
    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(existingUser));
    when(passwordEncoder.matches(PASSWORD, PASSWORD_HASH)).thenReturn(true);
    when(jwtService.generateToken(existingUser)).thenReturn("jwt-token");

    LoginResult result = authService.login(req);

    assertThat(result.token()).isEqualTo("jwt-token");
    assertThat(result.user().getId()).isEqualTo(USER_ID);
    assertThat(result.user().getUsername()).isEqualTo(DISPLAY_NAME);
  }

  @Test
  void login_withUnknownEmail_shouldThrowBadCredentials() {
    LoginRequest req = new LoginRequest(EMAIL, PASSWORD);
    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> authService.login(req))
        .isInstanceOf(BadCredentialsException.class);

    verify(jwtService, never()).generateToken(any(User.class));
  }

  @Test
  void login_withWrongPassword_shouldThrowBadCredentials() {
    LoginRequest req = new LoginRequest(EMAIL, "wrong-password");
    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(existingUser));
    when(passwordEncoder.matches("wrong-password", PASSWORD_HASH)).thenReturn(false);

    assertThatThrownBy(() -> authService.login(req))
        .isInstanceOf(BadCredentialsException.class);

    verify(jwtService, never()).generateToken(any(User.class));
  }
}
