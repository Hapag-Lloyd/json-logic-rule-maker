package com.hlag.rulemaker.operators;

import static com.hlag.rulemaker.operators.SingleOperator.LOWER;
import static com.hlag.rulemaker.operators.SingleOperator.LOWER_OR_EQUAL;

import com.hlag.rulemaker.RuleMakerComparisonOperators;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class DoubleOperator implements NumericOperator{
  private final SingleOperator firstOperator;
  private final SingleOperator secondOperator;

  @Override
  public String getSymbol() {
    return firstOperator.getSymbol().concat(" ").concat(secondOperator.getSymbol());
  }

  public static DoubleOperator build(Map<String, Object> jsonMapWithDoubleOperator){
    RuleMakerComparisonOperators compoundOperator = RuleMakerComparisonOperators.fromRuleWrightOperator(
        jsonMapWithDoubleOperator.keySet().stream().findFirst().orElseThrow());
    SingleOperator firstOperator;
    SingleOperator secondOperator;
    switch (compoundOperator) {
      case LOWER_LOWER:
        firstOperator = LOWER;
        secondOperator = LOWER;
        break;
      case LOWER_OR_EQUAL_LOWER_OR_EQUAL:
        firstOperator = LOWER_OR_EQUAL;
        secondOperator = LOWER_OR_EQUAL;
        break;
      case LOWER_OR_EQUAL_LOWER:
        firstOperator = LOWER_OR_EQUAL;
        secondOperator = LOWER;
        break;
      case LOWER_LOWER_OR_EQUAL:
        firstOperator = LOWER;
        secondOperator = LOWER_OR_EQUAL;
        break;
      default:
        throw new IllegalArgumentException("Unsupported compound operator: " + compoundOperator);
    }
    return DoubleOperator.builder()
        .firstOperator(firstOperator)
        .secondOperator(secondOperator)
        .build();
  }
}
