package fr.malt.feesrulesengine.service.rule.duration;

import com.fasterxml.jackson.databind.JsonNode;
import fr.malt.feesrulesengine.model.Contract;
import fr.malt.feesrulesengine.service.rule.Rule;
import fr.malt.feesrulesengine.utils.TimeString;
import org.springframework.stereotype.Component;

@Component("@commercialrelation.duration")
public class CommercialDuration implements Rule {

  @Override
  public boolean evaluate(Contract contract, JsonNode node) {
    return contract.getCommercialRelation().toDays()
        > TimeString.parseTimestring(node.get("gt").asText(), TimeString.Unit.DAY);
  }
}
