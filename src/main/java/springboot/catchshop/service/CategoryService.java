package springboot.catchshop.service;

import springboot.catchshop.dto.CategoryDTO;

import java.util.List;
import java.util.Map;
import springboot.catchshop.dto.CategoryRequestDTO;

public interface CategoryService {

    //카테고리 등록
    public void saveCategory (CategoryRequestDTO requestDTO);


    //카테고리 조회
    public List<CategoryDTO> readAll();

    //카테고리 삭제
    public void delete(Long id);


}
