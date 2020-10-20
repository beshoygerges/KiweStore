package com.kiwie.store.service.impl;

import com.kiwie.store.dto.BaseResponse;
import com.kiwie.store.dto.ProductDto;
import com.kiwie.store.entity.Image;
import com.kiwie.store.entity.Product;
import com.kiwie.store.entity.User;
import com.kiwie.store.exception.ProductExistException;
import com.kiwie.store.exception.ProductNotExistException;
import com.kiwie.store.exception.UserNotExistException;
import com.kiwie.store.repsoitory.ProductRepository;
import com.kiwie.store.repsoitory.UserRepository;
import com.kiwie.store.service.ProductService;
import com.kiwie.store.util.EncoderUtil;
import com.kiwie.store.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private Path root;
    @Value("${images.upload.folder}")
    private String imagesFolder;

    @PostConstruct
    public void init() {
        try {
            root = Paths.get(imagesFolder);

            if (!Files.exists(root))
                Files.createDirectory(root);

        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Transactional
    @Override
    public BaseResponse<Void> addProduct(Long userId, MultipartFile[] files, String name, Integer quantity, String category) {
        final Optional<User> optionalUser = userRepository.findById(userId);

        final User user = optionalUser.orElseThrow(() -> new UserNotExistException("User not found"));

        if (productRepository.existsByName(name)) throw new ProductExistException("Product already exist");

        final Product product = Product.builder()
                .category(category)
                .name(name)
                .quantity(quantity)
                .build();

        Arrays.asList(files).forEach(
                f -> product.addImage(new Image(imagesFolder + "/" + f.getOriginalFilename())
                ));

        user.addProduct(product);

        Arrays.asList(files).forEach(this::saveImage);

        return BaseResponse.ADD_PRODUCT_RESPONSE;
    }

    @Override
    public BaseResponse<List<ProductDto>> getProducts(Long userId, String name) {

        final Optional<User> optionalUser = userRepository.findById(userId);

        final User user = optionalUser.orElseThrow(() -> new UserNotExistException("User not found"));

        List<ProductDto> productDtos = new ArrayList<>();

        if (Objects.nonNull(name) && !name.isEmpty())
            user.getProducts().stream()
                    .filter(product -> product.getName().equals(name))
                    .forEach(p -> productDtos.add(MapperUtil.map(p, ProductDto.class)));

        else
            user.getProducts().forEach(p -> productDtos.add(MapperUtil.map(p, ProductDto.class)));

        productDtos.forEach(productDto -> productDto.setImages(
                productDto.getImages()
                        .stream()
                        .map(EncoderUtil::encode)
                        .collect(Collectors.toList())));

        return new BaseResponse(productDtos);
    }

    @Override
    public BaseResponse<ProductDto> getProduct(Long userId, Long productId) {

        final Optional<User> optionalUser = userRepository.findById(userId);

        final User user = optionalUser.orElseThrow(() -> new UserNotExistException("User not found"));

        final Optional<Product> optionalProduct = user
                .getProducts()
                .stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst();

        final Product product = optionalProduct.orElseThrow(() -> new ProductNotExistException("product not found"));

        ProductDto productDto = MapperUtil.map(product, ProductDto.class);

        productDto.setImages(
                productDto.getImages()
                        .stream()
                        .map(EncoderUtil::encode)
                        .collect(Collectors.toList()));

        return new BaseResponse(productDto);
    }

    private void saveImage(MultipartFile file) {
        try {
            if (!Files.exists(this.root.resolve(file.getOriginalFilename())))
                Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }
}
