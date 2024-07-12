package com.hlag.rulemaker.exception;

public class RuleMakerException extends RuntimeException {

  private RuleMakerException() {
  }

  public RuleMakerException(String msg) {
    super(msg);
  }

  public RuleMakerException(Throwable cause) {
    super(cause);
  }

  public RuleMakerException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
