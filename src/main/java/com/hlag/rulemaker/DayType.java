package com.hlag.rulemaker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public enum DayType {

  CALENDAR_DAYS("CALENDAR_DAYS"),

  BUSINESS_DAYS("BUSINESS_DAYS");

  private final String value;

  public static DayType fromValue(String text) {
    for (DayType dayType : DayType.values()) {
      if (String.valueOf(dayType.value).equals(text)) {
        return dayType;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + text + "'");
  }
}
