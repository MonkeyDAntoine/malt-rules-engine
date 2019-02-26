package fr.malt.feesrulesengine.service.rule.duration;

import com.fasterxml.jackson.databind.JsonNode;
import fr.malt.feesrulesengine.model.Contract;
import fr.malt.feesrulesengine.service.rule.Rule;
import fr.malt.feesrulesengine.utils.TimeString;
import org.springframework.stereotype.Component;

@Component("@mission.duration")
public class MissionDuration implements Rule {

  @Override
  public boolean evaluate(Contract contract, JsonNode node) {
    return contract.getMission().toDays()
        > TimeString.parseTimestring(node.get("gt").asText(), TimeString.Unit.DAY);
  }
}
