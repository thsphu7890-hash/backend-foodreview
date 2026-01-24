package com.example.foodreview.model;

public enum VoucherType {
    // 1. Các loại cơ bản
    DISCOUNT,       // Giảm giá theo % hoặc số tiền
    FREE_SHIP,      // Miễn phí vận chuyển
    
    // 2. Các loại đặc biệt (Dùng cho tính năng Game & Đổi điểm)
    POINT_EXCHANGE, // Voucher đổi từ điểm tích lũy
    GAME_REWARD,    // Voucher nhận được khi làm nhiệm vụ/chơi game
    
    // 3. Loại khác (nếu cần)
    PERSONAL,       // Voucher riêng tư (chỉ user đó thấy)
    PUBLIC,    // Voucher công khai (ai cũng thấy)
    REWARD_ORDER
}