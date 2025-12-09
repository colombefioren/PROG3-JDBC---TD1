package com.jdbctd1.repository;

import com.jdbctd1.db.DBConnection;
import com.jdbctd1.model.Category;
import com.jdbctd1.model.Product;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever implements ProductRepository, CategoryRepository {

  DBConnection dbConnection = new DBConnection();

  @Override
  public List<Category> getAllCategories() {
    String sql =
        """
        SELECT id, name
        FROM Product_category
        ORDER BY id
        """;

    List<Category> result = new ArrayList<>();

    try (Connection con = dbConnection.getDBConnection();
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

    try (Connection con = dbConnection.getDBConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {

      ps.setInt(1, size);
      ps.setInt(2, offset);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          result.add(createProductFromResultSet(rs));
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
    return getProductsByCriteriaWithPagination(
        productName, categoryName, creationMin, creationMax, 1, 0);
  }

  @Override
  public List<Product> getProductsByCriteria(
      String productName,
      String categoryName,
      Instant creationMin,
      Instant creationMax,
      int page,
      int size) {
    return getProductsByCriteriaWithPagination(
        productName, categoryName, creationMin, creationMax, page, size);
  }

  private List<Product> getProductsByCriteriaWithPagination(
      String productName,
      String categoryName,
      Instant creationMin,
      Instant creationMax,
      int page,
      int size) {

    String sql =
        "SELECT p.id, p.name, p.price, p.creation_datetime, "
            + "pc.id AS cat_id, pc.name AS cat_name "
            + "FROM Product p "
            + "LEFT JOIN Product_category pc ON p.id = pc.product_id";

    List<Object> params = new ArrayList<>();

    boolean hasWhere = false;

    if (productName != null && !productName.isBlank()) {
      sql += " WHERE p.name ILIKE ?";
      params.add("%" + productName + "%");
      hasWhere = true;
    }

    if (categoryName != null && !categoryName.isBlank()) {
      if (hasWhere) {
        sql += " AND pc.name ILIKE ?";
      } else {
        sql += " WHERE pc.name ILIKE ?";
        hasWhere = true;
      }
      params.add("%" + categoryName + "%");
    }

    if (creationMin != null) {
      if (hasWhere) {
        sql += " AND p.creation_datetime >= ?";
      } else {
        sql += " WHERE p.creation_datetime >= ?";
        hasWhere = true;
      }
      params.add(Timestamp.from(creationMin));
    }

    if (creationMax != null) {
      if (hasWhere) {
        sql += " AND p.creation_datetime <= ?";
      } else {
        sql += " WHERE p.creation_datetime <= ?";
        hasWhere = true;
      }
      params.add(Timestamp.from(creationMax));
    }

    sql += " ORDER BY p.id";

    if (size > 0) {
      int offset = (page - 1) * size;
      sql += " LIMIT ? OFFSET ?";
      params.add(size);
      params.add(offset);
    }

    try (Connection con = dbConnection.getDBConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {

      for (int i = 0; i < params.size(); i++) {
        ps.setObject(i + 1, params.get(i));
      }

      ResultSet rs = ps.executeQuery();
      List<Product> result = new ArrayList<>();

      while (rs.next()) {
        result.add(createProductFromResultSet(rs));
      }

      return result;

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private Product createProductFromResultSet(ResultSet rs) throws SQLException {
    Product product = new Product();
    product.setId(rs.getInt("id"));
    product.setName(rs.getString("name"));

    Timestamp timestamp = rs.getTimestamp("creation_datetime");
    if (timestamp != null) {
      product.setCreationDatetime(timestamp.toInstant());
    }

    int catId = rs.getInt("cat_id");
    if (!rs.wasNull()) {
      product.setCategory(new Category(catId, rs.getString("cat_name")));
    }

    return product;
  }
}
