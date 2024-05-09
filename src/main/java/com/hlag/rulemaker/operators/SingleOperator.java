package com.hlag.rulemaker.operators;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SingleOperator implements NumericOperator {

  LOWER_OR_EQUAL("<="),
  GREATER_OR_EQUAL(">="),
  LOWER("<"),
  GREATER(">");

  private final String operator;

  @Override
  public String getSymbol() {
    return operator;
  }
}
