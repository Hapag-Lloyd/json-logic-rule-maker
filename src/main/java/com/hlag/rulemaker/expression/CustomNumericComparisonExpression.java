package com.hlag.rulemaker.expression;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import java.math.BigDecimal;
import java.util.List;

public class CustomNumericComparisonExpression extends NumericExpression {

  public static final CustomNumericComparisonExpression GT = new CustomNumericComparisonExpression(">", 2);
  public static final CustomNumericComparisonExpression GTE = new CustomNumericComparisonExpression(">=", 2);
  public static final CustomNumericComparisonExpression LT = new CustomNumericComparisonExpression("<", 2);
  public static final CustomNumericComparisonExpression LTE = new CustomNumericComparisonExpression("<=", 2);

  public static final CustomNumericComparisonExpression LTE_LT = new CustomNumericComparisonExpression("<= <", 3);
  public static final CustomNumericComparisonExpression LT_LTE = new CustomNumericComparisonExpression("< <=", 3);
  public static final CustomNumericComparisonExpression LTE_LTE = new CustomNumericComparisonExpression("<= <=", 3);
  public static final CustomNumericComparisonExpression LT_LT = new CustomNumericComparisonExpression("< <", 3);

  private final String key;
  private final int numberOfArguments;

  private CustomNumericComparisonExpression(String key, int numberOfArguments) {
    this.key = key;
    this.numberOfArguments = numberOfArguments;
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
    if (arguments.size() != numberOfArguments) {
      throw new JsonLogicEvaluationException("Exactly " + numberOfArguments + " arguments required");
    }

    BigDecimal[] values = parseArguments(arguments);

    switch (key) {
      case "<":
        return values[0].compareTo(values[1]) < 0;

      case "<=":
        return values[0].compareTo(values[1]) <= 0;

      case ">":
        return values[0].compareTo(values[1]) > 0;

      case ">=":
        return values[0].compareTo(values[1]) >= 0;

      case "<= <":
        return values[1].compareTo(values[0]) >= 0 && values[1].compareTo(values[2]) < 0;

      case "< <=":
        return values[1].compareTo(values[0]) > 0 && values[1].compareTo(values[2]) <= 0;

      case "<= <=":
        return values[1].compareTo(values[0]) >= 0 && values[1].compareTo(values[2]) <= 0;

      case "< <":
        return values[1].compareTo(values[0]) > 0 && values[1].compareTo(values[2]) < 0;

      default:
        throw new JsonLogicEvaluationException("'" + key + "' is not a comparison expression");
    }
  }
}
