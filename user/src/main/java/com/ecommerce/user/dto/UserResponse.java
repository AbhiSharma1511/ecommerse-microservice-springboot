package com.ecommerce.user.dto;

import com.ecommerce.user.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResponse {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO addressDto;

}
