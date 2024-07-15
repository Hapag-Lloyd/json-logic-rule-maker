package com.hlag.rulemaker;

import com.hlag.rulemaker.expression.ClampExpression;
import com.hlag.rulemaker.expression.CustomMathExpression;
import com.hlag.rulemaker.expression.CustomNumericComparisonExpression;
import com.hlag.rulemaker.expression.DateDiffExpression;
import io.github.jamsesso.jsonlogic.JsonLogic;
import io.github.jamsesso.jsonlogic.JsonLogicException;

public class RuleMakerJsonLogic {

    private final JsonLogic jsonLogic;

    public RuleMakerJsonLogic() {
        this.jsonLogic = new JsonLogic();

        //Add custom operations
        jsonLogic.addOperation(CustomNumericComparisonExpression.GT);
        jsonLogic.addOperation(CustomNumericComparisonExpression.GTE);
        jsonLogic.addOperation(CustomNumericComparisonExpression.LT);
        jsonLogic.addOperation(CustomNumericComparisonExpression.LTE);
        jsonLogic.addOperation(CustomMathExpression.ADD);
        jsonLogic.addOperation(CustomMathExpression.SUBTRACT);
        jsonLogic.addOperation(CustomMathExpression.MULTIPLY);
        jsonLogic.addOperation(CustomMathExpression.DIVIDE);
        jsonLogic.addOperation(CustomMathExpression.MODULO);
        jsonLogic.addOperation(CustomMathExpression.MIN);
        jsonLogic.addOperation(CustomMathExpression.MAX);

        jsonLogic.addOperation(ClampExpression.INSTANCE);
        jsonLogic.addOperation(DateDiffExpression.INSTANCE);
        jsonLogic.addOperation(CustomNumericComparisonExpression.LT_LTE);
        jsonLogic.addOperation(CustomNumericComparisonExpression.LT_LT);
        jsonLogic.addOperation(CustomNumericComparisonExpression.LTE_LTE);
        jsonLogic.addOperation(CustomNumericComparisonExpression.LTE_LT);
    }

    public Object apply(String json, Object data) throws JsonLogicException {
        return jsonLogic.apply(json, data);
    }
}
