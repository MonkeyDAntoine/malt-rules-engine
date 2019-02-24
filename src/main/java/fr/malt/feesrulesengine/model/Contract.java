package fr.malt.feesrulesengine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class Contract {
  @JsonProperty("mission")
  @NotNull
  private Mission mission = new Mission();

  @JsonProperty("client")
  @NotNull
  private Contractor client = new Contractor();

  @JsonProperty("freelance")
  @NotNull
  private Contractor freelance = new Contractor();

  @JsonProperty("commercialrelation")
  @NotNull
  private CommercialRelation commercialRelation = new CommercialRelation();
}
