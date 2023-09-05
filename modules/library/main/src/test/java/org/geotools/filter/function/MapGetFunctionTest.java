/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import static java.util.Map.entry;

import java.util.Collections;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.expression.Function;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

public class MapGetFunctionTest {

    static FilterFactory FF = CommonFactoryFinder.getFilterFactory();
    private SimpleFeatureType sampleDataType;

    private String namespace = "ns";
    private SimpleFeature[] features;
    private GeometryFactory gf;

    @Before
    public void setUp() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("map");
        builder.setNamespaceURI(namespace);

        builder.add(createAttribute("id", String.class));
        builder.add(createAttribute("geom", LineString.class));
        builder.setDefaultGeometry("geom");
        builder.add(createAttribute("name", String.class));
        builder.add(createAttribute("attributes", Map.class));
        sampleDataType = builder.buildFeatureType();

        gf = new GeometryFactory();

        features = new SimpleFeature[3];
        features[0] =
                SimpleFeatureBuilder.build(
                        sampleDataType,
                        new Object[] {
                            Integer.valueOf(1),
                            line(new int[] {1, 1, 2, 2, 4, 2, 5, 1}),
                            "object1",
                            Map.ofEntries(
                                    entry("name", "firstObject"),
                                    entry("valid", false),
                                    entry("score", 100),
                                    entry("key1", "value1"))
                        },
                        "sample.s1");

        features[1] =
                SimpleFeatureBuilder.build(
                        sampleDataType,
                        new Object[] {
                            Integer.valueOf(2),
                            line(new int[] {3, 0, 3, 2, 3, 3, 3, 4}),
                            "object2",
                            Map.ofEntries(
                                    entry("name", "secondObject"),
                                    entry("valid", true),
                                    entry("score", 50),
                                    entry("key1", 10))
                        },
                        "sample.s2");

        features[2] =
                SimpleFeatureBuilder.build(
                        sampleDataType,
                        new Object[] {
                            Integer.valueOf(2),
                            line(new int[] {3, 2, 4, 2, 5, 3}),
                            "object3",
                            Map.ofEntries(
                                    entry("name", "thirdObject"),
                                    entry("score", 70),
                                    entry("key1", true))
                        },
                        "sample.s3");
    }

    private AttributeDescriptor createAttribute(String name, Class clazz) {
        AttributeType at =
                new AttributeTypeImpl(
                        new NameImpl(name),
                        clazz,
                        false,
                        false,
                        Collections.emptyList(),
                        null,
                        null);
        return new AttributeDescriptorImpl(at, new NameImpl(name), 0, 1, false, null);
    }

    /**
     * Creates a line from the specified (<var>x</var>,<var>y</var>) coordinates. The coordinates
     * are stored in a flat array.
     */
    public LineString line(int[] xy) {
        Coordinate[] coords = new Coordinate[xy.length / 2];

        for (int i = 0; i < xy.length; i += 2) {
            coords[i / 2] = new Coordinate(xy[i], xy[i + 1]);
        }

        return gf.createLineString(coords);
    }

    @Test
    public void testEvaluate() {
        // evaluating on attributes.name
        Function f = FF.function("mapGet", FF.property("attributes"), FF.literal("name"));
        Assert.assertEquals("firstObject", f.evaluate(features[0], String.class));
        Assert.assertEquals("secondObject", f.evaluate(features[1], String.class));
        Assert.assertEquals("thirdObject", f.evaluate(features[2], String.class));

        PropertyIsGreaterThan gt =
                FF.greater(
                        FF.function("mapGet", FF.property("attributes"), FF.literal("score")),
                        FF.literal(80));
        Assert.assertTrue(gt.evaluate(features[0]));
        Assert.assertFalse(gt.evaluate(features[1]));
        Assert.assertFalse(gt.evaluate(features[2]));

        Function f2 = FF.function("mapGet", FF.property("attributes"), FF.literal("valid"));
        Assert.assertFalse(f2.evaluate(features[0], Boolean.class));
        Assert.assertTrue(f2.evaluate(features[1], Boolean.class));
        Assert.assertNull(f2.evaluate(features[2], Boolean.class));

        Function f3 = FF.function("mapGet", FF.property("attributes"), FF.literal("key1"));
        Assert.assertEquals("value1", f3.evaluate(features[0], String.class));
        Assert.assertEquals(Integer.valueOf(10), f3.evaluate(features[1], Integer.class));
        Assert.assertEquals(Boolean.TRUE, f3.evaluate(features[2], Boolean.class));

        Function f4 = FF.function("mapGet", FF.property("missingMap"), FF.literal("key1"));
        Assert.assertNull(f4.evaluate(features[0], String.class));
    }
}
