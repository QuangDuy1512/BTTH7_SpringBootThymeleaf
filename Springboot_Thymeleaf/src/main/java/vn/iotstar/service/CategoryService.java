package vn.iotstar.service;

import org.springframework.data.domain.Page;
import vn.iotstar.entity.Category;

public interface CategoryService {
    Page<Category> listAll(String keyword, int page, int size);
    Category save(Category category);
    Category get(Long id);
    void delete(Long id);
}
