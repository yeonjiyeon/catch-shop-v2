package springboot.catchshop.dto;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

// 회원정보수정 폼
// author: 강수민, last modified: 22.02.22
@Data
@Getter
public class UpdateUserDto {

    @NotEmpty
    private String password;

    @NotEmpty
    private String telephone;

    @NotEmpty
    private String postalcode;

    @NotEmpty
    private String road;

    @NotEmpty
    private String detail;

    public UpdateUserDto(String password, String telephone,
                      String postalcode, String road, String detail) {
        this.password = password;
        this.telephone = telephone;
        this.postalcode = postalcode;
        this.road = road;
        this.detail = detail;
    }

}
