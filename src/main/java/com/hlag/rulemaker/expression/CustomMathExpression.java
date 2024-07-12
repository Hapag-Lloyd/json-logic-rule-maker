package com.hlag.rulemaker.expression;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.BinaryOperator;

public class CustomMathExpression extends NumericExpression {

  public static final CustomMathExpression ADD = new CustomMathExpression("+", BigDecimal::add);
  public static final CustomMathExpression SUBTRACT = new CustomMathExpression("-", BigDecimal::subtract, 2);
  public static final CustomMathExpression MULTIPLY = new CustomMathExpression("*", BigDecimal::multiply);
  public static final CustomMathExpression DIVIDE = new CustomMathExpression("/", BigDecimal::divide, 2);
  public static final CustomMathExpression MODULO = new CustomMathExpression("%", BigDecimal::remainder, 2);
  public static final CustomMathExpression MIN = new CustomMathExpression("min", BigDecimal::min);
  public static final CustomMathExpression MAX = new CustomMathExpression("max", BigDecimal::max);

  private final String key;
  private final BinaryOperator<BigDecimal> reducer;
  private final int maxArguments;

  public CustomMathExpression(String key, BinaryOperator<BigDecimal> reducer) {
    this(key, reducer, 0);
  }

  public CustomMathExpression(String key, BinaryOperator<BigDecimal> reducer, int maxArguments) {
    this.key = key;
    this.reducer = reducer;
    this.maxArguments = maxArguments;
  }

  @Override
  public String key() {
    return key;
  }

  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
    BigDecimal[] values = parseArguments(arguments);
    BigDecimal accumulator = values[0];

    if (arguments.size() == 1) {
      if (key.equals("+")) {
        return accumulator;
      }

      if (key.equals("-")) {
        return BigDecimal.valueOf(-1).multiply(accumulator);
      }

      throw new JsonLogicEvaluationException("Minimum 2 arguments required");
    }

    if (arguments.size() > maxArguments && maxArguments != 0) {
      throw new JsonLogicEvaluationException("Maximum " + maxArguments + " arguments required");
    }

    for (int i = 1; i < values.length && (i < maxArguments || maxArguments == 0); i++) {
      accumulator = reducer.apply(accumulator, values[i]);
    }

    return accumulator;
  }
}
