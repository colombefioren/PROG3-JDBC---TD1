package com.jdbctd1.db;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DBConnection Unit Tests")
class DBConnectionTest {

  @Test
  @DisplayName("Should create database connection successfully")
  void testGetDBConnection() {
    DBConnection dbConnection = new DBConnection();
    try {
      Connection connection = dbConnection.getDBConnection();

      assertNotNull(connection);
      assertFalse(connection.isClosed());
      assertTrue(connection.isValid(2));

      connection.close();

    } catch (SQLException e) {
      fail("Should not throw SQLException: " + e.getMessage());
    }
  }
}
