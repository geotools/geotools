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

import org.json.simple.JSONObject;
import org.junit.Test;

/** Tests the MapBox Decision expressions. */
public class MBDecisionTest extends AbstractMBExpressionTest {

    @Override
    protected String getTestResourceName() {
        return "expressionMBDecisionTest.json";
    }

    @Override
    protected Class getExpressionClassType() {
        return MBDecision.class;
    }

    /**
     * Helper method to create a MBDecision object from a JSON text field and evaluate the
     * Expression.
     *
     * @param json JSONObject to parse.
     * @param jsonTextField Name of the text field to retrieve.
     * @return The Boolean value of the MBDecision evaluation.
     */
    private Boolean getExpressionEvaluationAsBoolean(JSONObject json, String jsonTextField) {
        // get the object
        Object decisionEvaluation = getExpressionEvaluation(json, jsonTextField);
        assertTrue(Boolean.class.isAssignableFrom(decisionEvaluation.getClass()));
        return Boolean.class.cast(decisionEvaluation);
    }

    @Test
    public void testDecisionNot() throws Exception {
        final JSONObject j = getObjectByLayerId("decisionNot", "layout");
        // get the notFalse-field, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "notFalse-field"));
        // now get the notTrue-field and evaluate, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "notTrue-field"));
        // now get the chainedNot-field and evaluate, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "chainedNot-field"));
    }

    @Test
    public void testDecisionNotEqualTo() throws Exception {
        final JSONObject j = getObjectByLayerId("decisionNotEqualTo", "layout");
        // get the compareStrings-equal, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "compareStrings-equal"));
        // get the compareStrings-notEqual, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "compareStrings-notEqual"));
        // get the compareNumbers-equal, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "compareNumbers-equal"));
        // get the compareNumbers-notEqual, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "compareNumbers-notEqual"));
        // get the compareBooleans-equal, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "compareBooleans-equal"));
        // get the compareBooleans-notEqual, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "compareBooleans-notEqual"));
        // get the compareBooleans-chainedNotEqual, whcih should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "compareBooleans-chainedNotEqual"));
        // get the compareTypes-notEqual, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "compareTypes-notEqual"));
        // get the compareNull-equal, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "compareNull-equal"));
        // get the compareNull-notEqual, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "compareNull-notEqual"));
    }

    @Test
    public void testDecisionEqualTo() throws Exception {
        final JSONObject j = getObjectByLayerId("decisionEqualTo", "layout");
        // get the compareStrings-equal, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "compareStrings-equal"));
        // get the compareStrings-notEqual, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "compareStrings-notEqual"));
        // get the compareNumbers-equal, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "compareNumbers-equal"));
        // get the compareNumbers-notEqual, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "compareNumbers-notEqual"));
        // get the compareBooleans-equal, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "compareBooleans-equal"));
        // get the compareBooleans-notEqual, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "compareBooleans-notEqual"));
        // get the compareBooleans-chainedNotEqual, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "compareBooleans-chainedNotEqual"));
        // get the compareTypes-notEqual, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "compareTypes-notEqual"));
        // get the compareNull-equal, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "compareNull-equal"));
        // get the compareNull-notEqual, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "compareNull-notEqual"));
    }

    @Test
    public void testDecisionLessThan() throws Exception {
        final JSONObject j = getObjectByLayerId("decisionLessThan", "layout");
        // get the lessThan-numericField, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "lessThan-numericField"));
        // get the lessThan-numericField, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "lessThan-stringField"));
        // get the equalTo-numericField, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "equalTo-numericField"));
        // get the equalTo-stringField, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "equalTo-stringField"));
        // get the greaterThan-numericField, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "greaterThan-numericField"));
        // get the greaterThan-stringField, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "greaterThan-stringField"));
    }

    @Test
    public void testDecisionLessThanEquals() throws Exception {
        final JSONObject j = getObjectByLayerId("decisionLessThanEquals", "layout");
        // get the lessThan-numericField, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "lessThan-numericField"));
        // get the lessThan-numericField, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "lessThan-stringField"));
        // get the equalTo-numericField, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "equalTo-numericField"));
        // get the equalTo-stringField, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "equalTo-stringField"));
        // get the greaterThan-numericField, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "greaterThan-numericField"));
        // get the greaterThan-stringField, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "greaterThan-stringField"));
    }

    @Test
    public void testDecisionGreaterThan() throws Exception {
        final JSONObject j = getObjectByLayerId("decisionGreaterThan", "layout");
        // get the lessThan-numericField, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "lessThan-numericField"));
        // get the lessThan-numericField, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "lessThan-stringField"));
        // get the equalTo-numericField, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "equalTo-numericField"));
        // get the equalTo-stringField, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "equalTo-stringField"));
        // get the greaterThan-numericField, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "greaterThan-numericField"));
        // get the greaterThan-stringField, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "greaterThan-stringField"));
    }

    @Test
    public void testDecisionGreaterThanEquals() throws Exception {
        final JSONObject j = getObjectByLayerId("decisionGreaterThanEquals", "layout");
        // get the lessThan-numericField, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "lessThan-numericField"));
        // get the lessThan-numericField, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "lessThan-stringField"));
        // get the equalTo-numericField, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "equalTo-numericField"));
        // get the equalTo-stringField, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "equalTo-stringField"));
        // get the greaterThan-numericField, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "greaterThan-numericField"));
        // get the greaterThan-stringField, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "greaterThan-stringField"));
    }

    @Test
    public void testDecisionAny() throws Exception {
        final JSONObject j = getObjectByLayerId("decisionAny", "layout");
        // get the any-trueFalseFalse, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "any-trueFalseFalse"));
        // get the any-falseFalseTrue, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "any-falseFalseTrue"));
        // get the any-allFalse, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "any-allFalse"));
        // get the any-allTrue, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "any-allTrue"));
        // get the any-nullFalseFalse, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "any-nullFalseFalse"));
        // get the any-chainedFalseTrueFalse, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "any-chainedFalseTrueFalse"));
        // get the any-chainedAllFalse, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "any-chainedAllFalse"));
        // get the any-chainedAllTrue, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "any-chainedAllTrue"));
    }

    @Test
    public void testDecisionAll() throws Exception {
        final JSONObject j = getObjectByLayerId("decisionAll", "layout");
        // get the all-trueFalseFalse, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "all-trueFalseFalse"));
        // get the all-falseFalseTrue, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "all-falseFalseTrue"));
        // get the all-allFalse, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "all-allFalse"));
        // get the all-allTrue, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "all-allTrue"));
        // get the all-nullFalseFalse, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "all-nullFalseFalse"));
        // get the all-chainedFalseTrueFalse, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "all-chainedFalseTrueFalse"));
        // get the all-chainedAllFalse, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "all-chainedAllFalse"));
        // get the all-chainedAllTrue, which should evaluate to true
        assertTrue(getExpressionEvaluationAsBoolean(j, "all-chainedAllTrue"));
        // get the all-chained, which should evaluate to false
        assertFalse(getExpressionEvaluationAsBoolean(j, "all-chained"));
    }

    @Test
    public void testDecisionCase() throws Exception {
        final JSONObject j = getObjectByLayerId("decisionCase", "layout");
        // get the case-trueFalseDefault, which should evaluate to 10
        assertEquals(10l, getExpressionEvaluation(j, "case-trueFalseDefault"));
        // get the case-falseTrueDefault, which should evaluate to "aString"
        assertEquals("aString", getExpressionEvaluation(j, "case-falseTrueDefault"));
        // get the case-falseFalseDefault, which should evaluate to true
        assertEquals(true, getExpressionEvaluation(j, "case-falseFalseDefault"));
        // get the case-chainedTrueFalseDefault, which should evaluate to 10
        assertEquals(10l, getExpressionEvaluation(j, "case-chainedTrueFalseDefault"));
        // get the case-chainedFalseTrueDefault, which should evaluate to "aString"
        assertEquals("aString", getExpressionEvaluation(j, "case-chainedFalseTrueDefault"));
        // get the case-chainedFalseFalseDefault, which should evaluate to true
        assertEquals(true, getExpressionEvaluation(j, "case-chainedFalseFalseDefault"));
    }

    @Test
    public void testDecisionCoalesce() throws Exception {
        final JSONObject j = getObjectByLayerId("decisionCoalesce", "layout");
        // get the coalesce-stringNull, which should evaluate to "aString"
        assertEquals("aString", getExpressionEvaluation(j, "coalesce-stringNull"));
        // get the coalesce-nullNumber, which should evaluate to 5
        assertEquals(5l, getExpressionEvaluation(j, "coalesce-nullNumber"));
        // get the coalesce-stringNullNumber, which should evaluate to "aString"
        assertEquals("aString", getExpressionEvaluation(j, "coalesce-stringNullNumber"));
        // get the coalesce-nullNumberString, which should evaluate to 5
        assertEquals(5l, getExpressionEvaluation(j, "coalesce-nullNumberString"));
        // get the coalesce-chainedNullStringNumber, which should evaluate to "aString"
        assertEquals("aString", getExpressionEvaluation(j, "coalesce-chainedNullStringNumber"));
    }

    @Test
    public void testDecisionMatch() throws Exception {
        final JSONObject j = getObjectByLayerId("decisionMatch", "layout");
        // get the match-thirdLabel, which should evaluate to "thirdLabel"
        assertEquals("thirdLabel", getExpressionEvaluation(j, "match-thirdLabel"));
        // get the match-noMatchingLabel, which should evaluate to "defaultLabel"
        assertEquals("defaultLabel", getExpressionEvaluation(j, "match-noMatchingLabel"));
        // get the match-arrayLabel, which should evaluate to "arrayLabel"
        assertEquals("arrayLabel", getExpressionEvaluation(j, "match-arrayLabel"));
        // get the match-expressionThirdLabel, which should evaluate to "thirdLabel"
        assertEquals("thirdLabel", getExpressionEvaluation(j, "match-expressionThirdLabel"));
        // get the match-expressionArrayLabel, which should evaluate to "arrayLabel"
        assertEquals("arrayLabel", getExpressionEvaluation(j, "match-expressionArrayLabel"));
        // get the match-expressionLabelThirdLabel, which should evaluate to "thirdLabel"
        assertEquals("thirdLabel", getExpressionEvaluation(j, "match-expressionLabelThirdLabel"));
        // get the match-expressionLabelArrayLabel, which should evaluate to "arrayLabel"
        assertEquals("arrayLabel", getExpressionEvaluation(j, "match-expressionLabelArrayLabel"));
    }
}
