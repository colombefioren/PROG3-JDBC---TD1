package com.jdbctd1.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {

  public Instant toInstant(int year, int month, int day) {
    return LocalDateTime.of(year, month, day, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant();
  }

  public Instant toInstant(int year, int month, int day, int hour, int minute) {
    return LocalDateTime.of(year, month, day, hour, minute, 0)
        .atZone(ZoneId.systemDefault())
        .toInstant();
  }

  public Instant toInstant(int year, int month, int day, int hour, int minute, int second) {
    return LocalDateTime.of(year, month, day, hour, minute, second)
        .atZone(ZoneId.systemDefault())
        .toInstant();
  }
}
