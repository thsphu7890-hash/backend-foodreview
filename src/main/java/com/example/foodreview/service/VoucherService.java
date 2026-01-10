package com.example.foodreview.service;

import com.example.foodreview.dto.VoucherDTO;
import com.example.foodreview.model.Voucher;
import com.example.foodreview.model.VoucherType;
import com.example.foodreview.repository.VoucherRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;

    // 1. Lấy tất cả voucher
    public List<VoucherDTO> getAllVouchers() {
        return voucherRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // 2. Tạo mới Voucher
    @Transactional
    public VoucherDTO createVoucher(VoucherDTO dto) {
        if (voucherRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Mã voucher '" + dto.getCode() + "' đã tồn tại!");
        }
        
        // Kiểm tra logic ngày tháng
        validateVoucherDates(dto.getStartDate(), dto.getEndDate());

        Voucher voucher = new Voucher();
        updateEntityFromDto(voucher, dto);
        voucher.setActive(true); // Mặc định khi tạo mới là kích hoạt

        Voucher savedVoucher = voucherRepository.save(voucher);
        return mapToDTO(savedVoucher);
    }

    // 3. Cập nhật Voucher (Mới nâng cấp để đồng bộ với nút Sửa)
    @Transactional
    public VoucherDTO updateVoucher(Long id, VoucherDTO dto) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Voucher với ID: " + id));

        // Nếu đổi mã code, phải kiểm tra mã mới có trùng với voucher khác không
        if (!voucher.getCode().equals(dto.getCode()) && voucherRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Mã voucher mới '" + dto.getCode() + "' đã được sử dụng!");
        }

        validateVoucherDates(dto.getStartDate(), dto.getEndDate());
        updateEntityFromDto(voucher, dto);
        
        // Giữ nguyên hoặc cập nhật trạng thái Active nếu DTO có gửi lên
        if (dto.getActive() != null) {
            voucher.setActive(dto.getActive());
        }

        Voucher updatedVoucher = voucherRepository.save(voucher);
        return mapToDTO(updatedVoucher);
    }

    // 4. Xóa Voucher
    @Transactional
    public void deleteVoucher(Long id) {
        if (!voucherRepository.existsById(id)) {
            throw new RuntimeException("Voucher không tồn tại!");
        }
        voucherRepository.deleteById(id);
    }

    // --- CÁC HÀM HỖ TRỢ (HELPER METHODS) ---

    // Hàm dùng chung để gán dữ liệu từ DTO sang Entity
    private void updateEntityFromDto(Voucher voucher, VoucherDTO dto) {
        voucher.setCode(dto.getCode().toUpperCase().trim());
        voucher.setPercent(dto.getPercent());
        voucher.setMaxDiscount(dto.getMaxDiscount());
        voucher.setStartDate(dto.getStartDate() != null ? dto.getStartDate() : LocalDateTime.now());
        voucher.setEndDate(dto.getEndDate());
        voucher.setType(dto.getType() != null ? dto.getType() : VoucherType.PUBLIC);
        voucher.setConditionValue(dto.getConditionValue() != null ? dto.getConditionValue() : 0.0);
    }

    // Kiểm tra tính hợp lệ của ngày kết thúc
    private void validateVoucherDates(LocalDateTime start, LocalDateTime end) {
        if (end != null && end.isBefore(start != null ? start : LocalDateTime.now())) {
            throw new RuntimeException("Ngày kết thúc không được trước ngày bắt đầu!");
        }
    }

    // Chuyển đổi Entity sang DTO dùng Builder
    private VoucherDTO mapToDTO(Voucher voucher) {
        return VoucherDTO.builder()
                .id(voucher.getId())
                .code(voucher.getCode())
                .percent(voucher.getPercent())
                .maxDiscount(voucher.getMaxDiscount())
                .startDate(voucher.getStartDate())
                .endDate(voucher.getEndDate())
                .type(voucher.getType())
                .conditionValue(voucher.getConditionValue())
                .active(voucher.getActive())
                .build();
    }
}