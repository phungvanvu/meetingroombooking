package org.training.meetingroombooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthPageController {
  @GetMapping("/login")
  public String showLoginPage() {
    return "login";
  }
  @GetMapping("/register")
  public String showRegisterPage() {
    return "register";
  }
  @GetMapping("/home")
  public String homePage() {
    return "home";
  }
}
