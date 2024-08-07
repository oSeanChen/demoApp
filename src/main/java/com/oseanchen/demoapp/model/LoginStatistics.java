package com.oseanchen.demoapp.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Login_statistics")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LoginStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int loginCount;

    @Column(nullable = false)
    private LocalDate statisticDate;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdTime;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

//    @PrePersist
//    protected void onCreate() {
//        createdTime = LocalDateTime.now();
//        lastModifiedDate = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        lastModifiedDate = LocalDateTime.now();
//    }

}
