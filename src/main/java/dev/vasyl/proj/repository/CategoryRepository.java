package dev.vasyl.proj.repository;

import dev.vasyl.proj.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
