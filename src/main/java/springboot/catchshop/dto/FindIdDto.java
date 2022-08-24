package springboot.catchshop.dto;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

// 아이디 찾기 폼
// author: 강수민, created: 22.02.08
@Data
@Getter
public class FindIdDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String telephone;

    public FindIdDto(String name, String telephone) {
        this.name = name;
        this.telephone = telephone;
    }
}
