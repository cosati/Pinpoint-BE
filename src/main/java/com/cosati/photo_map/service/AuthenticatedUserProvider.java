package com.cosati.photo_map.service;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.cosati.photo_map.domain.User;

@Service
public class AuthenticatedUserProvider {
  public User getCurrentUser() {
    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public Optional<User> getCurrentUserOptional() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
      return Optional.empty();
    }
    return Optional.of(user);
  }
}
