package com.hlag.rulemaker.expression;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Clamp expression that represents value with additional minimum and maximum
 */
public class ClampExpression extends NumericExpression {

  public static final ClampExpression INSTANCE = new ClampExpression();

  private ClampExpression() {
  }

  @Override
  public String key() {
    return "clamp";
  }

  @SuppressWarnings({"squid:S1659"})
  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
    if (arguments.size() != 3) {
      throw new JsonLogicEvaluationException("clamp expects exactly 3 arguments: value, min, max");
    }

    BigDecimal[] values = parseArguments(arguments);
    return values[0].max(values[1]).min(values[2]);
  }
}
