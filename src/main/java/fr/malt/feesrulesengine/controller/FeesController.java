package fr.malt.feesrulesengine.controller;

import fr.malt.feesrulesengine.model.Contract;
import fr.malt.feesrulesengine.model.FeeResult;
import fr.malt.feesrulesengine.service.FeeRulesService;
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

  private final FeeRulesService feeRulesService;

  @PostMapping
  public FeeResult calculateFee(@RequestBody @Valid Contract contract) {
    return feeRulesService.evaluate(contract);
  }
}
