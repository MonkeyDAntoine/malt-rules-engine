package fr.malt.feesrulesengine.controller;

import fr.malt.feesrulesengine.model.FeeRule;
import fr.malt.feesrulesengine.service.RuleEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/rule")
@RequiredArgsConstructor
@Validated
public class RulesController {

  private final RuleEngineService ruleEngineService;

  @PostMapping
  public FeeRule addRule(@RequestBody @Valid FeeRule feeRule) {
    return ruleEngineService.addRule(feeRule);
  }

  @GetMapping("/{ruleId}")
  public ResponseEntity<FeeRule> getRule(@PathVariable("ruleId") String ruleId) {
    return ruleEngineService
        .getRule(ruleId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public Page<FeeRule> getRules(@PageableDefault Pageable pageable) {
    return ruleEngineService.list(pageable);
  }
}
