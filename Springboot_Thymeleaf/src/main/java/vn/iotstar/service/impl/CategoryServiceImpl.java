package vn.iotstar.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import vn.iotstar.entity.Category;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService  {
	
	private final CategoryRepository repo;
	
	@Override
	public Page<Category> listAll(String keyword, int page, int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (keyword != null && !keyword.isEmpty()) {
            return repo.findByNameContainingIgnoreCase(keyword, pageable);
        }
        return repo.findAll(pageable);
	}
	@Override
    public Category save(Category category) {
        return repo.save(category);
    }

    @Override
    public Category get(Long id) {
        return repo.findById(id).orElseThrow();
    }
    
    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
