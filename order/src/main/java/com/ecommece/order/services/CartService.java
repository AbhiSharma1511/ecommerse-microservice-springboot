package com.ecommece.order.services;

import com.ecommece.order.client.ProductServiceClient;
import com.ecommece.order.client.UserServiceClient;
import com.ecommece.order.dto.CartItemRequest;
import com.ecommece.order.dto.ProductResponse;
import com.ecommece.order.dto.UserResponse;
import com.ecommece.order.models.CartItem;
import com.ecommece.order.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    //private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    //private final UserRepository userRepository;
    //private final UserService userService;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

    public boolean addToCart(String userId, CartItemRequest cartRequest) {
        try{
            // look for the product
            ProductResponse productResponse = productServiceClient.getProductById(Long.valueOf(cartRequest.getProductId()));
            if(productResponse==null)
            {
                throw new RuntimeException("Product not found");
            }

            if(productResponse.getStockQuantity() < cartRequest.getQuantity())
            {
                throw new RuntimeException("Quantity exceeded");
            }

            UserResponse userResponse = userServiceClient.getUserById(userId);

            if(userResponse==null)
            {
                throw new RuntimeException("User not found");
            }

            CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, cartRequest.getProductId());
            if(existingCartItem != null)
            {
                // update the quantity of the product
                existingCartItem.setQuantity(cartRequest.getQuantity() + existingCartItem.getQuantity());
                existingCartItem.setPrice(productResponse.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
                //existingCartItem.setPrice(BigDecimal.valueOf(10));
                cartItemRepository.save(existingCartItem);
            }
            else{
                // create new cart for the order
                CartItem cartItem = new CartItem();
                cartItem.setUserId(userId);
                cartItem.setPrice(productResponse.getPrice().multiply(BigDecimal.valueOf(cartRequest.getQuantity())));
                //cartItem.setPrice(BigDecimal.valueOf(10));
                cartItem.setQuantity(cartRequest.getQuantity());
                cartItem.setProductId(cartRequest.getProductId());

                CartItem savedCartItem =  cartItemRepository.save(cartItem);
            }
            return true;
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public boolean deleteItemFromCart(String userId, String productId) {
        try{
            // look for the product
//            Optional<Product> productOpt = productRepository.findById(productId);
//            if(productOpt.isEmpty())
//            {
//                throw new RuntimeException("Product not found");
//            }
//
//            Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//            if(userOpt.isEmpty())
//            {
//                throw new RuntimeException("User not found");
//            }

            CartItem deletedItem = cartItemRepository.deleteCartItemByUserIdAndProductId(userId, productId);
            if(deletedItem != null){
                return true;
            }
            else{
                throw new RuntimeException("Product not found in user cart!");
            }
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public boolean deleteAllFromCart(String userId) {
        try{

            UserResponse userResponse = userServiceClient.getUserById(userId);
            if(userResponse==null)
            {
                throw new RuntimeException("User not found");
            }

            List<CartItem> deletedList = cartItemRepository.deleteCartByUserId(userId);
            if(!deletedList.isEmpty()){
                return true;
            }
            else{
                throw new RuntimeException("Cart is already empty!");
            }
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<CartItem> getCartItemsByUserId(String userId) {

        try{
            return cartItemRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

    public void clearCart(String userId) {
        List<CartItem> deletedList = cartItemRepository.deleteCartByUserId(userId);
    }
}
