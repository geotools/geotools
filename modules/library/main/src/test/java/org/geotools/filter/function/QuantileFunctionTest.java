/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/** @author Cory Horner, Refractions Research Inc. */
public class QuantileFunctionTest extends FunctionTestSupport {

    @Test
    public void testInstance() {
        Function equInt = ff.function("Quantile", ff.literal(new DefaultFeatureCollection()));
        assertNotNull(equInt);
    }

    @Test
    public void testGetName() {
        Function qInt = ff.function("Quantile", ff.literal(new DefaultFeatureCollection()));
        assertEquals("Quantile", qInt.getName());
    }

    @Test
    public void testSetParameters() throws Exception {
        Literal classes = ff.literal(3);
        PropertyName expr = ff.property("foo");
        QuantileFunction func = (QuantileFunction) ff.function("Quantile", expr, classes);
        assertEquals(3, func.getClasses());
        classes = ff.literal(12);
        func = (QuantileFunction) ff.function("Quantile", expr, classes);
        assertEquals(12, func.getClasses());
        // deprecated still works?
        classes = ff.literal(5);
        func = (QuantileFunction) ff.function("Quantile", expr, classes);
        assertEquals(5, func.getClasses());
    }

    @Test
    public void testEvaluateWithExpressions() throws Exception {
        Literal classes = ff.literal(2);
        PropertyName exp = ff.property("foo");
        Function func = ff.function("Quantile", exp, classes);

        Object value = func.evaluate(featureCollection);
        assertTrue(value instanceof RangedClassifier);
        RangedClassifier ranged = (RangedClassifier) value;

        // the values being quantiled are
        // {4,90,20,43,29,61,8,12};
        // so there should be two groups:
        // {4, 8, 12, 20}       4 <= x < 29
        // {29, 43, 61, 90}     29 <= x <= 90
        assertEquals(2, ranged.getSize());
        assertEquals("4..29", ranged.getTitle(0));
        assertEquals("29..90", ranged.getTitle(1));
    }

    /**
     * Test a feature collection where each feature will be in it's own bin.
     *
     * <p>Creates a feature collection with five features 1-5. Then uses the quantile function to
     * put these features in 5 bins. Each bin should have a single feature.
     */
    @Test
    public void testSingleBin() throws Exception {

        // create a feature collection with five features values 1-5
        SimpleFeatureType dataType =
                DataUtilities.createType("classification.test1", "id:0,value:int");
        int iVal[] = {1, 2, 3, 4, 5};
        SimpleFeature[] myfeatures = new SimpleFeature[iVal.length];
        for (int i = 0; i < iVal.length; i++) {
            myfeatures[i] =
                    SimpleFeatureBuilder.build(
                            dataType,
                            new Object[] {Integer.valueOf(i + 1), Integer.valueOf(iVal[i])},
                            "classification.test1" + (i + 1));
        }
        SimpleFeatureCollection myFeatureCollection = DataUtilities.collection(myfeatures);

        // run the quantile function
        org.opengis.filter.expression.Expression function =
                ff.function("Quantile", ff.property("value"), ff.literal(5));
        Classifier classifier = (Classifier) function.evaluate(myFeatureCollection);

        // verify the results
        assertNotNull(classifier);
        assertEquals(classifier.getClass(), RangedClassifier.class);
        RangedClassifier range = (RangedClassifier) classifier;
        assertEquals(5, range.getSize());

        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1d, ((Number) range.getMin(i)).doubleValue(), 0d);
            if (i != 4) {
                assertEquals(i + 2d, ((Number) range.getMax(i)).doubleValue(), 0d);
                assertEquals((i + 1) + ".." + (i + 2), range.getTitle(i));
            } else {
                assertEquals(i + 1d, ((Number) range.getMax(i)).doubleValue(), 0d);
                assertEquals((i + 1) + ".." + (i + 1), range.getTitle(i));
            }
        }
    }

    @Test
    public void test2() throws Exception {
        // create a feature collection with five features values 1-5
        SimpleFeatureType dataType =
                DataUtilities.createType("classification.test1", "id:0,value:int");
        int iVal[] = {1, 2, 3, 4, 5, 6};
        SimpleFeature[] myfeatures = new SimpleFeature[iVal.length];
        for (int i = 0; i < iVal.length; i++) {
            myfeatures[i] =
                    SimpleFeatureBuilder.build(
                            dataType,
                            new Object[] {Integer.valueOf(i + 1), Integer.valueOf(iVal[i])},
                            "classification.t" + (i + 1));
        }
        SimpleFeatureCollection myFeatureCollection = DataUtilities.collection(myfeatures);

        // run the quantile function
        org.opengis.filter.expression.Expression function =
                ff.function("Quantile", ff.property("value"), ff.literal(5));
        Classifier classifier = (Classifier) function.evaluate(myFeatureCollection);
        assertTrue(classifier instanceof RangedClassifier);
    }

    @Test
    public void testEvaluateWithStrings() throws Exception {
        org.opengis.filter.expression.Expression function =
                ff.function("Quantile", ff.property("group"), ff.literal(2));
        Classifier classifier = (Classifier) function.evaluate(featureCollection);
        assertNotNull(classifier);

        Classifier classifier2 = function.evaluate(featureCollection, Classifier.class);
        assertNotNull(classifier2);

        Integer number = function.evaluate(featureCollection, Integer.class);
        assertNull(number);
    }

    @Test
    @Ignore
    public void testNullNaNHandling() throws Exception {
        // create a feature collection
        SimpleFeatureType ft =
                DataUtilities.createType("classification.nullnan", "id:0,foo:int,bar:double");
        Integer iVal[] = {
            Integer.valueOf(0),
            Integer.valueOf(0),
            Integer.valueOf(0),
            Integer.valueOf(13),
            Integer.valueOf(13),
            Integer.valueOf(13),
            null,
            null,
            null
        };
        Double dVal[] = {
            Double.valueOf(0.0),
            Double.valueOf(50.01),
            null,
            Double.valueOf(0.0),
            Double.valueOf(50.01),
            null,
            Double.valueOf(0.0),
            Double.valueOf(50.01),
            null
        };

        SimpleFeature[] testFeatures = new SimpleFeature[iVal.length];

        for (int i = 0; i < iVal.length; i++) {
            testFeatures[i] =
                    SimpleFeatureBuilder.build(
                            ft,
                            new Object[] {
                                Integer.valueOf(i + 1), iVal[i], dVal[i],
                            },
                            "nantest.t" + (i + 1));
        }
        SimpleFeatureCollection thisFC = DataUtilities.collection(testFeatures);

        // create the expression
        Divide divide = ff.divide(ff.property("foo"), ff.property("bar"));
        QuantileFunction qf = (QuantileFunction) ff.function("Quantile", divide, ff.literal(3));

        RangedClassifier range = (RangedClassifier) qf.evaluate(thisFC);
        assertEquals(2, range.getSize()); // 2 or 3?
        assertEquals("0..0", range.getTitle(0));
        assertEquals("0..0.25995", range.getTitle(1));
    }

    @Test
    public void testConstantValuesNumeric() {
        Function function = ff.function("quantile", ff.property("v"), ff.literal(12));
        RangedClassifier classifier = (RangedClassifier) function.evaluate(constantCollection);
        assertNotNull(classifier);
        assertEquals(1, classifier.getSize());
        assertEquals(123.123, (Double) classifier.getMin(0), 0d);
        assertEquals(123.123, (Double) classifier.getMax(0), 0d);
    }

    @Test
    public void testConstantValuesString() {
        Function function = ff.function("quantile", ff.property("s"), ff.literal(12));
        ExplicitClassifier classifier = (ExplicitClassifier) function.evaluate(constantCollection);
        assertNotNull(classifier);
        assertEquals(1, classifier.getSize());
        assertEquals(1, classifier.getValues(0).size());
        assertEquals("abc", classifier.getValues(0).iterator().next());
    }

    @Test
    public void testEvaluateNumericalWithPercentages() {
        // numerical
        Literal classes = ff.literal(2);
        PropertyName exp = ff.property("foo");
        Function func = ff.function("Quantile", exp, classes, ff.literal(true));

        Object value = func.evaluate(featureCollection);
        assertTrue(value instanceof RangedClassifier);
        RangedClassifier ranged = (RangedClassifier) value;
        double[] percentages = ranged.getPercentages();
        assertEquals(percentages.length, 2);
        assertEquals(percentages[0], 50.0, 0d);
        assertEquals(percentages[1], 50.0, 0d);
    }

    @Test
    public void testEvaluateNotNumericalWithPercentages() throws SchemaException {
        SimpleFeatureType dataType =
                DataUtilities.createType("classification.test1", "id:0,value:String");
        String sVal[] = {"a", "b", "c", "d", "e", "f"};
        SimpleFeature[] myfeatures = new SimpleFeature[sVal.length];
        for (int i = 0; i < sVal.length; i++) {
            myfeatures[i] =
                    SimpleFeatureBuilder.build(
                            dataType,
                            new Object[] {Integer.valueOf(i + 1), sVal[i]},
                            "classification.test1" + (i + 1));
        }

        SimpleFeatureCollection myFeatureCollection = DataUtilities.collection(myfeatures);
        org.opengis.filter.expression.Expression func2 =
                ff.function("Quantile", ff.property("value"), ff.literal(6), ff.literal(true));

        Object value2 = func2.evaluate(myFeatureCollection);
        Classifier ranged2 = (Classifier) value2;
        double[] percentages2 = ranged2.getPercentages();
        assertEquals(percentages2.length, 6);
        for (double v : percentages2) {
            assertEquals(Math.floor(v), 16.0, 0d);
        }
    }

    @Test
    public void testPercentagesConsistencyWithUnevenDistributedValues() throws SchemaException {

        SimpleFeatureType dataType =
                DataUtilities.createType("classification.test1", "id:0,value:int");
        int iVal[] = {1, 1, 2, 3, 4, 5};
        SimpleFeature[] myfeatures = new SimpleFeature[iVal.length];
        for (int i = 0; i < iVal.length; i++) {
            myfeatures[i] =
                    SimpleFeatureBuilder.build(
                            dataType,
                            new Object[] {Integer.valueOf(i + 1), Integer.valueOf(iVal[i])},
                            "classification.test1" + (i + 1));
        }

        SimpleFeatureCollection myFeatureCollection = DataUtilities.collection(myfeatures);
        org.opengis.filter.expression.Expression func =
                ff.function("Quantile", ff.property("value"), ff.literal(5), ff.literal(true));

        Object value = func.evaluate(myFeatureCollection);
        assertTrue(value instanceof RangedClassifier);
        RangedClassifier ranged = (RangedClassifier) value;
        double[] percentages = ranged.getPercentages();
        assertEquals(percentages.length, 5);
        assertEquals(Math.floor(percentages[0]), 33.0, 0d);
        for (int i = 1; i < percentages.length; i++) {
            assertEquals(Math.floor(percentages[i]), 16.0, 0d);
        }
    }

    @Test
    public void testPercentagesConsistencyWithMoreClassThanIntervals() {
        org.opengis.filter.expression.Expression func =
                ff.function("Quantile", ff.property("foo"), ff.literal(10), ff.literal(true));
        Object value = func.evaluate(featureCollection);
        assertTrue(value instanceof RangedClassifier);
        RangedClassifier ranged = (RangedClassifier) value;
        double[] percentages = ranged.getPercentages();
        assertEquals(percentages.length, 8);
        for (double percentage : percentages) {
            assertEquals(12.5, percentage, 0d);
        }
    }
}
