package com.example.foodreview.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.foodreview.model.UserVoucher;

import java.util.List;

@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long> {
    // Tìm voucher của user mà chưa sử dụng
    List<UserVoucher> findByUserIdAndIsUsedFalse(Long userId);
}