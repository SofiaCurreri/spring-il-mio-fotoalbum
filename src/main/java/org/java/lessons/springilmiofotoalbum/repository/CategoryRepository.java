package org.java.lessons.springilmiofotoalbum.repository;

import org.java.lessons.springilmiofotoalbum.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
