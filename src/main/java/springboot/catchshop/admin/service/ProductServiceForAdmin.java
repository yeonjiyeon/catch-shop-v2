package springboot.catchshop.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.admin.dto.ProductDto;
import springboot.catchshop.domain.Product;
import springboot.catchshop.repository.ProductRepository;
import springboot.catchshop.repository.ReviewRepository;
import springboot.catchshop.service.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 관리자용 Product Service
// author: 강수민, created: 22.02.26
// last modified: 22.04.15
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceForAdmin {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final FileService fileService;

    // 상품 전체 조회 + 페이징
    @Transactional(readOnly = true)
    public Page<Product> getAllProductsWithPaging(int startAt) {
        // 최신순 조회
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(startAt, 10, Sort.by(sorts));
        return productRepository.findAll(pageable);
    }

    // 상품 생성
    public void saveProduct(ProductDto dto) throws IOException {
        // 상품 생성
        Product product = new Product();
        product.setProduct(dto);

        if (dto.getProductImg() != null) {
            String imgName = fileService.uploadFile(dto.getProductImg()); // 이미지 파일 업로드
            product.updateImageInfo(imgName);
        }

        productRepository.save(product);
    }

    // 상품 수정
    public void updateProduct(Long productId, ProductDto dto) throws IOException {
        Product product = productRepository.findById(productId).orElse(null);
        product.setProduct(dto);

        String imgName = fileService.uploadFile(dto.getProductImg());
        product.updateImageInfo(imgName);

        productRepository.save(product);
    }

    // 상품 삭제
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow( () -> new IllegalStateException("해당 주문이 존재하지 않습니다."));
        reviewRepository.deleteByProduct(product);
        productRepository.delete(product);
    }

}
