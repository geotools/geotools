/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.NullProgressListener;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

/**
 * This class implement the test that checks the group by visitor behavior with different aggregate
 * visitors. All the computations will be performed in memory. Optimization tests are implemented in
 * the specific stores.
 */
public class GroupByVisitorTest {

    private static WKTReader wktParser = new WKTReader();

    private static SimpleFeatureType buildingType;
    private static FeatureCollection featureCollection;

    @BeforeClass
    public static void setup() throws Exception {
        // the building feature type that will be used during the tests
        buildingType =
                DataUtilities.createType(
                        "buildings",
                        "id:Integer,building_id:String,"
                                + "building_type:String,energy_type:String,energy_consumption:Double,geo:Geometry");
        // the building features that will be used during the tests
        SimpleFeature[] buildingFeatures =
                new SimpleFeature[] {
                    SimpleFeatureBuilder.build(
                            buildingType,
                            new Object[] {
                                1,
                                "SCHOOL_A",
                                "SCHOOL",
                                "FLOWING_WATER",
                                50.0,
                                wktParser.read("POINT(-5 -5)")
                            },
                            null),
                    SimpleFeatureBuilder.build(
                            buildingType,
                            new Object[] {
                                2,
                                "SCHOOL_A",
                                "SCHOOL",
                                "NUCLEAR",
                                10.0,
                                wktParser.read("POINT(-5 -5)")
                            },
                            null),
                    SimpleFeatureBuilder.build(
                            buildingType,
                            new Object[] {
                                3,
                                "SCHOOL_A",
                                "SCHOOL",
                                "WIND",
                                20.0,
                                wktParser.read("POINT(-5 -5)")
                            },
                            null),
                    SimpleFeatureBuilder.build(
                            buildingType,
                            new Object[] {
                                4, "SCHOOL_B", "SCHOOL", "SOLAR", 30.0, wktParser.read("POINT(5 5)")
                            },
                            null),
                    SimpleFeatureBuilder.build(
                            buildingType,
                            new Object[] {
                                5, "SCHOOL_B", "SCHOOL", "FUEL", 60.0, wktParser.read("POINT(5 5)")
                            },
                            null),
                    SimpleFeatureBuilder.build(
                            buildingType,
                            new Object[] {
                                6,
                                "SCHOOL_B",
                                "SCHOOL",
                                "NUCLEAR",
                                10.0,
                                wktParser.read("POINT(5 5)")
                            },
                            null),
                    SimpleFeatureBuilder.build(
                            buildingType,
                            new Object[] {
                                7,
                                "FABRIC_A",
                                "FABRIC",
                                "FLOWING_WATER",
                                500.0,
                                wktParser.read("POINT(-5 5)")
                            },
                            null),
                    SimpleFeatureBuilder.build(
                            buildingType,
                            new Object[] {
                                8,
                                "FABRIC_A",
                                "FABRIC",
                                "NUCLEAR",
                                150.0,
                                wktParser.read("POINT(-5 5)")
                            },
                            null),
                    SimpleFeatureBuilder.build(
                            buildingType,
                            new Object[] {
                                9, "FABRIC_B", "FABRIC", "WIND", 20.0, wktParser.read("POINT(5 -5)")
                            },
                            null),
                    SimpleFeatureBuilder.build(
                            buildingType,
                            new Object[] {
                                10,
                                "FABRIC_B",
                                "FABRIC",
                                "SOLAR",
                                30.0,
                                wktParser.read("POINT(5 -5)")
                            },
                            null),
                    SimpleFeatureBuilder.build(
                            buildingType,
                            new Object[] {
                                11, "HOUSE_A", "HOUSE", "FUEL", 6.0, wktParser.read("POINT(0 0)")
                            },
                            null),
                    SimpleFeatureBuilder.build(
                            buildingType,
                            new Object[] {
                                12, "HOUSE_B", "HOUSE", "NUCLEAR", 4.0, wktParser.read("POINT(0 0)")
                            },
                            null),
                };
        // creating the bulding featrue collection
        featureCollection = DataUtilities.collection(buildingFeatures);
    }

    @Test
    public void testGroupByWithAverageAndSingleAttribute() throws Exception {
        testVisitor(
                "energy_consumption",
                "Average",
                "building_type",
                new Object[][] {
                    new Object[] {"SCHOOL", 30.0},
                    new Object[] {"FABRIC", 175.0},
                    new Object[] {"HOUSE", 5.0}
                });
    }

    @Test
    public void testGroupByWithAverageAndTwoAttributes() throws Exception {
        testVisitor(
                "energy_consumption",
                "Average",
                "building_type",
                "energy_type",
                new Object[][] {
                    new Object[] {"FABRIC", "FLOWING_WATER", 500.0},
                    new Object[] {"FABRIC", "NUCLEAR", 150.0},
                    new Object[] {"FABRIC", "SOLAR", 30.0},
                    new Object[] {"FABRIC", "WIND", 20.0},
                    new Object[] {"HOUSE", "FUEL", 6.0},
                    new Object[] {"HOUSE", "NUCLEAR", 4.0},
                    new Object[] {"SCHOOL", "FLOWING_WATER", 50.0},
                    new Object[] {"SCHOOL", "FUEL", 60.0},
                    new Object[] {"SCHOOL", "NUCLEAR", 10.0},
                    new Object[] {"SCHOOL", "SOLAR", 30.0},
                    new Object[] {"SCHOOL", "WIND", 20.0}
                });
    }

    @Test
    public void testGroupByWithCountAndSingleAttribute() throws Exception {
        testVisitor(
                "energy_consumption",
                "Count",
                "building_type",
                new Object[][] {
                    new Object[] {"SCHOOL", 6},
                    new Object[] {"FABRIC", 4},
                    new Object[] {"HOUSE", 2}
                });
    }

    @Test
    public void testGroupByWithCountAndTwoAttributes() throws Exception {
        testVisitor(
                "energy_consumption",
                "Count",
                "building_type",
                "energy_type",
                new Object[][] {
                    new Object[] {"FABRIC", "FLOWING_WATER", 1},
                    new Object[] {"FABRIC", "NUCLEAR", 1},
                    new Object[] {"FABRIC", "SOLAR", 1},
                    new Object[] {"FABRIC", "WIND", 1},
                    new Object[] {"HOUSE", "FUEL", 1},
                    new Object[] {"HOUSE", "NUCLEAR", 1},
                    new Object[] {"SCHOOL", "FLOWING_WATER", 1},
                    new Object[] {"SCHOOL", "FUEL", 1},
                    new Object[] {"SCHOOL", "NUCLEAR", 2},
                    new Object[] {"SCHOOL", "SOLAR", 1},
                    new Object[] {"SCHOOL", "WIND", 1}
                });
    }

    @Test
    public void testGroupByWithMaxAndSingleAttribute() throws Exception {
        testVisitor(
                "energy_consumption",
                "Max",
                "building_type",
                new Object[][] {
                    new Object[] {"SCHOOL", 60.0},
                    new Object[] {"FABRIC", 500.0},
                    new Object[] {"HOUSE", 6.0}
                });
    }

    @Test
    public void testGroupByWithMaxAndTwoAttributes() throws Exception {
        testVisitor(
                "energy_consumption",
                "Max",
                "building_type",
                "energy_type",
                new Object[][] {
                    new Object[] {"FABRIC", "FLOWING_WATER", 500.0},
                    new Object[] {"FABRIC", "NUCLEAR", 150.0},
                    new Object[] {"FABRIC", "SOLAR", 30.0},
                    new Object[] {"FABRIC", "WIND", 20.0},
                    new Object[] {"HOUSE", "FUEL", 6.0},
                    new Object[] {"HOUSE", "NUCLEAR", 4.0},
                    new Object[] {"SCHOOL", "FLOWING_WATER", 50.0},
                    new Object[] {"SCHOOL", "FUEL", 60.0},
                    new Object[] {"SCHOOL", "NUCLEAR", 10.0},
                    new Object[] {"SCHOOL", "SOLAR", 30.0},
                    new Object[] {"SCHOOL", "WIND", 20.0}
                });
    }

    @Test
    public void testGroupByWithMedianAndSingleAttribute() throws Exception {
        testVisitor(
                "energy_consumption",
                "Median",
                "building_type",
                new Object[][] {
                    new Object[] {"SCHOOL", 25.0},
                    new Object[] {"FABRIC", 90.0},
                    new Object[] {"HOUSE", 5.0}
                });
    }

    @Test
    public void testGroupByWithMedianAndTwoAttributes() throws Exception {
        testVisitor(
                "energy_consumption",
                "Median",
                "building_type",
                "energy_type",
                new Object[][] {
                    new Object[] {"FABRIC", "FLOWING_WATER", 500.0},
                    new Object[] {"FABRIC", "NUCLEAR", 150.0},
                    new Object[] {"FABRIC", "SOLAR", 30.0},
                    new Object[] {"FABRIC", "WIND", 20.0},
                    new Object[] {"HOUSE", "FUEL", 6.0},
                    new Object[] {"HOUSE", "NUCLEAR", 4.0},
                    new Object[] {"SCHOOL", "FLOWING_WATER", 50.0},
                    new Object[] {"SCHOOL", "FUEL", 60.0},
                    new Object[] {"SCHOOL", "NUCLEAR", 10.0},
                    new Object[] {"SCHOOL", "SOLAR", 30.0},
                    new Object[] {"SCHOOL", "WIND", 20.0}
                });
    }

    @Test
    public void testGroupByWithMinAndSingleAttribute() throws Exception {
        testVisitor(
                "energy_consumption",
                "Min",
                "building_type",
                new Object[][] {
                    new Object[] {"SCHOOL", 10.0},
                    new Object[] {"FABRIC", 20.0},
                    new Object[] {"HOUSE", 4.0}
                });
    }

    @Test
    public void testGroupByWithMinAndTwoAttributes() throws Exception {
        testVisitor(
                "energy_consumption",
                "Min",
                "building_type",
                "energy_type",
                new Object[][] {
                    new Object[] {"FABRIC", "FLOWING_WATER", 500.0},
                    new Object[] {"FABRIC", "NUCLEAR", 150.0},
                    new Object[] {"FABRIC", "SOLAR", 30.0},
                    new Object[] {"FABRIC", "WIND", 20.0},
                    new Object[] {"HOUSE", "FUEL", 6.0},
                    new Object[] {"HOUSE", "NUCLEAR", 4.0},
                    new Object[] {"SCHOOL", "FLOWING_WATER", 50.0},
                    new Object[] {"SCHOOL", "FUEL", 60.0},
                    new Object[] {"SCHOOL", "NUCLEAR", 10.0},
                    new Object[] {"SCHOOL", "SOLAR", 30.0},
                    new Object[] {"SCHOOL", "WIND", 20.0}
                });
    }

    @Test
    public void testGroupByWithStdDevAndSingleAttribute() throws Exception {
        testVisitor(
                "energy_consumption",
                "StdDev",
                "building_type",
                new Object[][] {
                    new Object[] {"SCHOOL", 19.149},
                    new Object[] {"FABRIC", 194.487},
                    new Object[] {"HOUSE", 1.0}
                });
    }

    @Test
    public void testGroupByWithStdDevAndTwoAttributes() throws Exception {
        testVisitor(
                "energy_consumption",
                "StdDev",
                "building_type",
                "energy_type",
                new Object[][] {
                    new Object[] {"FABRIC", "FLOWING_WATER", 0.0},
                    new Object[] {"FABRIC", "NUCLEAR", 0.0},
                    new Object[] {"FABRIC", "SOLAR", 0.0},
                    new Object[] {"FABRIC", "WIND", 0.0},
                    new Object[] {"HOUSE", "FUEL", 0.0},
                    new Object[] {"HOUSE", "NUCLEAR", 0.0},
                    new Object[] {"SCHOOL", "FLOWING_WATER", 0.0},
                    new Object[] {"SCHOOL", "FUEL", 0.0},
                    new Object[] {"SCHOOL", "NUCLEAR", 0.0},
                    new Object[] {"SCHOOL", "SOLAR", 0.0},
                    new Object[] {"SCHOOL", "WIND", 0.0}
                });
    }

    @Test
    public void testGroupByWithSumDevAndSingleAttribute() throws Exception {
        testVisitor(
                "energy_consumption",
                "Sum",
                "building_type",
                new Object[][] {
                    new Object[] {"SCHOOL", 180.0},
                    new Object[] {"FABRIC", 700.0},
                    new Object[] {"HOUSE", 10.0}
                });
    }

    @Test
    public void testGroupByWithSumDevAndTwoAttributes() throws Exception {
        testVisitor(
                "energy_consumption",
                "Sum",
                "building_type",
                "energy_type",
                new Object[][] {
                    new Object[] {"FABRIC", "FLOWING_WATER", 500.0},
                    new Object[] {"FABRIC", "NUCLEAR", 150.0},
                    new Object[] {"FABRIC", "SOLAR", 30.0},
                    new Object[] {"FABRIC", "WIND", 20.0},
                    new Object[] {"HOUSE", "FUEL", 6.0},
                    new Object[] {"HOUSE", "NUCLEAR", 4.0},
                    new Object[] {"SCHOOL", "FLOWING_WATER", 50.0},
                    new Object[] {"SCHOOL", "FUEL", 60.0},
                    new Object[] {"SCHOOL", "NUCLEAR", 20.0},
                    new Object[] {"SCHOOL", "SOLAR", 30.0},
                    new Object[] {"SCHOOL", "WIND", 20.0}
                });
    }

    @Test
    public void testMergingGroupByResults() throws Exception {
        // creating the features collections that will be used to test the merge behavior
        FeatureCollection featureCollectionA = featureCollection;
        FeatureCollection featureCollectionB =
                DataUtilities.collection(
                        new SimpleFeature[] {
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        1,
                                        "SCHOOL_C",
                                        "SCHOOL",
                                        "NUCLEAR",
                                        100.0,
                                        wktParser.read("POINT(-15 -15)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        1,
                                        "SCHOOL_C",
                                        "SCHOOL",
                                        "FUEL",
                                        15.0,
                                        wktParser.read("POINT(-15 -15)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        2,
                                        "FABRIC_C",
                                        "FABRIC",
                                        "NUCLEAR",
                                        250.0,
                                        wktParser.read("POINT(-25 -25)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        2,
                                        "FABRIC_C",
                                        "FABRIC",
                                        "WIND",
                                        75.0,
                                        wktParser.read("POINT(-25 -25)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        2,
                                        "HOUSE_C",
                                        "HOUSE",
                                        "WIND",
                                        10.0,
                                        wktParser.read("POINT(-35 -35)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        2,
                                        "HOUSE_C",
                                        "HOUSE",
                                        "DARK_MATTER",
                                        850.0,
                                        wktParser.read("POINT(-35 -35)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        2,
                                        "THEATER_A",
                                        "THEATER",
                                        "WIND",
                                        200.0,
                                        wktParser.read("POINT(-45 -45)")
                                    },
                                    null)
                        });
        // we visit the first feature collection calculating the energy consumption average by
        // building type
        GroupByVisitor visitorA =
                executeVisitor(
                        featureCollectionA,
                        "energy_consumption",
                        "Average",
                        new String[] {"building_type"});
        checkResults(
                visitorA.getResult(),
                new Object[][] {
                    new Object[] {"SCHOOL", 30.0},
                    new Object[] {"FABRIC", 175.0},
                    new Object[] {"HOUSE", 5.0}
                });
        // we visit the second feature collection calculating the energy consumption average by
        // building type
        GroupByVisitor visitorB =
                executeVisitor(
                        featureCollectionB,
                        "energy_consumption",
                        "Average",
                        new String[] {"building_type"});
        checkResults(
                visitorB.getResult(),
                new Object[][] {
                    new Object[] {"SCHOOL", 57.5},
                    new Object[] {"FABRIC", 162.5},
                    new Object[] {"HOUSE", 430.0},
                    new Object[] {"THEATER", 200.0}
                });
        // we merge the result of the two previous visitors
        checkResults(
                visitorA.getResult().merge(visitorB.getResult()),
                new Object[][] {
                    new Object[] {"SCHOOL", 36.875},
                    new Object[] {"FABRIC", 170.833},
                    new Object[] {"HOUSE", 217.5},
                    new Object[] {"THEATER", 200.0}
                });
    }

    @Test
    public void testMergingGroupByResultsWithNull() throws Exception {
        // creating the features collections that will be used to test the merge behavior
        FeatureCollection featureCollectionA = featureCollection;
        FeatureCollection featureCollectionB =
                DataUtilities.collection(
                        new SimpleFeature[] {
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        1,
                                        "SCHOOL_C",
                                        "SCHOOL",
                                        "NUCLEAR",
                                        100.0,
                                        wktParser.read("POINT(-15 -15)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        1,
                                        "SCHOOL_C",
                                        "SCHOOL",
                                        "FUEL",
                                        15.0,
                                        wktParser.read("POINT(-15 -15)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        2,
                                        "FABRIC_C",
                                        "FABRIC",
                                        "NUCLEAR",
                                        250.0,
                                        wktParser.read("POINT(-25 -25)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        2,
                                        "FABRIC_C",
                                        "FABRIC",
                                        "WIND",
                                        75.0,
                                        wktParser.read("POINT(-25 -25)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        2,
                                        "HOUSE_C",
                                        "HOUSE",
                                        "WIND",
                                        10.0,
                                        wktParser.read("POINT(-35 -35)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        2,
                                        "HOUSE_C",
                                        "HOUSE",
                                        "DARK_MATTER",
                                        850.0,
                                        wktParser.read("POINT(-35 -35)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        2,
                                        "THEATER_A",
                                        "THEATER",
                                        "WIND",
                                        200.0,
                                        wktParser.read("POINT(-45 -45)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        13,
                                        "MALL_A",
                                        "MALL",
                                        "GRAVITY",
                                        null,
                                        wktParser.read("POINT(-45 -45)")
                                    },
                                    null),
                            SimpleFeatureBuilder.build(
                                    buildingType,
                                    new Object[] {
                                        13,
                                        "MALL_B",
                                        "MALL",
                                        "GRAVITY",
                                        null,
                                        wktParser.read("POINT(-45 -45)")
                                    },
                                    null)
                        });
        // we visit the first feature collection calculating the energy consumption average by
        // building type
        GroupByVisitor visitorA =
                executeVisitor(
                        featureCollectionA,
                        "energy_consumption",
                        "Average",
                        new String[] {"building_type"});
        checkResults(
                visitorA.getResult(),
                new Object[][] {
                    new Object[] {"SCHOOL", 30.0},
                    new Object[] {"FABRIC", 175.0},
                    new Object[] {"HOUSE", 5.0}
                });
        // we visit the second feature collection calculating the energy consumption average by
        // building type
        GroupByVisitor visitorB =
                executeVisitor(
                        featureCollectionB,
                        "energy_consumption",
                        "Average",
                        new String[] {"building_type"});
        checkResults(
                visitorB.getResult(),
                new Object[][] {
                    new Object[] {"SCHOOL", 57.5},
                    new Object[] {"FABRIC", 162.5},
                    new Object[] {"HOUSE", 430.0},
                    new Object[] {"THEATER", 200.0},
                    new Object[] {"MALL", null}
                });
        // we merge the result of the two previous visitors
        checkResults(
                visitorA.getResult().merge(visitorB.getResult()),
                new Object[][] {
                    new Object[] {"SCHOOL", 36.875},
                    new Object[] {"FABRIC", 170.833},
                    new Object[] {"HOUSE", 217.5},
                    new Object[] {"THEATER", 200.0},
                    new Object[] {"MALL", null}
                });
    }

    @Test
    public void testFeatureAttributeVisitor() {
        GroupByVisitor visitor =
                buildVisitor("energy_consumption", "Average", new String[] {"building_type"});
        List<Expression> expressions = visitor.getExpressions();
        Set<String> names = new HashSet<>();
        for (Expression expression : expressions) {
            PropertyName pn = (PropertyName) expression;
            names.add(pn.getPropertyName());
        }
        assertEquals(2, names.size());
        assertTrue(names.contains("energy_consumption"));
        assertTrue(names.contains("building_type"));
    }

    private void testVisitor(
            String aggregateAttribute,
            String aggregateVisitor,
            String groupByAttribute,
            Object[][] expectedResults)
            throws Exception {
        testVisitor(
                aggregateAttribute,
                aggregateVisitor,
                new String[] {groupByAttribute},
                expectedResults);
    }

    private void testVisitor(
            String aggregateAttribute,
            String aggregateVisitor,
            String firstGroupByAttribute,
            String secondGroupByAttribute,
            Object[][] expectedResults)
            throws Exception {
        testVisitor(
                aggregateAttribute,
                aggregateVisitor,
                new String[] {firstGroupByAttribute, secondGroupByAttribute},
                expectedResults);
    }

    private void testVisitor(
            String aggregateAttribute,
            String aggregateVisitor,
            String[] groupByAttributes,
            Object[][] expectedResults)
            throws Exception {
        GroupByVisitor visitor =
                executeVisitor(
                        featureCollection, aggregateAttribute, aggregateVisitor, groupByAttributes);
        checkResults(visitor.getResult(), expectedResults);
    }

    /**
     * Helper method that construct the group by visitor and visit the given feature collection. The
     * visitor result is also checked against the expected result.
     */
    private GroupByVisitor executeVisitor(
            FeatureCollection featureCollection,
            String aggregateAttribute,
            String aggregateVisitor,
            String[] groupByAttributes)
            throws Exception {
        GroupByVisitor visitor =
                buildVisitor(aggregateAttribute, aggregateVisitor, groupByAttributes);
        featureCollection.accepts(visitor, new NullProgressListener());
        return visitor;
    }

    private GroupByVisitor buildVisitor(
            String aggregateAttribute, String aggregateVisitor, String[] groupByAttributes) {
        GroupByVisitorBuilder visitorBuilder =
                new GroupByVisitorBuilder()
                        .withAggregateAttribute(aggregateAttribute, buildingType)
                        .withAggregateVisitor(aggregateVisitor);
        for (String groupByAttribute : groupByAttributes) {
            visitorBuilder.withGroupByAttribute(groupByAttribute, buildingType);
        }
        return visitorBuilder.build();
    }

    /** Helper method the checks if the calculation result contains the expected result. */
    private void checkResults(CalcResult calcResult, Object[][] expectedResults) {
        assertThat(calcResult, notNullValue());
        // check that the array conversion looks sane
        Object[] results = calcResult.toArray();
        assertThat(results.length, is(expectedResults.length));
        // check that map conversion looks sane
        Map resultMap = calcResult.toMap();
        assertThat(resultMap.entrySet().size(), is(expectedResults.length));
        // check that the obtained values correspond to the expected ones
        for (Object[] expectedResult : expectedResults) {
            assertThat(contains(results, expectedResult), is(true));
        }
    }

    /**
     * Helper method that checks if the number of results and the number of expected results are the
     * same and if the expected results are contained in the results.
     */
    private boolean contains(Object[] results, Object[] expectedResult) {
        for (Object result : results) {
            assertThat(result, instanceOf(Object[].class));
            if (checkArraysAreEqual((Object[]) result, expectedResult)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method that compare two arrays. This method will compare double values by difference
     * (0.001).
     */
    private boolean checkArraysAreEqual(Object[] arrayA, Object[] arrayB) {
        assertThat(arrayA, notNullValue());
        assertThat(arrayB, notNullValue());
        if (arrayA.length != arrayB.length) {
            return false;
        }
        for (int i = 0; i < arrayA.length; i++) {
            if (arrayA[i] == null && arrayB[i] == null) {
                continue;
            }
            assertThat(arrayA[i], notNullValue());
            assertThat(arrayB[i], notNullValue());
            if (!arrayA[i].getClass().equals(arrayB[i].getClass())) {
                return false;
            }
            if (arrayA[i] instanceof Double) {
                double difference = Math.abs((double) arrayA[i] - (double) arrayB[i]);
                if (difference > 0.001) {
                    return false;
                }
            } else if (!arrayA[i].equals(arrayB[i])) {
                return false;
            }
        }
        return true;
    }
}
