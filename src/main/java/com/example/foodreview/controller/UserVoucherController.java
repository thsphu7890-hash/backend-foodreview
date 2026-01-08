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
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/user-vouchers")
@CrossOrigin(origins = "http://localhost:5173")
public class UserVoucherController {

    @Autowired private VoucherRepository voucherRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserVoucherRepository userVoucherRepository;

    // API này đã được mở public ở SecurityConfig
    @GetMapping("/available")
    public List<Voucher> getAvailableVouchers() {
        return voucherRepository.findAll();
    }

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

        user.setPoints(user.getPoints() - voucher.getConditionValue().intValue());
        userRepository.save(user);
        saveUserVoucher(user, voucher);

        return ResponseEntity.ok(Map.of("message", "Đổi voucher thành công!", "newPoints", user.getPoints()));
    }

    @GetMapping("/my-wallet/{userId}")
    public List<UserVoucher> getMyVouchers(@PathVariable Long userId) {
        return userVoucherRepository.findByUserIdAndIsUsedFalse(userId);
    }

    @PostMapping("/play-game/{userId}")
    public ResponseEntity<?> playGame(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        
        // Tỉ lệ trúng 30%
        if (new Random().nextInt(100) < 30) {
            List<Voucher> rewards = voucherRepository.findByType(VoucherType.GAME_REWARD);
            if (!rewards.isEmpty()) {
                Voucher prize = rewards.get(new Random().nextInt(rewards.size()));
                saveUserVoucher(user, prize);
                return ResponseEntity.ok(Map.of("success", true, "message", "Chúc mừng! Bạn trúng voucher: " + prize.getCode(), "voucher", prize));
            }
        }
        return ResponseEntity.ok(Map.of("success", false, "message", "Chúc bạn may mắn lần sau!"));
    }

    private void saveUserVoucher(User user, Voucher voucher) {
        UserVoucher uv = new UserVoucher();
        uv.setUser(user);
        uv.setVoucher(voucher);
        uv.setReceivedDate(LocalDate.now());
        uv.setUsed(false);
        userVoucherRepository.save(uv);
    }
}