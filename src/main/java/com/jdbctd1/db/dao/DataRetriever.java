package com.jdbctd1.db.dao;

import com.jdbctd1.db.DBConnection;
import com.jdbctd1.db.dao.repository.CategoryRepository;
import com.jdbctd1.db.dao.repository.ProductRepository;
import com.jdbctd1.model.Category;
import com.jdbctd1.model.Product;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever implements ProductRepository, CategoryRepository {
  @Override
  public List<Category> getAllCategories() {
    List<Category> categories = new ArrayList<>();
    String sql = "SELECT id, name FROM Product_category";

    try (Connection con = DBConnection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql); ) {

      while (rs.next()) {
        categories.add(new Category(rs.getInt("id"), rs.getString("name")));
      }

      return categories;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Product> getProductList(int page, int size) {
    return List.of();
  }

  @Override
  public List<Product> getProductsByCriteria(
      String productName, String categoryName, Instant creationMin, Instant creationMax) {
    return List.of();
  }

  @Override
  public List<Product> getProductsByCriteria(
      String productName,
      String categoryName,
      Instant creationMin,
      Instant creationMax,
      int page,
      int size) {
    return List.of();
  }
}
