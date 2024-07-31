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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginStatisticsService loginStatisticsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        User user = userService.register(userRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        try {
            UserLoginRequest userLoginRequest = new UserLoginRequest();
            userLoginRequest.setEmail(email);
            userLoginRequest.setPassword(password);
            User user = userService.login(userLoginRequest);

            if (user != null) {
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userEmail", user.getEmail());
                Map<String, Object> map = new HashMap<>();
                map.put("userId", user.getUserId());
                map.put("userEmail", user.getEmail());
                redisTemplate.opsForHash().putAll("userInfo", map);

            }
            System.out.println("Session ID: " + session.getId());
            return "redirect:/home";
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

    @GetMapping("/home")
    public String home(Model model, HttpSession session) throws Exception {
        Long userId = (Long) session.getAttribute("userId");
        String userEmail = (String) session.getAttribute("userEmail");

        if (userId == null) {
            // 如果用戶未登錄，重定向到登錄頁面
            return "redirect:/login";
        } else {
            Map<String, Object> loginInfo = redisTemplate.opsForHash().entries("userInfo");
            redisTemplate.delete("userInfo");
            model.addAttribute("message", "Login successful");
            if (loginInfo.get("userId") != null) {
                loginStatisticsService.recordLogin();
            }
            List<LoginStatistics> statistics = loginStatisticsService.getAllStatistics();
            String statisticsJson = objectMapper.writeValueAsString(statistics);
            model.addAttribute("userEmail", userEmail);
            model.addAttribute("statistics", statistics);
            model.addAttribute("statisticsForChart", statisticsJson);
            return "home";
        }
    }
}
