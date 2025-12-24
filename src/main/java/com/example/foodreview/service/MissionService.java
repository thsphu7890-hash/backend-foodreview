package com.example.foodreview.service;

import com.example.foodreview.dto.MissionDTO;
import com.example.foodreview.model.*;
import com.example.foodreview.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepo;
    private final UserMissionRepository userMissionRepo;
    private final OrderRepository orderRepo;
    private final VoucherRepository voucherRepo;

    // --- 1. ADMIN: TẠO & XÓA NHIỆM VỤ ---
    public Mission createMission(Mission mission) {
        return missionRepo.save(mission);
    }

    public void deleteMission(Long id) {
        missionRepo.deleteById(id);
    }

    // --- 2. USER: LẤY DANH SÁCH NHIỆM VỤ ---
    public List<MissionDTO> getMissionsForUser(Long userId) {
        List<Mission> allMissions = missionRepo.findAll();
        List<MissionDTO> result = new ArrayList<>();
        
        List<Order> userOrders = orderRepo.findByUserId(userId);
        LocalDate today = LocalDate.now();

        // Lọc đơn hàng hôm nay (để dùng cho nhiệm vụ Daily)
        List<Order> todayOrders = userOrders.stream()
                .filter(o -> o.getCreatedAt().toLocalDate().isEqual(today))
                .collect(Collectors.toList());

        for (Mission m : allMissions) {
            MissionDTO dto = new MissionDTO();
            dto.setId(m.getId());
            dto.setTitle(m.getTitle());
            dto.setDescription(m.getDescription());
            dto.setTargetValue(m.getTargetValue());
            dto.setIcon(m.getIcon());

            // Tính toán tiến độ
            calculateProgress(dto, m, userOrders, todayOrders);

            // Kiểm tra đã nhận quà chưa
            Optional<UserMission> um = userMissionRepo.findByUserIdAndMissionId(userId, m.getId());
            if (um.isPresent()) {
                if ("DAILY".equals(m.getFrequency())) {
                    dto.setClaimed(um.get().getLastClaimedDate().isEqual(today));
                } else {
                    dto.setClaimed(true); // ONCE thì luôn là true nếu đã có record
                }
            } else {
                dto.setClaimed(false);
            }

            if (m.getRewardVoucherId() != null) {
                voucherRepo.findById(m.getRewardVoucherId()).ifPresent(v -> dto.setRewardVoucherCode(v.getCode()));
            }
            result.add(dto);
        }
        return result;
    }

    // --- 3. USER: NHẬN THƯỞNG (LOGIC QUAN TRỌNG) ---
    @Transactional
    public String claimReward(Long userId, Long missionId) {
        Mission mission = missionRepo.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Nhiệm vụ không tồn tại"));
        
        LocalDate today = LocalDate.now();

        // BƯỚC 1: KIỂM TRA TIẾN ĐỘ (Backend phải tự tính lại để tránh Hack)
        List<Order> userOrders = orderRepo.findByUserId(userId);
        List<Order> todayOrders = userOrders.stream()
                .filter(o -> o.getCreatedAt().toLocalDate().isEqual(today))
                .collect(Collectors.toList());
        
        MissionDTO checkDto = new MissionDTO();
        calculateProgress(checkDto, mission, userOrders, todayOrders);

        if (!checkDto.isCompleted()) {
            throw new RuntimeException("Bạn chưa hoàn thành nhiệm vụ này!");
        }

        // BƯỚC 2: KIỂM TRA ĐÃ NHẬN CHƯA
        Optional<UserMission> existing = userMissionRepo.findByUserIdAndMissionId(userId, missionId);

        if (existing.isPresent()) {
            UserMission um = existing.get();
            
            // Nếu là Daily: Check xem hôm nay nhận chưa
            if ("DAILY".equals(mission.getFrequency()) && um.getLastClaimedDate().isEqual(today)) {
                throw new RuntimeException("Hôm nay bạn đã nhận quà rồi. Hãy quay lại vào ngày mai!");
            }
            // Nếu là Once: Đã có record nghĩa là đã nhận rồi
            if ("ONCE".equals(mission.getFrequency()) || mission.getFrequency() == null) {
                throw new RuntimeException("Nhiệm vụ này chỉ được nhận thưởng 1 lần duy nhất!");
            }

            // Update ngày nhận mới (cho Daily)
            um.setLastClaimedDate(today);
            userMissionRepo.save(um);
        } else {
            // Chưa nhận bao giờ -> Tạo mới
            UserMission um = new UserMission();
            um.setUserId(userId);
            um.setMissionId(missionId);
            um.setLastClaimedDate(today);
            userMissionRepo.save(um);
        }
        

        return "Nhận thưởng thành công!";
    }

    // --- HÀM PHỤ TRỢ: TÍNH TOÁN TIẾN ĐỘ ---
    private void calculateProgress(MissionDTO dto, Mission m, List<Order> allOrders, List<Order> todayOrders) {
        List<Order> ordersToCount = "DAILY".equals(m.getFrequency()) ? todayOrders : allOrders;

        long count = ordersToCount.stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()) || "COMPLETED".equals(o.getStatus()))
                .count();
        
        double spend = ordersToCount.stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()) || "COMPLETED".equals(o.getStatus()))
                .mapToDouble(Order::getTotalAmount).sum();

        if ("ORDER_COUNT".equals(m.getType())) {
            dto.setCurrentValue((int) count);
        } else if ("SPEND_TOTAL".equals(m.getType())) {
            dto.setCurrentValue((int) spend);
        }
        
        if (dto.getCurrentValue() > m.getTargetValue()) dto.setCurrentValue(m.getTargetValue());
        dto.setCompleted(dto.getCurrentValue() >= m.getTargetValue());
    }
}