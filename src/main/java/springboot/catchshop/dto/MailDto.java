package springboot.catchshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public class MailDto {

    private String address;
    private String title;
    private String message;
}
