package com.sparta.springnew.repository;

import com.sparta.springnew.model.ApiUseTime;
import com.sparta.springnew.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiUseTimeRepository extends JpaRepository<ApiUseTime, Long> {
    Optional<ApiUseTime> findByUser(User user);
}