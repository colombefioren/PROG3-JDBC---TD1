package com.jdbctd1.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.jdbctd1.model.Category;
import com.jdbctd1.model.Product;
import com.jdbctd1.util.DateUtils;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("DataRetriever Integration Tests")
class DataRetrieverTest {

  private DataRetriever dataRetriever;

  @BeforeEach
  void setUp() {
    dataRetriever = new DataRetriever();
  }

  // =========== TEST 1: getAllCategories() ===========

  @Test
  @DisplayName("TEST 1 - Should retrieve all 7 categories")
  void testGetAllCategories() {
    List<Category> categories = dataRetriever.getAllCategories();

    assertNotNull(categories);
    assertEquals(7, categories.size(), "Should have 7 categories");
  }

  // =========== TEST 2: getProductList() ===========

  @ParameterizedTest(name = "Page {0}, Size {1} should return {2} products")
  @CsvSource({
    "1, 10, 7", // scenario 1: all products
    "1, 5, 5", // scenario 2: first five products
    "1, 3, 3", // scenario 3: first three products
    "2, 2, 2", // scenario 4: the second pair of products
  })
  @DisplayName("TEST 2 - Should paginate products correctly for all scenarios")
  void testGetProductListPagination(int page, int size, int expectedCount) {
    List<Product> products = dataRetriever.getProductList(page, size);

    assertNotNull(products);
    assertEquals(expectedCount, products.size());

    products.forEach(
        product -> {
          assertNotNull(product.getCategory());
          assertNotNull(product.getCategory().getName());
          assertNotNull(product.getCreationDatetime());
        });
  }

  // =========== TEST 3: getProductsByCriteria() without pagination ===========

  @Test
  @DisplayName("TEST 3 - Scenario 1 - Should filter by product name (Dell)")
  void testGetProductsByCriteria_Scenario1_Dell() {
    List<Product> products = dataRetriever.getProductsByCriteria("Dell", null, null, null);
    assertEquals(1, products.size(), "Should find 1 Dell product");
    Product product = products.getFirst();
    assertTrue(product.getName().contains("Dell"));
    assertEquals("Informatique", product.getCategory().getName());
  }

  @Test
  @DisplayName("TEST 3 - Scenario 2 - Should filter by category name (info)")
  void testGetProductsByCriteria_Scenario2_CategoryInfo() {
    List<Product> products = dataRetriever.getProductsByCriteria(null, "info", null, null);

    assertEquals(2, products.size(), "Should find 2 products in 'info' category (Informatique)");
    products.forEach(
        product -> assertTrue(product.getCategory().getName().toLowerCase().contains("info")));
  }

  @Test
  @DisplayName(
      "TEST 3 - Scenario 3 - Should filter by product name and category name (iPhone + mobile)")
  void testGetProductsByCriteria_Scenario3_iPhoneMobile() {
    List<Product> products = dataRetriever.getProductsByCriteria("iPhone", "mobile", null, null);

    assertEquals(1, products.size(), "Should find iPhone 13 with Mobile category");
    Product product = products.getFirst();
    assertEquals("iPhone 13", product.getName());
    assertEquals("Mobile", product.getCategory().getName());
  }

  @Test
  @DisplayName("TEST 3 - Scenario 4 - Should filter by date range (2024-02-01 to 2024-03-01)")
  void testGetProductsByCriteria_Scenario4_DateRange() {
    DateUtils dateUtils = new DateUtils();
    Instant minDate = dateUtils.toInstant(2024, 2, 1);
    Instant maxDate = dateUtils.toInstant(2024, 3, 1, 23, 59, 59);
    List<Product> products = dataRetriever.getProductsByCriteria(null, null, minDate, maxDate);

    // should find: iPhone 13 (2024-02-01) and Casque Sony (2024-02-10)
    // a product can happen more than once but with a different category
    assertTrue(products.size() >= 2, "Should find at least iPhone 13 and Sony headset");

    for (Product product : products) {
      assertTrue(
          product.getCreationDatetime().isAfter(minDate)
              || product.getCreationDatetime().equals(minDate));
      assertTrue(
          product.getCreationDatetime().isBefore(maxDate)
              || product.getCreationDatetime().equals(maxDate));
    }
  }

  @Test
  @DisplayName(
      "TEST 3 - Scenario 5 - Should filter by product name and category (Samsung + bureau)")
  void testGetProductsByCriteria_Scenario5_SamsungBureau() {
    List<Product> products = dataRetriever.getProductsByCriteria("Samsung", "bureau", null, null);

    assertEquals(1, products.size(), "Should find Samsung screen with Bureau category");
    Product product = products.getFirst();
    assertTrue(product.getName().contains("Samsung"));
    assertEquals("Bureau", product.getCategory().getName());
  }

  @Test
  @DisplayName("TEST 3 - Scenario 6 - Should return empty when no match (Sony + informatique)")
  void testGetProductsByCriteria_Scenario6_SonyInformatique() {
    List<Product> products =
        dataRetriever.getProductsByCriteria("Sony", "informatique", null, null);

    assertTrue(products.isEmpty(), "Should return empty list for Sony with Informatique category");
  }

  @Test
  @DisplayName("TEST 3 - Scenario 7 - Should filter by category and date range (audio + 2024 year)")
  void testGetProductsByCriteria_Scenario7_AudioDateRange() {
    DateUtils dateUtils = new DateUtils();
    Instant minDate = dateUtils.toInstant(2024, 1, 1);
    Instant maxDate = dateUtils.toInstant(2024, 12, 1, 23, 59, 59);

    List<Product> products = dataRetriever.getProductsByCriteria(null, "audio", minDate, maxDate);

    assertEquals(1, products.size(), "Should find Sony headset with Audio category");
    Product product = products.getFirst();
    assertTrue(product.getName().contains("Sony"));
    assertEquals("Audio", product.getCategory().getName());

    assertTrue(
        product.getCreationDatetime().isAfter(minDate)
            || product.getCreationDatetime().equals(minDate));
    assertTrue(
        product.getCreationDatetime().isBefore(maxDate)
            || product.getCreationDatetime().equals(maxDate));
  }

  @Test
  @DisplayName("TEST 3 - Scenario 8 - Should return all products when all criteria are null")
  void testGetProductsByCriteria_Scenario8_AllNull() {
    List<Product> products = dataRetriever.getProductsByCriteria(null, null, null, null);
    assertEquals(7, products.size(), "Should return all 7 product-category entries");

    products.forEach(
        product -> {
          assertNotNull(product.getCategory());
          assertNotNull(product.getCategory().getName());
        });
  }

  // =========== TEST 4: getProductsByCriteria() with pagination ===========

  @Test
  @DisplayName("TEST 4 - Scenario 1 - Should return all products paginated (page 1, size 10)")
  void testGetProductsByCriteriaWithPagination_Scenario1_AllProducts() {
    List<Product> products = dataRetriever.getProductsByCriteria(null, null, null, null, 1, 10);
    assertEquals(7, products.size(), "Should return all 7 product-category entries");
  }

  @Test
  @DisplayName("TEST 4 - Scenario 2 - Should filter and paginate (Dell, page 1, size 5)")
  void testGetProductsByCriteriaWithPagination_Scenario2_Dell() {
    List<Product> products = dataRetriever.getProductsByCriteria("Dell", null, null, null, 1, 5);
    assertEquals(1, products.size(), "Should find 1 Dell product");
    assertEquals("Laptop Dell XPS", products.getFirst().getName());
  }

  @Test
  @DisplayName(
      "TEST 4 - Scenario 3 - Should filter by category and paginate (informatique, page 1, size 10)")
  void testGetProductsByCriteriaWithPagination_Scenario3_Informatique() {
    List<Product> products =
        dataRetriever.getProductsByCriteria(null, "informatique", null, null, 1, 10);
    assertEquals(2, products.size(), "Should find 2 products with Informatique category");
    products.forEach(product -> assertEquals("Informatique", product.getCategory().getName()));

    boolean hasDell = products.stream().anyMatch(p -> p.getName().contains("Dell"));
    boolean hasSamsung = products.stream().anyMatch(p -> p.getName().contains("Samsung"));
    assertTrue(hasDell && hasSamsung, "Should contain Dell and Samsung products");
  }

  // =========== ADDITIONAL TESTS ===========

  @Test
  @DisplayName("TEST 5 - Should handle empty string criteria")
  void testGetProductsByCriteria_EmptyStrings() {
    List<Product> products1 = dataRetriever.getProductsByCriteria("", null, null, null);
    List<Product> products2 = dataRetriever.getProductsByCriteria(null, "", null, null);

    // an empty string should be treated as null and thus no filter
    assertEquals(7, products1.size(), "Empty product name should return all");
    assertEquals(7, products2.size(), "Empty category name should return all");
  }

  @Test
  @DisplayName("TEST 5 - Should be case-insensitive with ILIKE")
  void testGetProductsByCriteria_CaseInsensitive() {
    List<Product> products1 = dataRetriever.getProductsByCriteria("DELL", null, null, null);
    List<Product> products2 = dataRetriever.getProductsByCriteria("dell", null, null, null);
    List<Product> products3 = dataRetriever.getProductsByCriteria("DelL", null, null, null);
    assertEquals(1, products1.size());
    assertEquals(1, products2.size());
    assertEquals(1, products3.size());
  }

  @Test
  @DisplayName("TEST 5 - Should handle partial matches with ILIKE")
  void testGetProductsByCriteria_PartialMatch() {
    List<Product> products1 = dataRetriever.getProductsByCriteria("lap", null, null, null);
    assertFalse(products1.isEmpty());
    assertTrue(products1.getFirst().getName().toLowerCase().contains("lap"));

    List<Product> products2 = dataRetriever.getProductsByCriteria(null, "form", null, null);
    assertEquals(2, products2.size());
  }

  @Test
  @DisplayName("TEST 5 - Should return empty list for non-existent criteria")
  void testGetProductsByCriteria_NonExistent() {
    List<Product> products =
        dataRetriever.getProductsByCriteria(
            "NonExistentProduct", "NonExistentCategory", null, null);
    assertTrue(products.isEmpty());
  }
}
