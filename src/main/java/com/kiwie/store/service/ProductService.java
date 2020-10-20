package com.kiwie.store.service;

import com.kiwie.store.dto.BaseResponse;
import com.kiwie.store.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    BaseResponse<Void> addProduct(Long userId, MultipartFile[] images, String name, Integer quantity, String category);

    BaseResponse<List<ProductDto>> getProducts(Long userId, String name);

    BaseResponse<ProductDto> getProduct(Long userId, Long productId);
}
