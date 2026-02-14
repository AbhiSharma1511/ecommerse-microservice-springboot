package com.ecommece.order.dto;

import com.ecommece.order.models.OrderStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public class OrderResponse {
    private Long id;
    private BigDecimal total;
    private OrderStatus orderStatus;
    private List<OrderItemResponse> itemsList;
    private LocalDateTime createdAt;
}
