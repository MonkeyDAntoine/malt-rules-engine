package fr.malt.feesrulesengine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
public class CommercialRelation implements DurationComparable {

  @JsonProperty("firstmission")
  private LocalDate firstMission;

  @JsonProperty("last_mission")
  private LocalDate lastMission;

  @Override
  public long toDays() {
    return firstMission.until(lastMission, ChronoUnit.DAYS);
  }
}
