package fr.malt.feesrulesengine.model;

import fr.malt.feesrulesengine.utils.TimeString;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class Mission implements DurationComparable {
  @NotBlank private String duration;

  @Override
  public long toDays() {
    return TimeString.parseTimestring(duration, TimeString.Unit.DAY);
  }
}
