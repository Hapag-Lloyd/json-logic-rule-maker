package com.hlag.rulemaker.expression;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import java.math.BigDecimal;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NumericExpressionUnitTest {

  private NumericExpression sut;

  @BeforeEach
  void setUp() {
    sut = new TestNumericExpression();
  }

  @Test
  @SneakyThrows
  void shouldReturnBigDecimals_whenParseArguments_givenStrings() {
    //Given
    List<?> arguments = List.of("2.0", "3.0");
    BigDecimal[] expected = new BigDecimal[]{new BigDecimal("2.0"), new BigDecimal("3.0")};

    //When
    BigDecimal[] parsed = sut.parseArguments(arguments);

    //Then
    assertThat(parsed).isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  void shouldReturnBigDecimals_whenParseArguments_givenBigDecimals() {
    //Given
    List<?> arguments = List.of(new BigDecimal("2.0"), new BigDecimal("3.0"));
    BigDecimal[] expected = new BigDecimal[]{new BigDecimal("2.0"), new BigDecimal("3.0")};

    //When
    BigDecimal[] parsed = sut.parseArguments(arguments);

    //Then
    assertThat(parsed).isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  void shouldReturnBigDecimals_whenParseArguments_givenNumbers() {
    //Given
    List<?> arguments = List.of(2, 3);
    BigDecimal[] expected = new BigDecimal[]{new BigDecimal("2"), new BigDecimal("3")};

    //When
    BigDecimal[] parsed = sut.parseArguments(arguments);

    //Then
    assertThat(parsed).isEqualTo(expected);
  }

  @Test
  void shouldThrowException_whenParseArguments_givenNonNumeric() {
    //Given
    List<?> arguments = List.of("xyz", 3);

    //When
    //Then
    assertThatThrownBy(() -> sut.parseArguments(arguments))
      .isInstanceOf(JsonLogicEvaluationException.class)
      .hasMessage("Non numeric argument: xyz");
  }

  private static class TestNumericExpression extends NumericExpression {

    @Override
    public Object evaluate(List arguments, Object data) {
      return null;
    }

    @Override
    public String key() {
      return null;
    }
  }
}
