package springboot.catchshop.dto;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

// 비밀번호 찾기 폼
// author: 강수민, created: 22.02.08
@Data
@Getter
public class FindPwDto {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String email;

    public FindPwDto(String loginId, String email) {
        this.loginId = loginId;
        this.email = email;
    }
}
