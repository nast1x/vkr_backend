package com.ssau.kurs_back.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String email;
    // ⚠️ Токены НЕ возвращаются в теле ответа — только в cookies!
}