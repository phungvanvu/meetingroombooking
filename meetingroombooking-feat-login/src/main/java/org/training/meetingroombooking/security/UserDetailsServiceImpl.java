package org.training.meetingroombooking.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.training.meetingroombooking.repository.UserRepository;

@Service
public class UserDetailsServiceImpl {

  @Autowired
  private UserRepository userRepository;

}
