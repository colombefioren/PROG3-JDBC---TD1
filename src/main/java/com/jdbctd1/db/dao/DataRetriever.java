package com.jdbctd1.db.dao;

import com.jdbctd1.db.DBConnection;
import com.jdbctd1.db.dao.repository.CategoryRepository;
import com.jdbctd1.db.dao.repository.ProductRepository;
import com.jdbctd1.model.Category;
import com.jdbctd1.model.Product;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever implements ProductRepository, CategoryRepository {

  @Override
  public List<Category> getAllCategories() {
    String sql =
        """
        SELECT id, name
        FROM Product_category
        ORDER BY id
        """;

    List<Category> result = new ArrayList<>();

    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        Category category = new Category(rs.getInt("id"), rs.getString("name"));
        result.add(category);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return result;
  }

  @Override
  public List<Product> getProductList(int page, int size) {
    String sql =
        """
        SELECT p.id, p.name, p.price, p.creation_datetime,
               pc.id AS cat_id, pc.name AS cat_name
        FROM Product p
        LEFT JOIN Product_category pc ON p.id = pc.product_id
        ORDER BY p.id
        LIMIT ? OFFSET ?
        """;

    int offset = (page - 1) * size;
    List<Product> result = new ArrayList<>();

    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setInt(1, size);
      ps.setInt(2, offset);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Product product = new Product();
          product.setId(rs.getInt("id"));
          product.setName(rs.getString("name"));
          product.setCreationDatetime(
              rs.getTimestamp("creation_datetime")
                  .toLocalDateTime()
                  .atZone(ZoneId.systemDefault())
                  .toInstant());

          Category category = new Category();
          category.setId(rs.getInt("cat_id"));
          category.setName(rs.getString("cat_name"));

          product.setCategory(category);

          result.add(product);
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return result;
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
