package com.oseanchen.demoapp.service;

import com.oseanchen.demoapp.dao.UserDao;
import com.oseanchen.demoapp.dto.UserLoginRequest;
import com.oseanchen.demoapp.dto.UserRegisterRequest;
import com.oseanchen.demoapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public User register(UserRegisterRequest userRegisterRequest) {
        User user = userDao.findByEmail(userRegisterRequest.getEmail());

        if (user != null) {
            log.warn("該 email: {} 已經被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        User newUser = new User();
        newUser.setEmail(userRegisterRequest.getEmail());
        newUser.setPassword(userRegisterRequest.getPassword());

        return userDao.save(newUser);
    }

    public User login(UserLoginRequest userLoginRequest) {
        User user = getUserByEmail(userLoginRequest.getEmail());

        if (user == null) {
            log.warn("email {} is not registered", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        if (user.getPassword().equals(hashedPassword)) {
            return user;
        } else {
            log.warn("email {} passwords is not correct", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public User getUserById(Long id) {
        return userDao.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

}