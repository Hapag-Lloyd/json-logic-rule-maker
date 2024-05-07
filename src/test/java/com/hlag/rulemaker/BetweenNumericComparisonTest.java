package com.hlag.rulemaker;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static com.hlag.rulemaker.RuleMaker.literal;
import static org.assertj.core.api.Assertions.assertThat;

class BetweenNumericComparisonTest {

  @ParameterizedTest
  @MethodSource("ltArguments")
  void shouldReturnExpected_WhenLt_GivenInput(int left, int middle, int right, boolean expected) {
    // Given
    RuleMaker ruleWright = RuleMaker.lt(literal(left), literal(middle), literal(right));

    // When
    Object evaluated = ruleWright.evaluate(Map.of());

    // Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("lteArguments")
  void shouldReturnExpected_WhenLte_GivenInput(int left, int middle, int right, boolean expected) {
    // Given
    RuleMaker ruleWright = RuleMaker.lte(literal(left), literal(middle), literal(right));

    // When
    Object evaluated = ruleWright.evaluate(Map.of());

    // Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("ltLteArguments")
  void shouldReturnExpected_WhenLtLte_GivenInput(int left, int middle, int right, boolean expected) {
    // Given
    RuleMaker ruleWright = RuleMaker.ltLte(literal(left), literal(middle), literal(right));

    // When
    Object evaluated = ruleWright.evaluate(Map.of());

    // Then
    assertThat(evaluated).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("lteLtArguments")
  void shouldReturnExpected_WhenLteLt_GivenInput(int left, int middle, int right, boolean expected) {
    // Given
    RuleMaker ruleWright = RuleMaker.lteLt(literal(left), literal(middle), literal(right));

    // When
    Object evaluated = ruleWright.evaluate(Map.of());

    // Then
    assertThat(evaluated).isEqualTo(expected);
  }

  private static Stream<Arguments> ltArguments() {
    return Stream.of(
        Arguments.of(1, 2, 3, true),
        Arguments.of(2, 1, 3, false),
        Arguments.of(3, 2, 1, false),
        Arguments.of(1, 1, 1, false)
    );
  }

  private static Stream<Arguments> lteArguments() {
    return Stream.of(
        Arguments.of(1, 2, 3, true),
        Arguments.of(1, 1, 1, true),
        Arguments.of(1, 1, 3, true),
        Arguments.of(2, 1, 3, false),
        Arguments.of(3, 2, 1, false)
    );
  }

  private static Stream<Arguments> ltLteArguments() {
    return Stream.of(
        Arguments.of(1, 2, 3, true),
        Arguments.of(1, 3, 3, true),
        Arguments.of(1, 1, 3, false),
        Arguments.of(2, 1, 3, false),
        Arguments.of(3, 2, 1, false),
        Arguments.of(1, 1, 1, false)
    );
  }

  private static Stream<Arguments> lteLtArguments() {
    return Stream.of(
        Arguments.of(1, 2, 3, true),
        Arguments.of(1, 1, 3, true),
        Arguments.of(1, 3, 3, false),
        Arguments.of(2, 1, 3, false),
        Arguments.of(3, 2, 1, false),
        Arguments.of(1, 1, 1, false)
    );
  }
}
