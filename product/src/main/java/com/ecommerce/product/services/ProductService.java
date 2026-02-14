package com.ecommerce.product.services;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.models.Product;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.utils.ProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(ProductRequest productRequest) {
        Product product = new Product();
        ProductUtils.mapProductRequestToProduct(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    public Product getProductById(String productId) {
        Product product = productRepository.findByProductIdAndActiveTrue(Long.valueOf(productId));

        if(product == null){
            throw new RuntimeException("Product not found");
        }
        else{
            return product;
        }
    }

    public List<ProductResponse> getAllActiveProducts() {
        List<Product> productsList = productRepository.findAll();
        List<ProductResponse> productResponseList = new ArrayList<>();
        for(Product product: productsList){
            if(product.getActive()){
                productResponseList.add(ProductUtils.mapToProductResponse(product));
            }
        }
        return productResponseList;
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product existingProduct  = productRepository.findById(id).orElse(null);
        if(existingProduct == null) {
            return null;
        }
        ProductUtils.mapProductRequestToProduct(existingProduct, productRequest);
        Product savedProduct = productRepository.save(existingProduct);
        return ProductUtils.mapToProductResponse(savedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product now found!!"));
        product.setActive(false);
        productRepository.save(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productsList = productRepository.findAll();
        List<ProductResponse> productResponseList = new ArrayList<>();
        for(Product product: productsList){
            productResponseList.add(ProductUtils.mapToProductResponse(product));
        }
        return productResponseList;
    }

    public List<ProductResponse> searchProduct(String pStrKeyword) {
        return productRepository.searchProduct(pStrKeyword).stream()
                .map(ProductUtils::mapToProductResponse)
                .collect(Collectors.toList());
    }
}
