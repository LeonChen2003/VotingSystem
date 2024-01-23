package com.vote.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participate {
  private Long voteId;
  private String userId;
  private Integer choice;
}
