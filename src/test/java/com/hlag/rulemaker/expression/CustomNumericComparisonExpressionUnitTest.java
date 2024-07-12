package com.hlag.rulemaker.expression;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CustomNumericComparisonExpressionUnitTest {

  @ParameterizedTest
  @MethodSource("gtArguments")
  @SneakyThrows
  void shouldReturnExpected_whenGtEvaluate_givenInput(List<Integer> arguments, boolean expected) {
    //Given
    //When
    Object evaluated = CustomNumericComparisonExpression.GT.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("gteArguments")
  @SneakyThrows
  void shouldReturnExpected_whenGteEvaluate_givenInput(List<Integer> arguments, boolean expected) {
    //Given
    //When
    Object evaluated = CustomNumericComparisonExpression.GTE.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("ltArguments")
  @SneakyThrows
  void shouldReturnExpected_whenLtEvaluate_givenInput(List<Integer> arguments, boolean expected) {
    //Given
    //When
    Object evaluated = CustomNumericComparisonExpression.LT.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("lteArguments")
  @SneakyThrows
  void shouldReturnExpected_whenLteEvaluate_givenInput(List<Integer> arguments, boolean expected) {
    //Given
    //When
    Object evaluated = CustomNumericComparisonExpression.LTE.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("ltLtArguments")
  @SneakyThrows
  void shouldReturnExpected_whenLtLtEvaluate_givenInput(List<Integer> arguments, boolean expected) {
    //Given
    //When
    Object evaluated = CustomNumericComparisonExpression.LT_LT.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("lteLteArguments")
  @SneakyThrows
  void shouldReturnExpected_whenLte_givenInput(List<Integer> arguments, boolean expected) {
    //Given
    //When
    Object evaluated = CustomNumericComparisonExpression.LTE_LTE.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("ltLteArguments")
  @SneakyThrows
  void shouldReturnExpected_whenLtLte_givenInput(List<Integer> arguments, boolean expected) {
    //Given
    //When
    Object evaluated = CustomNumericComparisonExpression.LT_LTE.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("lteLtArguments")
  @SneakyThrows
  void shouldReturnExpected_whenLteLt_givenInput(List<Integer> arguments, boolean expected) {
    //Given
    //When
    Object evaluated = CustomNumericComparisonExpression.LTE_LT.evaluate(arguments, null);

    //Then
    assertThat(evaluated).isEqualTo(expected);
  }

  private static Stream<Arguments> gtArguments() {
    return Stream.of(
      Arguments.of(List.of(2, 1), true),
      Arguments.of(List.of(2, 2), false),
      Arguments.of(List.of(1, 2), false)
    );
  }

  private static Stream<Arguments> gteArguments() {
    return Stream.of(
      Arguments.of(List.of(2, 1), true),
      Arguments.of(List.of(2, 2), true),
      Arguments.of(List.of(1, 2), false)
    );
  }

  private static Stream<Arguments> ltArguments() {
    return Stream.of(
      Arguments.of(List.of(2, 1), false),
      Arguments.of(List.of(2, 2), false),
      Arguments.of(List.of(1, 2), true)
    );
  }

  private static Stream<Arguments> lteArguments() {
    return Stream.of(
      Arguments.of(List.of(2, 1), false),
      Arguments.of(List.of(2, 2), true),
      Arguments.of(List.of(1, 2), true)
    );
  }

  private static Stream<Arguments> ltLtArguments() {
    return Stream.of(
      Arguments.of(List.of(1, 2, 3), true),
      Arguments.of(List.of(2, 1, 3), false),
      Arguments.of(List.of(3, 2, 1), false),
      Arguments.of(List.of(1, 1, 1), false)
    );
  }

  private static Stream<Arguments> lteLteArguments() {
    return Stream.of(
      Arguments.of(List.of(1, 2, 3), true),
      Arguments.of(List.of(1, 1, 1), true),
      Arguments.of(List.of(1, 1, 3), true),
      Arguments.of(List.of(2, 1, 3), false),
      Arguments.of(List.of(3, 2, 1), false)
    );
  }

  private static Stream<Arguments> ltLteArguments() {
    return Stream.of(
      Arguments.of(List.of(1, 2, 3), true),
      Arguments.of(List.of(1, 3, 3), true),
      Arguments.of(List.of(1, 1, 3), false),
      Arguments.of(List.of(2, 1, 3), false),
      Arguments.of(List.of(3, 2, 1), false),
      Arguments.of(List.of(1, 1, 1), false)
    );
  }

  private static Stream<Arguments> lteLtArguments() {
    return Stream.of(
      Arguments.of(List.of(1, 2, 3), true),
      Arguments.of(List.of(1, 1, 3), true),
      Arguments.of(List.of(1, 3, 3), false),
      Arguments.of(List.of(2, 1, 3), false),
      Arguments.of(List.of(3, 2, 1), false),
      Arguments.of(List.of(1, 1, 1), false)
    );
  }
}
