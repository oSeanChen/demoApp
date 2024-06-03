package com.oseanchen.demoapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oseanchen.demoapp.dto.UserLoginRequest;
import com.oseanchen.demoapp.dto.UserRegisterRequest;
import com.oseanchen.demoapp.model.LoginStatistics;
import com.oseanchen.demoapp.model.User;
import com.oseanchen.demoapp.service.LoginStatisticsService;
import com.oseanchen.demoapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginStatisticsService loginStatisticsService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        User user = userService.register(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) throws JsonProcessingException {
        try {
            UserLoginRequest userLoginRequest = new UserLoginRequest();
            userLoginRequest.setEmail(email);
            userLoginRequest.setPassword(password);
            User user = userService.login(userLoginRequest);
            model.addAttribute("message", "Login successful");
            model.addAttribute("email", user.getEmail());
            loginStatisticsService.recordLogin();
            List<LoginStatistics> statistics = loginStatisticsService.getAllStatistics();
            String statisticsJson = objectMapper.writeValueAsString(statistics);
            model.addAttribute("statistics", statistics);
            model.addAttribute("statisticsForChart", statisticsJson);
            return "home";
        } catch (ResponseStatusException exception) {
            model.addAttribute("message", "Invalid credentials");
            return "login";
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();  // 使 session 失效
        return ResponseEntity.ok().build();
    }
}
