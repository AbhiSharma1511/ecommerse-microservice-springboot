package com.ecommece.order.controllers;

import com.ecommece.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    private  final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestHeader("X-User-ID") String userId) {
        try{
            return new ResponseEntity<>(orderService.createOrder(userId).get(), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
//            return orderService.createOrder(userId)
//                    .map(orderResponse -> new ResponseEntity<>(orderResponse, HttpStatus.CREATED))
//                    .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

}
