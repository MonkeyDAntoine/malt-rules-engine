package fr.malt.feesrulesengine.service;

import fr.malt.feesrulesengine.service.rule.Rule;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RulesService implements ApplicationContextAware {

  private Map<String, Rule> rules = new HashMap<>();

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.rules = applicationContext.getBeansOfType(Rule.class);
  }

  public Rule rule(final String rule) {
    return rules.get(rule);
  }
}
