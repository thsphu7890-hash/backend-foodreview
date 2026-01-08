package com.example.foodreview.mapper;

import com.example.foodreview.dto.DriverDTO;
import com.example.foodreview.model.Driver;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {

    public DriverDTO toDTO(Driver driver) {
        if (driver == null) return null;
        
        DriverDTO dto = new DriverDTO();
        dto.setId(driver.getId());
        dto.setFullName(driver.getFullName());
        dto.setPhone(driver.getPhone());
        dto.setEmail(driver.getEmail());
        dto.setAddress(driver.getAddress());
        dto.setIdCardNumber(driver.getIdCardNumber());
        dto.setVehicleType(driver.getVehicleType());
        dto.setLicensePlate(driver.getLicensePlate());
        dto.setIdCardFrontImage(driver.getIdCardFrontImage());
        dto.setIdCardBackImage(driver.getIdCardBackImage());
        dto.setStatus(driver.getStatus());
        dto.setCreatedAt(driver.getCreatedAt());
        
        return dto;
    }
}