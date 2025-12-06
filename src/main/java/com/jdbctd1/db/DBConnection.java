package com.jdbctd1.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
  public static final String JDBC_URL = "jdbc:postgresql://localhost:5433/product_management_db";
  public static final String DB_USER = "product_manager_user";
  public static final String DB_PASSWORD = "123456";

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
  }
}
