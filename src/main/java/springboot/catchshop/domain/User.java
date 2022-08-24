package springboot.catchshop.domain;

import lombok.*;
import springboot.catchshop.dto.UpdateUserDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "C_USER")
public class User extends BaseEntity {

    @GeneratedValue
    @Id
    @Column(name = "user_id")
    private Long id;

    private String loginId;
    private String password;
    private String name;
    private String telephone;

    private String email;
    private Long kakaoId;

    @Embedded
    private Address address;
    private String role;

    @OneToMany(mappedBy = "user")
    private final List<Question> questionList = new ArrayList<>(); // 작성한 문의사항 리스트

    @OneToMany(mappedBy = "user")
    private final List<Answer> answerList = new ArrayList<>(); // 작성한 답변 리스트

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public User(String loginId, String password, String name, String telephone,
                String email, Address address, String role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.kakaoId = null;
        this.address = address;
        this.role = role;
    }

    public User(String name, String password, String email, String role, Long kakaoId) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId = kakaoId;
    }



    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateUser(UpdateUserDto dto) {
        this.telephone = dto.getTelephone();
        this.address = new Address(dto.getRoad(), dto.getDetail(), dto.getPostalcode());
    }

    public void updateKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
    }

}
