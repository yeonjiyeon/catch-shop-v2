package springboot.catchshop.admin.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

// 관리자용 ProductDto
// author: 강수민, created: 22.02.26
// last modified: 22.02.26
@Data
@Getter
public class ProductDto {

    private String name;
    private int price;
    private int stock;
    private MultipartFile productImg;

}
