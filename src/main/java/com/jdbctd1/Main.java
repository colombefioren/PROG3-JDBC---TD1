package com.jdbctd1;

import com.jdbctd1.db.dao.DataRetriever;

public class Main {
  public static void main(String[] args) {
    DataRetriever data = new DataRetriever();
    System.out.println(data.getAllCategories());
  }
}
