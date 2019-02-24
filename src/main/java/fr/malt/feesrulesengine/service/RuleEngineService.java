package fr.malt.feesrulesengine.service;

import com.sabre.oss.yare.core.RuleSession;
import com.sabre.oss.yare.core.RulesEngine;
import com.sabre.oss.yare.core.RulesEngineBuilder;
import fr.malt.feesrulesengine.model.Contract;
import fr.malt.feesrulesengine.model.FeeRule;
import fr.malt.feesrulesengine.repository.FeeRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;

import static java.util.Collections.singleton;

@Service
@RequiredArgsConstructor
@Validated
public class RuleEngineService {
  private final RulesEngineBuilder rulesEngineBuilder;
  private final FeeRuleRepository ruleRepository;

  public FeeRule addRule(final FeeRule rule) {
    return ruleRepository.save(rule);
  }

  public FeeResult compute(final @Valid Contract contract) {
    RulesEngine engine = rulesEngineBuilder.build();
    RuleSession ruleSession = engine.createSession("ruleSet");
    FeeResult result = new FeeResult();
    ruleSession.execute(result, singleton(contract));
    return result;
  }

  public Optional<FeeRule> getRule(String ruleId) {
    return ruleRepository.findById(ruleId);
  }

  public Page<FeeRule> list(Pageable pageable) {
    return ruleRepository.findAll(pageable);
  }
}
