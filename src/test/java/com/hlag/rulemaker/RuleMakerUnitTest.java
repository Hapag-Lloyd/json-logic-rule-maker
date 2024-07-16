package com.hlag.rulemaker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hlag.rulemaker.exception.RuleMakerEvaluationException;
import com.hlag.rulemaker.exception.RuleMakerMissingVariablesException;
import com.hlag.rulemaker.expression.DayType;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RuleMakerUnitTest {

  @ParameterizedTest
  @MethodSource("missingArguments")
  void shouldEvaluateMissing_whenEvaluate_givenMissing(List<String> literals, Map<String, Object> data,
    List<String> expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.missing(literals.stream().map(RuleMaker::literal).toArray(RuleMaker[]::new));

    //When
    Object value = ruleMaker.evaluate(data);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("missingSomeArguments")
  void shouldEvaluateMissingSome_whenEvaluate_givenMissingSome(int count, List<String> literals, Map<String, Object> data,
    List<String> expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.missingSome(count, literals.stream().map(RuleMaker::literal).toArray(RuleMaker[]::new));

    //When
    Object value = ruleMaker.evaluate(data);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("ifThenElseArguments")
  void shouldEvaluateIfThenElse_whenEvaluate_givenIfThenElse(boolean condition, String thenLiteral, String elseLiteral, String expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.ifThenElse(RuleMaker.literal(condition), RuleMaker.literal(thenLiteral),
      RuleMaker.literal(elseLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("eqArguments")
  void shouldEvaluateEq_whenEvaluate_givenEq(Object leftLiteral, Object rightLiteral, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.eq(RuleMaker.literal(leftLiteral), RuleMaker.literal(rightLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("notEqArguments")
  void shouldEvaluateNotEq_whenEvaluate_givenNotEq(Object leftLiteral, Object rightLiteral, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.notEq(RuleMaker.literal(leftLiteral), RuleMaker.literal(rightLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("negateArguments")
  void shouldEvaluateNegate_whenEvaluate_givenNegate(boolean valueLiteral, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.negate(RuleMaker.literal(valueLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("orArguments")
  void shouldEvaluateOr_whenEvaluate_givenOr(boolean firstLiteral, boolean secondLiteral, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.or(RuleMaker.literal(firstLiteral), RuleMaker.literal(secondLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("andArguments")
  void shouldEvaluateAnd_whenEvaluate_givenAnd(boolean firstLiteral, boolean secondLiteral, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.and(RuleMaker.literal(firstLiteral), RuleMaker.literal(secondLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("gtArguments")
  void shouldEvaluateGt_whenEvaluate_givenGt(BigDecimal leftLiteral, BigDecimal rightLiteral, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.gt(RuleMaker.literal(leftLiteral), RuleMaker.literal(rightLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("gteArguments")
  void shouldEvaluateGte_whenEvaluate_givenGte(BigDecimal leftLiteral, BigDecimal rightLiteral, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.gte(RuleMaker.literal(leftLiteral), RuleMaker.literal(rightLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("ltArguments")
  void shouldEvaluateLt_whenEvaluate_givenLt(BigDecimal leftLiteral, BigDecimal rightLiteral, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.lt(RuleMaker.literal(leftLiteral), RuleMaker.literal(rightLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("lteArguments")
  void shouldEvaluateLte_whenEvaluate_givenLte(BigDecimal leftLiteral, BigDecimal rightLiteral, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.lte(RuleMaker.literal(leftLiteral), RuleMaker.literal(rightLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("lteLteArguments")
  void shouldEvaluateLteLte_whenEvaluate_givenLteLte(BigDecimal leftLiteral, BigDecimal middleLiteral, BigDecimal rightLiteral,
    boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.lte(RuleMaker.literal(leftLiteral), RuleMaker.literal(middleLiteral), RuleMaker.literal(rightLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("ltLtArguments")
  void shouldEvaluateLtLt_whenEvaluate_givenLtLt(BigDecimal leftLiteral, BigDecimal middleLiteral, BigDecimal rightLiteral,
    boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.lt(RuleMaker.literal(leftLiteral), RuleMaker.literal(middleLiteral), RuleMaker.literal(rightLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("lteLtArguments")
  void shouldEvaluateLteLt_whenEvaluate_givenLteLt(BigDecimal leftLiteral, BigDecimal middleLiteral, BigDecimal rightLiteral,
    boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.lteLt(RuleMaker.literal(leftLiteral), RuleMaker.literal(middleLiteral),
      RuleMaker.literal(rightLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("ltLteArguments")
  void shouldEvaluateLtLte_whenEvaluate_givenLtLte(BigDecimal leftLiteral, BigDecimal middleLiteral, BigDecimal rightLiteral,
    boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.ltLte(RuleMaker.literal(leftLiteral), RuleMaker.literal(middleLiteral),
      RuleMaker.literal(rightLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("maxArguments")
  void shouldEvaluateMax_whenEvaluate_givenMax(BigDecimal firstLiteral, BigDecimal secondLiteral, BigDecimal expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.max(RuleMaker.literal(firstLiteral), RuleMaker.literal(secondLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("minArguments")
  void shouldEvaluateMin_whenEvaluate_givenMin(BigDecimal firstLiteral, BigDecimal secondLiteral, BigDecimal expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.min(RuleMaker.literal(firstLiteral), RuleMaker.literal(secondLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("addArguments")
  void shouldEvaluateAdd_whenEvaluate_givenAdd(BigDecimal firstLiteral, BigDecimal secondLiteral, BigDecimal thirdLiteral,
    BigDecimal expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.add(RuleMaker.literal(firstLiteral), RuleMaker.literal(secondLiteral), RuleMaker.literal(thirdLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("subArguments")
  void shouldEvaluateSub_whenEvaluate_givenSub(BigDecimal firstLiteral, BigDecimal secondLiteral, BigDecimal expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.sub(RuleMaker.literal(firstLiteral), RuleMaker.literal(secondLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("mulArguments")
  void shouldEvaluateMul_whenEvaluate_givenMul(BigDecimal firstLiteral, BigDecimal secondLiteral, BigDecimal thirdLiteral,
    BigDecimal expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.mul(RuleMaker.literal(firstLiteral), RuleMaker.literal(secondLiteral), RuleMaker.literal(thirdLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("divArguments")
  void shouldEvaluateDiv_whenEvaluate_givenDiv(BigDecimal firstLiteral, BigDecimal secondLiteral, BigDecimal expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.div(RuleMaker.literal(firstLiteral), RuleMaker.literal(secondLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("modArguments")
  void shouldEvaluateMod_whenEvaluate_givenMod(BigDecimal firstLiteral, BigDecimal secondLiteral, BigDecimal expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.mod(RuleMaker.literal(firstLiteral), RuleMaker.literal(secondLiteral));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @Test
  void shouldEvaluateMap_whenEvaluate_givenMap() {
    //Given
    Map<String, Object> data = Map.of("numbers", List.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3")));
    RuleMaker ruleMaker = RuleMaker.map(RuleMaker.var("numbers"), RuleMaker.mul(RuleMaker.var(""), RuleMaker.literal(new BigDecimal("3"))));

    List<BigDecimal> expected = List.of(new BigDecimal("3.00"), new BigDecimal("6.00"), new BigDecimal("9.00"));

    //When
    Object value = ruleMaker.evaluate(data);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @Test
  void shouldEvaluateFilter_whenEvaluate_givenFilter() {
    //Given
    Map<String, Object> data = Map.of("numbers", List.of(1.0, 2.0, 3.0));
    RuleMaker ruleMaker = RuleMaker.filter(RuleMaker.var("numbers"), RuleMaker.mod(RuleMaker.var(""), RuleMaker.literal(2)));

    List<Double> expected = List.of(1.0, 3.0);

    //When
    Object value = ruleMaker.evaluate(data);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @Test
  void shouldEvaluateReduce_whenEvaluate_givenReduce() {
    //Given
    Map<String, Object> data = Map.of("numbers", List.of(new BigDecimal("1.0"), new BigDecimal("2.0"), new BigDecimal("3.0")));
    RuleMaker ruleMaker = RuleMaker.reduce(RuleMaker.var("numbers"), RuleMaker.add(RuleMaker.var("current"),
      RuleMaker.var("accumulator")), RuleMaker.literal(0));

    BigDecimal expected = new BigDecimal("6.0");

    //When
    Object value = ruleMaker.evaluate(data);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("allArguments")
  void shouldEvaluateAll_whenEvaluate_givenAll(Integer[] numbers, Integer greaterThan, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.all(RuleMaker.literal(numbers), RuleMaker.gt(RuleMaker.var(""), RuleMaker.literal(greaterThan)));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("noneArguments")
  void shouldEvaluateNone_whenEvaluate_givenNone(Integer[] numbers, Integer greaterThan, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.none(RuleMaker.literal(numbers), RuleMaker.gt(RuleMaker.var(""), RuleMaker.literal(greaterThan)));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("someArguments")
  void shouldEvaluateSome_whenEvaluate_givenSome(Integer[] numbers, Integer greaterThan, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.some(RuleMaker.literal(numbers), RuleMaker.gt(RuleMaker.var(""), RuleMaker.literal(greaterThan)));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @Test
  void shouldEvaluateMerge_whenEvaluate_givenMerge() {
    //Given
    Double[] firstArray = new Double[]{1.0, 2.0};
    Double[] secondArray = new Double[]{3.0, 4.0, 5.0};

    RuleMaker ruleMaker = RuleMaker.merge(RuleMaker.literal(firstArray), RuleMaker.literal(secondArray));
    List<Double> expected = List.of(1.0, 2.0, 3.0, 4.0, 5.0);

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("inArguments")
  void shouldEvaluateIn_whenEvaluate_givenIn(String checkValue, Object values, boolean expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.in(RuleMaker.literal(checkValue), RuleMaker.literal(values));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("catArguments")
  void shouldEvaluateCat_whenEvaluate_givenCat(String[] values, String expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.cat(Arrays.stream(values).map(RuleMaker::literal).toArray(RuleMaker[]::new));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("substrArguments")
  void shouldEvaluateSubstr_whenEvaluate_givenSubstr(String stringValue, Integer startIndex, Integer length, String expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.substr(RuleMaker.literal(stringValue), RuleMaker.literal(startIndex), RuleMaker.literal(length));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @Test
  void shouldEvaluateLog_whenEvaluate_givenLog() {
    //Given
    String stringValue = "banana";

    RuleMaker ruleMaker = RuleMaker.log(stringValue);

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(stringValue);
  }

  @ParameterizedTest
  @MethodSource("dateDiffArguments")
  void shouldEvaluateDateDiff_whenEvaluate_givenDateDiff(String firstDate, String secondDate, DayType measuringRule,
    Long expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.dateDiff(RuleMaker.literal(secondDate), RuleMaker.literal(firstDate),
      RuleMaker.literal(measuringRule.getValue()));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("clampArguments")
  void shouldEvaluateClamp_whenEvaluate_givenClamp(BigDecimal number, BigDecimal min, BigDecimal max, BigDecimal expected) {
    //Given
    RuleMaker ruleMaker = RuleMaker.clamp(RuleMaker.literal(number), RuleMaker.literal(min), RuleMaker.literal(max));

    //When
    Object value = ruleMaker.evaluate(null);

    //Then
    assertThat(value).isEqualTo(expected);
  }

  @Test
  void shouldThrowException_whenEvaluate_givenMissingVariable() {
    //Given
    Map<String, Object> data = Map.of();
    RuleMaker ruleMaker = RuleMaker.var("x");

    //When
    //Then
    assertThatThrownBy(() -> ruleMaker.evaluate(data))
      .isInstanceOf(RuleMakerMissingVariablesException.class)
      .hasMessage("Missing variables: [x]");
  }

  @Test
  void shouldThrowException_whenEvaluate_givenWrongDataFormat() {
    //Given
    Map<String, Object> data = Map.of("x", "wrong_format");
    RuleMaker ruleMaker = RuleMaker.add(RuleMaker.literal(1), RuleMaker.var("x"));

    //When
    //Then
    assertThatThrownBy(() -> ruleMaker.evaluate(data))
      .isInstanceOf(RuleMakerEvaluationException.class)
      .hasMessage("Non numeric argument: wrong_format");
  }

  private static Stream<Arguments> missingArguments() {
    return Stream.of(
      Arguments.of(List.of("x", "y"), Map.of("x", 1, "z", 1), List.of("y")),
      Arguments.of(List.of("x"), Map.of("x", 1), List.of())
    );
  }

  private static Stream<Arguments> missingSomeArguments() {
    return Stream.of(
      Arguments.of(1, List.of("x", "y"), Map.of("x", 1, "z", 1), List.of()),
      Arguments.of(2, List.of("x", "y", "z"), Map.of("x", 1), List.of("y", "z"))
    );
  }

  private static Stream<Arguments> ifThenElseArguments() {
    return Stream.of(
      Arguments.of(true, "yes", "no", "yes"),
      Arguments.of(false, "yes", "no", "no")
    );
  }

  private static Stream<Arguments> eqArguments() {
    return Stream.of(
      Arguments.of(1, 1, true),
      Arguments.of(1, "1", true),
      Arguments.of(0, false, true),
      Arguments.of(1, 2, false)
    );
  }

  private static Stream<Arguments> notEqArguments() {
    return Stream.of(
      Arguments.of(1, 2, true),
      Arguments.of(1, "1", false)
    );
  }

  private static Stream<Arguments> negateArguments() {
    return Stream.of(
      Arguments.of(true, false),
      Arguments.of(false, true)
    );
  }

  private static Stream<Arguments> orArguments() {
    return Stream.of(
      Arguments.of(true, false, true),
      Arguments.of(false, false, false),
      Arguments.of(true, true, true)
    );
  }

  private static Stream<Arguments> andArguments() {
    return Stream.of(
      Arguments.of(true, false, false),
      Arguments.of(false, false, false),
      Arguments.of(true, true, true)
    );
  }

  private static Stream<Arguments> gtArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("2"), new BigDecimal("1"), true),
      Arguments.of(new BigDecimal("2"), new BigDecimal("2"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), false)
    );
  }

  private static Stream<Arguments> gteArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("2"), new BigDecimal("1"), true),
      Arguments.of(new BigDecimal("2"), new BigDecimal("2"), true),
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), false)
    );
  }

  private static Stream<Arguments> ltArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("2"), new BigDecimal("1"), false),
      Arguments.of(new BigDecimal("2"), new BigDecimal("2"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), true)
    );
  }

  private static Stream<Arguments> lteArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("2"), new BigDecimal("1"), false),
      Arguments.of(new BigDecimal("2"), new BigDecimal("2"), true),
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), true)
    );
  }

  private static Stream<Arguments> lteLteArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"), true),
      Arguments.of(new BigDecimal("1"), new BigDecimal("3"), new BigDecimal("2"), false),
      Arguments.of(new BigDecimal("3"), new BigDecimal("1"), new BigDecimal("2"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("3"), true),
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("2"), true),
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("1"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1"), true)
    );
  }

  private static Stream<Arguments> ltLtArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"), true),
      Arguments.of(new BigDecimal("1"), new BigDecimal("3"), new BigDecimal("2"), false),
      Arguments.of(new BigDecimal("3"), new BigDecimal("1"), new BigDecimal("2"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("3"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("2"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("1"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1"), false)
    );
  }

  private static Stream<Arguments> lteLtArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"), true),
      Arguments.of(new BigDecimal("1"), new BigDecimal("3"), new BigDecimal("2"), false),
      Arguments.of(new BigDecimal("3"), new BigDecimal("1"), new BigDecimal("2"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("3"), true),
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("2"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("1"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1"), false)
    );
  }

  private static Stream<Arguments> ltLteArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"), true),
      Arguments.of(new BigDecimal("1"), new BigDecimal("3"), new BigDecimal("2"), false),
      Arguments.of(new BigDecimal("3"), new BigDecimal("1"), new BigDecimal("2"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("3"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("2"), true),
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("1"), false),
      Arguments.of(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1"), false)
    );
  }

  private static Stream<Arguments> maxArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("2.0")),
      Arguments.of(new BigDecimal("2"), new BigDecimal("1"), new BigDecimal("2.0")),
      Arguments.of(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1.0"))
    );
  }

  private static Stream<Arguments> minArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("1.0")),
      Arguments.of(new BigDecimal("2"), new BigDecimal("1"), new BigDecimal("1.0")),
      Arguments.of(new BigDecimal("1"), new BigDecimal("1"), new BigDecimal("1.0"))
    );
  }

  private static Stream<Arguments> addArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"), new BigDecimal("6.0")),
      Arguments.of(new BigDecimal("-1"), new BigDecimal("-2"), new BigDecimal("3"), new BigDecimal("0.0"))
    );
  }

  private static Stream<Arguments> subArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("3"), new BigDecimal("2"), new BigDecimal("1.0")),
      Arguments.of(new BigDecimal("-1"), new BigDecimal("-2"), new BigDecimal("1.0"))
    );
  }

  private static Stream<Arguments> mulArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("2"), new BigDecimal("3"), new BigDecimal("4"), new BigDecimal("24.000")),
      Arguments.of(new BigDecimal("-1"), new BigDecimal("-2"), new BigDecimal("3"), new BigDecimal("6.000"))
    );
  }

  private static Stream<Arguments> divArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("3"), new BigDecimal("2"), new BigDecimal("1.5")),
      Arguments.of(new BigDecimal("-1"), new BigDecimal("-2"), new BigDecimal("0.5"))
    );
  }

  private static Stream<Arguments> modArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("3"), new BigDecimal("2"), new BigDecimal("1.0")),
      Arguments.of(new BigDecimal("-1"), new BigDecimal("-2"), new BigDecimal("-1.0"))
    );
  }

  private static Stream<Arguments> allArguments() {
    return Stream.of(
      Arguments.of(new Integer[]{1, 2, 3, 4}, 2, false),
      Arguments.of(new Integer[]{1, 2, 3}, 4, false),
      Arguments.of(new Integer[]{2, 3, 4}, 1, true)
    );
  }

  private static Stream<Arguments> noneArguments() {
    return Stream.of(
      Arguments.of(new Integer[]{1, 2, 3, 4}, 2, false),
      Arguments.of(new Integer[]{1, 2, 3}, 4, true),
      Arguments.of(new Integer[]{2, 3, 4}, 1, false)
    );
  }

  private static Stream<Arguments> someArguments() {
    return Stream.of(
      Arguments.of(new Integer[]{1, 2, 3, 4}, 2, true),
      Arguments.of(new Integer[]{1, 2, 3}, 4, false),
      Arguments.of(new Integer[]{2, 3, 4}, 1, true)
    );
  }

  private static Stream<Arguments> inArguments() {
    return Stream.of(
      Arguments.of("banana", new String[]{"apple", "orange", "banana"}, true),
      Arguments.of("kiwi", new String[]{"apple", "orange", "banana"}, false),
      Arguments.of("kiwi", "kiwimaster", true)
    );
  }

  private static Stream<Arguments> catArguments() {
    return Stream.of(
      Arguments.of(new String[]{"I ", "like", " bananas"}, "I like bananas"),
      Arguments.of(new String[]{"", ""}, "")
    );
  }

  private static Stream<Arguments> substrArguments() {
    return Stream.of(
      Arguments.of("banana", 3, 3, "ana"),
      Arguments.of("banana", 0, 6, "banana")
    );
  }

  private static Stream<Arguments> dateDiffArguments() {
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

  private static Stream<Arguments> clampArguments() {
    return Stream.of(
      Arguments.of(new BigDecimal("2.0"), new BigDecimal("1.0"), new BigDecimal("3.0"), new BigDecimal("2.0")),
      Arguments.of(new BigDecimal("2.0"), new BigDecimal("3.0"), new BigDecimal("4.0"), new BigDecimal("3.0")),
      Arguments.of(new BigDecimal("4.0"), new BigDecimal("1.0"), new BigDecimal("3.0"), new BigDecimal("3.0")),
      Arguments.of(new BigDecimal("2.0"), new BigDecimal("2.0"), new BigDecimal("2.0"), new BigDecimal("2.0"))
    );
  }
}
