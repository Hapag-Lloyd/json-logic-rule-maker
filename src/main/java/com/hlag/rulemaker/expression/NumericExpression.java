package com.hlag.rulemaker.expression;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.expressions.PreEvaluatedArgumentsExpression;
import java.math.BigDecimal;
import java.util.List;

public abstract class NumericExpression implements PreEvaluatedArgumentsExpression {

  protected BigDecimal[] parseArguments(List<?> arguments) throws JsonLogicEvaluationException {
    int amountOfArguments = arguments.size();
    BigDecimal[] values = new BigDecimal[amountOfArguments];

    for (int i = 0; i < amountOfArguments; i++) {
      Object value = arguments.get(i);

      if (value instanceof String) {
        try {
          values[i] = new BigDecimal((String) value);
        } catch (NumberFormatException e) {
          throw new JsonLogicEvaluationException("Non numeric argument: " + value);
        }
      } else if (!(value instanceof Number)) {
        throw new JsonLogicEvaluationException("Non numeric argument: " + value);
      } else {
        values[i] = new BigDecimal(value.toString());
      }
    }

    return values;
  }
}
