package com.example.foodreview.controller;

import com.example.foodreview.model.User;
import com.example.foodreview.model.UserVoucher;
import com.example.foodreview.model.Voucher;
import com.example.foodreview.model.VoucherType;
import com.example.foodreview.repository.UserRepository;
import com.example.foodreview.repository.UserVoucherRepository;
import com.example.foodreview.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random; // <--- Cần thêm import này để Random

@RestController
@RequestMapping("/api/user-vouchers")
@CrossOrigin(origins = "*")
public class UserVoucherController {

    @Autowired private VoucherRepository voucherRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserVoucherRepository userVoucherRepository;

    // 1. Lấy danh sách Voucher khả dụng (Săn Voucher)
    @GetMapping("/available")
    public List<Voucher> getAvailableVouchers() {
        return voucherRepository.findAll(); 
    }

    // 2. Đổi điểm lấy Voucher
    @PostMapping("/exchange")
    public ResponseEntity<?> exchangeVoucher(@RequestParam Long userId, @RequestParam Long voucherId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(() -> new RuntimeException("Voucher not found"));

        if (voucher.getType() != VoucherType.POINT_EXCHANGE) {
            return ResponseEntity.badRequest().body("Voucher này không phải để đổi điểm!");
        }
        if (user.getPoints() < voucher.getConditionValue()) {
            return ResponseEntity.badRequest().body("Bạn không đủ điểm để đổi!");
        }

        // Trừ điểm
        user.setPoints(user.getPoints() - voucher.getConditionValue().intValue());
        userRepository.save(user);

        // Lưu vào ví
        saveUserVoucher(user, voucher);

        return ResponseEntity.ok("Đổi voucher thành công!");
    }
    
    // 3. Lấy ví voucher của tôi
    @GetMapping("/my-wallet/{userId}")
    public List<UserVoucher> getMyVouchers(@PathVariable Long userId) {
        return userVoucherRepository.findByUserIdAndIsUsedFalse(userId);
    }

    // --- 👇 PHẦN BẠN ĐANG THIẾU 👇 ---

    // 4. Chơi Game Quay Thưởng
    @PostMapping("/play-game/{userId}")
    public ResponseEntity<?> playGame(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        
        // Tỉ lệ trúng thưởng: 30%
        // Random từ 0-99, nếu nhỏ hơn 30 là trúng
        if (new Random().nextInt(100) < 30) {
            // Tìm tất cả voucher loại GAME_REWARD
            List<Voucher> gameVouchers = voucherRepository.findByType(VoucherType.GAME_REWARD);
            
            if (!gameVouchers.isEmpty()) {
                // Chọn ngẫu nhiên 1 voucher trong danh sách quà
                Voucher prize = gameVouchers.get(new Random().nextInt(gameVouchers.size()));
                
                // Lưu voucher trúng được vào ví user
                saveUserVoucher(user, prize);
                
                return ResponseEntity.ok("Chúc mừng! Bạn đã trúng voucher mã: " + prize.getCode());
            }
        }
        
        return ResponseEntity.ok("Rất tiếc! Chúc bạn may mắn lần sau.");
    }

    // Hàm phụ để lưu UserVoucher (Code đỡ lặp lại)
    private void saveUserVoucher(User user, Voucher voucher) {
        UserVoucher uv = new UserVoucher();
        uv.setUser(user);
        uv.setVoucher(voucher);
        uv.setReceivedDate(LocalDate.now());
        uv.setUsed(false);
        userVoucherRepository.save(uv);
    }
}