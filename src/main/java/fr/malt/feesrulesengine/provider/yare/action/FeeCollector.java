package fr.malt.feesrulesengine.provider.yare.action;

import com.sabre.oss.yare.core.RulesEngineBuilder;
import com.sabre.oss.yare.dsl.RuleDsl;
import fr.malt.feesrulesengine.model.FeeRule;
import fr.malt.feesrulesengine.provider.yare.RulesEngineComponent;
import fr.malt.feesrulesengine.service.FeeResult;
import lombok.Getter;
import org.springframework.stereotype.Component;

import static com.sabre.oss.yare.dsl.RuleDsl.param;
import static com.sabre.oss.yare.dsl.RuleDsl.value;
import static com.sabre.oss.yare.invoker.java.MethodCallMetadata.method;

@Component
@Getter
public class FeeCollector implements RulesEngineComponent {
  private static final String NAME = "collect";
  private static final String CONTEXT_PARAM = "context";
  private static final String FEE_PARAM = "fee";
  private static final String RULE_PARAM = "rule";

  private double fee = 10;
  private String rule = "Default rule";

  public void collect(FeeResult context, Double fee, String rule) {
    if (fee < context.getFee()) {
      context.setFee(fee);
      context.setRule(rule);
    }
  }

  public static final void doCollectInRule(
      final RuleDsl.RuleBuilder ruleBuilder, final FeeRule feeRule) {
    ruleBuilder.action(
        FeeCollector.NAME,
        param(FeeCollector.CONTEXT_PARAM, value("${ctx}")),
        param(FeeCollector.FEE_PARAM, value(feeRule.getRate().getPercent())),
        param(FeeCollector.RULE_PARAM, value(feeRule.getName())));
  }

  @Override
  public void configure(RulesEngineBuilder engineBuilder) {
    engineBuilder.withActionMapping(NAME, method(this, a -> a.collect(null, null, null)));
  }
}
