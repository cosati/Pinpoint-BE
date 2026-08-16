package com.cosati.photo_map.controllers;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cosati.photo_map.domain.User;
import com.cosati.photo_map.dto.LoginRequest;
import com.cosati.photo_map.dto.LoginResult;
import com.cosati.photo_map.dto.RegisterRequest;
import com.cosati.photo_map.dto.UserDTO;
import com.cosati.photo_map.security.JwtService;
import com.cosati.photo_map.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired private JwtService jwtService;

  @Autowired private AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest req) {
    UserDTO created = authService.registerNewUser(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PostMapping("/login")
  public ResponseEntity<UserDTO> login(
      @Valid @RequestBody LoginRequest req, HttpServletResponse response) {
    LoginResult result = authService.login(req);
    response.addHeader(
        HttpHeaders.SET_COOKIE, authCookie(result.token(), jwtService.getExpiration()).toString());
    return ResponseEntity.ok(result.user());
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    response.addHeader(HttpHeaders.SET_COOKIE, authCookie("", Duration.ZERO).toString());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  public ResponseEntity<UserDTO> me(Authentication authentication) {
    if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok(new UserDTO(user.getId(), user.getDisplayName()));
  }

  private ResponseCookie authCookie(String value, Duration maxAge) {
    return ResponseCookie.from("auth_token", value)
        .httpOnly(true)
        .secure(true)
        .sameSite("Lax")
        .path("/")
        .maxAge(maxAge)
        .build();
  }
}
