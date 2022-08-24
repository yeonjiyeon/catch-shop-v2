package springboot.catchshop.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Data
public class QuestionDto {

    private Boolean secret; // 비밀글 check-box, true-비밀글 false-공개글
    private String password;
    private String category;
    private String content;
    private MultipartFile questionImg; // 첨부이미지

    public QuestionDto(Boolean secret, String password, String category, String content) {
        this.secret = secret;
        this.password = password;
        this.category = category;
        this.content = content;
    }
}
