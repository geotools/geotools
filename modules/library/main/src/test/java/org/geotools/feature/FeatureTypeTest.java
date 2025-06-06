/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Created on July 21, 2003, 4:00 PM
 */
package org.geotools.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.data.DataTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.BasicFeatureTypes;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Test SimpleFeatureTypeBuilder and friends.
 *
 * @author en
 * @author jgarnett
 */
public class FeatureTypeTest extends DataTestCase {
    @Test
    public void testAbstractType() throws Exception {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("AbstractThing");
        tb.setAbstract(true);
        tb.setNamespaceURI(new URI("http://www.nowhereinparticular.net"));

        SimpleFeatureType abstractType = tb.buildFeatureType();
        tb.setName("AbstractType2");
        tb.setSuperType(abstractType);
        tb.add("X", String.class);
        SimpleFeatureType abstractType2 = tb.buildFeatureType();

        assertTrue(abstractType.isAbstract());
        assertTrue(abstractType2.isAbstract());

        // assertTrue("extends gml feature", FeatureTypes.isDecendedFrom(abstractType, new
        // URI("http://www.opengis.net/gml"),"Feature"));
        // assertTrue("extends gml feature", FeatureTypes.isDecendedFrom(abstractType2, new
        // URI("http://www.opengis.net/gml"),"Feature"));
        assertTrue("abstractType2 --|> abstractType", FeatureTypes.isDecendedFrom(abstractType2, abstractType));
        assertFalse("abstractType2 !--|> abstractType", FeatureTypes.isDecendedFrom(abstractType, abstractType2));

        try {
            SimpleFeatureBuilder.build(abstractType, new Object[0], null);
            fail("abstract type allowed create");
        } catch (IllegalArgumentException | UnsupportedOperationException iae) {
        }

        try {
            SimpleFeatureBuilder.build(abstractType2, new Object[0], null);
            fail("abstract type allowed create");
        } catch (IllegalArgumentException | UnsupportedOperationException iae) {
        }
    }

    @Test
    public void testEquals() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("Thing");
        tb.setNamespaceURI("http://www.nowhereinparticular.net");
        tb.add("X", String.class);
        final SimpleFeatureType ft = tb.buildFeatureType();

        tb = new SimpleFeatureTypeBuilder();
        tb.setName("Thing");
        tb.setNamespaceURI("http://www.nowhereinparticular.net");
        tb.add("X", String.class);

        SimpleFeatureType ft2 = tb.buildFeatureType();
        assertEquals(ft, ft2);

        tb.setName("Thingee");
        assertNotEquals(ft, tb.buildFeatureType());

        tb.init(ft);
        tb.setNamespaceURI("http://www.somewhereelse.net");

        assertNotEquals(ft, tb.buildFeatureType());
        assertNotEquals(ft, null);
    }

    @Test
    public void testCopyFeature() throws Exception {
        SimpleFeature feature = lakeFeatures[0];
        assertDuplicate("feature", feature, SimpleFeatureBuilder.copy(feature));
    }

    /**
     * Test FeatureTypes.getAncestors() by constructing three levels of derived types and testing that the expected
     * ancestors are returned at each level in reverse order.
     *
     * <p>UML type hierarchy of test types: Feature <|-- A <|-- B <|-- C
     */
    @Test
    public void testAncestors() throws Exception {
        URI uri = new URI("http://www.geotools.org/example");

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("A");
        tb.setNamespaceURI(uri);
        final SimpleFeatureType typeA = tb.buildFeatureType();

        tb = new SimpleFeatureTypeBuilder();
        tb.setName("B");
        tb.setNamespaceURI(uri);
        tb.setSuperType(typeA);
        tb.add("b", String.class);
        final SimpleFeatureType typeB = tb.buildFeatureType();

        tb = new SimpleFeatureTypeBuilder();
        tb.setName("C");
        tb.setNamespaceURI(uri);
        tb.setSuperType(typeB);
        tb.add("c", Integer.class);
        final SimpleFeatureType typeC = tb.buildFeatureType();

        // base type should have no ancestors
        assertEquals(
                "Ancestors of Feature, nearest first",
                Collections.<FeatureType>emptyList(),
                FeatureTypes.getAncestors(BasicFeatureTypes.FEATURE));

        assertEquals(
                "Ancestors of A, nearest first", List.of(BasicFeatureTypes.FEATURE), FeatureTypes.getAncestors(typeA));

        assertEquals(
                "Ancestors of B, nearest first",
                List.of(typeA, BasicFeatureTypes.FEATURE),
                FeatureTypes.getAncestors(typeB));

        assertEquals(
                "Ancestors of C, nearest first",
                List.of(typeB, typeA, BasicFeatureTypes.FEATURE),
                FeatureTypes.getAncestors(typeC));
    }

    @Test
    public void testDeepCopy() throws Exception {
        // primative
        String str = "FooBar";
        Integer i = Integer.valueOf(3);
        Float f = Float.valueOf(3.14f);
        Double d = Double.valueOf(3.14159);

        assertSame("String", str, DataUtilities.duplicate(str));
        assertSame("Integer", i, DataUtilities.duplicate(i));
        assertSame("Float", f, DataUtilities.duplicate(f));
        assertSame("Double", d, DataUtilities.duplicate(d));

        // collections
        Object[] objs = {
            str, i, f, d,
        };
        int[] ints = {
            1, 2, 3, 4,
        };
        List<Object> list = new ArrayList<>();
        list.add(str);
        list.add(i);
        list.add(f);
        list.add(d);
        Map<String, Object> map = new HashMap<>();
        map.put("a", str);
        map.put("b", i);
        map.put("c", f);
        map.put("d", d);
        assertDuplicate("objs", objs, DataUtilities.duplicate(objs));
        assertDuplicate("ints", ints, DataUtilities.duplicate(ints));
        assertDuplicate("list", list, DataUtilities.duplicate(list));
        assertDuplicate("map", map, DataUtilities.duplicate(map));

        // complex type
        SimpleFeature feature = lakeFeatures[0];

        Coordinate coords = new Coordinate(1, 3);
        Coordinate coords2 = new Coordinate(1, 3);
        GeometryFactory gf = new GeometryFactory();
        Geometry point = gf.createPoint(coords);
        Geometry point2 = gf.createPoint(coords2);

        assertDuplicate("jts duplicate", point, point2);
        assertDuplicate("feature", feature, DataUtilities.duplicate(feature));
        assertDuplicate("point", point, DataUtilities.duplicate(point));
    }

    static Set<Class<?>> immutable;

    static {
        immutable = new HashSet<>();
        immutable.add(String.class);
        immutable.add(Integer.class);
        immutable.add(Double.class);
        immutable.add(Float.class);
    }

    protected void assertDuplicate(String message, Object expected, Object value) {
        // Ensure value is equal to expected
        if (expected.getClass().isArray()) {
            int length1 = Array.getLength(expected);
            int length2 = Array.getLength(value);
            assertEquals(message, length1, length2);
            for (int i = 0; i < length1; i++) {
                assertDuplicate(message + "[" + i + "]", Array.get(expected, i), Array.get(value, i));
            }
            // assertNotSame( message, expected, value );
        } else if (expected instanceof SimpleFeature) {
            assertDuplicate(
                    message, ((SimpleFeature) expected).getAttributes(), ((SimpleFeature) value).getAttributes());
        } else {
            assertEquals(message, expected, value);
        }
    }
}
