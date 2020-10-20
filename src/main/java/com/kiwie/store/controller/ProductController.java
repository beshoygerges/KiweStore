package com.kiwie.store.controller;

import com.kiwie.store.dto.BaseResponse;
import com.kiwie.store.dto.ProductDto;
import com.kiwie.store.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.List;


@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/rest/v1/users/{userId}/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public BaseResponse<Void> addProduct(@PathVariable Long userId, @RequestParam @Size(min = 2) MultipartFile[] files, @RequestParam String name, @RequestParam Integer quantity, @RequestParam String category) {
        return productService.addProduct(userId, files, name, quantity, category);
    }

    @GetMapping("/{productId}")
    public BaseResponse<ProductDto> getProduct(@PathVariable Long userId, @PathVariable Long productId) {
        return productService.getProduct(userId, productId);
    }

    @GetMapping
    public BaseResponse<List<ProductDto>> getProducts(@PathVariable Long userId, @RequestParam(defaultValue = "") String name) {
        return productService.getProducts(userId, name);
    }
}
