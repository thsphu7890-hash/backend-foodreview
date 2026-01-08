package com.example.foodreview.util;

import com.example.foodreview.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey; // Lưu ý import này (thay cho java.security.Key)
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    // Secret key: Cần đủ dài (ít nhất 32 ký tự cho HS256)
    private final String SECRET_KEY = "khoa_bi_mat_sieu_cuc_ky_bao_mat_cua_ban_ne";
    private final long EXPIRATION_TIME = 86400000; // 1 ngày (ms)

    // Hàm lấy Key chuẩn bảo mật (trả về SecretKey thay vì Key chung chung)
    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // --- 1. TẠO TOKEN ---
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        // Lưu ý: Đảm bảo User entity của bạn có method getRole() và getId()
        claims.put("role", user.getRole());
        claims.put("userId", user.getId());

        return Jwts.builder()
                .claims(claims) // Cú pháp thêm claims map
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey()) // Không cần truyền thuật toán, tự động nhận diện HS256
                .compact();
    }

    // --- 2. GIẢI MÃ TOKEN ---

    // Lấy Username từ Token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Lấy ngày hết hạn
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Hàm chung để lấy dữ liệu từ Claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Giải mã toàn bộ Token để lấy Claims (Payload) -> ĐÂY LÀ PHẦN THAY ĐỔI NHIỀU NHẤT
    private Claims extractAllClaims(String token) {
        return Jwts.parser()                    // 1. Dùng parser() thường, không dùng parserBuilder()
                .verifyWith(getSigningKey())    // 2. Dùng verifyWith thay cho setSigningKey
                .build()                        // 3. Phải có build()
                .parseSignedClaims(token)       // 4. Dùng parseSignedClaims thay cho parseClaimsJws
                .getPayload();                  // 5. Dùng getPayload thay cho getBody
    }

    // Kiểm tra Token có hết hạn chưa
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Kiểm tra tính hợp lệ của Token
    public Boolean validateToken(String token, String username) { // Sửa tham số để dễ so sánh hơn
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
    
    // Nếu bạn muốn validate trực tiếp với User object
    public Boolean validateToken(String token, User user) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(user.getUsername()) && !isTokenExpired(token));
    }
}