package com.sparta.week05.controller;

import com.sparta.week05.models.Product;
import com.sparta.week05.models.ProductMypriceRequestDto;
import com.sparta.week05.models.ProductRepository;
import com.sparta.week05.models.ProductRequestDto;
import com.sparta.week05.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController // JSON 자동 응답기
public class ProductRestController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    // 관심 상품 조회
    @GetMapping("/api/products")
    public List<Product> readProduct() {
        return productRepository.findAll();
    }

    // 관심 상품 등록
    @PostMapping("/api/products")
    public Product createProduct(@RequestBody ProductRequestDto requestDto) {
        Product product = new Product(requestDto);
        return productRepository.save(product);
    }

    // 관심 가격(최저가) 변경
    @PutMapping("/api/products/{id}")
    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) {
        return productService.update(id, requestDto);
    }
}
