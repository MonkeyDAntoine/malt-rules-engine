package fr.malt.feesrulesengine.service;

import com.fasterxml.jackson.databind.JsonNode;
import fr.malt.feesrulesengine.model.Contract;
import fr.malt.feesrulesengine.model.FeeResult;
import fr.malt.feesrulesengine.model.FeeRule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeeRulesService {
  private final RulesService rulesService;
  private final Map<String, FeeRule> feeRulesById = new HashMap<>();

  public FeeRule addRule(final FeeRule rule) {
    this.feeRulesById.put(rule.getId(), rule);
    return rule;
  }

  public FeeResult evaluate(Contract contract) {
    FeeRule defaultRule = new FeeRule();
    defaultRule.setName("Default rule");
    defaultRule.getRate().setPercent(10D);

    return feeRulesById
        .values()
        .stream()
        .filter(
            r -> {
              Iterator<Map.Entry<String, JsonNode>> it = r.getRestrictions().fields();
              while (it.hasNext()) {
                Map.Entry<String, JsonNode> a = it.next();
                if (!rulesService.rule(a.getKey()).evaluate(contract, a.getValue())) {
                  return false;
                }
              }
              return true;
            })
        .findFirst()
        .map(r -> new FeeResult(r.getName(), r.getRate().getPercent()))
        .orElse(FeeResult.DEFAULT);
  }

  public void deleteAll() {
    this.feeRulesById.clear();
  }

  public FeeRule getRule(String ruleId) {
    return feeRulesById.get(ruleId);
  }

  public Page<FeeRule> list(Pageable pageable) {
    return new PageImpl<>(
        feeRulesById
            .values()
            .stream()
            .skip(pageable.getOffset())
            .limit(pageable.getPageSize())
            .collect(Collectors.toList()),
        pageable,
        feeRulesById.size());
  }
}
