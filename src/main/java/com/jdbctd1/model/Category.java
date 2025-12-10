package com.jdbctd1.model;

import java.util.Objects;

public class Category {

  private int id;
  private String name;

  public Category(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public Category() {}

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name + " (ID: " + id + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Category category = (Category) o;
    return id == category.id && Objects.equals(name, category.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
