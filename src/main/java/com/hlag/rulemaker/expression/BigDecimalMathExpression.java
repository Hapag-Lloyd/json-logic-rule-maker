package com.hlag.rulemaker.expression;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.BinaryOperator;

public class BigDecimalMathExpression extends NumericExpression {

  public static final BigDecimalMathExpression ADD = new BigDecimalMathExpression("+", BigDecimal::add);
  public static final BigDecimalMathExpression SUBTRACT = new BigDecimalMathExpression("-", BigDecimal::subtract, 2);
  public static final BigDecimalMathExpression MULTIPLY = new BigDecimalMathExpression("*", BigDecimal::multiply);
  public static final BigDecimalMathExpression DIVIDE = new BigDecimalMathExpression("/", BigDecimal::divide, 2);
  public static final BigDecimalMathExpression MODULO = new BigDecimalMathExpression("%", BigDecimal::remainder, 2);
  public static final BigDecimalMathExpression MIN = new BigDecimalMathExpression("min", BigDecimal::min);
  public static final BigDecimalMathExpression MAX = new BigDecimalMathExpression("max", BigDecimal::max);

  private final String key;
  private final BinaryOperator<BigDecimal> reducer;
  private final int maxArguments;

  public BigDecimalMathExpression(String key, BinaryOperator<BigDecimal> reducer) {
    this(key, reducer, 0);
  }

  public BigDecimalMathExpression(String key, BinaryOperator<BigDecimal> reducer, int maxArguments) {
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
