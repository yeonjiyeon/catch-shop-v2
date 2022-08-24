package springboot.catchshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static springboot.catchshop.dto.CategoryRequestDTOFactory.categoryRequestDTO;

import java.time.LocalDateTime;
import java.util.Arrays;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.catchshop.domain.Address;
import springboot.catchshop.domain.CategoryFactory;
import springboot.catchshop.domain.Product;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.CategoryDTO;
import springboot.catchshop.dto.CategoryRequestDTO;
import springboot.catchshop.exception.CategoryNotFoundException;
import springboot.catchshop.repository.CategoryRepository;

import javax.transaction.Transactional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    CategoryServiceImpl categoryService;
    @Mock
    CategoryRepository categoryRepository;

    @Test
    void readAllTest() {
        // given
        given(categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc())
            .willReturn(
                Arrays.asList(CategoryFactory.createCategoryWithName("name1"),
                    CategoryFactory.createCategoryWithName("name2")
                )
            );

        // when
        List<CategoryDTO> result = categoryService.readAll();

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("name1");
        assertThat(result.get(1).getName()).isEqualTo("name2");
    }

    @Test
    void createTest() {
        // given
        CategoryRequestDTO req = categoryRequestDTO();

        // when
        categoryService.saveCategory(req);

        // then
        verify(categoryRepository).save(any());
    }

    @Test
    void deleteTest() {
        // given
        given(categoryRepository.existsById(anyLong())).willReturn(true);

        // when
        categoryService.delete(1L);

        // then
        verify(categoryRepository).deleteById(anyLong());
    }

    @Test
    void deleteExceptionByCategoryNotFoundTest() {
        // given
        given(categoryRepository.existsById(anyLong())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> categoryService.delete(1L)).isInstanceOf(CategoryNotFoundException.class);
    }


}
