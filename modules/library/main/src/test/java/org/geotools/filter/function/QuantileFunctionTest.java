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

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/** @author Cory Horner, Refractions Research Inc. */
public class QuantileFunctionTest extends FunctionTestSupport {

    public QuantileFunctionTest(String testName) {
        super(testName);
    }

    protected void tearDown() throws java.lang.Exception {}

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(QuantileFunctionTest.class);

        return suite;
    }

    public void testInstance() {
        Function equInt = ff.function("Quantile", ff.literal(new DefaultFeatureCollection()));
        assertNotNull(equInt);
    }

    public void testGetName() {
        Function qInt = ff.function("Quantile", ff.literal(new DefaultFeatureCollection()));
        assertEquals("Quantile", qInt.getName());
    }

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
    public void testSingleBin() throws Exception {

        // create a feature collection with five features values 1-5
        SimpleFeatureType dataType =
                DataUtilities.createType("classification.test1", "id:0,value:int");
        int iVal[] = new int[] {1, 2, 3, 4, 5};
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
            assertTrue(i + 1 == ((Number) range.getMin(i)).doubleValue());
            if (i != 4) {
                assertTrue(i + 2 == ((Number) range.getMax(i)).doubleValue());
                assertEquals((i + 1) + ".." + (i + 2), range.getTitle(i));
            } else {
                assertTrue(i + 1 == ((Number) range.getMax(i)).doubleValue());
                assertEquals((i + 1) + ".." + (i + 1), range.getTitle(i));
            }
        }
    }

    public void test2() throws Exception {
        // create a feature collection with five features values 1-5
        SimpleFeatureType dataType =
                DataUtilities.createType("classification.test1", "id:0,value:int");
        int iVal[] = new int[] {1, 2, 3, 4, 5, 6};
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

    public void testEvaluateWithStrings() throws Exception {
        org.opengis.filter.expression.Expression function =
                ff.function("Quantile", ff.property("group"), ff.literal(2));
        Classifier classifier = (Classifier) function.evaluate(featureCollection);
        assertNotNull(classifier);

        Classifier classifier2 =
                (Classifier) function.evaluate(featureCollection, Classifier.class);
        assertNotNull(classifier2);

        Integer number = (Integer) function.evaluate(featureCollection, Integer.class);
        assertNull(number);
    }

    public void xtestNullNaNHandling() throws Exception {
        // create a feature collection
        SimpleFeatureType ft =
                DataUtilities.createType("classification.nullnan", "id:0,foo:int,bar:double");
        Integer iVal[] =
                new Integer[] {
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
        Double dVal[] =
                new Double[] {
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

    public void testConstantValuesNumeric() {
        Function function = ff.function("quantile", ff.property("v"), ff.literal(12));
        RangedClassifier classifier = (RangedClassifier) function.evaluate(constantCollection);
        assertNotNull(classifier);
        assertEquals(1, classifier.getSize());
        assertEquals(123.123, (Double) classifier.getMin(0), 0d);
        assertEquals(123.123, (Double) classifier.getMax(0), 0d);
    }

    public void testConstantValuesString() {
        Function function = ff.function("quantile", ff.property("s"), ff.literal(12));
        ExplicitClassifier classifier = (ExplicitClassifier) function.evaluate(constantCollection);
        assertNotNull(classifier);
        assertEquals(1, classifier.getSize());
        assertEquals(1, classifier.getValues(0).size());
        assertEquals("abc", classifier.getValues(0).iterator().next());
    }
}
