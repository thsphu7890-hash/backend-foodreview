package com.example.foodreview.service;

import com.example.foodreview.dto.DriverDTO;
import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.mapper.DriverMapper;
import com.example.foodreview.mapper.OrderMapper;
import com.example.foodreview.model.Driver;
import com.example.foodreview.model.Order;
import com.example.foodreview.repository.DriverRepository;
import com.example.foodreview.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final OrderRepository orderRepo;
    private final OrderMapper orderMapper;
    private final DriverRepository driverRepo;
    private final DriverMapper driverMapper;

    // Lấy danh sách cho Admin
    public List<DriverDTO> getAllDrivers() {
        return driverRepo.findAll().stream()
                .map(driverMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Đăng ký tài xế
    public DriverDTO registerDriver(String fullName, String phone, String email, String address,
                                    String idCard, String vehicle, String license,
                                    MultipartFile frontImg, MultipartFile backImg) {
        if (driverRepo.existsByPhone(phone)) {
            throw new RuntimeException("Số điện thoại này đã được đăng ký!");
        }

        Driver driver = new Driver();
        driver.setFullName(fullName);
        driver.setPhone(phone);
        driver.setEmail(email);
        driver.setAddress(address);
        driver.setIdCardNumber(idCard);
        driver.setVehicleType(vehicle);
        driver.setLicensePlate(license);
        
        // --- QUAN TRỌNG: Mặc định là PENDING để chờ Admin duyệt ---
        driver.setStatus("PENDING");

        // Lưu file ảnh (Giả lập)
        if (frontImg != null && !frontImg.isEmpty()) driver.setIdCardFrontImage("/uploads/" + frontImg.getOriginalFilename());
        if (backImg != null && !backImg.isEmpty()) driver.setIdCardBackImage("/uploads/" + backImg.getOriginalFilename());

        Driver savedDriver = driverRepo.save(driver);
        return driverMapper.toDTO(savedDriver);
    }

    // Đăng nhập
    public DriverDTO loginDriver(String phone) {
        Driver driver = driverRepo.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Số điện thoại chưa đăng ký!"));
        
        if ("PENDING".equals(driver.getStatus())) {
            throw new RuntimeException("Tài khoản đang chờ duyệt. Vui lòng quay lại sau!");
        }
        if ("BLOCKED".equals(driver.getStatus())) {
            throw new RuntimeException("Tài khoản đã bị khóa!");
        }
        return driverMapper.toDTO(driver);
    }

    // Cập nhật trạng thái (Duyệt/Khóa)
    public void updateStatus(Long driverId, String status) {
        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài xế"));
        driver.setStatus(status);
        driverRepo.save(driver);
    }

    // Lấy đơn săn
    public List<OrderDTO> getAvailableOrders() {
        return orderRepo.findAll().stream()
                .filter(o -> "CONFIRMED".equals(o.getStatus()) && o.getDriver() == null)
                .map(orderMapper::toDTO).collect(Collectors.toList());
    }

    // Nhận đơn
    @Transactional
    public void acceptOrder(Long orderId, Long driverId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại!"));
        if (order.getDriver() != null) throw new RuntimeException("Đã có tài xế khác nhận!");
        
        Driver driver = driverRepo.findById(driverId).orElseThrow(() -> new RuntimeException("Tài khoản lỗi"));
        if (!"ACTIVE".equals(driver.getStatus())) throw new RuntimeException("Tài khoản chưa kích hoạt!");

        order.setDriver(driver);
        order.setStatus("SHIPPING");
        orderRepo.save(order);
    }

    // Lấy đơn hiện tại
    public List<OrderDTO> getCurrentOrder(Long driverId) {
        return orderRepo.findAll().stream()
                .filter(o -> "SHIPPING".equals(o.getStatus()) && o.getDriver() != null && o.getDriver().getId().equals(driverId))
                .map(orderMapper::toDTO).collect(Collectors.toList());
    }

    // Hoàn thành đơn
    @Transactional
    public void completeOrder(Long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Lỗi đơn"));
        order.setStatus("COMPLETED"); // Dùng COMPLETED để khớp với Admin
        orderRepo.save(order);
    }

    // Lịch sử
    public List<OrderDTO> getHistory(Long driverId) {
         return orderRepo.findAll().stream()
                .filter(o -> "COMPLETED".equals(o.getStatus()) && o.getDriver() != null && o.getDriver().getId().equals(driverId))
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .map(orderMapper::toDTO).collect(Collectors.toList());
    }
}