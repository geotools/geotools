/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/** @author James */
public class EqualIntervalFunctionTest extends FunctionTestSupport {

    private static final org.opengis.filter.FilterFactory ff =
            CommonFactoryFinder.getFilterFactory(null);

    /** Test of getName method, of class org.geotools.filter.functions.EqualIntervalFunction. */
    @Test
    public void testInstance() {
        Function equInt =
                ff.function("EqualInterval", org.opengis.filter.expression.Expression.NIL);
        assertNotNull(equInt);
        assertEquals("test get name", "EqualInterval", equInt.getName());
    }

    /**
     * Test of setNumberOfClasses method, of class
     * org.geotools.filter.functions.EqualIntervalFunction.
     */
    @Test
    public void testSetClasses() throws Exception {
        PropertyName property = ff.property("foo");
        Literal literal = ff.literal(3);

        EqualIntervalFunction func =
                (EqualIntervalFunction) ff.function("EqualInterval", property, literal);
        assertEquals(3, func.getClasses());

        func.getParameters().set(1, ff.literal(12));
        assertEquals(12, func.getClasses());
    }

    @Test
    public void testEvaluateWithExpressions() throws Exception {
        Literal classes = ff.literal(3);
        PropertyName name = ff.property("foo");
        Function func = ff.function("EqualInterval", name, classes);

        Object classifier = func.evaluate(featureCollection);
        assertTrue(classifier instanceof RangedClassifier);
        RangedClassifier ranged = (RangedClassifier) classifier;
        // values = 4,90,20,43,29,61,8,12
        // 4..90 = 4..32.67, 32.67..61.33, 61.33..90

        // correct number of classes
        assertEquals(3, ranged.getSize());
        // correct titles
        assertEquals("4..32.667", ranged.getTitle(0));
        assertEquals("32.667..61.333", ranged.getTitle(1));
        assertEquals("61.333..90", ranged.getTitle(2));
        // check classifier binning
        assertEquals(0, ranged.classify(Double.valueOf(4)));
        assertEquals(2, ranged.classify(name, testFeatures[1])); // 90
        assertEquals(0, ranged.classify(Double.valueOf(20)));
        assertEquals(1, ranged.classify(Double.valueOf(43)));
        assertEquals(0, ranged.classify(Double.valueOf(29)));
        assertEquals(1, ranged.classify(Double.valueOf(61)));
        assertEquals(0, ranged.classify(name, testFeatures[6])); // 8
        assertEquals(0, ranged.classify(Double.valueOf(12)));

        // try again with foo
    }

    /** FIXME: Please for the love on binpop */
    @Test
    public void testEvaulateWithStrings() throws Exception {
        org.opengis.filter.expression.Expression function =
                ff.function("EqualInterval", ff.property("group"), ff.literal(5));
        Classifier classifier = (Classifier) function.evaluate(featureCollection);
        assertNotNull(classifier);

        Classifier classifier2 = function.evaluate(featureCollection, Classifier.class);
        assertNotNull(classifier2);

        Integer number = function.evaluate(featureCollection, Integer.class);
        assertNull(number);
    }

    @Test
    public void testUpgradeExample() {
        Function function = ff.function("equalInterval", ff.property("foo"), ff.literal(12));
        Object value = function.evaluate(featureCollection);
        assertNotNull("classifier failed", value);

        Classifier split = (Classifier) value;
        Function classify = ff.function("classify", ff.property("foo"), ff.literal(split));

        SimpleFeature victim = testFeatures[2]; // foo = 20
        assertEquals(
                "Feature was placed in wrong bin", Integer.valueOf(2), classify.evaluate(victim));
    }

    @Test
    public void testConstantValuesNumeric() {
        Function function = ff.function("equalInterval", ff.property("v"), ff.literal(12));
        RangedClassifier classifier = (RangedClassifier) function.evaluate(constantCollection);
        assertNotNull(classifier);
        assertEquals(1, classifier.getSize());
        assertEquals(123.123, (Double) classifier.getMin(0), 0d);
        assertEquals(123.123, (Double) classifier.getMax(0), 0d);
    }

    @Test
    public void testConstantValuesString() {
        Function function = ff.function("equalInterval", ff.property("s"), ff.literal(12));
        RangedClassifier classifier = (RangedClassifier) function.evaluate(constantCollection);
        assertNotNull(classifier);
        assertEquals(1, classifier.getSize());
        assertEquals("abc", classifier.getMin(0));
        assertEquals("abc", classifier.getMax(0));
    }

    @Test
    public void testEvaluateNumericalWithPercentages() {
        Literal classes = ff.literal(3);
        PropertyName name = ff.property("foo");
        Function func = ff.function("EqualInterval", name, classes, ff.literal(true));

        Object classifier = func.evaluate(featureCollection);
        assertTrue(classifier instanceof RangedClassifier);
        RangedClassifier ranged = (RangedClassifier) classifier;
        double[] percentages = ranged.getPercentages();
        assertEquals(3, percentages.length);
        assertEquals(62.5, percentages[0], 0d);
        assertEquals(25.0, percentages[1], 0d);
        assertEquals(12.5, percentages[2], 0d);
    }

    @Test
    public void testEvaluateNumericalWithPercentagesAndOutlier() throws SchemaException {
        SimpleFeatureType dataType =
                DataUtilities.createType("classification.test1", "id:0,foo:int,geom:Point");

        int iVal[] = {1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        FeatureCollection featureCollection = new ListFeatureCollection(dataType);
        GeometryFactory fac = new GeometryFactory();
        featureCollection = new ListFeatureCollection(dataType);
        for (int i = 0; i < iVal.length; i++) {
            SimpleFeature feature =
                    SimpleFeatureBuilder.build(
                            dataType,
                            new Object[] {
                                Integer.valueOf(i + 1),
                                Integer.valueOf(iVal[i]),
                                fac.createPoint(new Coordinate(iVal[i], iVal[i]))
                            },
                            "classification.t" + (i + 1));
            ((ListFeatureCollection) featureCollection).add(feature);
        }
        Literal classes = ff.literal(3);
        PropertyName name = ff.property("foo");
        Function func = ff.function("EqualInterval", name, classes, ff.literal(true));

        Object classifier = func.evaluate(featureCollection);
        assertTrue(classifier instanceof RangedClassifier);
        RangedClassifier ranged = (RangedClassifier) classifier;
        double[] percentages = ranged.getPercentages();
        assertEquals(3, percentages.length);
        assertEquals(33.0, Math.floor(percentages[0]), 0d);
        assertEquals(26.0, Math.floor(percentages[1]), 0d);
        assertEquals(39.0, Math.floor(percentages[2]), 0d);
    }

    @Test
    public void testEvaluateNonNumericalWithPercentages() {
        Literal classes = ff.literal(3);
        PropertyName name = ff.property("foo");
        Function func = ff.function("EqualInterval", name, classes, ff.literal(true));

        Object classifier = func.evaluate(featureCollection);
        assertTrue(classifier instanceof RangedClassifier);
        RangedClassifier ranged = (RangedClassifier) classifier;
        double[] percentages = ranged.getPercentages();
        assertEquals(3, percentages.length);
        assertEquals(62.5, percentages[0], 0d);
        assertEquals(25.0, percentages[1], 0d);
        assertEquals(12.5, percentages[2], 0d);
    }
}
