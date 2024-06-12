package com.hlag.rulemaker.exception;

public class RuleMakerEvaluationException extends RuleMakerException {
    public RuleMakerEvaluationException(String msg) {
        super(msg);
    }

    public RuleMakerEvaluationException(Throwable cause) {
        super(cause);
    }

    public RuleMakerEvaluationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
