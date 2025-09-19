package vn.iotstar.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import vn.iotstar.service.CategoryService;
import vn.iotstar.entity.Category;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
	private final CategoryService service;
	
	@GetMapping
	public String listCategories(Model model,
	                             @RequestParam(defaultValue = "0") int page,
	                             @RequestParam(defaultValue = "5") int size,
	                             @RequestParam(required = false) String keyword) {
	    Page<Category> categories = service.listAll(keyword, page, size);
	    model.addAttribute("categories", categories); // giữ nguyên Page
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", categories.getTotalPages());
	    model.addAttribute("keyword", keyword);
	    return "category/list";
	}

	  @GetMapping("/new")
	    public String createCategoryForm(Model model) {
	        model.addAttribute("category", new Category());
	        return "category/form";
	    }

	  @PostMapping("/save")
	  public String saveCategory(@ModelAttribute("category") Category category,
	                             @RequestParam("fileImage") MultipartFile file) throws IOException {
	      if (!file.isEmpty()) {
	          // Lưu vào thư mục ngoài target để tránh mất khi build
	          String uploadDir = System.getProperty("user.dir") + "/uploads/";

	          Files.createDirectories(Paths.get(uploadDir));

	          String fileName = file.getOriginalFilename();
	          Path path = Paths.get(uploadDir, fileName);

	          Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

	          // Lưu đường dẫn để truy cập từ /uploads/**
	          category.setImages("/uploads/" + fileName);
	      }

	      service.save(category);
	      return "redirect:/categories";
	  }


	    @GetMapping("/edit/{id}")
	    public String editCategory(@PathVariable Long id, Model model) {
	        model.addAttribute("category", service.get(id));
	        return "category/form";
	    }

	    @GetMapping("/delete/{id}")
	    public String deleteCategory(@PathVariable Long id) {
	        service.delete(id);
	        return "redirect:/categories";
	    }
}
