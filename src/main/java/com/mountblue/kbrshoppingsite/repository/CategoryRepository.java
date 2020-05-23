package com.mountblue.kbrshoppingsite.repository;

import com.mountblue.kbrshoppingsite.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findCategoryById(Long categoryId);

    Category findCategoryByCategoryName(String name);

    Optional<Category> findById(Long id);

    Category findByCategoryName(String Name);

}
