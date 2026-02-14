package com.ecommece.order.utils;


import com.ecommece.order.dto.CartItemResponse;
import com.ecommece.order.models.CartItem;

public class CartItemUtils {

    public static CartItemResponse mapToCartItemResponse(CartItem cartItem){
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setUserId(cartItem.getUserId());
        cartItemResponse.setPrice(cartItem.getPrice().toString());
        cartItemResponse.setQuantity(cartItem.getQuantity().toString());
        cartItemResponse.setProductId(cartItem.getProductId());

        return cartItemResponse;
    }

}
