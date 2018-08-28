/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2017, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.ProcessException;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

public class ClassifyByRangeProcessTest {

    WKTReader reader = new WKTReader();

    private ListFeatureCollection fc;

    @Before
    public void setup() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("geom", Polygon.class, "EPSG:4326");
        tb.add("name", String.class);
        tb.add("value", Integer.class);
        tb.setName("ft");
        SimpleFeatureType ft = tb.buildFeatureType();

        fc = new ListFeatureCollection(ft);

        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
        fb.add(reader.read("POINT(0 0)").buffer(10));
        fb.add("one");
        fb.add(1);
        fc.add(fb.buildFeature(null));
        fb.add(reader.read("POINT(10 0)").buffer(10));
        fb.add("two");
        fb.add(5);
        fc.add(fb.buildFeature(null));
        fb.add(reader.read("POINT(10 0)").buffer(10));
        fb.add("three");
        fb.add(10);
        fc.add(fb.buildFeature(null));
    }

    @Test
    public void testClassifyAddsAttribute() {
        ClassifyByRangeProcess cp = new ClassifyByRangeProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        fc,
                        "value",
                        new String[] {"2", "5", "10"},
                        new String[] {"A", "B", "C", "D"},
                        null,
                        null,
                        false,
                        "myclass",
                        null);
        SimpleFeatureType ft = result.getSchema();
        assertEquals(4, ft.getAttributeCount());
        AttributeDescriptor classAttribute = ft.getDescriptor("myclass");
        assertNotNull(classAttribute);
        assertEquals(String.class, classAttribute.getType().getBinding());
    }

    @Test
    public void testClassifyValues() {
        ClassifyByRangeProcess cp = new ClassifyByRangeProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        fc,
                        "value",
                        new String[] {"2", "5", "10"},
                        new String[] {"A", "B", "C", "D"},
                        null,
                        null,
                        false,
                        "class",
                        null);
        assertEquals(3, result.size());
        SimpleFeatureIterator iterator = result.features();
        SimpleFeature ft = iterator.next();
        assertEquals("A", ft.getAttribute("class"));
        ft = iterator.next();
        assertEquals("C", ft.getAttribute("class"));
    }

    @Test
    public void testClassifyValuesInclude() {
        ClassifyByRangeProcess cp = new ClassifyByRangeProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        fc,
                        "value",
                        new String[] {"2", "5", "10"},
                        new String[] {"A", "B", "C", "D"},
                        null,
                        null,
                        true,
                        "class",
                        null);
        assertEquals(3, result.size());
        SimpleFeatureIterator iterator = result.features();
        SimpleFeature ft = iterator.next();
        assertEquals("A", ft.getAttribute("class"));
        ft = iterator.next();
        assertEquals("B", ft.getAttribute("class"));
    }

    @Test
    public void testClassifyWrongAttribute() {
        ClassifyByRangeProcess cp = new ClassifyByRangeProcess();
        boolean error = false;
        try {
            cp.execute(
                    fc,
                    "notexisting",
                    new String[] {"2", "5", "10"},
                    new String[] {"A", "B", "C", "D"},
                    null,
                    null,
                    true,
                    "class",
                    null);
        } catch (ProcessException e) {
            error = true;
        }
        assertTrue(error);
    }

    @Test
    public void testClassifyNoThresholdsAndClassifier() {
        ClassifyByRangeProcess cp = new ClassifyByRangeProcess();
        boolean error = false;
        try {
            cp.execute(
                    fc,
                    "value",
                    null,
                    new String[] {"A", "B", "C", "D"},
                    null,
                    null,
                    true,
                    "class",
                    null);
        } catch (ProcessException e) {
            error = true;
        }
        assertTrue(error);
    }

    @Test
    public void testClassifyDefaultOutput() {
        ClassifyByRangeProcess cp = new ClassifyByRangeProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        fc,
                        "value",
                        new String[] {"2", "5", "10"},
                        new String[] {"A", "B", "C", "D"},
                        null,
                        null,
                        false,
                        null,
                        null);
        SimpleFeatureType ft = result.getSchema();
        assertEquals(4, ft.getAttributeCount());
        AttributeDescriptor classAttribute = ft.getDescriptor("class");
        assertNotNull(classAttribute);
        assertEquals(String.class, classAttribute.getType().getBinding());
    }

    @Test
    public void testClassifyDefaultOutputValues() {
        ClassifyByRangeProcess cp = new ClassifyByRangeProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        fc,
                        "value",
                        new String[] {"2", "5", "10"},
                        null,
                        null,
                        null,
                        false,
                        "class",
                        null);
        assertEquals(3, result.size());
        SimpleFeatureIterator iterator = result.features();
        SimpleFeature ft = iterator.next();
        assertEquals("1", ft.getAttribute("class"));
        ft = iterator.next();
        assertEquals("3", ft.getAttribute("class"));
    }

    @Test
    public void testClassifyWrongValues() {
        ClassifyByRangeProcess cp = new ClassifyByRangeProcess();
        boolean error = false;
        try {
            cp.execute(
                    fc,
                    "value",
                    new String[] {"2", "5", "10"},
                    new String[] {"A", "B", "C", "D", "E"},
                    null,
                    null,
                    true,
                    "class",
                    null);
        } catch (ProcessException e) {
            error = true;
        }
        assertTrue(error);
    }

    @Test
    public void testClassifyWithClassifierEqualInterval() {
        ClassifyByRangeProcess cp = new ClassifyByRangeProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        fc,
                        "value",
                        null,
                        new String[] {"A", "B", "C", "D", "E"},
                        "EqualInterval",
                        3,
                        true,
                        "class",
                        null);
        assertEquals(3, result.size());
        SimpleFeatureIterator iterator = result.features();
        SimpleFeature ft = iterator.next();
        assertEquals("A", ft.getAttribute("class"));
        ft = iterator.next();
        assertEquals("C", ft.getAttribute("class"));
        ft = iterator.next();
        assertEquals("D", ft.getAttribute("class"));
    }

    @Test
    public void testClassifyWithClassifierEqualIntervalMoreClasses() {
        ClassifyByRangeProcess cp = new ClassifyByRangeProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        fc,
                        "value",
                        null,
                        new String[] {"A", "B", "C", "D", "E", "F", "G"},
                        "EqualInterval",
                        5,
                        true,
                        "class",
                        null);
        assertEquals(3, result.size());
        SimpleFeatureIterator iterator = result.features();
        SimpleFeature ft = iterator.next();
        assertEquals("A", ft.getAttribute("class"));
        ft = iterator.next();
        assertEquals("D", ft.getAttribute("class"));
        ft = iterator.next();
        assertEquals("F", ft.getAttribute("class"));
    }

    @Test
    public void testClassifyWithQuantileInterval() {
        ClassifyByRangeProcess cp = new ClassifyByRangeProcess();
        SimpleFeatureCollection result =
                cp.execute(
                        fc,
                        "value",
                        null,
                        new String[] {"A", "B", "C", "D", "E"},
                        "Quantile",
                        3,
                        true,
                        "class",
                        null);
        assertEquals(3, result.size());
        SimpleFeatureIterator iterator = result.features();
        SimpleFeature ft = iterator.next();
        assertEquals("A", ft.getAttribute("class"));
        ft = iterator.next();
        assertEquals("B", ft.getAttribute("class"));
        ft = iterator.next();
        assertEquals("C", ft.getAttribute("class"));
    }

    @Test
    public void testClassifyWithInvalidClassifier() {
        ClassifyByRangeProcess cp = new ClassifyByRangeProcess();
        boolean error = false;
        try {
            SimpleFeatureCollection result =
                    cp.execute(
                            fc,
                            "value",
                            null,
                            new String[] {"A", "B", "C", "D", "E"},
                            "NotExisting",
                            3,
                            true,
                            "class",
                            null);
        } catch (Exception e) {
            error = true;
        }
        assertTrue(error);
    }
}
