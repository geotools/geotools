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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import org.geotools.geometry.jts.CompoundCurve;
import org.geotools.geometry.jts.MultiCurve;
import org.geotools.geometry.jts.MultiSurface;
import org.geotools.geometry.jts.WKTWriter2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.xml.sax.SAXException;

@RunWith(Parameterized.class)
public class XmlStreamGeometryReaderParameterizedTest {
    @Parameterized.Parameters(name = "GML file: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][] {
                    {"point.gml", Point.class, "POINT (25888.999 387917.524)", false},
                    {"point2.gml", Point.class, "POINT (1 0)", false},
                    {
                        "linestring.gml",
                        LineString.class,
                        "LINESTRING (-1.28 -0.11, -0.63 0.38, -0.22 0.09, -0.45 -0.36)",
                        false
                    },
                    {
                        "curve.gml",
                        CompoundCurve.class,
                        "COMPOUNDCURVE ((66693.157 438945.144, 66699.93 438933.685), CIRCULARSTRING (66699.93 438933.685, 66709.032 438911.512, 66710.378 438885.271), (66710.378 438885.271, 66710.27 438879.854), CIRCULARSTRING (66710.27 438879.854, 66709.268 438869.136, 66706.634 438860.394), (66706.634 438860.394, 66704.527 438855.457))",
                        true
                    },
                    {"polygon.gml", Polygon.class, "POLYGON ((1 0, 0 0, 0 1, 1 1, 1 0))", false},
                    {
                        "polygon2.gml",
                        Polygon.class,
                        "POLYGON ((15 7, 16 -7, -4 -7, 1 5, -2 6, 6 10, 15 7), (3 -4, 11 -4, 13 4, 5 4, 3 -4))",
                        false
                    },
                    {
                        "polygon3.gml",
                        Polygon.class,
                        "POLYGON ((15 7, 16 -7, -4 -7, 1 5, -2 6, 6 10, 15 7), (3 -4, 11 -4, 13 4, 5 4, 3 -4), (3 -6, 2 -2, 5 6, 14 5, 12 -6, 3 -6), (2 -5, 1 0, 4 6, 6 8, 5 9, 1 7, 2 5, 0 -2, 2 -5))",
                        false
                    },
                    {
                        "multipoint.gml",
                        MultiPoint.class,
                        "MULTIPOINT ((-23 11), (-24 9), (-22 9), (-24 8))",
                        false
                    },
                    {"multipoint2.gml", MultiPoint.class, "MULTIPOINT ((-23 11), (-24 9))", false},
                    {
                        "multilinestring.gml",
                        MultiLineString.class,
                        "MULTILINESTRING ((-27 -5, -11 -5), (-20 1, -19 -12))",
                        false
                    },
                    {
                        "multipolygon.gml",
                        MultiPolygon.class,
                        "MULTIPOLYGON (((-11 -4, -8 9, 14 5, 5 -11, -11 -4), (-4 2, -6 -3, 1 -5, -4 2)), ((-19 5, -18 -10, -27 -10, -19 5)))",
                        false
                    },
                    {"multicurvez.gml", MultiCurve.class, "MULTICURVE ((1 2, 3 4))", false},
                    {
                        "multisurface.gml",
                        MultiPolygon.class,
                        "MULTIPOLYGON (((10.5 3.34, 100 123.456, 150 130, 10.5 3.34)))",
                        false
                    },
                    {
                        "multisurface_curve.gml",
                        MultiSurface.class,
                        "MULTISURFACE (CURVEPOLYGON (COMPOUNDCURVE ((25879.87 387915.774, 25880.336 387915.199, 25882.811 387912.147, 25888.868 387914.246), CIRCULARSTRING (25888.868 387914.246, 25891.589 387914.778, 25894.351 387914.539), (25894.351 387914.539, 25894.42 387914.848, 25893.844 387914.977, 25893.904 387915.98, 25896.156 387916.778, 25894.031 387922.9, 25886.656 387920.34, 25884.603 387919.628, 25884.646 387919.521, 25879.87 387915.774))))",
                        false // WKTWriter2 for XSD does not write curves because XSD returns a
                        // MultiPolygon instead of a MultiSurface
                    },
                    {
                        "multisurface_curve2.gml",
                        MultiSurface.class,
                        "MULTISURFACE (CURVEPOLYGON (COMPOUNDCURVE (CIRCULARSTRING (20716.496 371544.559, 20700.044 371528.107, 20716.496 371511.655), CIRCULARSTRING (20716.496 371511.655, 20732.948 371528.107, 20716.496 371544.559))))",
                        false // WKTWriter2 for XSD does not write curves because XSD returns a
                        // MultiPolygon instead of a MultiSurface
                    }
                });
    }

    private String gmlResource;

    private Class expectedGeometryClass;

    private String expectedWKT;

    private boolean compareXSD;

    public XmlStreamGeometryReaderParameterizedTest(
            String gmlResource,
            Class expectedGeometryClass,
            String expectedWKT,
            boolean compareXSD) {
        this.gmlResource = gmlResource;
        this.expectedGeometryClass = expectedGeometryClass;
        this.expectedWKT = expectedWKT;
        this.compareXSD = compareXSD;
    }

    @Test
    public void test() throws Exception {

        String gml = null;

        try (InputStream gmlBytes =
                getClass()
                        .getClassLoader()
                        .getResourceAsStream("org/geotools/gml/stream/" + gmlResource)) {

            gml =
                    new BufferedReader(new InputStreamReader(gmlBytes, StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
        }

        /* Just use a simple string replace on the namespace URI to make GML 3.2 testcases, assuming no differences in the
         * geometries exist between GML 2/3.1 and 3.2.
         */
        String gml3_2 =
                gml.replaceAll(
                        Pattern.quote("http://www.opengis.net/gml"),
                        "http://www.opengis.net/gml/3.2");

        testWithGmlString(gml, false);
        testWithGmlString(gml3_2, true);
    }

    private void testWithGmlString(final String gml, final boolean isGml3_2) throws Exception {
        XMLInputFactory f = XMLInputFactory.newInstance();
        f.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        XMLStreamReader r = f.createXMLStreamReader(new StringReader(gml));
        XmlStreamGeometryReader geometryReader = new XmlStreamGeometryReader(r);
        r.nextTag();
        Geometry g = geometryReader.readGeometry();
        assertNotNull(g);
        assertEquals(expectedGeometryClass, g.getClass());

        WKTWriter2 writer = new WKTWriter2();
        String wkt = writer.write(g);
        assertEquals(expectedWKT, wkt);

        if (compareXSD) {
            Geometry xsdGeometry = parseWithXSD(gml, isGml3_2);
            String xsdWKT = writer.write(xsdGeometry);
            assertEquals(xsdWKT, wkt);
        }
    }

    private static Geometry parseWithXSD(final String gml, final boolean isGml3_2)
            throws IOException, ParserConfigurationException, SAXException {
        org.geotools.xsd.Configuration configuration;
        if (isGml3_2) {
            configuration = new org.geotools.gml3.v3_2.GMLConfiguration();
        } else {
            configuration = new org.geotools.gml3.GMLConfiguration();
        }
        org.geotools.xsd.Parser parser = new org.geotools.xsd.Parser(configuration);
        return (Geometry) parser.parse(new StringReader(gml));
    }
}
