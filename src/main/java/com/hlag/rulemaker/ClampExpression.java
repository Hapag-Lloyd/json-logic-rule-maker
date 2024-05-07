package com.hlag.rulemaker;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.expressions.PreEvaluatedArgumentsExpression;
import java.util.List;

/**
 * Clamp expression that represents value with additional minimum and maximum
 */
public class ClampExpression implements PreEvaluatedArgumentsExpression {

  /**
   * Singleton instance of the expression
   */
  public static final ClampExpression INSTANCE = new ClampExpression();

  private ClampExpression() {
  }

  @Override
  public String key() {
    return "clamp";
  }

  @SuppressWarnings({ "rawtypes", "squid:S1659" })
  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
    if (arguments.size() != 3) {
      throw new JsonLogicEvaluationException("clamp expects exactly 3 arguments: value, min, max");
    }

    // Parsing and validating input
    double value, min, max;
    try {
      value = toDouble(arguments.get(0));
      min = toDouble(arguments.get(1));
      max = toDouble(arguments.get(2));
    } catch (NumberFormatException e) {
      throw new JsonLogicEvaluationException("clamp arguments must be numeric");
    }

    // Clamp the value within the specified range [min, max]
    return Math.min(Math.max(value, min), max);
  }

  private double toDouble(Object obj) {
    if (obj instanceof Number) {
      return ((Number) obj).doubleValue();
    }
    return Double.parseDouble(obj.toString());
  }
}
