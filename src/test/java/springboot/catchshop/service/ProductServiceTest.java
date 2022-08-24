package springboot.catchshop.service;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Category;
import springboot.catchshop.domain.Product;
import springboot.catchshop.domain.ProductStatus;
import springboot.catchshop.dto.PageRequestDTO;
import springboot.catchshop.dto.PageResultDTO;
import springboot.catchshop.dto.ProductDTO;
import springboot.catchshop.repository.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void 상품_등록(){
        //given
        ProductDTO productDTO = ProductDTO.builder()
                .id(10001L)
                .name("product1001")
                .price(100000)
                .stock(100)
                .build();


        //when
        //Long saveId = productService.addProduct(productDTO);

        //then
        assertThat(productService.addProduct(productDTO)).isEqualTo(10001L);

    }

    @Test
    @DisplayName("상품 상태가 없을 때")
    public void 상품_목록_페이징1(){
        //given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        //when
        PageResultDTO<ProductDTO, Object[]> resultDTO = productService.readProductsWithProductStatus(pageRequestDTO, null);

        //then
        for(ProductDTO productDTO : resultDTO.getDtoList()){
            System.out.println("productDTO" + productDTO);
        };
    }

    @Test
    @DisplayName("상품 상태가 있을 때")
    public void 상품_목록_페이징2(){
        //given
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        //when
        PageResultDTO<ProductDTO, Object[]> resultDTO = productService.readProductsWithProductStatus(pageRequestDTO, ProductStatus.BEST);

        //then
        for(ProductDTO productDTO : resultDTO.getDtoList()){
            System.out.println("productDTO" + productDTO);
        };
    }

    @Test
    public void 상품_수정(){
        //given
        Product product10001 = Product.builder().id(10002L).name("product1001").price(100000).stock(100).build();
        Product saveProduct = productRepository.save(product10001);

        Optional<Product> findProduct = productRepository.findById(saveProduct.getId());
        ProductDTO productDTO = ProductDTO.builder()
            .id(findProduct.get().getId())
            .name("Product10001L")
            .price(findProduct.get().getPrice())
            .stock(1000)
            .build();


        //when
        productService.updateProduct(productDTO);
        Optional<Product> findProduct2 = productRepository.findById(saveProduct.getId());

        //then
        assertThat(findProduct2.get().getName()).isEqualTo("Product10001L");
        assertThat(findProduct2.get().getStock()).isEqualTo(1000);

    }

    @Test
    public void 상품_삭제(){
        //given
        Product product10001 = Product.builder().id(10002L).name("product1001").price(100000).stock(100).build();
        Product saveProduct = productRepository.save(product10001);

        Optional<Product> findProduct = productRepository.findById(saveProduct.getId());

        //when
        productService.deleteProduct(findProduct.get().getId());
        Optional<Product> findProduct2 = productRepository.findById(saveProduct.getId());

        //then
        assertEquals(findProduct2, Optional.empty());
    }
    
    
}
