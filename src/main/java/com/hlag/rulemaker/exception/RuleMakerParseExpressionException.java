package com.hlag.rulemaker.exception;

public class RuleMakerParseExpressionException extends RuleMakerException {
    public RuleMakerParseExpressionException(String msg) {
        super(msg);
    }

    public RuleMakerParseExpressionException(Throwable cause) {
        super(cause);
    }

    public RuleMakerParseExpressionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
