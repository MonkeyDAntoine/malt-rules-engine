package fr.malt.feesrulesengine.provider.yare.function;

import com.sabre.oss.yare.core.RulesEngineBuilder;
import fr.malt.feesrulesengine.model.DurationComparable;
import fr.malt.feesrulesengine.provider.yare.RulesEngineComponent;
import fr.malt.feesrulesengine.utils.TimeString;
import org.springframework.stereotype.Component;

import static com.sabre.oss.yare.invoker.java.MethodCallMetadata.method;

@Component
public class DurationComparison implements RulesEngineComponent {

  @Override
  public void configure(RulesEngineBuilder engineBuilder) {
    engineBuilder.withFunctionMapping(
        "duration >=", method(this, f -> f.isDurationGreaterThanInclusive(null, null)));
  }

  public boolean isDurationGreaterThanInclusive(
      DurationComparable durationComparable, String duration) {
    return Long.compare(
            durationComparable.toDays(), TimeString.parseTimestring(duration, TimeString.Unit.DAY))
        >= 1;
  }
}
