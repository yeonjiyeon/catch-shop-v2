package springboot.catchshop.dto;

import lombok.Data;
import lombok.Getter;
import springboot.catchshop.domain.Address;
import springboot.catchshop.domain.Role;
import springboot.catchshop.domain.User;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

// 회원가입 폼
// author: 강수민, last modified: 22.02.08
@Getter
@Data
public class JoinDto {
    @NotEmpty(message = "필수 항목입니다.")
    private String loginId;

    @NotEmpty(message = "필수 항목입니다.")
    private String password;

    @NotEmpty(message = "필수 항목입니다.")
    private String name;

    @NotEmpty(message = "필수 항목입니다.")
    private String telephone;

    @NotEmpty(message = "필수 항목입니다.")
    private String email;

    @NotEmpty(message = "필수 항목입니다.")
    private String road;

    @NotEmpty(message = "필수 항목입니다.")
    private String detail;

    @NotEmpty(message = "필수 항목입니다.")
    private String postalcode;

    private String role;

    private LocalDateTime joindate;

    public JoinDto() {

    }

    public JoinDto(String loginId, String password, String name, String telephone, String email,
                   String road, String detail, String postalcode, String role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.road = road;
        this.detail = detail;
        this.postalcode = postalcode;
        this.role = role;
    }

    public User toEntity() {
        return new User(loginId, password, name, telephone, email, new Address(road, detail, postalcode), role);
//        return User.builder()
//                .loginId(loginId)
//                .password(password)
//                .name(name)
//                .telephone(telephone)
//                .address(new Address(road, detail, postalcode))
//                .role(role)
//                .joindate(joindate)
//                .build();
    }
}
