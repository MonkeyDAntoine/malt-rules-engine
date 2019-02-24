package fr.malt.feesrulesengine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class Contractor {

  @JsonProperty("ip")
  @NotBlank
  @Pattern(regexp = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")
  private String ip;
}
