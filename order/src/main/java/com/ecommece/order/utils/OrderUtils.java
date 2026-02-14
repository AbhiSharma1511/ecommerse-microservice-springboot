package com.ecommece.order.utils;


import com.ecommece.order.dto.OrderItemResponse;
import com.ecommece.order.dto.OrderResponse;
import com.ecommece.order.models.Order;

import java.math.BigDecimal;

public class OrderUtils {

    public static OrderResponse mapToOrderResponse(Order order){

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderStatus(order.getStatus());
        orderResponse.setId(order.getOrderId());
        orderResponse.setTotal(order.getTotalAmount());
        orderResponse.setItemsList(order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(new BigDecimal(item.getQuantity()))
                )).toList());
        orderResponse.setCreatedAt(order.getCreateDate());
        return orderResponse;
    }

}
