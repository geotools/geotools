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
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.mbstyle.MapboxTestUtils;
import org.geotools.mbstyle.parse.MBObjectParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

/**
 * Abstract Test class the implements common/shared functionality for setting up tests for the
 * various MapBox Expression classes, particularly for testing the JSON parsing of each. For each
 * Expression type, a separate test class should be written that extends this abstract class, with
 * appropriate method implementations provided. A typical test method would look like this:
 *
 * <p>
 *
 * <pre>
 *     final JSONObject j = getObjectByLayerId("layerName", "fieldName");
 *     // test for literal JSONArray
 *     Object array = getExpressionEvaluation(j, "fieldElement");
 *     // appropriate validation and verification of the evaluated expression goes here
 * </pre>
 *
 * Where "layerName" is the name of the layer to test in the test resource, "fieldName" is the JSON
 * blob to retrieve, and "fieldElement" is the specific JSON element that a given test method should
 * be validating. Once the evaluated expression Object is retrieved, any appropriate assertions
 * should be made within the specific test.
 */
public abstract class AbstractMBExpressionTest {

    protected Map<String, JSONObject> testLayersById = new HashMap<>();
    protected MBObjectParser parse;
    protected FilterFactory2 ff;
    protected JSONObject mbstyle;
    protected SimpleFeature[] testFeatures;
    protected final GeometryFactory geometryFactory = new GeometryFactory();
    protected final int[] intVals = new int[] {4, 90, 20, 43, 29, -61, 8, 12};
    protected final double[] doubleVals =
            new double[] {11.11, 22.22, 99.9, 78.654, 0.01, 100.0, -4.2, 44.44};

    @Before
    public void setUp() throws Exception {
        mbstyle = MapboxTestUtils.parseTestStyle(getTestResourceName());
        final JSONArray layers = (JSONArray) mbstyle.get("layers");
        for (Object o : layers) {
            JSONObject layer = (JSONObject) o;
            testLayersById.put((String) layer.get("id"), layer);
        }
        parse = new MBObjectParser(MBExpression.class);
        ff = parse.getFilterFactory();
        // setup test features
        final SimpleFeatureType dataType =
                DataUtilities.createType(
                        "mbexpression.test",
                        "anIntField:int,anotherIntField:int,doubleField:double,geom:Point,name:String");
        testFeatures = new SimpleFeature[intVals.length];
        for (int i = 0; i < intVals.length; ++i) {
            final SimpleFeature simpleFeature =
                    SimpleFeatureBuilder.build(
                            dataType,
                            new Object[] {
                                Integer.valueOf(i),
                                Integer.valueOf(intVals[i]),
                                Double.valueOf(doubleVals[i]),
                                geometryFactory.createPoint(new Coordinate(intVals[i], intVals[i])),
                                "name_" + intVals[i]
                            },
                            "mbexpression." + (i + 1));
            testFeatures[i] = simpleFeature;
        }
        // finally, do any subclass setup steps
        setupInternal();
    }

    protected void setupInternal() throws Exception {
        // do nothing, subclass can override to provide more setup steps
    }

    /**
     * Returns the desired test resource filename that provides the MBStyle JSON test data.
     *
     * @return A String representing the test file name reosurce.
     */
    protected abstract String getTestResourceName();

    /**
     * Returns the MapBox Expression class type associated with this Test class. The assumption is
     * tests for each MapBox Expression type will be grouped into a single class that extends this
     * abstract class.
     *
     * @return the Class type of the MapBox Expression class for this Test.
     */
    protected abstract Class getExpressionClassType();

    /**
     * Traverse a nested map using the array of strings, and cast the result to the provided class,
     * or return {@link Optional#empty()}.
     *
     * @param <T> Class type of the returned value.
     * @param map JSON Object to traverse.
     * @param clazz expected type
     * @param path used to access map
     * @return result at the provided path, or {@link Optional#empty()}.
     */
    protected final <T> Optional<T> traverse(JSONObject map, Class<T> clazz, String... path) {
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
     * Helper method to create an Expression object from a JSON text field and evaluate the
     * Expression.
     *
     * @param json JSONObject to parse.
     * @param jsonTextField Name of the text field to retrieve.
     * @return The Expression evaluation of the supplied tectField value.
     */
    protected Object getExpressionEvaluation(JSONObject json, String jsonTextField) {
        return getExpressionEvaluation(json, jsonTextField, testFeatures[0]);
    }

    /**
     * Helper method to create an Expression object from a JSON text field and evaluate the
     * Expression.
     *
     * @param json JSONObject to parse.
     * @param jsonTextField Name of the text field to retrieve.
     * @param feature Feature to which the expression should be evaluated.
     * @return The Expression evaluation of the supplied tectField value.
     */
    protected Object getExpressionEvaluation(
            JSONObject json, String jsonTextField, SimpleFeature feature) {
        // get the Object from the supplied JSON
        final Object textFieldObj = json.get(jsonTextField);
        // make sure we got a field
        assertNotNull(
                String.format(
                        "JSON Text Field not extracted. Is the field name spelled correctly? \"%s\"",
                        jsonTextField),
                textFieldObj);
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
        return transformExpression.evaluate(feature);
    }

    /**
     * Retrieves the JSON object from the test resource identified by the supplied layer and field
     * Ids.
     *
     * @param layerId String representation of the layer for which the JSON object should be
     *     retrieved.
     * @param fieldId String representation of the field in the layer to retrieve.
     * @return A JSONObject instance that represents the test JSON for the supplied layer and field
     *     Ids.
     */
    protected JSONObject getObjectByLayerId(final String layerId, String fieldId) {
        final JSONObject layer = testLayersById.get(layerId);
        final Optional<JSONObject> o = traverse(layer, JSONObject.class, fieldId);
        return o.get();
    }
}
