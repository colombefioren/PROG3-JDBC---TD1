package com.jdbctd1.model;

import java.time.Instant;
import java.util.Objects;

public class Product {

  private int id;
  private String name;
  private Instant creationDatetime;
  private Category category;

  public Product(int id, String name, Instant creationDatetime, Category category) {
    this.id = id;
    this.name = name;
    this.creationDatetime = creationDatetime;
    this.category = category;
  }

  public Product() {}

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Instant getCreationDatetime() {
    return creationDatetime;
  }

  public Category getCategory() {
    return category;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCreationDatetime(Instant creationDatetime) {
    this.creationDatetime = creationDatetime;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public String getCategoryName() {
    return this.category.getName();
  }

  @Override
  public String toString() {
    return name + " | (ID: " + id + " | " + creationDatetime + ") | CATEGORY: " + category;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return id == product.id
        && Objects.equals(name, product.name)
        && Objects.equals(creationDatetime, product.creationDatetime)
        && Objects.equals(category, product.category);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, creationDatetime, category);
  }
}
