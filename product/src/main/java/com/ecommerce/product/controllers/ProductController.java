package com.ecommerce.product.controllers;

import com.ecommerce.product.dto.ErrorResponse;
import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.models.Product;
import com.ecommerce.product.services.ProductService;
import com.ecommerce.product.utils.ProductUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        System.out.println("User request hit: " + productRequest);
        return new ResponseEntity<>(ProductUtils.mapToProductResponse(productService.createProduct(productRequest)), HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        try {
            Product product = productService.getProductById(productId);
            return new ResponseEntity<>(ProductUtils.mapToProductResponse(product), HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllActiveProducts(){
        System.out.println("All product function is called");
        return new ResponseEntity<>(productService.getAllActiveProducts(), HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable Long id) {

        if(id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(productRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ProductResponse productResponse = productService.updateProduct(id, productRequest);
        if(productResponse == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productResponse, HttpStatus.OK );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Product delete successfully", HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword){

        List<ProductResponse> l_listProductResponse = null;

        try {
            l_listProductResponse = productService.searchProduct(keyword);
            return new ResponseEntity<>(l_listProductResponse, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(l_listProductResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
