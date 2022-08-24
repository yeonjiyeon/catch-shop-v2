package springboot.catchshop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static javax.persistence.FetchType.LAZY;
/**
 * Category 기능
 * author:김지연
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")//카테고리 번호
    private Long id;


    @Column(name = "category_nm")//카테고리명
    private String name;


    @OneToMany(mappedBy = "categories", cascade = CascadeType.ALL)//상품 번호
    private List<Product> products = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();



    public Category(String name, Category parent){
        this.name = name;
        this.parent = parent;

    }

    //==연관관계 메서드==//
    public void addChildCategory(Category children) {
        this.children.add(children);
        children.changeParent(this);
    }
    public void changeParent(Category parentCategory) {
        this.parent = parentCategory;
    }

    public void changeName(String categoryName) {
        this.name = categoryName;
    }
}
