package springboot.catchshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import springboot.catchshop.domain.User;

import java.util.List;
import java.util.Optional;

// User Repository
// author: 강수민, created: 22.02.01
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByName(String name);
    void deleteById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByKakaoId(Long kakaoId);

    Page<User> findByLoginIdNotNull(Pageable pageable);
    Page<User> findByLoginIdNull(Pageable pageable);
}
