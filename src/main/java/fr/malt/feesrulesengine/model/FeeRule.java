package fr.malt.feesrulesengine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class FeeRule implements Serializable {

  @JsonProperty("id")
  @NotBlank
  @Id
  private String id;

  @JsonProperty("name")
  @NotBlank
  private String name;

  @JsonProperty("rate")
  @NotNull
  private Rate rate = new Rate();

  @JsonProperty("restrictions")
  @NotNull
  private ObjectNode restrictions;

  @Data
  @NoArgsConstructor
  public static class Rate {
    @NotNull
    @Min(0)
    @Max(100)
    @JsonProperty("percent")
    private Double percent;
  }
}
