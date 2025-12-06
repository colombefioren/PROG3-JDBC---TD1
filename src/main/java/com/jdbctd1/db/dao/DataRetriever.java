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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    return getProductsByCriteriaWithPagination(
        productName, categoryName, creationMin, creationMax, 0, 0);
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

    StringBuilder sql =
        new StringBuilder(
            """
        SELECT DISTINCT p.id, p.name, p.price, p.creation_datetime,
               pc.id AS cat_id, pc.name AS cat_name
        FROM Product p
        LEFT JOIN Product_category pc ON p.id = pc.product_id
        WHERE 1=1
        """);

    List<Object> parameters = new ArrayList<>();

    if (productName != null && !productName.isBlank()) {
      sql.append(" AND p.name ILIKE ?");
      parameters.add("%" + productName + "%");
    }

    if (categoryName != null && !categoryName.isBlank()) {
      sql.append(" AND pc.name ILIKE ?");
      parameters.add("%" + categoryName + "%");
    }

    if (creationMin != null) {
      sql.append(" AND p.creation_datetime > ?");
      parameters.add(Timestamp.from(creationMin));
    }

    if (creationMax != null) {
      sql.append(" AND p.creation_datetime < ?");
      parameters.add(Timestamp.from(creationMax));
    }

    sql.append(" ORDER BY p.id");

    if (size > 0) {
      int offset = (page - 1) * size;
      sql.append(" LIMIT ? OFFSET ?");
      parameters.add(size);
      parameters.add(offset);
    }

    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql.toString())) {

      for (int i = 0; i < parameters.size(); i++) {
        ps.setObject(i + 1, parameters.get(i));
      }

      try (ResultSet rs = ps.executeQuery()) {
        return mapProductsFromResultSet(rs);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Product> mapProductsFromResultSet(ResultSet rs) throws SQLException {
    Map<Integer, Product> productMap = new LinkedHashMap<>();

    while (rs.next()) {
      int productId = rs.getInt("id");

      Product product =
          productMap.computeIfAbsent(
              productId,
              id -> {
                try {
                  Product p = new Product();
                  p.setId(id);
                  p.setName(rs.getString("name"));

                  Timestamp ts = rs.getTimestamp("creation_datetime");
                  if (ts != null) {
                    p.setCreationDatetime(ts.toInstant());
                  }

                  return p;
                } catch (SQLException e) {
                  throw new RuntimeException(e);
                }
              });

      int catId = rs.getInt("cat_id");
      if (!rs.wasNull()) {
        product.setCategory(new Category(catId, rs.getString("cat_name")));
      }
    }

    return new ArrayList<>(productMap.values());
  }
}
