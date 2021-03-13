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

import static org.geotools.feature.visitor.EqualAreaListVisitorTest.getMinMax;
import static org.geotools.feature.visitor.EqualAreaListVisitorTest.getSimplifiedStatesCollection;
import static org.geotools.feature.visitor.EqualAreaListVisitorTest.getTotalArea;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
            int iVal[] = {1, 2, 3, 4, 5};
            SimpleFeature[] myfeatures = new SimpleFeature[iVal.length];
            Polygon polygon = (Polygon) new WKTReader().read("POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))");
            for (int i = 0; i < iVal.length; i++) {
                myfeatures[i] =
                        SimpleFeatureBuilder.build(
                                dataType,
                                new Object[] {
                                    Integer.valueOf(i + 1), Integer.valueOf(iVal[i]), polygon
                                },
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

        /** Test a feature collection where the classified attribute always has the same value */
        @Test
        public void testConstantValue() throws Exception {
            // create a feature collection with five features values 1-5
            SimpleFeatureType dataType =
                    DataUtilities.createType("singleBin", "id:0,value:int,geom:Polygon");
            int iVal[] = {1, 1, 1, 1, 1};
            SimpleFeature[] myfeatures = new SimpleFeature[iVal.length];
            Polygon polygon = (Polygon) new WKTReader().read("POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))");
            for (int i = 0; i < iVal.length; i++) {
                myfeatures[i] =
                        SimpleFeatureBuilder.build(
                                dataType,
                                new Object[] {
                                    Integer.valueOf(i + 1), Integer.valueOf(iVal[i]), polygon
                                },
                                "classification.test1" + (i + 1));
            }
            SimpleFeatureCollection myFeatureCollection = DataUtilities.collection(myfeatures);

            // run the equal area function
            org.opengis.filter.expression.Expression function =
                    FF.function("EqualArea", FF.property("value"), FF.literal(5));
            RangedClassifier ranged =
                    assertRangedClassifier(function.evaluate(myFeatureCollection));
            assertEquals(1, ranged.getSize());
            assertEquals(1, ranged.getMin(0));
            assertEquals(1, ranged.getMax(0));
        }

        @Test
        public void testEvaluateWithPercentages() throws Exception {
            // create a feature collection with five features values 1-5
            SimpleFeatureType dataType =
                    DataUtilities.createType("singleBin", "id:0,value:int,geom:Polygon");
            SimpleFeature[] myfeatures = new SimpleFeature[3];
            Polygon polygon =
                    (Polygon)
                            new WKTReader()
                                    .read(
                                            "POLYGON ((22.52313252202016 20, 22.47465123810405 19.50776126378972, 22.33107049490801 19.03443898616134, 22.097908019872047 18.598222676801925, 21.78412411615277 18.21587588384723, 21.401777323198075 17.902091980127953, 20.96556101383866 17.66892950509199, 20.49223873621028 17.52534876189595, 20 17.47686747797984, 19.50776126378972 17.52534876189595, 19.03443898616134 17.66892950509199, 18.598222676801925 17.902091980127953, 18.21587588384723 18.21587588384723, 17.902091980127953 18.598222676801925, 17.668929505091988 19.03443898616134, 17.52534876189595 19.50776126378972, 17.47686747797984 20.000000000000004, 17.52534876189595 20.492238736210282, 17.66892950509199 20.965561013838663, 17.902091980127956 21.40177732319808, 18.215875883847232 21.784124116152775, 18.598222676801928 22.097908019872047, 19.034438986161344 22.331070494908012, 19.507761263789728 22.474651238104055, 20.000000000000007 22.52313252202016, 20.492238736210286 22.47465123810405, 20.965561013838666 22.33107049490801, 21.401777323198083 22.09790801987204, 21.78412411615278 21.784124116152764, 22.09790801987205 21.40177732319807, 22.331070494908012 20.965561013838652, 22.474651238104055 20.49223873621027, 22.52313252202016 20))");
            Polygon polygon2 =
                    (Polygon)
                            new WKTReader()
                                    .read(
                                            "POLYGON ((95.35237234845832 90, 95.24952801460519 88.95580395498905, 94.94494726312 87.95173577839597, 94.4503349614715 87.02638124715934, 93.78469878303024 86.21530121696976, 92.97361875284066 85.5496650385285, 92.04826422160403 85.05505273688, 91.04419604501095 84.75047198539481, 90 84.64762765154168, 88.95580395498905 84.75047198539481, 87.95173577839597 85.05505273688, 87.02638124715934 85.5496650385285, 86.21530121696976 86.21530121696976, 85.5496650385285 87.02638124715934, 85.05505273688 87.95173577839597, 84.75047198539481 88.95580395498905, 84.64762765154168 90, 84.75047198539482 91.04419604501096, 85.05505273688 92.04826422160403, 85.5496650385285 92.97361875284066, 86.21530121696976 93.78469878303025, 87.02638124715935 94.4503349614715, 87.95173577839599 94.94494726312001, 88.95580395498907 95.24952801460519, 90.00000000000001 95.35237234845832, 91.04419604501096 95.24952801460518, 92.04826422160404 94.94494726312, 92.97361875284068 94.45033496147148, 93.78469878303025 93.78469878303022, 94.45033496147151 92.97361875284065, 94.94494726312001 92.048264221604, 95.24952801460519 91.04419604501093, 95.35237234845832 90))");
            Polygon polygon3 =
                    (Polygon)
                            new WKTReader()
                                    .read(
                                            "POLYGON ((95.35237234845832 90, 95.24952801460519 88.95580395498905, 94.94494726312 87.95173577839597, 94.4503349614715 87.02638124715934, 93.78469878303024 86.21530121696976, 92.97361875284066 85.5496650385285, 92.04826422160403 85.05505273688, 91.04419604501095 84.75047198539481, 90 84.64762765154168, 88.95580395498905 84.75047198539481, 87.95173577839597 85.05505273688, 87.02638124715934 85.5496650385285, 86.21530121696976 86.21530121696976, 85.5496650385285 87.02638124715934, 85.05505273688 87.95173577839597, 84.75047198539481 88.95580395498905, 84.64762765154168 90, 84.75047198539482 91.04419604501096, 85.05505273688 92.04826422160403, 85.5496650385285 92.97361875284066, 86.21530121696976 93.78469878303025, 87.02638124715935 94.4503349614715, 87.95173577839599 94.94494726312001, 88.95580395498907 95.24952801460519, 90.00000000000001 95.35237234845832, 91.04419604501096 95.24952801460518, 92.04826422160404 94.94494726312, 92.97361875284068 94.45033496147148, 93.78469878303025 93.78469878303022, 94.45033496147151 92.97361875284065, 94.94494726312001 92.048264221604, 95.24952801460519 91.04419604501093, 95.35237234845832 90))");
            myfeatures[0] =
                    SimpleFeatureBuilder.build(
                            dataType,
                            new Object[] {Integer.valueOf(1), Integer.valueOf(0), polygon},
                            "classification.test1" + 1);
            myfeatures[1] =
                    SimpleFeatureBuilder.build(
                            dataType,
                            new Object[] {Integer.valueOf(2), Integer.valueOf(1), polygon2},
                            "classification.test1" + 2);
            myfeatures[2] =
                    SimpleFeatureBuilder.build(
                            dataType,
                            new Object[] {Integer.valueOf(3), Integer.valueOf(2), polygon3},
                            "classification.test1" + 3);
            SimpleFeatureCollection myFeatureCollection = DataUtilities.collection(myfeatures);
            // run the equal area function
            org.opengis.filter.expression.Expression function =
                    FF.function(
                            "EqualArea",
                            FF.property("value"),
                            FF.literal(2),
                            FF.literal(1.0),
                            FF.literal(true));
            RangedClassifier ranged =
                    assertRangedClassifier(function.evaluate(myFeatureCollection));
            double[] percentages = ranged.getPercentages();
            assertEquals(2, ranged.getSize());
            assertEquals(2, percentages.length);
            assertEquals(Math.ceil(percentages[0]), 67.0, 0d);
            assertEquals(Math.floor(percentages[1]), 33.0, 0d);
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
                filterMap.put(filter, Double.valueOf(0));
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
