package com.sparta.week05.service;

import com.sparta.week05.models.ItemDto;
import com.sparta.week05.models.Product;
import com.sparta.week05.models.ProductMypriceRequestDto;
import com.sparta.week05.models.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional // DB 업데이트
    public Long update(Long id, ProductMypriceRequestDto requestDto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NullPointerException("상품이 없습니다.")
        );
        product.update(requestDto);
        return id;
    }

    @Transactional
    public Long updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 없습니다.")
        );
        product.updateByItemDto(itemDto);
        return id;
    }
}
