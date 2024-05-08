package com.hlag.rulemaker;

import static com.hlag.rulemaker.ExtendedNumericComparisonExpression.LTE_LT;
import static com.hlag.rulemaker.ExtendedNumericComparisonExpression.LTE_LTE;
import static com.hlag.rulemaker.ExtendedNumericComparisonExpression.LT_LT;
import static com.hlag.rulemaker.ExtendedNumericComparisonExpression.LT_LTE;
import static io.github.jamsesso.jsonlogic.evaluator.expressions.NumericComparisonExpression.GT;
import static io.github.jamsesso.jsonlogic.evaluator.expressions.NumericComparisonExpression.GTE;
import static io.github.jamsesso.jsonlogic.evaluator.expressions.NumericComparisonExpression.LT;
import static io.github.jamsesso.jsonlogic.evaluator.expressions.NumericComparisonExpression.LTE;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleMakerComparisonOperatorMapper {

  LOWER(LT.key()),
  LOWER_OR_EQUAL(LTE.key()),
  GREATER(GT.key()),
  GREATER_OR_EQUAL(GTE.key()),
  LOWER_OR_EQUAL_LOWER_OR_EQUAL(LTE_LTE.key()),
  LOWER_LOWER(LT_LT.key()),
  LOWER_OR_EQUAL_LOWER(LTE_LT.key()),
  LOWER_LOWER_OR_EQUAL(LT_LTE.key());

  private final String ruleWrightOperator;

  public static RuleMakerComparisonOperatorMapper fromRuleWrightOperator(String ruleWrightOperator) {
    return Arrays.stream(RuleMakerComparisonOperatorMapper.values())
        .filter(operatorMapper -> operatorMapper.getRuleWrightOperator().equals(ruleWrightOperator))
        .findFirst().orElseThrow();
  }
}
