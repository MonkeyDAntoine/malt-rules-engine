package fr.malt.feesrulesengine.service.rule.location;

import com.fasterxml.jackson.databind.JsonNode;
import fr.malt.feesrulesengine.model.Contract;
import fr.malt.feesrulesengine.provider.ipstack.IPStackClient;
import fr.malt.feesrulesengine.service.rule.Rule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("@freelancer.location")
@RequiredArgsConstructor
public class FreelancerLocation implements Rule {

  private final IPStackClient ipStackClient;

  @Override
  public boolean evaluate(Contract contract, JsonNode node) {
    return node.get("country")
        .asText()
        .equals(ipStackClient.locate(contract.getFreelance()).getCountryCode());
  }
}
