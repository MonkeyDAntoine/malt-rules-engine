package fr.malt.feesrulesengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import fr.malt.feesrulesengine.model.Contract;
import fr.malt.feesrulesengine.model.FeeRule;
import fr.malt.feesrulesengine.repository.FeeRuleRepository;
import fr.malt.feesrulesengine.service.FeeResult;
import fr.malt.feesrulesengine.service.RuleEngineService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeeRulesEngineApplicationTests {

  @Autowired RuleEngineService service;
  @Autowired FeeRuleRepository repository;
  @Autowired ObjectMapper objectMapper;

  @Before
  public void addRule() throws Exception {
    service.addRule(mailCaseRule());
  }

  @After
  public void cleanup() {
    repository.deleteAll();
  }

  @Test
  public void mail_test_case() throws Exception {
    Contract contract = new Contract();
    contract.getClient().setIp("217.127.206.227");
    contract.getFreelance().setIp("217.127.206.227");
    contract.getMission().setDuration("3months");
    contract.getCommercialRelation().setLastMission(LocalDate.now());
    contract.getCommercialRelation().setFirstMission(LocalDate.now().minus(5, ChronoUnit.MONTHS));

    FeeResult result = service.compute(contract);
    assertThat(result.getFee()).isEqualTo(8D);
    assertThat(result.getRule()).isEqualTo("spain or repeat");
  }

  @Test
  public void default_test_case() throws Exception {
    Contract contract = new Contract();
    contract.getClient().setIp("217.127.206.227");
    contract.getFreelance().setIp("217.127.206.227");
    contract.getMission().setDuration("1months");
    contract.getCommercialRelation().setLastMission(LocalDate.now());
    contract.getCommercialRelation().setFirstMission(LocalDate.now().minus(1, ChronoUnit.MONTHS));

    FeeResult result = service.compute(contract);
    assertThat(result).isEqualTo(new FeeResult());
  }

  private FeeRule mailCaseRule() throws Exception {
    FeeRule rule = new FeeRule();
    rule.setId("...");
    rule.setName("spain or repeat");
    rule.getRate().setPercent(8D);
    rule.setRestrictions(
        BasicDBObject.parse(
            objectMapper
                .readTree(
                    FeeRulesEngineApplicationTests.class
                        .getClassLoader()
                        .getResourceAsStream("mail-rule-predicate.json"))
                .toString()));
    return rule;
  }
}
