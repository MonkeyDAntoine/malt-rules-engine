package fr.malt.feesrulesengine.service.rule.logical;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.malt.feesrulesengine.model.Contract;
import fr.malt.feesrulesengine.service.RulesService;
import fr.malt.feesrulesengine.service.rule.Rule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

@Component("@or")
@RequiredArgsConstructor
public class RuleOr implements Rule {
  private final RulesService rulesService;

  @Override
  public boolean evaluate(Contract contract, JsonNode ruleNode) {
    if (ruleNode.isArray()) {
      ArrayNode rules = (ArrayNode) ruleNode;
      for (JsonNode rule : rules) {
        if (rule.isObject()) {
          ObjectNode r = (ObjectNode) rule;
          Iterator<Map.Entry<String, JsonNode>> fields = r.fields();
          while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            if (rulesService.rule(field.getKey()).evaluate(contract, field.getValue())) {
              return true;
            }
          }
          return false;
        }
      }
    }
    return false;
  }
}
