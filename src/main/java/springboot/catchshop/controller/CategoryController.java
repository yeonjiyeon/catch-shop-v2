package springboot.catchshop.controller;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springboot.catchshop.admin.dto.ProductDto;
import springboot.catchshop.domain.User;
import springboot.catchshop.dto.CategoryDTO;
import springboot.catchshop.dto.CategoryRequestDTO;
import springboot.catchshop.dto.PageRequestDTO;
import springboot.catchshop.dto.Response;
import springboot.catchshop.service.CategoryService;
import springboot.catchshop.session.SessionConst;


@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public String getOneCategory(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
        @ModelAttribute("categoryRequestDto") CategoryRequestDTO dto) {
        if (loginUser == null) {
            return "login";
        }
        return "admin/create-category";
    }

    @PostMapping("/category")
    //카테고리 등록
    public String savaCategory(@SessionAttribute(name = SessionConst.ROLE, required = false) String role,
                             @ModelAttribute CategoryRequestDTO dto){

        if (!Objects.equals(role, "ADMIN")) { // 권한없음
            return "admin/create-category";
        }

        categoryService.saveCategory(dto);
        return "redirect:/categories";
    }

    //모든 카테고리 조회
    @GetMapping("/categories")
    public String getCategories (@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, Model model) {
        if (loginUser == null) {
            return "login";
        }
        model.addAttribute("categories",categoryService.readAll());
        return "admin/categories";
    }


    //카테고리 삭제
    @DeleteMapping("/categories/{id}")
    public String deleteCategory (@PathVariable Long id) {
        categoryService.delete(id);
        return "redirect:/categories";
    }


}
