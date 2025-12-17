package com.quynhtd.ecommerce.dto.auth;

import com.quynhtd.ecommerce.dto.user.UserResponse;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private UserResponse user;
    private String token;
}
