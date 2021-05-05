/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml.stream;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.StringReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import org.geotools.gml3.GML;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;

public class XmlStreamGeometryReaderTest {
    @FunctionalInterface
    public interface TestFunction<T> {
        void apply(T t) throws Exception;
    }

    @Test
    public void testUnsafeXMLStreamReader() throws Exception {
        XMLInputFactory f = XMLInputFactory.newInstance();
        // DTD support allows XXE and XML bombs
        f.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.TRUE);
        XMLStreamReader r = f.createXMLStreamReader(new StringReader("<root></root>"));
        XmlStreamGeometryReader geometryReader =
                new XmlStreamGeometryReader(new GeometryFactory(), r, null);
        r.nextTag();
        Exception exception =
                assertThrows(IllegalStateException.class, geometryReader::readGeometry);
        assertTrue(exception.getMessage().contains("XMLStreamReader allows DTDs"));
    }

    @Test
    public void testPoint() throws Exception {
        testWithXmlStreamGeometryReader(
                "org/geotools/gml/stream/point.gml",
                "Point",
                (XmlStreamGeometryReader geometryReader) -> {
                    Geometry g = geometryReader.readGeometry();
                    assertNotNull(g);
                    assertTrue(g instanceof Point);
                    assertEquals(g.toString(), "POINT (25888.999 387917.524)");
                });
    }

    @Test
    public void testMultiSurface() throws Exception {
        testWithXmlStreamGeometryReader(
                "org/geotools/gml/stream/multisurface.gml",
                "MultiSurface",
                (XmlStreamGeometryReader geometryReader) -> {
                    Geometry g = geometryReader.readGeometry();
                    assertNotNull(g);
                    assertTrue(g instanceof MultiPolygon);
                    assertEquals(
                            g.toString(),
                            "MULTIPOLYGON (((10.5 3.34, 100 123.456, 150 130, 10.5 3.34)))");
                });
    }

    private void testWithXmlStreamGeometryReader(
            String resource, String element, TestFunction<XmlStreamGeometryReader> tester)
            throws Exception {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(resource)) {
            XMLInputFactory f = XMLInputFactory.newInstance();
            f.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
            XMLStreamReader r = f.createXMLStreamReader(input);
            XmlStreamGeometryReader geometryReader =
                    new XmlStreamGeometryReader(new GeometryFactory(), r, null);
            r.nextTag();
            r.require(XMLStreamConstants.START_ELEMENT, GML.NAMESPACE, element);
            tester.apply(geometryReader);
        }
    }
}
