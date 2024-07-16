package com.hlag.rulemaker.expression;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BigDecimalMathExpressionUnitTest {

  @ParameterizedTest
  @MethodSource("addArguments")
  @SneakyThrows
  void shouldReturnExpected_whenAddEvaluate_givenInput(List<BigDecimal> arguments, BigDecimal expected) {
    //Given
    //When
    Object evaluated = BigDecimalMathExpression.ADD.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("subArguments")
  @SneakyThrows
  void shouldReturnExpected_whenSubtractEvaluate_givenInput(List<BigDecimal> arguments, BigDecimal expected) {
    //Given
    //When
    Object evaluated = BigDecimalMathExpression.SUBTRACT.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("multiplyArguments")
  @SneakyThrows
  void shouldReturnExpected_whenMultiplyEvaluate_givenInput(List<BigDecimal> arguments, BigDecimal expected) {
    //Given
    //When
    Object evaluated = BigDecimalMathExpression.MULTIPLY.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  void shouldReturnExpected_whenDivideEvaluate_givenInput() {
    //Given
    List<BigDecimal> arguments = List.of(new BigDecimal("7"), new BigDecimal("4.0"));
    BigDecimal expected = new BigDecimal("1.75");

    //When
    Object evaluated = BigDecimalMathExpression.DIVIDE.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  void shouldReturnExpected_whenModuloEvaluate_givenInput() {
    //Given
    List<BigDecimal> arguments = List.of(new BigDecimal("7.0"), new BigDecimal("4.0"));
    BigDecimal expected = new BigDecimal("3.0");

    //When
    Object evaluated = BigDecimalMathExpression.MODULO.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("minArguments")
  @SneakyThrows
  void shouldReturnExpected_whenMinEvaluate_givenInput(List<BigDecimal> arguments, BigDecimal expected) {
    //Given
    //When
    Object evaluated = BigDecimalMathExpression.MIN.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("maxArguments")
  @SneakyThrows
  void shouldReturnExpected_whenMaxEvaluate_givenInput(List<BigDecimal> arguments, BigDecimal expected) {
    //Given
    //When
    Object evaluated = BigDecimalMathExpression.MAX.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("customMathExpressionsThatRequireMoreThanOneArgument")
  void shouldThrowException_whenEvaluate_givenOneArgument(BigDecimalMathExpression bigDecimalMathExpression) {
    //Given
    List<BigDecimal> arguments = List.of(new BigDecimal("3.0"));

    //When
    //Then
    assertThatThrownBy(() -> bigDecimalMathExpression.evaluate(arguments, null))
      .isInstanceOf(JsonLogicEvaluationException.class)
      .hasMessage("Minimum 2 arguments required");
  }

  @ParameterizedTest
  @MethodSource("customMathExpressionsThatRequireExactlyTwoArguments")
  void shouldThrowException_whenEvaluate_givenMoreThanTwoArguments(BigDecimalMathExpression bigDecimalMathExpression) {
    //Given
    List<BigDecimal> arguments = List.of(new BigDecimal("3.0"), new BigDecimal("3.0"), new BigDecimal("3.0"));

    //When
    //Then
    assertThatThrownBy(() -> bigDecimalMathExpression.evaluate(arguments, null))
      .isInstanceOf(JsonLogicEvaluationException.class)
      .hasMessage("Maximum 2 arguments required");
  }

  private static Stream<Arguments> addArguments() {
    return Stream.of(
      Arguments.of(List.of(new BigDecimal("3.0"), new BigDecimal("4.0")), new BigDecimal("7.0")),
      Arguments.of(List.of(new BigDecimal("3.0"), new BigDecimal("4.0"), new BigDecimal("5.0")), new BigDecimal("12.0")),
      Arguments.of(List.of(new BigDecimal("3.0")), new BigDecimal("3.0"))
    );
  }

  private static Stream<Arguments> subArguments() {
    return Stream.of(
      Arguments.of(List.of(new BigDecimal("7.0"), new BigDecimal("4.0")), new BigDecimal("3.0")),
      Arguments.of(List.of(new BigDecimal("3.0")), new BigDecimal("-3.0"))
    );
  }

  private static Stream<Arguments> multiplyArguments() {
    return Stream.of(
      Arguments.of(List.of(new BigDecimal("3.0"), new BigDecimal("4.0")), new BigDecimal("12.00")),
      Arguments.of(List.of(new BigDecimal("3.0"), new BigDecimal("4.0"), new BigDecimal("5.0")), new BigDecimal("60.000"))
    );
  }

  private static Stream<Arguments> minArguments() {
    return Stream.of(
      Arguments.of(List.of(new BigDecimal("3.0"), new BigDecimal("4.0")), new BigDecimal("3.0")),
      Arguments.of(List.of(new BigDecimal("3.0"), new BigDecimal("4.0"), new BigDecimal("5.0")), new BigDecimal("3.0"))
    );
  }

  private static Stream<Arguments> maxArguments() {
    return Stream.of(
      Arguments.of(List.of(new BigDecimal("3.0"), new BigDecimal("4.0")), new BigDecimal("4.0")),
      Arguments.of(List.of(new BigDecimal("3.0"), new BigDecimal("4.0"), new BigDecimal("5.0")), new BigDecimal("5.0"))
    );
  }

  private static Stream<Arguments> customMathExpressionsThatRequireMoreThanOneArgument() {
    return Stream.of(
      Arguments.of(BigDecimalMathExpression.MULTIPLY),
      Arguments.of(BigDecimalMathExpression.DIVIDE),
      Arguments.of(BigDecimalMathExpression.MODULO),
      Arguments.of(BigDecimalMathExpression.MIN),
      Arguments.of(BigDecimalMathExpression.MAX)
    );
  }

  private static Stream<Arguments> customMathExpressionsThatRequireExactlyTwoArguments() {
    return Stream.of(
      Arguments.of(BigDecimalMathExpression.SUBTRACT),
      Arguments.of(BigDecimalMathExpression.DIVIDE),
      Arguments.of(BigDecimalMathExpression.MODULO)
    );
  }
}
