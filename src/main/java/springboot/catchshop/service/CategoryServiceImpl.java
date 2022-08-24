package springboot.catchshop.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.Category;
import springboot.catchshop.dto.CategoryDTO;
import springboot.catchshop.dto.CategoryRequestDTO;
import springboot.catchshop.exception.CategoryNotFoundException;
import springboot.catchshop.repository.CategoryRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public void saveCategory(CategoryRequestDTO requestDTO) {
        Category parent = Optional.ofNullable(requestDTO.getParentId())
            .map(id -> categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new))
            .orElse(null);
        categoryRepository.save(new Category(requestDTO.getName(), parent));
    }

    @Override
    public List<CategoryDTO> readAll() {
        List<Category> categories = categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc();
        //List<Category> categories = categoryRepository.findAll();
        return CategoryDTO.toDtoList(categories);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);
    }

    private boolean notExistsCategory(Long id){
        return !categoryRepository.existsById(id);
    }
}
