package com.weatherforecast.repository;

import com.weatherforecast.model.PredictionHistory;
import com.weatherforecast.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<PredictionHistory, Long> {
    List<PredictionHistory> findByUserOrderByPredictionDateDesc(User user);
}
