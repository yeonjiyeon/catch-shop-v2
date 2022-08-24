package springboot.catchshop.service;
/**
 * ProductService
 * author:김지연
 */
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.catchshop.domain.Product;
import springboot.catchshop.domain.ProductStatus;
import springboot.catchshop.dto.PageRequestDTO;
import springboot.catchshop.dto.PageResultDTO;
import springboot.catchshop.dto.ProductDTO;

import java.util.List;


public interface ProductService {
    //dto->Entity
    default Product dtoToEntity(ProductDTO productDTO){
        Product entity = Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .text(productDTO.getText())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .productImg(productDTO.getProductImg())
                .categories(productDTO.getCategory())
                .productStatus(productDTO.getProductStatus())
                .build();
        return entity;
    }


    //entity -> Dto
    default ProductDTO entityToDto(Product product, double avg, Long reviewCnt){
        ProductDTO dto = ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .text(product.getText())
                .price(product.getPrice())
                .stock(product.getStock())
                .productImg(product.getProductImg())
                .category(product.getCategories())
                .productStatus(product.getProductStatus())
                .build();

        dto.setAvg(avg);
        dto.setReviewCnt(reviewCnt);

        return dto;
    }


    //상품 등록
    Long addProduct(ProductDTO productDTO);


    //상품 전체 조회 + 상품 상태별 조회
    PageResultDTO<ProductDTO, Object[]> readProductsWithProductStatus(PageRequestDTO requestDTO, ProductStatus productStatus);

    //상품 검색
    //List<ProductDTO> searchProducts(String keyword);

    //상품 개별 조회
    ProductDTO readSingleProduct(Long id);


    //상품 수정
    void updateProduct(ProductDTO dto);

    //상품 삭제
    void deleteProduct(Long id);


}
