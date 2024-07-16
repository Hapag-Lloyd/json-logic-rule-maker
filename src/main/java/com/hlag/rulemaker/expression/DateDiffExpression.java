package com.hlag.rulemaker.expression;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.expressions.PreEvaluatedArgumentsExpression;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Date diff expression that represents difference between two dates in Days.
 */
@Slf4j
@NoArgsConstructor
public class DateDiffExpression implements PreEvaluatedArgumentsExpression {

  public static final DateDiffExpression INSTANCE = new DateDiffExpression();
  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
  private static final int DATE_NOW_INDEX = 1;
  private static final int MEASURING_RULE_INDEX = 2;
  private static final int MEASURING_POINT_DATE_INDEX = 0;

  @Override
  public String key() {
    return "dateDiff";
  }

  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {

    if (arguments.size() != 3) {
      throw new JsonLogicEvaluationException(
        "dateDiffExpression expects exactly 3 arguments: dateTo(YYYY-MM-DD), dateNow(YYYY-MM-DD), measuringRule"
      );
    }
    LocalDate dateNow = extractDate(arguments.get(DATE_NOW_INDEX).toString());
    LocalDate measuringPointDate = extractDate(arguments.get(MEASURING_POINT_DATE_INDEX).toString());

    DayType measuringRule = DayType.fromValue(arguments.get(MEASURING_RULE_INDEX).toString());

    long dateDiff = 0L;
    if (measuringRule.equals(DayType.CALENDAR_DAYS)) {
      dateDiff = ChronoUnit.DAYS.between(dateNow, measuringPointDate);
    } else if (measuringRule.equals(DayType.BUSINESS_DAYS)) {
      dateDiff = daysWithoutWeekend(dateNow, measuringPointDate);
    }
    return dateDiff;
  }

  private static LocalDate extractDate(String dateAsString) {
    try {
      return LocalDate.parse(dateAsString, formatter);
    } catch (DateTimeParseException exception) {
      throw new IllegalArgumentException("Wrong date as argument: " + dateAsString);
    }
  }

  private long daysWithoutWeekend(LocalDate start, LocalDate end) {
    long daysWithoutWeekends = 0;

    for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
      if (isDayOfWeek(date.getDayOfWeek())) {
        daysWithoutWeekends++;
      }
    }
    return daysWithoutWeekends;
  }

  private boolean isDayOfWeek(DayOfWeek dayOfWeek) {
    return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
  }
}
