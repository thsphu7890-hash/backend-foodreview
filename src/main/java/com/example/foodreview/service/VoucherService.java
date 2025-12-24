package com.example.foodreview.service;

import com.example.foodreview.dto.VoucherDTO;
import com.example.foodreview.model.Voucher;
import com.example.foodreview.model.VoucherType;
import com.example.foodreview.mapper.VoucherMapper;
import com.example.foodreview.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate; // <--- NHỚ IMPORT CÁI NÀY
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;
    
    @Autowired
    private VoucherMapper voucherMapper;

    // 1. Lấy danh sách
    public List<VoucherDTO> getAllVouchers() {
        return voucherRepository.findAll().stream()
                .map(voucherMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 2. Tạo mới
    public VoucherDTO createVoucher(VoucherDTO dto) {
        if (voucherRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Mã Voucher đã tồn tại!");
        }
        
        validateVoucherLogic(dto); // Validate

        Voucher voucher = voucherMapper.toEntity(dto);
        Voucher savedVoucher = voucherRepository.save(voucher);
        return voucherMapper.toDTO(savedVoucher);
    }

    // 3. Cập nhật
    public VoucherDTO updateVoucher(Long id, VoucherDTO dto) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Voucher"));

        validateVoucherLogic(dto); // Validate

        voucherMapper.updateEntityFromDTO(dto, voucher);
        Voucher updatedVoucher = voucherRepository.save(voucher);
        return voucherMapper.toDTO(updatedVoucher);
    }

    // 4. Xóa
    public void deleteVoucher(Long id) {
        voucherRepository.deleteById(id);
    }

    // --- 5. HÀM MỚI: CHECK MÃ CHO GIỎ HÀNG ---
    public VoucherDTO checkVoucherValid(String code) {
        // Tìm voucher theo code (Cần thêm findByCode vào Repository)
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Mã giảm giá không tồn tại!"));

        LocalDate now = LocalDate.now();

        // Kiểm tra ngày bắt đầu
        if (voucher.getStartDate().isAfter(now)) {
            throw new RuntimeException("Mã này chưa đến đợt áp dụng!");
        }
        // Kiểm tra ngày kết thúc
        if (voucher.getEndDate().isBefore(now)) {
            throw new RuntimeException("Mã này đã hết hạn!");
        }

        return voucherMapper.toDTO(voucher);
    }

    // --- VALIDATE LOGIC ---
    private void validateVoucherLogic(VoucherDTO dto) {
        if (dto.getType() == VoucherType.POINT_EXCHANGE || dto.getType() == VoucherType.REWARD_ORDER) {
            if (dto.getConditionValue() == null || dto.getConditionValue() <= 0) {
                throw new RuntimeException("Loại Voucher này yêu cầu nhập 'Giá trị điều kiện' (Điểm/Tiền) lớn hơn 0!");
            }
        }
        if (dto.getType() == null) {
            dto.setType(VoucherType.PUBLIC);
        }
    }
}