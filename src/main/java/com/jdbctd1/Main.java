package com.jdbctd1;

import com.jdbctd1.db.dao.DataRetriever;
import com.jdbctd1.model.Category;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    DataRetriever dataRetriever = new DataRetriever();

    // a) List<Category> getAllCategories()
    System.out.println("=== TEST 1: getAllCategories() ===");
    List<Category> categories = dataRetriever.getAllCategories();
    System.out.println("Total categories: " + categories.size());
    categories.forEach(cat -> System.out.println("  - " + cat));

    // b) List<Product> getProductList (int page, int size
    System.out.println("\n=== TEST 2: getProductList() ===");
    System.out.println("Page 1, Size 3:");
    dataRetriever.getProductList(1, 3).forEach(p -> System.out.println("  - " + p));

    System.out.println("\nPage 2, Size 2:");
    dataRetriever.getProductList(2, 2).forEach(p -> System.out.println("  - " + p));
  }
}
