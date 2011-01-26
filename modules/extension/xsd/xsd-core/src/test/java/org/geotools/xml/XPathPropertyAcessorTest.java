/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import junit.framework.TestCase;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.expression.PropertyAccessor;


public class XPathPropertyAcessorTest extends TestCase {
    SimpleFeatureType type;
    SimpleFeature target;

    protected void setUp() throws Exception {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("test");
        typeBuilder.setNamespaceURI("http://www.geotools.org/test");
        typeBuilder.add("name", String.class);
        typeBuilder.add("description", String.class);
        typeBuilder.add("geometry", Geometry.class);
        //NC- added - if you want null, property should still exist, otherwise exception
        typeBuilder.add("foo", Object.class);
        
        type = (SimpleFeatureType) typeBuilder.buildFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add("theName");
        builder.add("theDescription");
        builder.add(new GeometryFactory().createPoint(new Coordinate(0, 0)));
        //NC- foo remains null
        
        target = (SimpleFeature) builder.buildFeature("fid");
    }

    public void testSimpleXpath() {
        PropertyAccessor accessor = accessor("name");
        Object o = accessor.get(target, "name", null);
        assertNotNull(o);
        assertEquals("theName", o);

        accessor = accessor("description");
        o = accessor.get(target, "description", null);
        assertNotNull(o);
        assertEquals("theDescription", o);

        accessor = accessor("geometry");
        o = accessor.get(target, "geometry", null);
        assertNotNull(o);
        assertTrue(o instanceof Point);

        accessor = accessor("foo");
        o = accessor.get(target, "foo", null);
        assertNull(o);
    }

    public void testSimpleXpathType() {
        PropertyAccessor accessor = accessor("name");
        Object o = accessor.get(type, "name", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("name"), o);

        accessor = accessor("description");
        o = accessor.get(type, "description", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("description"), o);

        accessor = accessor("geometry");
        o = accessor.get(type, "geometry", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("geometry"), o);

        accessor = accessor("foo");
        o = accessor.get(type, "foo", null);
        //NC - changed, it does exist now
        //assertNull(o);
        assertEquals(type.getDescriptor("foo"), o);
    }

    public void testSimpleXpathWithNamespace() {
        PropertyAccessor accessor = accessor("gml:name");
        Object o = accessor.get(target, "gml:name", null);
        assertNotNull(o);
        assertEquals("theName", o);

        accessor = accessor("gml:description");
        o = accessor.get(target, "gml:description", null);
        assertNotNull(o);
        assertEquals("theDescription", o);

        accessor = accessor("test:geometry");
        o = accessor.get(target, "test:geometry", null);
        assertNotNull(o);
        assertTrue(o instanceof Point);
    }

    public void testSimpleXpathWithNamespaceType() {
        PropertyAccessor accessor = accessor("gml:name");
        Object o = accessor.get(type, "gml:name", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("name"), o);

        accessor = accessor("gml:description");
        o = accessor.get(type, "gml:description", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("description"), o);

        accessor = accessor("test:geometry");
        o = accessor.get(type, "test:geometry", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("geometry"), o);
    }

    public void testPath() {
        PropertyAccessor accessor = accessor("//name");
        Object o = accessor.get(target, "//name", null);
        assertNotNull(o);
        assertEquals("theName", o);

        accessor = accessor("//description");
        o = accessor.get(target, "//description", null);
        assertNotNull(o);
        assertEquals("theDescription", o);

        accessor = accessor("//geometry");
        o = accessor.get(target, "//geometry", null);
        assertNotNull(o);
        assertTrue(o instanceof Point);
    }

    public void testPathType() {
        PropertyAccessor accessor = accessor("//name");
        Object o = accessor.get(type, "//name", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("name"), o);

        accessor = accessor("//description");
        o = accessor.get(type, "//description", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("description"), o);

        accessor = accessor("//geometry");
        o = accessor.get(type, "//geometry", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("geometry"), o);
    }

    public void testPathWithNamespace() {
        PropertyAccessor accessor = accessor("//gml:name");
        Object o = accessor.get(target, "//gml:name", null);
        assertNotNull(o);
        assertEquals("theName", o);

        accessor = accessor("//gml:description");
        o = accessor.get(target, "//gml:description", null);
        assertNotNull(o);
        assertEquals("theDescription", o);

        accessor = accessor("//test:geometry");
        o = accessor.get(target, "//test:geometry", null);
        assertNotNull(o);
        assertTrue(o instanceof Point);
    }

    public void testPathWithNamespaceType() {
        PropertyAccessor accessor = accessor("//gml:name");
        Object o = accessor.get(type, "//gml:name", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("name"), o);

        accessor = accessor("//gml:description");
        o = accessor.get(type, "//gml:description", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("description"), o);

        accessor = accessor("//test:geometry");
        o = accessor.get(type, "//test:geometry", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("geometry"), o);
    }

    public void testIndex() {
        PropertyAccessor accessor = accessor("*[1]");
        Object o = accessor.get(target, "*[1]", null);
        assertNotNull(o);
        assertEquals("theName", o);

        accessor = accessor("*[2]");
        o = accessor.get(target, "*[2]", null);
        assertNotNull(o);
        assertEquals("theDescription", o);

        accessor = accessor("*[3]");
        o = accessor.get(target, "*[3]", null);
        assertNotNull(o);
        assertTrue(o instanceof Point);
    }

    public void testIndexType() {
        PropertyAccessor accessor = accessor("*[1]");
        Object o = accessor.get(type, "*[1]", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("name"), o);

        accessor = accessor("*[2]");
        o = accessor.get(type, "*[2]", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("description"), o);

        accessor = accessor("*[3]");
        o = accessor.get(type, "*[3]", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("geometry"), o);
    }

    public void testPosition() {
        PropertyAccessor accessor = accessor("*[position()=1]");
        Object o = accessor.get(target, "*[position()=1]", null);
        assertEquals("theName", o);

        accessor = accessor("*[position()=2]");
        o = accessor.get(target, "*[position()=2]", null);
        assertNotNull(o);
        assertEquals("theDescription", o);

        accessor = accessor("*[position()=3]");
        o = accessor.get(target, "*[position()=3]", null);
        assertNotNull(o);
        assertTrue(o instanceof Point);
    }

    public void testPositionType() {
        PropertyAccessor accessor = accessor("*[position()=1]");
        Object o = accessor.get(type, "*[position()=1]", null);
        assertEquals(type.getDescriptor("name"), o);

        accessor = accessor("*[position()=2]");
        o = accessor.get(type, "*[position()=2]", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("description"), o);

        accessor = accessor("*[position()=3]");
        o = accessor.get(type, "*[position()=3]", null);
        assertNotNull(o);
        assertEquals(type.getDescriptor("geometry"), o);
    }

    public void testId() {
        PropertyAccessor accessor = accessor("./@gml:id");
        Object o = accessor.get(target, "./@gml:id", null);
        assertEquals("fid", o);
    }

    public void testEmptyXpath() {
        assertFalse(accessor("").canHandle(target, "", null));
    }

    PropertyAccessor accessor(String xpath) {
        return new XPathPropertyAccessorFactory().createPropertyAccessor(SimpleFeature.class,
            xpath, null, null);
    }
}
