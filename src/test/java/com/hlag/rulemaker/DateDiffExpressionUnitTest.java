package com.hlag.rulemaker;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateDiffExpressionUnitTest {

  DateDiffExpression dateDiffExpression = new DateDiffExpression();

  @ParameterizedTest
  @MethodSource("provideCorrectTestData")
  void shouldCalculateAmountOfCalendarDays_whenEvaluateWithSpecificDate(String dateNow, String dateTo, DayType measuringRule,
      Long expectedResult) throws JsonLogicEvaluationException {
    //Given
    List<String> arguments = List.of(dateTo, dateNow, measuringRule.getValue());

    //When
    Long result = (Long) dateDiffExpression.evaluate(arguments, null);

    //Then
    assertThat(result).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @MethodSource("provideIncorrectTestData")
  void shouldThrowException_whenEvaluateWithIncorrectDate(String dateNow, String dateTo, DayType measuringRule,
      Class exception, String exceptionMessage) {
    //Given
    List<String> arguments = List.of(dateTo, dateNow, measuringRule.getValue());

    //When
    //Then
    assertThatThrownBy(() -> dateDiffExpression.evaluate(arguments, null))
        .isInstanceOf(exception)
        .hasMessage(exceptionMessage);
  }

  @Test
  void shouldThrowException_whenEvaluateWithWrongArgumentAmount() {
    //Given
    List<String> arguments = List.of("2024-04-19", "2024-04-20", DayType.CALENDAR_DAYS.toString(), "WrongArg");

    //When
    //Then
    assertThatThrownBy(() -> dateDiffExpression.evaluate(arguments, null))
        .isInstanceOf(JsonLogicEvaluationException.class)
        .hasMessage(
            "dateDiffExpression expects exactly 3 arguments: dateTo(YYYY-MM-DD), dateNow(YYYY-MM-DD), measuringRule");
  }

  @Test
  void shouldThrowException_whenIncorrectMeasuringRule() {
    //Given
    List<String> arguments = List.of("2024-04-19", "2024-04-20", "Incorrect");

    //When
    //Then
    assertThatThrownBy(() -> dateDiffExpression.evaluate(arguments, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Unexpected value 'Incorrect'");
  }

  private static Stream<Arguments> provideCorrectTestData() {
    return Stream.of(
        Arguments.of("2024-04-19", "2024-04-20", DayType.CALENDAR_DAYS, 1L),
        Arguments.of("2024-04-19", "2024-04-20", DayType.BUSINESS_DAYS, 1L),
        Arguments.of("2024-04-19", "2024-04-26", DayType.CALENDAR_DAYS, 7L),
        Arguments.of("2024-04-19", "2024-04-26", DayType.BUSINESS_DAYS, 5L),
        Arguments.of("2024-04-19", "2024-05-03", DayType.BUSINESS_DAYS, 10L),
        Arguments.of("2024-04-19", "2024-05-03", DayType.CALENDAR_DAYS, 14L),
        Arguments.of("2024-04-19", "2024-04-19", DayType.CALENDAR_DAYS, 0L),
        Arguments.of("2024-04-19", "2024-04-19", DayType.CALENDAR_DAYS, 0L),
        Arguments.of("2024-04-20", "2024-04-21", DayType.BUSINESS_DAYS, 0L)
    );
  }


  private static Stream<Arguments> provideIncorrectTestData() {
    return Stream.of(
        Arguments.of("", "2024-05-03", DayType.CALENDAR_DAYS, IllegalArgumentException.class, "Wrong date as argument: "),
        Arguments.of("2024-02-31", "2024-03-03", DayType.CALENDAR_DAYS, IllegalArgumentException.class,
            "Wrong date as argument: 2024-02-31"),
        Arguments.of("2024-05-02", "", DayType.CALENDAR_DAYS, IllegalArgumentException.class, "Wrong date as argument: ")
    );
  }
}
