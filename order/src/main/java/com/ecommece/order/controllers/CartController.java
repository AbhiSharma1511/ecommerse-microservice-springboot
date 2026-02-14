package com.ecommece.order.controllers;


import com.ecommece.order.utils.CartItemUtils;
import com.ecommece.order.dto.CartItemRequest;
import com.ecommece.order.dto.CartItemResponse;
import com.ecommece.order.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader ("X-User-ID") String userId,
            @RequestBody CartItemRequest request) {

        try{
            if(cartService.addToCart(userId, request))
                return ResponseEntity.status(HttpStatus.CREATED).body("Item added successfully");
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Required request data not found");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeFromCart(
            @RequestHeader ("X-User-ID") String userId,
            @PathVariable String productId){
        System.out.println("Product Id: " + productId);
        try {
            if(cartService.deleteItemFromCart(userId, productId))
                return ResponseEntity.status(HttpStatus.OK).body("Successfully removed the item");
            else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server Error");
        }
        catch(Exception e){
             return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Required request data not found");
    }

    @DeleteMapping
    public ResponseEntity<String> removeAllFromCart(
            @RequestHeader ("X-User-ID") String userId){
        try {
            if(cartService.deleteAllFromCart(userId))
                return ResponseEntity.status(HttpStatus.OK).body("Cart has been empty successfully");
            else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server Error");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
    }

    @GetMapping("/items/{userId}")
    public ResponseEntity<?> getCartItems(@PathVariable String userId) {
        try {
//            User user = userService.getUserById(Long.valueOf(userId));
//            if (user == null) {
//                throw new RuntimeException("User not found!");
//            }
            List<CartItemResponse> cartItemResponseList = cartService.getCartItemsByUserId(userId)
                    .stream().map(cartItem -> CartItemUtils.mapToCartItemResponse(cartItem)).collect(Collectors.toList());
            return new ResponseEntity<>(cartItemResponseList, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
