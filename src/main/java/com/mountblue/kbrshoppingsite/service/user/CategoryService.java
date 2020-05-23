package com.mountblue.kbrshoppingsite.service.user;

import com.mountblue.kbrshoppingsite.model.Category;
import com.mountblue.kbrshoppingsite.repository.CategoryRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public void save(Category category) throws Exception {
        if (categoryRepository.findByCategoryName(category.getCategoryName()) != null){
            throw new Exception("Category already exist");
        }
            categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(long id) throws NotFoundException {
        if (categoryRepository.findById(id).isPresent()){
            return categoryRepository.findById(id).get();
        }
        throw new NotFoundException("Category Not found");
    }
}
