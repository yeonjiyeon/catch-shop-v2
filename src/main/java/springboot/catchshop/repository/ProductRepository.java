package springboot.catchshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springboot.catchshop.domain.Product;

import java.util.List;
import java.util.Optional;
import springboot.catchshop.domain.ProductStatus;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product>{

    @Override
    List<Product> findAll();
    Optional<Product> findById(Long id);



    //평균 상품, 상품별 평점, 리뷰수 구하기(상품 전체 조회)
    @Query("select p, avg(coalesce(r.star, 0)), count(distinct r) from Product p left outer join Review r on r.product = p group by p")
    Page<Object[]> getListPage(Pageable pageable);

    //평균 상품, 상품별 평점, 리뷰수 구하기(상품 상태별 조회)
    @Query("select p, avg(coalesce(r.star, 0)), count(distinct r) from Product p left outer join Review r on r.product = p where p.productStatus = :productStatus group by p")
    Page<Object[]> getListPageAndProductStatus(Pageable pageable, ProductStatus productStatus);


    //상품상세조회
    @Query("select p, avg(coalesce(r.star, 0)), count(r) from Product p left outer join Review r on r.product = p where p.id = :id group by p")
    List<Object[]> getProductWithAll(@Param("id") Long id);


    //검색------------
    //상품 검색하기
    @Query("select p, avg(coalesce(r.star, 0)), count(distinct r) from Product p left outer join Review r on r.product = p where p.name = :keyword group by p")
    Page<Object[]> getSearch(Pageable pageable, String keyword);
}
