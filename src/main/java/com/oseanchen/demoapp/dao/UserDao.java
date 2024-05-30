package com.oseanchen.demoapp.dao;

import com.oseanchen.demoapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
    User findByEmail(String email);

}
