package springboot.catchshop.dto;
/**
 * CategoryDTO
 * author:김지연
 */
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.catchshop.domain.Category;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import springboot.catchshop.helper.NestedConvertHelper;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private List<CategoryDTO> children;

    public static List<CategoryDTO> toDtoList(List<Category> categories) {
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
            categories,
            c -> new CategoryDTO(c.getId(), c.getName(), new ArrayList<>()),
            c -> c.getParent(),
            c -> c.getId(),
            d -> d.getChildren());
        return helper.convert();
    }
}
