package springboot.catchshop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.admin.service.ProductServiceForAdmin;
import springboot.catchshop.domain.Product;
import springboot.catchshop.repository.ProductRepository;

import static org.assertj.core.api.Assertions.*;

// author: 강수민
@SpringBootTest
@Transactional
public class ProductServiceForAdminTest {

    @Autowired
    private ProductServiceForAdmin productServiceForAdmin;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품삭제() throws Exception {
        Product product = new Product();
        product.changePrice(10000);
        product.changeStock(10);
        productRepository.save(product);

        int beforeSize = productRepository.findAll().size(); // 상품 삭제 전 전체 상품 개수

        productServiceForAdmin.deleteProduct(product.getId()); // 상품 삭제
        int afterSize = productRepository.findAll().size(); // 상품 삭제 후 전체 상품 개수

        assertThat(beforeSize-afterSize).isEqualTo(1);
    }
}
