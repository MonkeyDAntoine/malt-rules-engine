package fr.malt.feesrulesengine.service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeeResult {
  private double fee = 10D;
  private String rule = "Default fee rule";
}
