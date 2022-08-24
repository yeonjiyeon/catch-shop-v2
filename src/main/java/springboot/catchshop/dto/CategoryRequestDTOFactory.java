package springboot.catchshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class CategoryRequestDTOFactory {
    public static CategoryRequestDTO categoryRequestDTO(){
        return new CategoryRequestDTO("category", null);
    }

    public static CategoryRequestDTO categoryRequestDTO(String name){
        return new CategoryRequestDTO(name, null);
    }
}
