package com.jdbctd1.repository;

import com.jdbctd1.model.Product;
import java.time.Instant;
import java.util.List;

public interface ProductRepository {
  List<Product> getProductList(int page, int size);

  List<Product> getProductsByCriteria(
      String productName, String categoryName, Instant creationMin, Instant creationMax);

  List<Product> getProductsByCriteria(
      String productName,
      String categoryName,
      Instant creationMin,
      Instant creationMax,
      int page,
      int size);
}
