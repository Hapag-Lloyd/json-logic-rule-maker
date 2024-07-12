package com.hlag.rulemaker.expression;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ClampExpressionUnitTest {

  @ParameterizedTest
  @MethodSource("clampArguments")
  @SneakyThrows
  void shouldReturnExpected_whenEvaluate_givenInput(List<BigDecimal> arguments, BigDecimal expected) {
    //Given
    //When
    Object evaluated = ClampExpression.INSTANCE.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("incorrectNumberOfArguments")
  void shouldThrowException_whenEvaluate_givenLessThenThreeArguments(List<BigDecimal> arguments) {
    //Given
    //When
    //Then
    assertThatThrownBy(() -> ClampExpression.INSTANCE.evaluate(arguments, null))
      .isInstanceOf(JsonLogicEvaluationException.class)
      .hasMessage("clamp expects exactly 3 arguments: value, min, max");
  }

  private static Stream<Arguments> clampArguments() {
    return Stream.of(
      Arguments.of(List.of(new BigDecimal("2.0"), new BigDecimal("1.0"), new BigDecimal("3.0")), new BigDecimal("2.0")),
      Arguments.of(List.of(new BigDecimal("2.0"), new BigDecimal("3.0"), new BigDecimal("4.0")), new BigDecimal("3.0")),
      Arguments.of(List.of(new BigDecimal("4.0"), new BigDecimal("1.0"), new BigDecimal("3.0")), new BigDecimal("3.0")),
      Arguments.of(List.of(new BigDecimal("2.0"), new BigDecimal("2.0"), new BigDecimal("2.0")), new BigDecimal("2.0"))
    );
  }

  private static Stream<Arguments> incorrectNumberOfArguments() {
    return Stream.of(
      Arguments.of(List.of()),
      Arguments.of(List.of(new BigDecimal("1.0"))),
      Arguments.of(List.of(new BigDecimal("1.0"), new BigDecimal("1.0")))
    );
  }
}
