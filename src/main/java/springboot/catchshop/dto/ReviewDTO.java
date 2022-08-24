package springboot.catchshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewDTO {
    private Long id;

    private Long pId;//productid

   // private String uid;//userid

    private String name;

    private String loginId;

    private int star;

    private String contents;

    private LocalDateTime regDate, modDate;
}
