/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;

public class UniqueVisitorTest {

    private static WKTReader wktParser = new WKTReader();

    private static SimpleFeatureType uniqueValuesCountTestType;
    private static FeatureCollection featureCollection;
    static FilterFactory ff = CommonFactoryFinder.getFilterFactory2();

    @BeforeClass
    public static void setup() throws Exception {
        // the feature type that will be used during the tests
        uniqueValuesCountTestType =
                DataUtilities.createType(
                        "uniqueValuesCount",
                        "id:Integer,aStringValue:String,aDoubleValue:Double,aIntValue:Integer,geo:Geometry");
        // the features that will be used during the tests
        SimpleFeature[] simpleFeatures = {
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {1, "A", 50.0, 3, wktParser.read("POINT(-5 -5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {2, "B", 10.0, 4, wktParser.read("POINT(-5 -5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {3, "C", 20.0, 1, wktParser.read("POINT(-5 -5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {4, "D", 30.0, 5, wktParser.read("POINT(5 5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {5, "E", 60.0, 6, wktParser.read("POINT(5 5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {6, "E", 10.0, 7, wktParser.read("POINT(5 5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {7, "C", 20.0, 10, wktParser.read("POINT(5 -5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {8, "C", 30.0, 11, wktParser.read("POINT(5 -5)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {9, "A", 500.0, 12, wktParser.read("POINT(0 0)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {10, "A", 500.0, 12, wktParser.read("POINT(0 0)")},
                    null),
            SimpleFeatureBuilder.build(
                    uniqueValuesCountTestType,
                    new Object[] {11, "A", 500.0, 12, wktParser.read("POINT(0 0)")},
                    null),
        };
        // creating the feature collection
        featureCollection = DataUtilities.collection(simpleFeatures);
    }

    @Test
    public void testUniqueVisitor() throws IOException {
        UniqueVisitor uniqueVisitor = new UniqueVisitor("aStringValue");
        featureCollection.accepts(uniqueVisitor, null);
        Set result = uniqueVisitor.getResult().toSet();
        List<String> expected = Arrays.asList("A", "B", "C", "D", "E");
        assertEquals(expected.size(), result.size());
        assertTrue(expected.containsAll(result));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUniqueVisitor2() throws IOException {
        UniqueVisitor uniqueVisitor = new UniqueVisitor("aDoubleValue");
        featureCollection.accepts(uniqueVisitor, null);
        Set result = uniqueVisitor.getResult().toSet();
        List expected = Arrays.asList(10d, 20d, 30d, 50d, 60d, 500d);
        assertEquals(expected.size(), result.size());
        assertTrue(result.containsAll(expected));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMultipleAttributes() throws IOException {
        UniqueVisitor uniqueVisitor = new UniqueVisitor("aStringValue", "aDoubleValue");
        featureCollection.accepts(uniqueVisitor, null);
        Set result = uniqueVisitor.getResult().toSet();

        Set expected = new HashSet(8);
        addValues(expected, "A", 500d);
        addValues(expected, "C", 30d);
        addValues(expected, "C", 20d);
        addValues(expected, "E", 10d);
        addValues(expected, "E", 60d);
        addValues(expected, "D", 30d);
        addValues(expected, "B", 10d);
        addValues(expected, "A", 50d);

        assertEquals(expected.size(), result.size());
        assertTrue(expected.containsAll(result));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMultipleAttributes2() throws IOException {
        UniqueVisitor uniqueVisitor =
                new UniqueVisitor("aStringValue", "aDoubleValue", "aIntValue");
        featureCollection.accepts(uniqueVisitor, null);
        Set result = uniqueVisitor.getResult().toSet();

        Set expected = new HashSet(9);
        addValues(expected, "C", 20d, 10);
        addValues(expected, "A", 500d, 12);
        addValues(expected, "E", 10d, 7);
        addValues(expected, "B", 10d, 4);
        addValues(expected, "E", 60d, 6);
        addValues(expected, "D", 30d, 5);
        addValues(expected, "C", 30d, 11);
        addValues(expected, "A", 50d, 3);
        addValues(expected, "C", 20d, 1);

        assertEquals(expected.size(), result.size());
        assertTrue(expected.containsAll(result));
    }

    @SuppressWarnings("unchecked")
    private void addValues(Set set, Object... values) {
        LinkedList list =
                new LinkedList() {
                    {
                        for (Object val : values) add(val);
                    }
                };
        set.add(list);
    }
}
