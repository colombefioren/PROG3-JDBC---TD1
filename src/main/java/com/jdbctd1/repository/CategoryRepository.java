package com.jdbctd1.repository;

import com.jdbctd1.model.Category;
import java.util.List;

public interface CategoryRepository {
  List<Category> getAllCategories();
}
