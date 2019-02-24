package fr.malt.feesrulesengine.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sabre.oss.yare.core.RulesEngineBuilder;
import com.sabre.oss.yare.core.model.Rule;
import com.sabre.oss.yare.dsl.RuleDsl;
import com.sabre.oss.yare.serializer.json.RuleToJsonConverter;
import fr.malt.feesrulesengine.model.Contract;
import fr.malt.feesrulesengine.model.FeeRule;
import fr.malt.feesrulesengine.provider.yare.RulesEngineComponent;
import fr.malt.feesrulesengine.provider.yare.action.FeeCollector;
import fr.malt.feesrulesengine.repository.FeeRuleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.sabre.oss.yare.dsl.RuleDsl.*;

@Configuration
public class RuleEngineConfiguration {
  private static final RuleToJsonConverter RULE_CONVERTER = new RuleToJsonConverter();
  private static final ObjectMapper OBJECT_MAPPER = RuleToJsonConverter.createObjectMapper();

  @Bean
  public RulesEngineBuilder engine(
      final FeeRuleRepository repository, final List<RulesEngineComponent> actions) {
    RulesEngineBuilder engineBuilder = new RulesEngineBuilder();
    engineBuilder.withRulesRepository(
        (uri) ->
            repository.findAll().stream().map(this::toEngineRule).collect(Collectors.toList()));
    actions.forEach(a -> a.configure(engineBuilder));
    return engineBuilder;
  }

  private Rule toEngineRule(FeeRule feeRule) {
    RuleBuilder ruleBuilder =
        RuleDsl.ruleBuilder()
            .name(feeRule.getName())
            .fact("contract", Contract.class)
            .priority(feeRule.getRate().getPercent().longValue())
            .predicate(isTrue(value(false)));
    FeeCollector.doCollectInRule(ruleBuilder, feeRule);
    try {
      ObjectNode json =
          (ObjectNode) OBJECT_MAPPER.readTree(RULE_CONVERTER.marshal(ruleBuilder.build()));
      json.replace("predicate", OBJECT_MAPPER.readTree(feeRule.getRestrictions().toJson()));
      return RULE_CONVERTER.unmarshal(json.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
