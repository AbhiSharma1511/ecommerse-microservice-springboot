package com.ecommece.order.dto;

import com.ecommece.order.models.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO addressDto;
}
