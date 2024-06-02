package com.oseanchen.demoapp.dao;

import com.oseanchen.demoapp.model.LoginStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface LoginStatisticsDao extends JpaRepository<LoginStatistics, Long>{
        LoginStatistics findByStatisticDate(LocalDate StatisticDate);
}
