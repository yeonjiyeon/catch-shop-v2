package springboot.catchshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import springboot.catchshop.domain.Product;
import springboot.catchshop.domain.Question;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.QuestionDto;
import springboot.catchshop.repository.ProductRepository;
import springboot.catchshop.repository.QuestionRepository;
import springboot.catchshop.repository.UserRepository;

import java.io.IOException;

// Question Service
// author: 강수민, created: 22.02.01
@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final ProductRepository productRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    // 질문 생성
    @Transactional
    public Question saveQuestion(User user, Long productId, QuestionDto dto) throws IOException {
        User writer = userRepository.findById(user.getId()).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        assert product != null;
        Question question = new Question(writer, product, dto.getCategory(), dto.getContent(), dto.getSecret(), dto.getPassword());
        if (dto.getQuestionImg() != null) {
            String imgName = fileService.uploadFile(dto.getQuestionImg());
            question.updateImageInfo(imgName);
        }

        questionRepository.save(question);
        return question;
    }

}
