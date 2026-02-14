package com.ecommece.order.dto;

import lombok.Data;

@Data
public class CartItemResponse {

    private String userId;
    private String productId;
    private String productName;
    private String price;
    private String quantity;

}
