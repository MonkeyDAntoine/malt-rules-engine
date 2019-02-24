package fr.malt.feesrulesengine.provider.yare.function;

import com.sabre.oss.yare.core.RulesEngineBuilder;
import fr.malt.feesrulesengine.model.Contractor;
import fr.malt.feesrulesengine.provider.ipstack.IPStackClient;
import fr.malt.feesrulesengine.provider.yare.RulesEngineComponent;
import org.springframework.stereotype.Component;

import static com.sabre.oss.yare.invoker.java.MethodCallMetadata.method;

@Component
public class IpLocator implements RulesEngineComponent {

  @Override
  public void configure(RulesEngineBuilder engineBuilder) {
    engineBuilder.withFunctionMapping("ipLocation", method(this, f -> f.locate(null)));
  }

  public String locate(final Contractor contractor) {
    IPStackClient ipStack = IPStackClient.getInstance();
    if (ipStack == null || contractor == null) {
      return null;
    } else {
      return ipStack.locate(contractor).getCountryCode();
    }
  }
}
