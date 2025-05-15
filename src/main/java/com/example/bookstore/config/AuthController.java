package com.example.bookstore.config;

import com.example.bookstore.dto.UserRegistrationDto;
import com.example.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;

@Controller
@Slf4j
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/auth/login")
    public String login() {
        log.info("Accessing login page");
        return "auth/login";
    }

    @GetMapping("/auth/signup")
    public String showRegistrationForm(Model model) {
        log.info("Accessing signup page");
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String registerUser(@Valid @ModelAttribute("userRegistrationDto") UserRegistrationDto userDto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("Processing signup request for user: {}", userDto.getUsername());

        if (bindingResult.hasErrors()) {
            log.error("Validation errors occurred during registration");
            return "auth/signup";
        }

        try {
            userService.registerUser(userDto);
            log.info("User registration successful for: {}", userDto.getUsername());
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            log.error("Registration failed for user: {}", userDto.getUsername(), e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/signup";
        }
    }
}
