package com.example.foodreview.mapper;

import com.example.foodreview.dto.VoucherDTO;
import com.example.foodreview.model.Voucher;
import org.springframework.stereotype.Component;

@Component
public class VoucherMapper {

    public VoucherDTO toDTO(Voucher voucher) {
        VoucherDTO dto = new VoucherDTO();
        dto.setId(voucher.getId());
        dto.setCode(voucher.getCode());
        dto.setPercent(voucher.getPercent());
        dto.setMaxDiscount(voucher.getMaxDiscount());
        dto.setStartDate(voucher.getStartDate());
        dto.setEndDate(voucher.getEndDate());
        
        // Map thêm 2 trường mới
        dto.setType(voucher.getType());
        dto.setConditionValue(voucher.getConditionValue());
        
        return dto;
    }

    public Voucher toEntity(VoucherDTO dto) {
        Voucher voucher = new Voucher();
        voucher.setCode(dto.getCode());
        voucher.setPercent(dto.getPercent());
        voucher.setMaxDiscount(dto.getMaxDiscount());
        voucher.setStartDate(dto.getStartDate());
        voucher.setEndDate(dto.getEndDate());
        
        // Map thêm 2 trường mới
        voucher.setType(dto.getType());
        voucher.setConditionValue(dto.getConditionValue());
        
        return voucher;
    }
    
    public void updateEntityFromDTO(VoucherDTO dto, Voucher entity) {
        entity.setCode(dto.getCode());
        entity.setPercent(dto.getPercent());
        entity.setMaxDiscount(dto.getMaxDiscount());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        
        // Map thêm 2 trường mới
        entity.setType(dto.getType());
        entity.setConditionValue(dto.getConditionValue());
    }
}