package fr.malt.feesrulesengine.provider.yare;

import com.sabre.oss.yare.core.RulesEngineBuilder;

public interface RulesEngineComponent {
  void configure(final RulesEngineBuilder engineBuilder);
}
