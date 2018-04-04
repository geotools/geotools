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

import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract Test class the implements common/shared functionality for setting up tests for the various MapBox Expression
 * classes, particularly for testing the JSON parsing of each. For each Expression type, a separate test class should be
 * written that extends this abstract class, with appropriate method implementations provided. A typical test method
 * would look like this:
 * <p>
 * <pre>
 *     final JSONObject j = getObjectByLayerId("layerName", "fieldName");
 *     // test for literal JSONArray
 *     Object array = getExpressionEvaluation(j, "fieldElement");
 *     // appropriate validation and verification of the evaluated expression goes here
 * </pre>
 * </p>
 * Where "layerName" is the name of the layer to test in the test resource, "fieldName" is the JSON blob to
 * retrieve, and "fieldElement" is the specific JSON element that a given test method should be validating. Once the
 * evaluated expression Object is retrieved, any appropriate assertions should be made within the specific test.
 */
public abstract class AbstractMBExpressionTest {

    protected Map<String, JSONObject> testLayersById = new HashMap<>();
    protected MBObjectParser parse;
    protected FilterFactory2 ff;
    protected JSONObject mbstyle;

    @Before
    public void setUp() throws IOException, ParseException {
        mbstyle = MapboxTestUtils.parseTestStyle(getTestResourceName());
        JSONArray layers = (JSONArray) mbstyle.get("layers");
        for (Object o : layers) {
            JSONObject layer = (JSONObject) o;
            testLayersById.put((String) layer.get("id"), layer);
        }
        parse = new MBObjectParser(MBExpression.class);
        ff = parse.getFilterFactory();
    }

    /**
     * Returns the desired test resource filename that provides the MBStyle JSON test data.
     *
     * @return A String representing the test file name reosurce.
     */
    protected abstract String getTestResourceName();

    /**
     * Returns the MapBox Expression class type associated with this Test class. The assumption is tests for each
     * MapBox Expression type will be grouped into a single class that extends this abstract class.
     *
     * @return the Class type of the MapBox Expression class for this Test.
     */
    protected abstract Class getExpressionClassType();

    /**
     * Traverse a nested map using the array of strings, and cast the result to the provided class, or return
     * {@link Optional#empty()}.
     *
     * @param <T> Class type of the returned value.
     * @param map JSON Object to traverse.
     * @param clazz expected type
     * @param path used to access map
     *
     * @return result at the provided path, or {@link Optional#empty()}.
     */
    protected final <T> Optional<T> traverse(JSONObject map, Class<T> clazz,
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

    /**
     * Helper method to create an Expression object from a JSON text field and evaluate the Expression.
     *
     * @param json JSONObject to parse.
     * @param jsonTextField Name of the text field to retrieve.
     *
     * @return The Expression evaluation of the supplied tectField value.
     */
    protected Object getExpressionEvaluation(JSONObject json, String jsonTextField) {
        // get the Object from the supplied JSON
        final Object textFieldObj = json.get(jsonTextField);
        // assert the field is a JSONArray
        assertEquals(JSONArray.class, textFieldObj.getClass());
        // cast to a JSONArray
        final JSONArray arr = JSONArray.class.cast(textFieldObj);
        // create an Expression from the array
        final MBExpression mbExp = MBExpression.create(arr);
        // assert it to be of the MBExpression type this test handles
        assertEquals(getExpressionClassType(), mbExp.getClass());
        // transform the expression
        final Expression transformExpression = MBExpression.transformExpression(arr);
        // evaluate and return the result
        return transformExpression.evaluate(transformExpression);
    }

    /**
     * Retrieves the JSON object from the test resource identified by the supplied layer and field Ids.
     * @param layerId String representation of the layer for which the JSON object should be retrieved.
     * @param fieldId String representation of the field in the layer to retrieve.
     * @return A JSONObject instance that represents the test JSON for the supplied layer and field Ids.
     */
    protected JSONObject getObjectByLayerId(final String layerId, String fieldId) {
        final JSONObject layer = testLayersById.get(layerId);
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, fieldId);
        return o.get();
    }
}
