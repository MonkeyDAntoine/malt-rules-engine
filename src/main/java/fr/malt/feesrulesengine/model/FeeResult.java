package fr.malt.feesrulesengine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeResult {
  public static final FeeResult DEFAULT = new FeeResult("Default rule", 10D);
  private String rule;
  private Double fee;
}
