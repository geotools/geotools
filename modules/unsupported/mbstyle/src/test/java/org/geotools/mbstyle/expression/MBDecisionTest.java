/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.mbstyle.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MBDecisionTest {
    Map<String, JSONObject> testLayersById = new HashMap<>();
    MBObjectParser parse;
    FilterFactory2 ff;
    JSONObject mbstyle;

    @Before
    public void setUp() throws IOException, ParseException {
        mbstyle = MapboxTestUtils.parseTestStyle("expressionMBDecisionTest.json");
        JSONArray layers = (JSONArray) mbstyle.get("layers");
        for (Object o : layers) {
            JSONObject layer = (JSONObject) o;
            testLayersById.put((String) layer.get("id"), layer);
        }
        parse = new MBObjectParser(MBExpression.class);
        ff = parse.getFilterFactory();
    }
    /**
     * Helper method to create a MBDecision object from a JSON text field and evaluate the Expression.
     * @param json JSONObject to parse.
     * @param jsonTextField Name of the text field to retrieve.
     * @return The MBDecision evaluation of the supplied tectField value.
     */
    private Object getDecisionEvaluation(JSONObject json, String jsonTextField) {
        // get the Object from the supplied JSON
        final Object textFieldObj = json.get(jsonTextField);
        // assert the field is a JSONArray
        assertEquals(JSONArray.class, textFieldObj.getClass());
        // cast to a JSONArray
        final JSONArray arr = JSONArray.class.cast(textFieldObj);
        // create an Expression from the array
        final MBExpression mbExp = MBExpression.create(arr);
        // assert it of type MBDecision
        assertEquals(MBDecision.class, mbExp.getClass());
        // transform the expression
        final Expression transformExpression = MBExpression.transformExpression(arr);
        // evaluate and return the result
        return transformExpression.evaluate(transformExpression);
    }

    /**
     * Helper method to create a MBDecision object from a JSON text field and evaluate the Expression.
     * @param json JSONObject to parse.
     * @param jsonTextField Name of the text field to retrieve.
     * @return The Boolean value of the MBDecision evaluation.
     */
    private Boolean getDecisionEvaluationAsBoolean(JSONObject json, String jsonTextField) {
        // get the object
        Object decisionEvaluation = getDecisionEvaluation(json, jsonTextField);
        assertTrue(Boolean.class.isAssignableFrom(decisionEvaluation.getClass()));
        return Boolean.class.cast(decisionEvaluation);
    }

    /**
     * Traverse a nested map using the array of strings, and cast the result to the provided class, or return {@link Optional#empty()}.
     * @param map
     * @param clazz expected type
     * @param path used to access map
     * @return result at the provided path, or {@link Optional#empty()}.
     */
    private <T> Optional<T> traverse(JSONObject map, Class<T> clazz,
                                     String... path) {
        if (path == null || path.length == 0) {
            return Optional.empty();
        }

        Object value = map;
        for (String key : path) {
            if (value instanceof JSONObject) {
                JSONObject m = (JSONObject) value;
                if (m.containsKey(key)) {
                    value = m.get(key);
                }
                    } else {
                return Optional.empty();
            }
        }
        return Optional.of(clazz.cast(value));
    }

    @Test
    public void testDecisionNot() throws Exception {
        final JSONObject layer = testLayersById.get("decisionNot");
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        final JSONObject j = o.get();
        // get the notFalse-field, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "notFalse-field"));
        // now get the notTrue-field and evaluate, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "notTrue-field"));
        // now get the chainedNot-field and evaluate, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "chainedNot-field"));
    }

    @Test
    public void testDecisionNotEqualTo() throws Exception {
        final JSONObject layer = testLayersById.get("decisionNotEqualTo");
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        final JSONObject j = o.get();
        // get the compareStrings-equal, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "compareStrings-equal"));
        // get the compareStrings-notEqual, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "compareStrings-notEqual"));
        // get the compareNumbers-equal, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "compareNumbers-equal"));
        // get the compareNumbers-notEqual, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "compareNumbers-notEqual"));
        // get the compareBooleans-equal, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "compareBooleans-equal"));
        // get the compareBooleans-notEqual, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "compareBooleans-notEqual"));
        // get the compareBooleans-chainedNotEqual, whcih should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "compareBooleans-chainedNotEqual"));
        // get the compareTypes-notEqual, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "compareTypes-notEqual"));
        // get the compareNull-equal, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "compareNull-equal"));
        // get the compareNull-notEqual, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "compareNull-notEqual"));
    }

    @Test
    public void testDecisionEqualTo() throws Exception {
        final JSONObject layer = testLayersById.get("decisionEqualTo");
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        final JSONObject j = o.get();
        // get the compareStrings-equal, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "compareStrings-equal"));
        // get the compareStrings-notEqual, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "compareStrings-notEqual"));
        // get the compareNumbers-equal, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "compareNumbers-equal"));
        // get the compareNumbers-notEqual, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "compareNumbers-notEqual"));
        // get the compareBooleans-equal, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "compareBooleans-equal"));
        // get the compareBooleans-notEqual, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "compareBooleans-notEqual"));
        // get the compareBooleans-chainedNotEqual, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "compareBooleans-chainedNotEqual"));
        // get the compareTypes-notEqual, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "compareTypes-notEqual"));
        // get the compareNull-equal, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "compareNull-equal"));
        // get the compareNull-notEqual, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "compareNull-notEqual"));
    }

    @Test
    public void testDecisionLessThan() throws Exception {
        final JSONObject layer = testLayersById.get("decisionLessThan");
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        final JSONObject j = o.get();
        // get the lessThan-numericField, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "lessThan-numericField"));
        // get the lessThan-numericField, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "lessThan-stringField"));
        // get the equalTo-numericField, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "equalTo-numericField"));
        // get the equalTo-stringField, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "equalTo-stringField"));
        // get the greaterThan-numericField, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "greaterThan-numericField"));
        // get the greaterThan-stringField, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "greaterThan-stringField"));
    }

    @Test
    public void testDecisionLessThanEquals() throws Exception {
        final JSONObject layer = testLayersById.get("decisionLessThanEquals");
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        final JSONObject j = o.get();
        // get the lessThan-numericField, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "lessThan-numericField"));
        // get the lessThan-numericField, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "lessThan-stringField"));
        // get the equalTo-numericField, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "equalTo-numericField"));
        // get the equalTo-stringField, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "equalTo-stringField"));
        // get the greaterThan-numericField, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "greaterThan-numericField"));
        // get the greaterThan-stringField, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "greaterThan-stringField"));
    }

    @Test
    public void testDecisionGreaterThan() throws Exception {
        final JSONObject layer = testLayersById.get("decisionGreaterThan");
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        final JSONObject j = o.get();
        // get the lessThan-numericField, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "lessThan-numericField"));
        // get the lessThan-numericField, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "lessThan-stringField"));
        // get the equalTo-numericField, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "equalTo-numericField"));
        // get the equalTo-stringField, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "equalTo-stringField"));
        // get the greaterThan-numericField, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "greaterThan-numericField"));
        // get the greaterThan-stringField, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "greaterThan-stringField"));
    }

    @Test
    public void testDecisionGreaterThanEquals() throws Exception {
        final JSONObject layer = testLayersById.get("decisionGreaterThanEquals");
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        final JSONObject j = o.get();
        // get the lessThan-numericField, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "lessThan-numericField"));
        // get the lessThan-numericField, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "lessThan-stringField"));
        // get the equalTo-numericField, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "equalTo-numericField"));
        // get the equalTo-stringField, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "equalTo-stringField"));
        // get the greaterThan-numericField, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "greaterThan-numericField"));
        // get the greaterThan-stringField, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "greaterThan-stringField"));
    }

    @Test
    public void testDecisionAny() throws Exception {
        final JSONObject layer = testLayersById.get("decisionAny");
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        final JSONObject j = o.get();
        // get the any-trueFalseFalse, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "any-trueFalseFalse"));
        // get the any-falseFalseTrue, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "any-falseFalseTrue"));
        // get the any-allFalse, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "any-allFalse"));
        // get the any-allTrue, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "any-allTrue"));
        // get the any-nullFalseFalse, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "any-nullFalseFalse"));
        // get the any-chainedFalseTrueFalse, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "any-chainedFalseTrueFalse"));
        // get the any-chainedAllFalse, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "any-chainedAllFalse"));
        // get the any-chainedAllTrue, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "any-chainedAllTrue"));
    }

    @Test
    public void testDecisionAll() throws Exception {
        final JSONObject layer = testLayersById.get("decisionAll");
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        final JSONObject j = o.get();
        // get the all-trueFalseFalse, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "all-trueFalseFalse"));
        // get the all-falseFalseTrue, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "all-falseFalseTrue"));
        // get the all-allFalse, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "all-allFalse"));
        // get the all-allTrue, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "all-allTrue"));
        // get the all-nullFalseFalse, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "all-nullFalseFalse"));
        // get the all-chainedFalseTrueFalse, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "all-chainedFalseTrueFalse"));
        // get the all-chainedAllFalse, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "all-chainedAllFalse"));
        // get the all-chainedAllTrue, which should evaluate to true
        assertTrue(getDecisionEvaluationAsBoolean(j, "all-chainedAllTrue"));
        // get the all-chained, which should evaluate to false
        assertFalse(getDecisionEvaluationAsBoolean(j, "all-chained"));
    }

    @Test
    public void testDecisionCase() throws Exception {
        final JSONObject layer = testLayersById.get("decisionCase");
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        final JSONObject j = o.get();
        // get the case-trueFalseDefault, which should evaluate to 10
        assertEquals(10l, getDecisionEvaluation(j, "case-trueFalseDefault"));
        // get the case-falseTrueDefault, which should evaluate to "aString"
        assertEquals("aString", getDecisionEvaluation(j, "case-falseTrueDefault"));
        // get the case-falseFalseDefault, which should evaluate to true
        assertEquals(true, getDecisionEvaluation(j, "case-falseFalseDefault"));
        // get the case-chainedTrueFalseDefault, which should evaluate to 10
        assertEquals(10l, getDecisionEvaluation(j, "case-chainedTrueFalseDefault"));
        // get the case-chainedFalseTrueDefault, which should evaluate to "aString"
        assertEquals("aString", getDecisionEvaluation(j, "case-chainedFalseTrueDefault"));
        // get the case-chainedFalseFalseDefault, which should evaluate to true
        assertEquals(true, getDecisionEvaluation(j, "case-chainedFalseFalseDefault"));
    }

    @Test
    public void testDecisionCoalesce() throws Exception {
        final JSONObject layer = testLayersById.get("decisionCoalesce");
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        final JSONObject j = o.get();
        // get the coalesce-stringNull, which should evaluate to "aString"
        assertEquals("aString", getDecisionEvaluation(j, "coalesce-stringNull"));
        // get the coalesce-nullNumber, which should evaluate to 5
        assertEquals(5l, getDecisionEvaluation(j, "coalesce-nullNumber"));
        // get the coalesce-stringNullNumber, which should evaluate to "aString"
        assertEquals("aString", getDecisionEvaluation(j, "coalesce-stringNullNumber"));
        // get the coalesce-nullNumberString, which should evaluate to 5
        assertEquals(5l, getDecisionEvaluation(j, "coalesce-nullNumberString"));
        // get the coalesce-chainedNullStringNumber, which should evaluate to "aString"
        assertEquals("aString", getDecisionEvaluation(j, "coalesce-chainedNullStringNumber"));
    }

    @Test
    public void testDecisionMatch() throws Exception {
        final JSONObject layer = testLayersById.get("decisionMatch");
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, "layout");
        final JSONObject j = o.get();
        // get the match-thirdLabel, which should evaluate to "thirdLabel"
        assertEquals("thirdLabel", getDecisionEvaluation(j, "match-thirdLabel"));
        // get the match-noMatchingLabel, which should evaluate to "defaultLabel"
        assertEquals("defaultLabel", getDecisionEvaluation(j, "match-noMatchingLabel"));
        // get the match-arrayLabel, which should evaluate to "arrayLabel"
        assertEquals("arrayLabel", getDecisionEvaluation(j, "match-arrayLabel"));
        // get the match-expressionThirdLabel, which should evaluate to "thirdLabel"
        assertEquals("thirdLabel", getDecisionEvaluation(j, "match-expressionThirdLabel"));
        // get the match-expressionArrayLabel, which should evaluate to "arrayLabel"
        assertEquals("arrayLabel", getDecisionEvaluation(j, "match-expressionArrayLabel"));
        // get the match-expressionLabelThirdLabel, which should evaluate to "thirdLabel"
        assertEquals("thirdLabel", getDecisionEvaluation(j, "match-expressionLabelThirdLabel"));
        // get the match-expressionLabelArrayLabel, which should evaluate to "arrayLabel"
        assertEquals("arrayLabel", getDecisionEvaluation(j, "match-expressionLabelArrayLabel"));
    }
}
