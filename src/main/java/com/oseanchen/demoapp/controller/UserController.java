package com.oseanchen.demoapp.controller;

import com.oseanchen.demoapp.dto.UserRegisterRequest;
import com.oseanchen.demoapp.model.User;
import com.oseanchen.demoapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        User user = userService.register(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }

    @GetMapping("users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // 返回 login.html 視圖
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            model.addAttribute("message", "Login successful");
            model.addAttribute("email", user.getEmail());
            return "home";  // 返回 home.html 視圖
        }
        model.addAttribute("message", "Invalid credentials");
        return "login";  // 返回 login.html 視圖
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();  // 使 session 失效
        return ResponseEntity.ok().build();
    }
}
