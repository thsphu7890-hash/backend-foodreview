package com.example.foodreview.repository;

import com.example.foodreview.model.Voucher;
import com.example.foodreview.model.VoucherType; // <--- Nhớ Import Enum này
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    
    // 1. Kiểm tra mã đã tồn tại chưa (Dùng khi Admin tạo mới)
    boolean existsByCode(String code);
    
    // 2. Tìm voucher theo mã (Dùng cho Khách nhập mã ở Giỏ hàng)
    Optional<Voucher> findByCode(String code);

    // 3. Tìm danh sách voucher theo loại (Dùng cho Game & Tặng tự động)
    // 👇 BẠN ĐANG THIẾU DÒNG NÀY 👇
   
    List<Voucher> findByType(VoucherType type);
}