package com.hlag.rulemaker;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.expressions.PreEvaluatedArgumentsExpression;

import java.util.List;

public class ExtendedNumericComparisonExpression implements PreEvaluatedArgumentsExpression {

  public static final ExtendedNumericComparisonExpression LTE_LT = new ExtendedNumericComparisonExpression("<= <");
  public static final ExtendedNumericComparisonExpression LT_LTE = new ExtendedNumericComparisonExpression("< <=");
  public static final ExtendedNumericComparisonExpression LTE_LTE = new ExtendedNumericComparisonExpression("<= <=");
  public static final ExtendedNumericComparisonExpression LT_LT = new ExtendedNumericComparisonExpression("< <");

  private final String key;

  private ExtendedNumericComparisonExpression(String key) {
    this.key = key;
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
    if (arguments.size() != 3) {
      throw new JsonLogicEvaluationException("between comparison expects exactly 3 arguments");
    }

    double[] values = parseArguments(arguments);

    switch (key) {
      case "<= <":
        return values[0] <= values[1] && values[1] < values[2];

      case "< <=":
        return values[0] < values[1] && values[1] <= values[2];

      case "<= <=":
        return values[0] <= values[1] && values[1] <= values[2];

      case "< <":
        return values[0] < values[1] && values[1] < values[2];

      default:
        throw new JsonLogicEvaluationException("'" + key + "' does not support between comparisons");
    }
  }

  private double[] parseArguments(List<?> arguments) {
    int amountOfArguments  = arguments.size();
    double[] values = new double[amountOfArguments ];

    for (int i = 0; i < amountOfArguments; i++) {
      Object value = arguments.get(i);

      if (value instanceof String) {
        values[i] = Double.parseDouble((String) value);
      } else if (value instanceof Number) {
        values[i] = ((Number) value).doubleValue();
      } else {
        throw new IllegalArgumentException("Illegal argument :" + value);
      }
    }

    return values;
  }
}
