package com.hlag.rulemaker.exception;

import lombok.Getter;

import java.util.Set;

@Getter
public class RuleMakerMissingVariablesException extends RuleMakerException {

    private final Set<String> missingVariables;

    public RuleMakerMissingVariablesException(Set<String> missingVariables) {
        super("Missing variables: " + missingVariables);
        this.missingVariables = missingVariables;
    }
}
