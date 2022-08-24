package springboot.catchshop.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.catchshop.domain.Answer;

import java.util.List;
import java.util.Optional;

// Answer Repository
// author: 강수민, created: 22.02.01
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findById(Long id);
    List<Answer> findAllByQuestionId(Long question_id);
}
