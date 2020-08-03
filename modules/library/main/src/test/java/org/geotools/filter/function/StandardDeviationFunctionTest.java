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

import java.util.logging.Logger;
import java.util.stream.DoubleStream;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/** @author Cory Horner, Refractions Research */
public class StandardDeviationFunctionTest extends FunctionTestSupport {
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(StandardDeviationFunctionTest.class);

    public StandardDeviationFunctionTest(String testName) {
        super(testName);
    }

    protected void tearDown() throws java.lang.Exception {}

    public static junit.framework.Test suite() {
        junit.framework.TestSuite suite =
                new junit.framework.TestSuite(StandardDeviationFunctionTest.class);

        return suite;
    }

    public void testInstance() {
        Function stdDev =
                ff.function("StandardDeviation", ff.literal(new DefaultFeatureCollection()));
        assertNotNull(stdDev);
    }

    public void testGetName() {
        Function equInt =
                ff.function("StandardDeviation", ff.literal(new DefaultFeatureCollection()));
        LOGGER.finer("testGetName");
        assertEquals("StandardDeviation", equInt.getName());
    }

    public void testSetNumberOfClasses() throws Exception {
        LOGGER.finer("testSetNumberOfClasses");

        Literal classes = ff.literal(3);
        PropertyName exp = ff.property("foo");
        StandardDeviationFunction func =
                (StandardDeviationFunction) ff.function("StandardDeviation", exp, classes);
        assertEquals(3, func.getClasses());
        classes = ff.literal(12);
        func = (StandardDeviationFunction) ff.function("StandardDeviation", exp, classes);
        assertEquals(12, func.getClasses());
    }

    public void testGetValue() throws Exception {
        // doesn't work yet?
        Literal classes = ff.literal(5);
        PropertyName exp = ff.property("foo");
        Function standardDeviation = ff.function("StandardDeviation", exp, classes);
        assertNotNull("step 1 - standard deviation function", standardDeviation);

        final Classifier classifer =
                standardDeviation.evaluate(featureCollection, Classifier.class);
        featureCollection.accepts(
                new FeatureVisitor() {
                    @Override
                    public void visit(Feature f) {
                        SimpleFeature feature = (SimpleFeature) f;
                        Object value = feature.getAttribute("foo");
                        assertNotNull(feature.getID() + " foo", value);

                        int slot = classifer.classify(value);
                        assertNotNull("Slot " + slot, classifer.getTitle(slot));
                    }
                },
                null);

        Function classify = ff.function("classify", exp, ff.literal(classifer));
        assertNotNull("step 2 - classify function", classify);

        SimpleFeatureIterator list = featureCollection.features();

        // feature 1
        SimpleFeature f = list.next();
        Integer slot = classify.evaluate(f, Integer.class);
        assertEquals("value " + f.getAttribute("foo"), 1, slot.intValue());

        // feature 2
        f = list.next();
        slot = classify.evaluate(f, Integer.class);
        assertEquals("value " + f.getAttribute("foo"), 4, slot.intValue());

        // feature 3
        f = list.next();
        slot = classify.evaluate(f, Integer.class);
        assertEquals("value " + f.getAttribute("foo"), 2, slot.intValue());

        // feature 4
        f = list.next();
        slot = classify.evaluate(f, Integer.class);
        assertEquals("value " + f.getAttribute("foo"), 2, slot.intValue());

        // feature 5
        f = list.next();
        slot = classify.evaluate(f, Integer.class);
        assertEquals("value " + f.getAttribute("foo"), 2, slot.intValue());

        // feature 6
        f = list.next();
        slot = classify.evaluate(f, Integer.class);
        assertEquals("value " + f.getAttribute("foo"), 3, slot.intValue());

        // feature 7
        f = list.next();
        slot = classify.evaluate(f, Integer.class);
        assertEquals("value " + f.getAttribute("foo"), 1, slot.intValue());

        // feature 8
        f = list.next();
        slot = classify.evaluate(f, Integer.class);
        assertEquals("value " + f.getAttribute("foo"), 1, slot.intValue());
    }

    public void testConstantValuesNumeric() {
        Function function = ff.function("StandardDeviation", ff.property("v"), ff.literal(12));
        RangedClassifier classifier = (RangedClassifier) function.evaluate(constantCollection);
        assertNotNull(classifier);
        assertEquals(1, classifier.getSize());
        assertEquals(123.123, (Double) classifier.getMin(0), 0d);
        assertEquals(123.123, (Double) classifier.getMax(0), 0d);
    }

    public void testPercentagesOddClassNum() {
        Literal classes = ff.literal(5);
        PropertyName exp = ff.property("foo");
        Function standardDeviation =
                ff.function("StandardDeviation", exp, classes, ff.literal(true));
        assertNotNull("step 1 - standard deviation function", standardDeviation);

        final Classifier classifier =
                standardDeviation.evaluate(stddevCollection, Classifier.class);
        double[] percentages = classifier.getPercentages();
        assertNotNull(percentages);
        assertEquals(5, percentages.length);
        assertEquals(10d, percentages[0]);
        assertEquals(0d, percentages[1]);
        assertEquals(70d, percentages[2]);
        assertEquals(10d, percentages[3]);
        assertEquals(10d, percentages[4]);

        assertEquals(100d, DoubleStream.of(percentages).sum());
    }

    public void testPercentagesEvenClassNum() {
        Literal classes = ff.literal(6);
        PropertyName exp = ff.property("foo");
        Function standardDeviation =
                ff.function("StandardDeviation", exp, classes, ff.literal(true));
        assertNotNull("step 1 - standard deviation function", standardDeviation);

        final Classifier classifier =
                standardDeviation.evaluate(stddevCollection, Classifier.class);
        double[] percentages = classifier.getPercentages();
        assertNotNull(percentages);
        assertEquals(6, percentages.length);
        assertEquals(10d, percentages[0]);
        assertEquals(0d, percentages[1]);
        assertEquals(50d, percentages[2]);
        assertEquals(30d, percentages[3]);
        assertEquals(0d, percentages[4]);
        assertEquals(10d, percentages[5]);
        assertEquals(100d, DoubleStream.of(percentages).sum());
    }
}
