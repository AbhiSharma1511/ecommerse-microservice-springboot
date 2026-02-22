package com.ecommece.order.services;

import com.ecommece.order.client.UserServiceClient;
import com.ecommece.order.dto.OrderResponse;
import com.ecommece.order.dto.UserResponse;
import com.ecommece.order.models.CartItem;
import com.ecommece.order.models.Order;
import com.ecommece.order.models.OrderItem;
import com.ecommece.order.models.OrderStatus;
import com.ecommece.order.repository.OrderRepository;
import com.ecommece.order.utils.OrderUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;

    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;

    private final RabbitTemplate rabbitTemplate;

    public Optional<OrderResponse> createOrder(String userId) {

        // validate the user
        UserResponse userResponse = userServiceClient.getUserById(userId);
        if (userResponse==null) {
            throw new RuntimeException("User not found!");
        }

        // validate the cart items for order
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userResponse.getUserId());
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }
        //calculate the total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //create the order
        Order order  = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(
                                        null,
                                        cartItem.getProductId(),
                                        cartItem.getQuantity(),
                                        cartItem.getPrice(),
                                        order
                                    )).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // clear the cart
        cartService.clearCart(userId);

        rabbitTemplate.convertAndSend("order.exchange",
                "order.tracking",
                Map.of(
                        "oderId", savedOrder.getOrderId(),
                        "status", "CREATED"
                ));

        return Optional.of(OrderUtils.mapToOrderResponse(savedOrder));
    }

    public Optional<List<OrderResponse>> getAllOrders() {

        List<OrderResponse> ordersResponse = orderRepository.findAll()
                .stream().map(OrderUtils::mapToOrderResponse)
                .toList();

        return Optional.of(ordersResponse);
    }
}
