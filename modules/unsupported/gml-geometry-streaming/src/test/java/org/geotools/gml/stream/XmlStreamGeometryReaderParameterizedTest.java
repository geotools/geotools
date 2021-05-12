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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.FactoryException;

@RunWith(Parameterized.class)
public class XmlStreamGeometryReaderParameterizedTest {
    @Parameterized.Parameters(name = "GML file: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][] {
                    {"point.gml", Point.class, "POINT (25888.999 387917.524)"},
                    {"point2.gml", Point.class, "POINT (1 0)"},
                    {
                        "linestring.gml",
                        LineString.class,
                        "LINESTRING (-1.28 -0.11, -0.63 0.38, -0.22 0.09, -0.45 -0.36)"
                    },
                    {"polygon.gml", Polygon.class, "POLYGON ((1 0, 0 0, 0 1, 1 1, 1 0))"},
                    {
                        "polygon2.gml",
                        Polygon.class,
                        "POLYGON ((15 7, 16 -7, -4 -7, 1 5, -2 6, 6 10, 15 7), (3 -4, 11 -4, 13 4, 5 4, 3 -4))"
                    },
                    {
                        "polygon3.gml",
                        Polygon.class,
                        "POLYGON ((15 7, 16 -7, -4 -7, 1 5, -2 6, 6 10, 15 7), (3 -4, 11 -4, 13 4, 5 4, 3 -4), (3 -6, 2 -2, 5 6, 14 5, 12 -6, 3 -6), (2 -5, 1 0, 4 6, 6 8, 5 9, 1 7, 2 5, 0 -2, 2 -5))"
                    },
                    {
                        "multipoint.gml",
                        MultiPoint.class,
                        "MULTIPOINT ((-23 11), (-24 9), (-22 9), (-24 8))"
                    },
                    {"multipoint2.gml", MultiPoint.class, "MULTIPOINT ((-23 11), (-24 9))"},
                    {
                        "multilinestring.gml",
                        MultiLineString.class,
                        "MULTILINESTRING ((-27 -5, -11 -5), (-20 1, -19 -12))"
                    },
                    {
                        "multipolygon.gml",
                        MultiPolygon.class,
                        "MULTIPOLYGON (((-11 -4, -8 9, 14 5, 5 -11, -11 -4), (-4 2, -6 -3, 1 -5, -4 2)), ((-19 5, -18 -10, -27 -10, -19 5)))"
                    },
                    {
                        "multicurvez.gml",
                        org.geotools.geometry.jts.MultiCurve.class,
                        "MULTILINESTRING ((1 2, 3 4))"
                    },
                    {
                        "multisurface.gml",
                        MultiPolygon.class,
                        "MULTIPOLYGON (((10.5 3.34, 100 123.456, 150 130, 10.5 3.34)))"
                    },
                });
    }

    private String gmlResource;

    private Class expectedGeometryClass;

    private String expectedWKT;

    public XmlStreamGeometryReaderParameterizedTest(
            String gmlResource, Class expectedGeometryClass, String expectedWKT) {
        this.gmlResource = gmlResource;
        this.expectedGeometryClass = expectedGeometryClass;
        this.expectedWKT = expectedWKT;
    }

    @Test
    public void test() throws IOException, XMLStreamException, FactoryException {
        try (InputStream input =
                getClass()
                        .getClassLoader()
                        .getResourceAsStream("org/geotools/gml/stream/" + gmlResource)) {
            XMLInputFactory f = XMLInputFactory.newInstance();
            f.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
            XMLStreamReader r = f.createXMLStreamReader(input);
            XmlStreamGeometryReader geometryReader =
                    new XmlStreamGeometryReader(r, new GeometryFactory());
            r.nextTag();
            Geometry g = geometryReader.readGeometry();
            assertNotNull(g);
            assertEquals(expectedGeometryClass, g.getClass());
            assertEquals(expectedWKT, g.toString());
        }
    }
}
