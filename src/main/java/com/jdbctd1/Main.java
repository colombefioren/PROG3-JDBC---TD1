package com.jdbctd1;

import com.jdbctd1.db.dao.DataRetriever;
import com.jdbctd1.model.Category;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    DataRetriever dataRetriever = new DataRetriever();

    // a) List<Category> getAllCategories()
    System.out.println("=== TEST 1: getAllCategories() ===");
    List<Category> categories = dataRetriever.getAllCategories();
    System.out.println("Total categories: " + categories.size());
    categories.forEach(cat -> System.out.println("  - " + cat));

    // b) List<Product> getProductList (int page, int size)
    System.out.println("\n=== TEST 2: getProductList() ===");
    System.out.println("Page 1, Size 3:");
    dataRetriever.getProductList(1, 3).forEach(p -> System.out.println("  - " + p));

    System.out.println("\nPage 2, Size 2:");
    dataRetriever.getProductList(2, 2).forEach(p -> System.out.println("  - " + p));

    // c) List<Product> getProductsByCriteria(String productName, String categoryName, Instant
    // creationMin, instant creationMax)
    System.out.println("\n=== TEST 3: getProductsByCriteria() ===");
    Instant minDate = toInstant(2024, 2, 1);
    Instant maxDate = toInstant(2024, 3, 1, 23, 59);

    System.out.println(
        "a) productName='Dell': "
            + dataRetriever.getProductsByCriteria("Dell", null, null, null).size()
            + " products");

    System.out.println(
        "b) categoryName='info': "
            + dataRetriever.getProductsByCriteria(null, "info", null, null).size()
            + " products");

    System.out.println(
        "c) productName='iPhone', categoryName='mobile': "
            + dataRetriever.getProductsByCriteria("iPhone", "mobile", null, null).size()
            + " products");

    System.out.println(
        "d) date range 2024-02-01 to 2024-03-01: "
            + dataRetriever.getProductsByCriteria(null, null, minDate, maxDate).size()
            + " products");

    // d) List<Product> getProductsByCriteria(String productName, String
    // categoryName, Instant creationMin, instant creationMax, int page, int size)
    System.out.println("\n=== TEST 4: getProductsByCriteria() WITH PAGINATION ===");
    System.out.println(
        "a) All products, page 1, size 10: "
            + dataRetriever.getProductsByCriteria(null, null, null, null, 1, 10).size()
            + " products");

    System.out.println(
        "b) productName='Dell', page 1, size 5: "
            + dataRetriever.getProductsByCriteria("Dell", null, null, null, 1, 5).size()
            + " products");

    System.out.println(
        "c) categoryName='informatique', page 1, size 10: "
            + dataRetriever.getProductsByCriteria(null, "informatique", null, null, 1, 10).size()
            + " products");
  }

  private static Instant toInstant(int year, int month, int day) {
    return LocalDateTime.of(year, month, day, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant();
  }

  private static Instant toInstant(int year, int month, int day, int hour, int minute) {
    return LocalDateTime.of(year, month, day, hour, minute, 0)
        .atZone(ZoneId.systemDefault())
        .toInstant();
  }
}
