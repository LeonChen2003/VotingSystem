package com.vote.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
  private Long id;
  private String title;
  private String userId;
  private String choice;
  private LocalDate createTime;
  private Integer account;
  private Integer state;
}
