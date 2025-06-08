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
package org.geotools.feature.visitor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.expression.Expression;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.filter.function.EqualAreaFunction;
import org.geotools.util.NumberRange;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Enclosed.class)
public class EqualAreaListVisitorTest {

    static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();
    public static final Expression PERSONS = FF.property("PERSONS");

    /** Returns a feature collection with a simplified US geometry and PERSONS count */
    public static ListFeatureCollection getSimplifiedStatesCollection() throws SchemaException, IOException {
        SimpleFeatureType schema =
                DataUtilities.createType("states", "the_geom:MultiPolygon,STATE_ABBR:String,PERSONS:Double");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                EqualAreaListVisitorTest.class.getResourceAsStream("states.properties"), StandardCharsets.UTF_8))) {
            List<SimpleFeature> featureList = br.lines()
                    .map(line -> DataUtilities.createFeature(schema, line))
                    .collect(Collectors.toList());
            return new ListFeatureCollection(schema, featureList);
        }
    }

    public static double getTotalArea(ListFeatureCollection fc, Expression areaFunction) {
        return fc.stream()
                .mapToDouble(f -> areaFunction.evaluate(f, Double.class))
                .sum();
    }

    /** Returns min and max value applying the expression over the feature collection */
    public static NumberRange<Double> getMinMax(ListFeatureCollection fc, Expression attributeExpression) {
        DoubleSummaryStatistics stats =
                fc.stream().collect(Collectors.summarizingDouble(f -> attributeExpression.evaluate(f, Double.class)));
        return new NumberRange<>(Double.class, stats.getMin(), stats.getMax());
    }

    public static class BasicTests {

        @Test
        public void testEmptyCollection() throws SchemaException, IOException {
            EqualAreaListVisitor visitor =
                    new EqualAreaListVisitor(PERSONS, EqualAreaFunction.getCartesianAreaFunction(), 2);
            ListFeatureCollection empty =
                    new ListFeatureCollection(getSimplifiedStatesCollection().getSchema());
            empty.accepts(visitor, null);
            assertEquals(CalcResult.NULL_RESULT, visitor.getResult());
        }

        @Test
        public void testMoreClassesThanFeatures() throws SchemaException, IOException {
            ListFeatureCollection fc = getSimplifiedStatesCollection();
            Expression areaFunction = EqualAreaFunction.getCartesianAreaFunction();

            // twice as many classes
            EqualAreaListVisitor visitor = new EqualAreaListVisitor(PERSONS, areaFunction, fc.size() * 2);
            fc.accepts(visitor, null);
            @SuppressWarnings("unchecked")
            List<Double>[] result = (List<Double>[]) visitor.getResult().getValue();

            // check the number of classes has been reduced (43 not 49, not always possible to
            // fill all bins when there are so many classes)
            assertEquals(result.length, 43);
        }
    }

    /**
     * Tests different number of classes with a tolerance on the deviation from an ideal equal area distribution (each
     * class having 1/nth of the area, which is of course impossible)
     */
    @RunWith(Parameterized.class)
    public static class ParametricBreaksTest {

        @Parameterized.Parameter
        public int numClasses;

        @Parameterized.Parameter(1)
        public double tolerance;

        @Parameterized.Parameters(name = "Classes: {0}, Tolerance {1}")
        public static Collection<Object[]> data() {
            // as a rule of thumb, the more classes, the harder it is to split them in even blocks,
            // especially if the original polygons have much variation in size
            return Arrays.asList(new Object[][] {{2, 0.1}, {3, 0.1}, {4, 0.4}, {5, 0.5}, {6, 0.6}, {7, 0.5}});
        }

        @Test
        public void testEqualAreaClassification() throws IOException, SchemaException {
            ListFeatureCollection fc = getSimplifiedStatesCollection();
            Expression areaFunction = EqualAreaFunction.getCartesianAreaFunction();
            double totalArea = getTotalArea(fc, areaFunction);
            NumberRange<Double> minMax = getMinMax(fc, PERSONS);

            EqualAreaListVisitor visitor = new EqualAreaListVisitor(PERSONS, areaFunction, numClasses);
            fc.accepts(visitor, null);
            @SuppressWarnings("unchecked")
            List<Double>[] result = (List<Double>[]) visitor.getResult().getValue();

            List<Double> firstBin = result[0];
            assertThat(minMax.getMinimum(), Matchers.equalTo(firstBin.get(0)));
            List<Double> lastBin = result[result.length - 1];
            assertThat(minMax.getMaximum(), Matchers.equalTo(lastBin.get(lastBin.size() - 1)));

            Map<PropertyIsBetween, Double> filterMap = Arrays.stream(result)
                    .map(list -> FF.between(PERSONS, FF.literal(list.get(0)), FF.literal(list.get(list.size() - 1))))
                    .collect(Collectors.toMap(k -> k, k -> Double.valueOf(0)));
            for (SimpleFeature feature : fc) {
                boolean found = false;
                for (Map.Entry<PropertyIsBetween, Double> entry : filterMap.entrySet()) {

                    if (entry.getKey().evaluate(feature)) {
                        entry.setValue(entry.getValue() + areaFunction.evaluate(feature, Double.class));
                        found = true;
                        break;
                    }
                }
                assertTrue(found);
            }
            double averageArea = totalArea / filterMap.size();

            for (Map.Entry<PropertyIsBetween, Double> entry : filterMap.entrySet()) {
                double area = entry.getValue();
                double deviation = Math.abs(area - averageArea) / averageArea;
                assertThat(deviation, Matchers.lessThanOrEqualTo(tolerance));
            }
        }
    }
}
