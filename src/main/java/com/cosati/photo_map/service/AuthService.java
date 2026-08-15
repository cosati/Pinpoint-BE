package com.cosati.photo_map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.cosati.photo_map.domain.User;
import com.cosati.photo_map.dto.LoginRequest;
import com.cosati.photo_map.dto.LoginResult;
import com.cosati.photo_map.dto.RegisterRequest;
import com.cosati.photo_map.dto.UserDTO;
import com.cosati.photo_map.exceptions.EmailAlreadyRegisteredException;
import com.cosati.photo_map.repository.UserRepository;
import com.cosati.photo_map.security.JwtService;

@Service
public class AuthService {

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private JwtService jwtService;

  public UserDTO registerNewUser(RegisterRequest req) {
    if (userRepository.findByEmail(req.email()).isPresent()) {
      throw new EmailAlreadyRegisteredException("Email already registered");
    }
    User user =
        User.builder()
            .email(req.email())
            .passwordHash(passwordEncoder.encode(req.password()))
            .displayName(req.displayName())
            .build();
    User saved = userRepository.save(user);
    return new UserDTO(saved.getId(), saved.getDisplayName());
  }

  public LoginResult login(LoginRequest req) {
    User user =
        userRepository
            .findByEmail(req.email())
            .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
    if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
      throw new BadCredentialsException("Invalid credentials");
    }
    String token = jwtService.generateToken(user);
    return new LoginResult(token, new UserDTO(user.getId(), user.getDisplayName()));
  }
}
