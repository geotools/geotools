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
package org.geotools.filter.function;

import static junit.framework.TestCase.assertEquals;
import static org.geotools.feature.visitor.EqualAreaListVisitorTest.getMinMax;
import static org.geotools.feature.visitor.EqualAreaListVisitorTest.getSimplifiedStatesCollection;
import static org.geotools.feature.visitor.EqualAreaListVisitorTest.getTotalArea;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.NumberRange;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

@RunWith(Enclosed.class)
public class EqualAreaFunctionTest {

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();
    public static final Expression PERSONS = FF.property("PERSONS");

    static RangedClassifier assertRangedClassifier(Object classifier) {
        assertNotNull(classifier);
        assertThat(classifier, CoreMatchers.instanceOf(RangedClassifier.class));
        RangedClassifier ranged = (RangedClassifier) classifier;

        for (int i = 1; i < ranged.getSize(); i++) {
            assertEquals(
                    ((Number) ranged.getMin(i)).doubleValue(),
                    ((Number) ranged.getMax(i - 1)).doubleValue(),
                    0d);
        }

        return ranged;
    }

    public static class BasicTests {

        @Test
        public void testEmptyCollection() throws SchemaException, IOException {
            ListFeatureCollection empty =
                    new ListFeatureCollection(getSimplifiedStatesCollection().getSchema());
            org.opengis.filter.expression.Expression function =
                    FF.function("EqualArea", PERSONS, FF.literal(5));
            assertNull(function.evaluate(empty));
        }

        @Test
        public void testMoreClassesThanFeatures() throws SchemaException, IOException {
            ListFeatureCollection fc = getSimplifiedStatesCollection();
            Expression areaFunction = EqualAreaFunction.getCartesianAreaFunction();

            // twice as many classes
            org.opengis.filter.expression.Expression function =
                    FF.function("EqualArea", PERSONS, FF.literal(fc.size() * 2));
            RangedClassifier ranged = assertRangedClassifier(function.evaluate(fc));

            // check the number of classes has been reduced (not 49 but 43, with high
            // number of classes it's likely that it's not possible to fill bins
            assertEquals(ranged.getSize(), 43);
        }

        /** Test a feature collection where each feature has the same area */
        @Test
        public void testSingleBin() throws Exception {
            // create a feature collection with five features values 1-5
            SimpleFeatureType dataType =
                    DataUtilities.createType("singleBin", "id:0,value:int,geom:Polygon");
            int iVal[] = new int[] {1, 2, 3, 4, 5};
            SimpleFeature[] myfeatures = new SimpleFeature[iVal.length];
            Polygon polygon = (Polygon) new WKTReader().read("POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))");
            for (int i = 0; i < iVal.length; i++) {
                myfeatures[i] =
                        SimpleFeatureBuilder.build(
                                dataType,
                                new Object[] {new Integer(i + 1), new Integer(iVal[i]), polygon},
                                "classification.test1" + (i + 1));
            }
            SimpleFeatureCollection myFeatureCollection = DataUtilities.collection(myfeatures);

            // run the equal area function
            org.opengis.filter.expression.Expression function =
                    FF.function("EqualArea", FF.property("value"), FF.literal(5));
            RangedClassifier ranged =
                    assertRangedClassifier(function.evaluate(myFeatureCollection));
            assertEquals(5, ranged.getSize());

            for (int i = 0; i < 5; i++) {
                assertEquals(i + 1, ((Number) ranged.getMin(i)).doubleValue(), 0d);
                if (i != 4) {
                    assertEquals(i + 2, ((Number) ranged.getMax(i)).doubleValue(), 0d);
                    assertEquals((i + 1) + ".." + (i + 2), ranged.getTitle(i));
                } else {
                    assertEquals(i + 1, ((Number) ranged.getMax(i)).doubleValue(), 0d);
                    assertEquals((i + 1) + ".." + (i + 1), ranged.getTitle(i));
                }
            }
        }
    }

    /**
     * Tests different number of classes with a tolerance on the deviation from an ideal equal area
     * distribution (each class having 1/nth of the area, which is of course impossible)
     */
    @RunWith(Parameterized.class)
    public static class ParametricBreaksTest {

        @Parameterized.Parameter public int numClasses;

        @Parameterized.Parameter(1)
        public double tolerance;

        @Parameterized.Parameters(name = "Classes: {0}, Tolerance {1}")
        public static Collection<Object[]> data() {
            // as a rule of thumb, the more classes, the harder it is to split them in even blocks,
            // especially if the original polygons have much variation in size
            return Arrays.asList(
                    new Object[][] {{2, 0.1}, {3, 0.1}, {4, 0.4}, {5, 0.5}, {6, 0.6}, {7, 0.5}});
        }

        @Test
        public void testEqualAreaClassification() throws IOException, SchemaException {
            ListFeatureCollection fc = getSimplifiedStatesCollection();
            Function areaFunction = EqualAreaFunction.getCartesianAreaFunction();
            double totalArea = getTotalArea(fc, areaFunction);
            NumberRange<Double> minMax = getMinMax(fc, PERSONS);

            org.opengis.filter.expression.Expression function =
                    FF.function("EqualArea", PERSONS, FF.literal(numClasses));
            RangedClassifier rangedClassifier = assertRangedClassifier(function.evaluate(fc));

            assertEquals(numClasses, rangedClassifier.getSize());
            assertThat(minMax.getMinimum(), Matchers.equalTo(rangedClassifier.getMin(0)));
            assertThat(
                    minMax.getMaximum(), Matchers.equalTo(rangedClassifier.getMax(numClasses - 1)));

            // build the total area for each range
            Map<Filter, Double> filterMap = new LinkedHashMap<>();
            for (int i = 0; i < rangedClassifier.getSize(); i++) {
                // the classifier uses the first element of the next bin as the break point,
                // and property is between is implemented to catch both ends, so we need to
                // use an explict filter instead
                PropertyIsGreaterThanOrEqualTo greater =
                        FF.greaterOrEqual(PERSONS, FF.literal(rangedClassifier.getMin(i)));
                Filter less;
                if (i < rangedClassifier.getSize() - 1) {
                    less = FF.less(PERSONS, FF.literal(rangedClassifier.getMax(i)));
                } else {
                    less = FF.lessOrEqual(PERSONS, FF.literal(rangedClassifier.getMax(i)));
                }
                Filter filter = FF.and(greater, less);
                filterMap.put(filter, new Double(0));
            }
            for (SimpleFeature feature : fc) {
                boolean found = false;
                for (Map.Entry<Filter, Double> entry : filterMap.entrySet()) {
                    if (entry.getKey().evaluate(feature)) {
                        entry.setValue(
                                entry.getValue() + areaFunction.evaluate(feature, Double.class));
                        found = true;
                        break;
                    }
                }
                assertTrue("Feature " + feature + " not matched by " + filterMap.keySet(), found);
            }

            // test deviation from ideal average area
            double averageArea = totalArea / filterMap.size();
            for (Map.Entry<Filter, Double> entry : filterMap.entrySet()) {
                double area = entry.getValue();
                double deviation = Math.abs(area - averageArea) / averageArea;
                assertThat(deviation, Matchers.lessThanOrEqualTo(tolerance));
            }
        }
    }
}
