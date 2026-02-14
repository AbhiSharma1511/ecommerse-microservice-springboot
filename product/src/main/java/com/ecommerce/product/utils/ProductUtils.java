package com.ecommerce.product.utils;

import com.ecommerce.product.dto.*;
import com.ecommerce.product.models.Product;
import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;

public class ProductUtils {

    public static ProductResponse mapToProductResponse(Product product){
        ProductResponse response = new ProductResponse();

        response.setId(product.getProductId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory());
        response.setStockQuantity(product.getStockQuantity());
        response.setActive(product.getActive());
        response.setImageUrl(product.getImageUrl());

        return response;

    }

    public static void mapProductRequestToProduct(Product product, ProductRequest productRequest) {

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setImageUrl(productRequest.getImageUrl());
    }

}
