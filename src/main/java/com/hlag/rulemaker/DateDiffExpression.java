package com.hlag.rulemaker;

import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.expressions.PreEvaluatedArgumentsExpression;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DateDiffExpression implements PreEvaluatedArgumentsExpression {

  public static final DateDiffExpression INSTANCE = new DateDiffExpression();

  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private DateDiffExpression() {
  }

  @Override
  public String key() {
    return "dateDiff";
  }

  @SuppressWarnings({ "rawtypes", "squid:S1166", "squid:S818" })
  @Override
  public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {

    if (arguments.size() != 2) {
      throw new JsonLogicEvaluationException(
          "dateDiff expects exactly 2 arguments: date1(YYYY-MM-DD), date2(YYYY-MM-DD)");
    }
    try {
      
      String date1 = StringUtils.left(Objects.toString(arguments.get(0)), 10);
      String date2 = StringUtils.left(Objects.toString(arguments.get(1)), 10);

      LocalDate d1 = LocalDate.parse(date1, formatter);
      LocalDate d2 = LocalDate.parse(date2, formatter);
      long days = ChronoUnit.DAYS.between(d1, d2);
      log.info("Date1: {}, Date2: {}, Days: {}", date1, date2, days);
      return days;
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    return 0l;
  }

}
