package com.jdbctd1;

import com.jdbctd1.model.Category;
import com.jdbctd1.repository.DataRetriever;
import com.jdbctd1.util.DateUtils;
import java.time.Instant;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    DataRetriever dataRetriever = new DataRetriever();
    DateUtils dateUtils = new DateUtils();
    // a) List<Category> getAllCategories()
    System.out.println("/////===> TEST 1: getAllCategories() <===/////");
    List<Category> categories = dataRetriever.getAllCategories();
    System.out.println("Total categories: " + categories.size());
    categories.forEach(cat -> System.out.println("  - " + cat));

    // b) List<Product> getProductList (int page, int size)
    System.out.println("\n/////===> TEST 2: getProductList() <===/////");

    System.out.println("Scenario 1 - Page 1, Size 10:");
    dataRetriever.getProductList(1, 10).forEach(p -> System.out.println("  - " + p));

    System.out.println("\nScenario 2 - Page 1, Size 5:");
    dataRetriever.getProductList(1, 5).forEach(p -> System.out.println("  - " + p));

    System.out.println("\nScenario 3 - Page 1, Size 3:");
    dataRetriever.getProductList(1, 3).forEach(p -> System.out.println("  - " + p));

    System.out.println("\nScenario 4 - Page 2, Size 2:");
    dataRetriever.getProductList(2, 2).forEach(p -> System.out.println("  - " + p));

    // c) List<Product> getProductsByCriteria(String productName, String categoryName, Instant
    // creationMin, instant creationMax)
    System.out.println("\n/////===> TEST 3: getProductsByCriteria() <===/////");

    System.out.println(
        "Scenario 1 - productName='Dell': "
            + dataRetriever.getProductsByCriteria("Dell", null, null, null).size()
            + " products");

    System.out.println(
        "Scenario 2 - categoryName='info': "
            + dataRetriever.getProductsByCriteria(null, "info", null, null).size()
            + " products");

    System.out.println(
        "Scenario 3 - productName='iPhone', categoryName='mobile': "
            + dataRetriever.getProductsByCriteria("iPhone", "mobile", null, null).size()
            + " products");

    Instant minDate1 = dateUtils.toInstant(2024, 2, 1);
    Instant maxDate1 = dateUtils.toInstant(2024, 3, 1);
    System.out.println(
        "Scenario 4 - date range 2024-02-01 to 2024-03-01: "
            + dataRetriever.getProductsByCriteria(null, null, minDate1, maxDate1).size()
            + " products");

    System.out.println(
        "Scenario 5 - productName='Samsung', categoryName='bureau': "
            + dataRetriever.getProductsByCriteria("Samsung", "bureau", null, null).size()
            + " products");

    System.out.println(
        "Scenario 6 - productName='Sony', categoryName='informatique': "
            + dataRetriever.getProductsByCriteria("Sony", "informatique", null, null).size()
            + " products");

    Instant minDate2 = dateUtils.toInstant(2024, 1, 1);
    Instant maxDate2 = dateUtils.toInstant(2024, 12, 1);
    System.out.println(
        "Scenario 7 - categoryName='audio', date range 2024-01-01 to 2024-12-01: "
            + dataRetriever.getProductsByCriteria(null, "audio", minDate2, maxDate2).size()
            + " products");

    System.out.println(
        "Scenario 8 - all null (all products): "
            + dataRetriever.getProductsByCriteria(null, null, null, null).size()
            + " products");

    // d) List<Product> getProductsByCriteria(String productName, String
    // categoryName, Instant creationMin, instant creationMax, int page, int size)
    System.out.println("\n/////===> TEST 4: getProductsByCriteria() with paginations <===/////");

    System.out.println(
        "Scenario 1 - All products, page 1, size 10: "
            + dataRetriever.getProductsByCriteria(null, null, null, null, 1, 10).size()
            + " products");

    System.out.println(
        "Scenario 2 - productName='Dell', page 1, size 5: "
            + dataRetriever.getProductsByCriteria("Dell", null, null, null, 1, 5).size()
            + " products");

    System.out.println(
        "Scenario 3 - categoryName='informatique', page 1, size 10: "
            + dataRetriever.getProductsByCriteria(null, "informatique", null, null, 1, 10).size()
            + " products");
  }
}
