package com.hlag.rulemaker;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class WrappedRuleMaker extends RuleMaker {

  public WrappedRuleMaker(Object expression) {
    super(expression);
  }

  @SuppressWarnings("unchecked")
  public WrappedRuleMaker[] unwrap() {
    if (isLiteral(expression)) {
      throw new IllegalStateException("Expression is already a literal");
    }

    Map<?, ?> expressionMap = (Map<?, ?>) expression;

    if (expressionMap.keySet().size() > 1) {
      throw new IllegalStateException("Wrong format of expression, " + expression);
    }

    return isLiteral(expressionMap.values().toArray()[0]) ?
        unwrapLiteral((Map<String, Object>) expression) :
        unwrapCollection((Map<String, List<Object>>) expression);
  }

  private WrappedRuleMaker[] unwrapLiteral(Map<String, Object> expressionMap) {
    return expressionMap
        .values()
        .stream()
        .map(WrappedRuleMaker::new)
        .toArray(WrappedRuleMaker[]::new);
  }

  private WrappedRuleMaker[] unwrapCollection(Map<String, List<Object>> expressionMap) {
    return expressionMap
        .values()
        .stream()
        .flatMap(Collection::stream)
        .map(WrappedRuleMaker::new)
        .toArray(WrappedRuleMaker[]::new);
  }

  public Object getLiteral() {
    if (isLiteral(expression)) {
      return expression;
    } else {
      throw new IllegalStateException("Expression is not a literal");
    }
  }

  public boolean isLiteral(Object object) {
    boolean notMap = !(object instanceof Map);
    boolean notNestedMap = true;

    if (object instanceof Collection) {
      notNestedMap = ((Collection<?>) object).stream().noneMatch(Map.class::isInstance);
    }

    return notMap && notNestedMap;
  }

  public String getTopLevelOperator() {
    Map<?, ?> expressionMap = (Map<?, ?>) expression;
      return (String) expressionMap.keySet().stream().findFirst().orElseThrow();
    }
}
