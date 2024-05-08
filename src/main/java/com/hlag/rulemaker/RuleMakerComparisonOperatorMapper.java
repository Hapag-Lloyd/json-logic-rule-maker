package com.hlag.rulemaker;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleMakerComparisonOperatorMapper {

  LOWER("<", "lower"),
  LOWER_OR_EQUAL("<=", "lowerOrEqual"),
  GREATER(">", "greater"),
  GREATER_OR_EQUAL(">=", "greaterOrEqual"),
  LOWER_OR_EQUAL_LOWER_OR_EQUAL("<= <=", "lte"),
  LOWER_LOWER("< <", "lt"),
  LOWER_OR_EQUAL_LOWER("<= <", "lteLt"),
  LOWER_LOWER_OR_EQUAL("< <=", "ltLte");

  private final String ruleWrightOperator;
  private final String ruleWrightName;

  public static RuleMakerComparisonOperatorMapper fromRuleWrightOperator(String ruleWrightOperator) {
    return Arrays.stream(RuleMakerComparisonOperatorMapper.values())
        .filter(operatorMapper -> operatorMapper.getRuleWrightOperator().equals(ruleWrightOperator))
        .findFirst().orElseThrow();
  }
}
