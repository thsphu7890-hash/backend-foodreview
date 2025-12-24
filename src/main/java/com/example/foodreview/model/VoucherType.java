package com.example.foodreview.model;

public enum VoucherType {
    PUBLIC,         // 0. Nhập mã là dùng (Ai cũng dùng được)
    REWARD_ORDER,   // 1. Tặng khi đơn hàng đạt mức tối thiểu
    GAME_REWARD,    // 2. Phần thưởng khi chơi game
    POINT_EXCHANGE, // 3. Đổi bằng điểm tích lũy
    EVENT           // 4. Tặng dịp sự kiện (Sinh nhật, Lễ...)
}