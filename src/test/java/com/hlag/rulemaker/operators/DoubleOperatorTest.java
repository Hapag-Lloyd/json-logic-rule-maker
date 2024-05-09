package com.hlag.rulemaker.operators;

import static com.hlag.rulemaker.operators.SingleOperator.LOWER;
import static com.hlag.rulemaker.operators.SingleOperator.LOWER_OR_EQUAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DoubleOperatorTest {

  @ParameterizedTest
  @MethodSource("correctDoubleOperatorDate")
  void shouldCreateDoubleOperator_whenBuildDoubleOperatorFromMap(Map<String, Object> doubleNumericOperatorMap, DoubleOperator expected) {

    //Given
    //When
    DoubleOperator result = DoubleOperator.build(doubleNumericOperatorMap);

    //Then
    assertThat(result).isEqualTo(expected);
  }

  private static Stream<Arguments> correctDoubleOperatorDate() {
    return Stream.of(
        Arguments.of(Map.of("< <", List.of()), new DoubleOperator(LOWER, LOWER)),
        Arguments.of(Map.of("< <=", List.of()), new DoubleOperator(LOWER, LOWER_OR_EQUAL)),
        Arguments.of(Map.of("<= <", List.of()), new DoubleOperator(LOWER_OR_EQUAL, LOWER)),
        Arguments.of(Map.of("<= <=", List.of()), new DoubleOperator(LOWER_OR_EQUAL, LOWER_OR_EQUAL))
    );
  }

  @Test
  void shouldThrowException_whenBuildFromWrongOperatorInMap() {
    //Given
    Map<String, Object> source = Map.of("< <1", List.of());

    //When
    //Then
    assertThatThrownBy(() -> DoubleOperator.build(source))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("No such operator: \"< <1\"");
  }
}
