package com.jdbctd1.db.dao.repository;

import com.jdbctd1.model.Category;
import java.util.List;

public interface CategoryRepository {
  List<Category> getAllCategories();
}
