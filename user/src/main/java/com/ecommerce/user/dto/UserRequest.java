package com.ecommerce.user.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private AddressDTO address;

}
