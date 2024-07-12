package com.hlag.rulemaker;

import com.hlag.rulemaker.expression.ClampExpression;
import com.hlag.rulemaker.expression.CustomMathExpression;
import com.hlag.rulemaker.expression.CustomNumericComparisonExpression;
import com.hlag.rulemaker.expression.DateDiffExpression;
import io.github.jamsesso.jsonlogic.JsonLogicException;
import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;
import io.github.jamsesso.jsonlogic.ast.JsonLogicParser;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.AllExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.ArrayHasExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.ConcatenateExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.EqualityExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.FilterExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.IfExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.InExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.InequalityExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.LogExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.LogicExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.MapExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.MergeExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.MissingExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.NotExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.ReduceExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.StrictEqualityExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.StrictInequalityExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.SubstringExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonLogic {

  private final List<JsonLogicExpression> expressions;
  private final Map<String, JsonLogicNode> parseCache;
  private JsonLogicEvaluator evaluator;

  public JsonLogic() {
    this.expressions = new ArrayList<>();
    this.parseCache = new ConcurrentHashMap<>();

    // Add default operations
    addOperation(IfExpression.IF);
    addOperation(IfExpression.TERNARY);
    addOperation(EqualityExpression.INSTANCE);
    addOperation(InequalityExpression.INSTANCE);
    addOperation(StrictEqualityExpression.INSTANCE);
    addOperation(StrictInequalityExpression.INSTANCE);
    addOperation(NotExpression.SINGLE);
    addOperation(NotExpression.DOUBLE);
    addOperation(LogicExpression.AND);
    addOperation(LogicExpression.OR);
    addOperation(LogExpression.STDOUT);
    addOperation(MapExpression.INSTANCE);
    addOperation(FilterExpression.INSTANCE);
    addOperation(ReduceExpression.INSTANCE);
    addOperation(AllExpression.INSTANCE);
    addOperation(ArrayHasExpression.SOME);
    addOperation(ArrayHasExpression.NONE);
    addOperation(MergeExpression.INSTANCE);
    addOperation(InExpression.INSTANCE);
    addOperation(ConcatenateExpression.INSTANCE);
    addOperation(SubstringExpression.INSTANCE);
    addOperation(MissingExpression.ALL);
    addOperation(MissingExpression.SOME);

    //Add custom operations
    addOperation(CustomNumericComparisonExpression.GT);
    addOperation(CustomNumericComparisonExpression.GTE);
    addOperation(CustomNumericComparisonExpression.LT);
    addOperation(CustomNumericComparisonExpression.LTE);
    addOperation(CustomMathExpression.ADD);
    addOperation(CustomMathExpression.SUBTRACT);
    addOperation(CustomMathExpression.MULTIPLY);
    addOperation(CustomMathExpression.DIVIDE);
    addOperation(CustomMathExpression.MODULO);
    addOperation(CustomMathExpression.MIN);
    addOperation(CustomMathExpression.MAX);

    addOperation(ClampExpression.INSTANCE);
    addOperation(DateDiffExpression.INSTANCE);
    addOperation(CustomNumericComparisonExpression.LT_LTE);
    addOperation(CustomNumericComparisonExpression.LT_LT);
    addOperation(CustomNumericComparisonExpression.LTE_LTE);
    addOperation(CustomNumericComparisonExpression.LTE_LT);
  }

  public JsonLogic addOperation(JsonLogicExpression expression) {
    expressions.add(expression);
    evaluator = null;

    return this;
  }

  public Object apply(String json, Object data) throws JsonLogicException {
    if (!parseCache.containsKey(json)) {
      parseCache.put(json, JsonLogicParser.parse(json));
    }

    if (evaluator == null) {
      evaluator = new JsonLogicEvaluator(expressions);
    }

    return evaluator.evaluate(parseCache.get(json), data);
  }
}
