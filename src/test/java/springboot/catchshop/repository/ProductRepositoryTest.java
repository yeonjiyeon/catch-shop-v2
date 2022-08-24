package springboot.catchshop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Product;
import springboot.catchshop.domain.QProduct;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @AfterEach
    public void cleanup(){
        productRepository.deleteAll();
    }

    @Test
    public void 상품_등록(){
        //given
        Product productA = Product.builder()
            .id(500L)
            .name("productA")
            .build();

        Product saveProduct = productRepository.save(productA);

        //when
        Optional<Product> product = productRepository.findById(saveProduct.getId());
        //System.out.println("product!!!!"+product);

        //then
        assertThat(product.get().getId()).isEqualTo(saveProduct.getId());
        assertThat(product.get().getName()).isEqualTo("productA");
    }

    @Test
    public void 상품_목록(){
        //given
        Product productA = productRepository.save(Product.builder()
                .id(1L)
                .name("productA")
                .build());

        Product productB = productRepository.save(Product.builder()
                .id(1L)
                .name("productB")
                .build());

        //when
        List<Product> result = productRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(26);

    }

    @Test
    public void testQuery_작성(){
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        QProduct qProduct = QProduct.product;
        String keyWord = "1";
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression expression = qProduct.name.contains(keyWord);
        builder.and(expression);
        Page<Product> result = productRepository.findAll(builder, pageable);
    }


    @Test
    public void 상품_수정(){
        //given
        Product productA = Product.builder()
            .id(500L)
            .name("productA")
            .build();
        Product saveProduct = productRepository.save(productA);

        //when
        Optional<Product> product = productRepository.findById(saveProduct.getId());
        product.get().changeName("productB");
        productRepository.save(productA);

        //then
        assertThat(product.get().getName()).isEqualTo("productB");

    }

    @Test
    public void 상품_삭제(){
        //given
        Product productA = Product.builder()
            .id(500L)
            .name("productA")
            .build();
        Product saveProduct = productRepository.save(productA);


        //when
        productRepository.deleteById(saveProduct.getId());
        Optional<Product> findProduct = productRepository.findById(saveProduct.getId());

        //then
        assertThat(findProduct).isEqualTo(Optional.empty());

    }

}
