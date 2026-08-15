package com.cosati.photo_map.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.cosati.photo_map.domain.User;

@Service
public class AuthenticatedUserProvider {
  public User getCurrentUser() {
    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
