package com.hlag.rulemaker;

import java.util.Map;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WrappedRuleMakerUnitTest {

    private static final String JSON_LOGIC_FIRST = "{\"clamp\":[{\"*\":[{\"+\":[{\"var\":\"SEA\"},{\"var\":\"MFR\"}]},60.0]},5,100]}";
    private static final String JSON_LOGIC_SECOND = "{\"in\":[{\"var\":\"EQUIPMENT_SIZE\"},[\"EQ_20\",\"EQ_40\"]]}";

    private static final int MUL = 0, ADD = 0, VAR_SEA = 0, SEA = 0, CLAMP_100 = 2;
    private static final int EQ = 1;

    @Test
    void shouldReturnSea_whenUnwrapAndGetLiteral_givenInput() {
        // Given
        WrappedRuleMaker sut = new WrappedRuleMaker(getJsonLogicMap(JSON_LOGIC_FIRST));
        String expected = "SEA";

        // When
        String sea = sut.unwrap()[MUL].unwrap()[ADD].unwrap()[VAR_SEA].unwrap()[SEA].getLiteral().toString();

        // Then
        assertThat(sea).isEqualTo(expected);
    }

    @Test
    void shouldReturnClampSecondArg_whenUnwrapAndGetLiteral_givenInput() {
        // Given
        WrappedRuleMaker sut = new WrappedRuleMaker(getJsonLogicMap(JSON_LOGIC_FIRST));
        int expected = 100;

        // When
        int clampSecondArg = (Integer) sut.unwrap()[CLAMP_100].getLiteral();

        // Then
        assertThat(clampSecondArg).isEqualTo(expected);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldReturnEquipmentSizeList_whenGetLiteral_givenInput() {
        // Given
        WrappedRuleMaker sut = new WrappedRuleMaker(getJsonLogicMap(JSON_LOGIC_SECOND));
        List<String> expected = List.of("EQ_20", "EQ_40");

        // When
        List<String> equipmentSizes = (List<String>) sut.unwrap()[EQ].getLiteral();

        // Then
        assertThat(equipmentSizes).containsExactlyElementsOf(expected);
    }

    @Test
    void shouldThrowException_whenGetLiteral_givenNonLiteral() {
        // Given
        WrappedRuleMaker sut = new WrappedRuleMaker(getJsonLogicMap(JSON_LOGIC_FIRST));
        WrappedRuleMaker givenWrappedRuleWright = sut.unwrap()[MUL];

        // When
        // Then
        assertThatThrownBy(givenWrappedRuleWright::getLiteral).isInstanceOf(IllegalStateException.class)
                .hasMessage("Expression is not a literal");
    }

    @Test
    void shouldThrowException_whenUnwrap_givenLiteral() {
        // Given
        WrappedRuleMaker sut = new WrappedRuleMaker(getJsonLogicMap(JSON_LOGIC_FIRST));
        WrappedRuleMaker givenWrappedRuleWright = sut.unwrap()[CLAMP_100];

        // When
        // Then
        assertThatThrownBy(givenWrappedRuleWright::unwrap).isInstanceOf(IllegalStateException.class)
                .hasMessage("Expression is already a literal");
    }

    @Test
    void shouldReturnTopLevelOperator_whenGetTopLevelOperator() {
      //Given
      Map<String, Object> source = Map.of("< <", List.of());
      WrappedRuleMaker sut = new WrappedRuleMaker(source);

      //When
      String result = sut.getTopLevelOperator();

      //Then
      assertThat(result).isEqualTo("< <");
    }

    private Object getJsonLogicMap(String jsonLogic) {
        return new JSONObject(jsonLogic).toMap();
    }
}
