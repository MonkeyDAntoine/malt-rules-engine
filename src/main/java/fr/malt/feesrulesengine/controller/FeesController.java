package fr.malt.feesrulesengine.controller;

import fr.malt.feesrulesengine.model.Contract;
import fr.malt.feesrulesengine.service.FeeResult;
import fr.malt.feesrulesengine.service.RuleEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/fee")
@RequiredArgsConstructor
@Validated
public class FeesController {

  private final RuleEngineService ruleEngineService;

  @PostMapping
  public FeeResult calculateFee(@RequestBody @Valid Contract contract) {
    return ruleEngineService.compute(contract);
  }
}
