package com.example.foodreview.model;

public enum VoucherType {
    DISCOUNT,       // Giảm giá thường
    FREE_SHIP,      // Miễn phí vận chuyển
    
    // 👇 THÊM 2 CÁI NÀY ĐỂ HẾT LỖI GẠCH ĐỎ Ở CONTROLLER
    POINT_EXCHANGE, // Voucher đổi điểm
    GAME_REWARD     // Voucher trúng thưởng game
}