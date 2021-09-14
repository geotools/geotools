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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.FactoryException;

public class XmlStreamGeometryReaderTest {
    @Test
    public void testUnsafeXMLStreamReader() throws Exception {
        XMLInputFactory f = XMLInputFactory.newInstance();
        // DTD support allows XXE and XML bombs
        f.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.TRUE);
        XMLStreamReader r = f.createXMLStreamReader(new StringReader("<root></root>"));
        XmlStreamGeometryReader geometryReader =
                new XmlStreamGeometryReader(r, new GeometryFactory());
        r.nextTag();
        Exception exception =
                assertThrows(IllegalStateException.class, geometryReader::readGeometry);
        assertTrue(exception.getMessage().contains("XMLStreamReader allows DTDs"));
    }

    @Test
    public void testUnknownElement() throws XMLStreamException {
        XMLInputFactory f = XMLInputFactory.newInstance();
        f.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        XMLStreamReader r = f.createXMLStreamReader(new StringReader("<unknown></unknown>"));
        XmlStreamGeometryReader geometryReader =
                new XmlStreamGeometryReader(r, new GeometryFactory());
        r.nextTag();
        Exception exception =
                assertThrows(IllegalStateException.class, geometryReader::readGeometry);
        assertEquals(
                "Expected a geometry element in the GML namespace but found \"unknown\"",
                exception.getMessage());
    }

    @Test
    public void testZ() throws XMLStreamException, FactoryException, IOException {
        XMLInputFactory f = XMLInputFactory.newInstance();
        f.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        XMLStreamReader r =
                f.createXMLStreamReader(
                        new StringReader(
                                "<gml:Point xmlns:gml=\"http://www.opengis.net/gml\" srsDimension=\"3\"><gml:pos>1 2 3</gml:pos></gml:Point>"));
        XmlStreamGeometryReader geometryReader =
                new XmlStreamGeometryReader(r, new GeometryFactory());
        r.nextTag();
        Geometry g = geometryReader.readGeometry();
        assertNotNull(g);
        assertEquals(Point.class, g.getClass());
        Coordinate c = g.getCoordinate();
        assertEquals(c.x, 1, 0);
        assertEquals(c.y, 2, 0);
        assertEquals(c.z, 3, 0);
    }
}
