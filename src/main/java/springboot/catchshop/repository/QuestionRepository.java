package springboot.catchshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.catchshop.domain.Answer;
import springboot.catchshop.domain.Question;

import java.util.List;
import java.util.Optional;

// Question Repository
// author: 강수민, created: 22.02.01
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findById(Long id);
    List<Question> findAll();
    List<Question> findByProductId(Long id);
    List<Question> findByProductName(String name);
}
