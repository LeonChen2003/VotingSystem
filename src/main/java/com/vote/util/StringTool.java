package com.vote.util;

public class StringTool {
  /** 按中英文逗号分隔字符串 */
  public static String[] splitByDot(String s) {
    String[] strings;
    if (s.contains(",")) {
      strings = s.split(",");
    } else {
      strings = s.split("，");
    }
    return strings;
  }
}
