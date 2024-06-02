package com.oseanchen.demoapp.service;

import com.oseanchen.demoapp.dao.LoginStatisticsDao;
import com.oseanchen.demoapp.model.LoginStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoginStatisticsService {
    @Autowired
    private LoginStatisticsDao loginStatisticsDao;

    public void recordLogin() {
        LocalDate today = LocalDate.now();
        LoginStatistics recordForToday = (LoginStatistics) loginStatisticsDao.findByStatisticDate(today);
        if (recordForToday == null) {
            recordForToday = new LoginStatistics();
            recordForToday.setStatisticDate(today);
            recordForToday.setLoginCount(0);
        }
        recordForToday.setLoginCount(recordForToday.getLoginCount() + 1);
        recordForToday.setStatisticDate(today);
        loginStatisticsDao.save(recordForToday);
    }

    public List<LoginStatistics> getAllStatistics() {
        return loginStatisticsDao.findAll();
    }
}
