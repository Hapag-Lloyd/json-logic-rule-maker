package com.hlag.rulemaker;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import io.github.jamsesso.jsonlogic.JsonLogic;
import io.github.jamsesso.jsonlogic.JsonLogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * RuleMaker is a simple JSON expression builder that can be evaluated with
 * data using JSONLogic.
 */
public class RuleMaker {

    private static final Logger log = LoggerFactory.getLogger(RuleMaker.class);

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private static final Type type = new TypeToken<Map<String, Object>>() {
    }.getType();

    private static final JsonLogic jsonLogic = new JsonLogic()
            .addOperation(ClampExpression.INSTANCE)
            .addOperation(DateDiffExpression.INSTANCE)
            .addOperation(ExtendedNumericComparisonExpression.LT_LTE)
            .addOperation(ExtendedNumericComparisonExpression.LT_LT)
            .addOperation(ExtendedNumericComparisonExpression.LTE_LTE)
            .addOperation(ExtendedNumericComparisonExpression.LTE_LT);

    private static final Set<Class<?>> ALLOWED_LITERAL_TYPES = Set.of(
            Number.class,
            String.class,
            Boolean.class,
            Arrays.class,
            List.class,
            Integer[].class
    );

    protected final Object expression;

    protected RuleMaker(Object expression) {
        this.expression = expression;
    }

    /**
     * Creates a RuleMaker representing a variable.
     *
     * @param name The name of the variable.
     * @return A new RuleMaker representing the variable.
     */
    @SuppressWarnings("squid:S6213")
    public static RuleMaker var(String name) {
        Preconditions.checkNotNull(name, "Variable name cannot be null.");
        return new RuleMaker(Map.of("var", name));
    }

    /**
     * Creates a RuleMaker representing a literal value.
     *
     * @param value The literal value.
     * @return A new RuleMaker representing the literal value.
     */
    public static RuleMaker literal(Object value) {
        if (value == null) {
            return new RuleMaker(null);
        }

        if (isAllowedLiteralType(value)) {
            return new RuleMaker(value);
        }
        return new RuleMaker(Map.of("literal", value));
    }

    private static boolean isAllowedLiteralType(Object value) {
        return ALLOWED_LITERAL_TYPES
                .stream()
                .anyMatch(clazz -> clazz.isInstance(value));
    }

    /**
     * Creates a RuleMaker representing an array of data keys to search for (same
     * format as var). Returns an array of any keys that are missing from the data
     * object, or an empty array.
     *
     * @param values The RuleMaker expression values to search for.
     * @return A new RuleMaker representing the missing values expression.
     */
    public static RuleMaker missing(RuleMaker... values) {
        return template("missing", values);
    }

    /**
     * Creates a RuleMaker representing an array of data keys to search for (same
     *
     * @param count  The min number of keys that must be not missing.
     * @param values The RuleMaker expression values to search for.
     * @return A new RuleMaker representing the missing values expressionm if not
     * meeting the minimum count.
     */
    @SuppressWarnings("null")
    public static RuleMaker missingSome(Integer count, RuleMaker... values) {
        return new RuleMaker(Map.of("missing_some",
                Arrays.asList(count, Arrays.stream(values)
                        .map(ruleWright -> ruleWright.expression)
                        .collect(Collectors.toList()))));
    }

    /**
     * Creates a RuleMaker representing an if-then-else conditional expression.
     *
     * @param condition The condition to evaluate.
     * @param then      The expression to evaluate if the condition is true.
     * @param _else     The expression to evaluate if the condition is false.
     * @return A new RuleMaker representing the if-then-else conditional
     */
    @SuppressWarnings({"null", "squid:S117"})
    public static RuleMaker ifThenElse(RuleMaker condition, RuleMaker then, RuleMaker _else) {
        return new RuleMaker(Map.of("if",
                Arrays.asList(condition.expression, then.expression, _else.expression)));
    }

    /**
     * Creates a RuleMaker representing an equality comparison.
     *
     * @param left  The left side of the comparison.
     * @param right The right side of the comparison.
     * @return A new RuleMaker representing the equality comparison.
     */
    @SuppressWarnings("null")
    public static RuleMaker eq(RuleMaker left, RuleMaker right) {
        return new RuleMaker(Map.of("==", Arrays.asList(left.expression, right.expression)));
    }

    /**
     * Creates a RuleMaker representing an inequality comparison.
     *
     * @param left  The left side of the comparison.
     * @param right The right side of the comparison.
     * @return A new RuleMaker representing the inequality comparison.
     */
    @SuppressWarnings("null")
    public static RuleMaker notEq(RuleMaker left, RuleMaker right) {
        return new RuleMaker(Map.of("!=", Arrays.asList(left.expression, right.expression)));
    }

    /**
     * Creates a RuleMaker representing a negation of the given value.
     *
     * @param value The value to negate.
     * @return A new RuleMaker representing the negation of the given value.
     */
    @SuppressWarnings("null")
    public static RuleMaker negate(RuleMaker value) {
        return new RuleMaker(Map.of("!", value.expression));
    }

    /**
     * Creates a RuleMaker representing a logical OR operation.
     *
     * @param values The RuleMaker expressions to OR together.
     * @return A new RuleMaker representing the logical OR operation.
     */
    public static RuleMaker or(RuleMaker... values) {
        return template("or", values);
    }

    /**
     * Creates a RuleMaker representing a logical AND operation.
     *
     * @param values The RuleMaker expressions to AND together.
     * @return A new RuleMaker representing the logical AND operation.
     */
    public static RuleMaker and(RuleMaker... values) {
        return template("and", values);
    }

    /**
     * Creates a RuleMaker representing a greater-than comparison.
     *
     * @param left  The left side of the comparison.
     * @param right The right side of the comparison.
     * @return A new RuleMaker representing the greater-than comparison.
     */
    public static RuleMaker gt(RuleMaker left, RuleMaker right) {
        return template(">", left, right);
    }

    /**
     * Creates a RuleMaker representing a greater-than-or-equal comparison.
     *
     * @param left  The left side of the comparison.
     * @param right The right side of the comparison.
     * @return A new RuleMaker representing the greater-than-or-equal comparison.
     */
    public static RuleMaker gte(RuleMaker left, RuleMaker right) {
        return template(">=", left, right);
    }

    /**
     * Creates a RuleMaker representing a less-than comparison.
     *
     * @param left  The left side of the comparison.
     * @param right The right side of the comparison.
     * @return A new RuleMaker representing the less-than comparison.
     */
    public static RuleMaker lt(RuleMaker left, RuleMaker right) {
        return template("<", left, right);
    }

    /**
     * Creates a RuleMaker representing a less-than-or-equal comparison.
     *
     * @param left  The left side of the comparison.
     * @param right The right side of the comparison.
     * @return A new RuleMaker representing the less-than-or-equal comparison.
     */
    public static RuleMaker lte(RuleMaker left, RuleMaker right) {
        return template("<=", left, right);
    }

    /**
     * Creates a RuleMaker representing a between less-than-or-equal comparison.
     *
     * @param left   The left side of the comparison.
     * @param middle The middle of the comparison.
     * @param right  The right side of the comparison.
     * @return A new RuleMaker representing the between x &lt;= z &lt;= y comparison
     */
    public static RuleMaker lte(RuleMaker left, RuleMaker middle, RuleMaker right) {
        return template("<= <=", left, middle, right);
    }

    /**
     * Creates a RuleMaker representing a between less-than comparison.
     *
     * @param left   The left side of the comparison.
     * @param middle The middle of the comparison.
     * @param right  The right side of the comparison.
     * @return A new RuleMaker representing the between x &lt; z &lt; y comparison
     */
    public static RuleMaker lt(RuleMaker left, RuleMaker middle, RuleMaker right) {
        return template("< <", left, middle, right);
    }

    /**
     * Creates a RuleMaker representing a between less-than-or-equal less-than comparison.
     *
     * @param left   The left side of the comparison.
     * @param middle The middle of the comparison.
     * @param right  The right side of the comparison.
     * @return A new RuleMaker representing the between x &lt;= z &lt; y comparison
     */
    public static RuleMaker lteLt(RuleMaker left, RuleMaker middle, RuleMaker right) {
        return template("<= <", left, middle, right);
    }

    /**
     * Creates a RuleMaker representing a between less-than less-than-or-equal comparison.
     *
     * @param left   The left side of the comparison.
     * @param middle The middle of the comparison.
     * @param right  The right side of the comparison.
     * @return A new RuleMaker representing the between x &lt; z &lt;= y comparison
     */
    public static RuleMaker ltLte(RuleMaker left, RuleMaker middle, RuleMaker right) {
        return template("< <=", left, middle, right);
    }


    /**
     * Creates a RuleMaker representing a sum of the given ruleMaker.
     *
     * @param values The ruleMaker to sum.
     * @return A new RuleMaker representing the sum of the given ruleMaker.
     */
    public static RuleMaker max(RuleMaker... values) {
        return template("max", values);
    }

    /**
     * Creates a RuleMaker representing a minimum of the given ruleMaker.
     *
     * @param values The ruleMaker to find the minimum of.
     * @return A new RuleMaker representing the minimum of the given ruleMaker.
     */
    public static RuleMaker min(RuleMaker... values) {
        return template("min", values);
    }

    /**
     * Creates a RuleMaker representing a sum of the given ruleMaker.
     *
     * @param expressions The ruleMaker to sum.
     * @return A new RuleMaker representing the sum of the given ruleMaker.
     */
    public static RuleMaker add(RuleMaker... expressions) {
        return template("+", expressions);
    }

    /**
     * Creates a RuleMaker representing a subtraction of the given ruleMaker.
     *
     * @param left  The left ruleWright.
     * @param right The right ruleWright.
     * @return A new RuleMaker representing the subtraction of the given
     * ruleMaker.
     */
    public static RuleMaker sub(RuleMaker left, RuleMaker right) {
        return template("-", left, right);
    }

    /**
     * Creates a RuleMaker representing a multiplication of the given ruleMaker.
     *
     * @param expressions The ruleMaker to multiply.
     * @return A new RuleMaker representing the multiplication of the given
     */
    public static RuleMaker mul(RuleMaker... expressions) {
        return template("*", expressions);
    }

    /**
     * Creates a RuleMaker representing a division of the given ruleMaker.
     *
     * @param left  The left ruleWright.
     * @param right The right ruleWright.
     * @return A new RuleMaker representing the division of the given ruleMaker.
     */
    public static RuleMaker div(RuleMaker left, RuleMaker right) {
        return template("/", left, right);
    }

    /**
     * Creates a RuleMaker representing a modulo operation of the given
     * ruleMaker.
     *
     * @param left  The left ruleWright.
     * @param right The right ruleWright.
     * @return A new RuleMaker representing the modulo operation of the given
     * ruleMaker.
     */
    public static RuleMaker mod(RuleMaker left, RuleMaker right) {
        return template("%", left, right);
    }

    /**
     * Creates a RuleMaker representing a string length operation.
     *
     * @param variable The variable to get the length of.
     * @param action   The action to perform on the variable.
     * @return A new RuleMaker representing the string length operation.
     */
    @SuppressWarnings("null")
    public static RuleMaker map(RuleMaker variable, RuleMaker action) {
        return new RuleMaker(Map.of("map", Arrays.asList(variable.expression, action.expression)));
    }

    /**
     * Creates a RuleMaker representing a filter operation.
     *
     * @param variable The variable to filter.
     * @param action   The action to perform on the variable.
     * @return A new RuleMaker representing the filter operation.
     */
    @SuppressWarnings("null")
    public static RuleMaker filter(RuleMaker variable, RuleMaker action) {
        return new RuleMaker(Map.of("filter", Arrays.asList(variable.expression, action.expression)));
    }

    /**
     * Creates a RuleMaker representing a reduce operation.
     *
     * <pre>
     * {
     *  "current" : // this element of the array,
     *  "accumulator" : // progress so far, or the initial value
     * }
     * </pre>
     *
     * @param variable The variable to reduce.
     * @param action   The action to perform on the variable.
     * @param initialValue The initial value
     * @return A new RuleMaker representing the reduce operation.
     */
    @SuppressWarnings("null")
    public static RuleMaker reduce(RuleMaker variable, RuleMaker action, RuleMaker initialValue) {
        return new RuleMaker(
                Map.of("reduce", Arrays.asList(variable.expression, action.expression, initialValue.expression)));
    }

    /**
     * Creates a RuleMaker representing a some operation.
     *
     * @return A new RuleMaker representing some operation.
     */
    @SuppressWarnings("null")
    public static RuleMaker all(RuleMaker action, RuleMaker variable) {
        return new RuleMaker(Map.of("all", Arrays.asList(variable.expression, action.expression)));
    }

    /**
     * Creates a RuleMaker representing a none operation.
     *
     * @return A new RuleMaker representing the none operation.
     */
    @SuppressWarnings("null")
    public static RuleMaker none(RuleMaker action, RuleMaker variable) {
        return new RuleMaker(Map.of("none", Arrays.asList(variable.expression, action.expression)));
    }

    /**
     * Creates a RuleMaker representing a some operation.
     *
     * @return A new RuleMaker representing the some operation.
     */
    @SuppressWarnings("null")
    public static RuleMaker some(RuleMaker action, RuleMaker variable) {
        return new RuleMaker(Map.of("some", Arrays.asList(variable.expression, action.expression)));
    }

    /**
     * Creates a RuleMaker representing a merge operation.
     *
     * @param values The RuleMaker expressions to merge.
     * @return A new RuleMaker representing the merge operation.
     */
    public static RuleMaker merge(RuleMaker... values) {
        return template("merge", values);
    }

    /**
     * Creates a RuleMaker representing an in operation.
     *
     * @param value The value to check.
     * @param array The array to check against.
     * @return A new RuleMaker representing the in operation.
     */
    public static RuleMaker in(RuleMaker value, RuleMaker array) {
        return template("in", value, array);
    }

    /**
     * Concatenates the given ruleMaker.
     *
     * @param values The ruleMaker to concatenate.
     * @return A new RuleMaker representing the concatenation of the given
     * ruleMaker.
     */
    public static RuleMaker cat(RuleMaker... values) {
        return template("cat", values);
    }

    /**
     * Creates a RuleMaker representing a substring operation.
     *
     * @param value  The value to substring.
     * @param start  The start index of the substring.
     * @param length The length of the substring.
     */
    public static RuleMaker substr(RuleMaker value, RuleMaker start, RuleMaker length) {
        return template("substr", value, start, length);
    }

    @SuppressWarnings("null")
    public static RuleMaker log(String value) {
        return new RuleMaker(Map.of("log", literal(value).expression));
    }

    /**
     * Creates a RuleMaker representing a date difference operation.
     *
     * @param dateVar1 The first date variable.
     * @param dateVar2 The second date variable.
     */
    @SuppressWarnings("null")
    public static RuleMaker dateDiff(RuleMaker dateVar1, RuleMaker dateVar2, RuleMaker measuringPoint) {
        Preconditions.checkNotNull(dateVar1, "Date1 cannot be null.");
        Preconditions.checkNotNull(dateVar2, "Date2 cannot be null.");
        return new RuleMaker(Map.of("dateDiff", Arrays.asList(dateVar1.expression, dateVar2.expression, measuringPoint.expression)));
    }

    @SuppressWarnings("null")
    public static RuleMaker clamp(RuleMaker value, RuleMaker min, RuleMaker max) {
        return new RuleMaker(Map.of("clamp", Arrays.asList(value.expression, min.expression, max.expression)));
    }

    @SuppressWarnings("null")
    private static RuleMaker template(String op, RuleMaker... values) {
        return new RuleMaker(Map.of(op,
                Arrays.stream(values)
                        .map(ruleWright -> ruleWright.expression)
                        .collect(Collectors.toList())));
    }

    public String toJson() {
        return gson.toJson(expression);
    }

    /**
     * Evaluates the JSON expression with the given data.
     *
     * @param data The data to evaluate the JSON expression with.
     * @return The result of the evaluation.
     */
    @SuppressWarnings({"squid:S1166", "squid:S112"})
    public Object evaluate(Object data) {
        Object result = null;
        try {
            String json = toJson();
            // TODO a strange behavior, need to investigate it further
            if (data instanceof Map) {
                Map<String, Object> normalizedMap = gson.fromJson(gson.toJson(data), type);
                Set<String> missingVariables = findMissingVariables(normalizedMap);
                if (!missingVariables.isEmpty()) {
                    throw new IllegalArgumentException("Missing variables: " + missingVariables);
                }
                result = jsonLogic.apply(json, normalizedMap);
            } else {
                result = jsonLogic.apply(json, data);
            }
        } catch (NullPointerException e) {
            return null; // bad data
        } catch (JsonLogicException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        if (log.isDebugEnabled()) {
            log.debug("RuleMaker: {} -> {}", toJson(), result);
        }
        return result;
    }

    /**
     * Finds all "var" fields in the JSON expression.
     *
     * @param json The JSON expression to search for "var" fields.
     * @return A list of found "var" fields.
     */
    private static List<String> findVariables(final String json) {
        List<String> foundVariables = new ArrayList<>();
        final JsonElement root = gson.fromJson(json, JsonElement.class);
        findVarFields(root, foundVariables);
        return foundVariables;
    }

    /**
     * Recursively finds all "var" fields in the JSON expression.
     *
     * @param element The current JSON element to search for "var" fields.
     * @param fields  The list of found "var" fields.
     */
    private static void findVarFields(JsonElement element, List<String> fields) {
        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            for (Entry<String, JsonElement> entry : obj.entrySet()) {
                if ("var".equals(entry.getKey()) && entry.getValue().isJsonPrimitive()) {
                    fields.add(entry.getValue().getAsString());
                } else {
                    findVarFields(entry.getValue(), fields);
                }
            }
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            for (JsonElement subElement : array) {
                findVarFields(subElement, fields);
            }
        }
        // Note: No explicit action for JsonPrimitive or JsonNull,
        // as they don't contain nested elements.
    }

    /**
     * Finds variables in the expression that are not present in the data map.
     *
     * @param data The data map to check for variable presence.
     * @return A set of variables that are missing in the data map.
     */
    private Set<String> findMissingVariables(Map<String, Object> data) {
        return findVariables(toJson()).stream()
                .filter(variable -> !isVariablePresent(data, variable))
                .collect(Collectors.toSet());
    }

    /**
     * Checks if a variable is present in the data map, supporting nested
     * properties.
     *
     * @param data     The data map where variables are looked up.
     * @param variable The variable to check, possibly a nested property like
     *                 "person.firstName".
     * @return true if the variable is present in the data map; false otherwise.
     */
    @SuppressWarnings("unchecked")
    private boolean isVariablePresent(Map<String, Object> data, String variable) {
        if (variable == null || variable.isBlank()) {
            return true;
        }
        String[] parts = variable.split("\\.");
        Map<String, Object> current = data;
        for (int i = 0; i < parts.length; i++) {
            if (i == parts.length - 1) { // Last part: check existence directly
                return current.containsKey(parts[i]);
            } else { // Not the last part: navigate to the next nested map
                Object next = current.get(parts[i]);
                if (next instanceof Map) {
                    // Safely cast and continue navigating
                    current = (Map<String, Object>) next;
                } else {
                    // Next part is not a Map, indicating the variable path is invalid
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean truthy(Object data) {
        return JsonLogic.truthy(data);
    }
}
