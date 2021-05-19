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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.geotools.geometry.jts.CompoundCurve;
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
import org.opengis.referencing.FactoryException;
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
                    {
                        "multicurvez.gml",
                        org.geotools.geometry.jts.MultiCurve.class,
                        "MULTILINESTRING ((1 2, 3 4))",
                        false
                    },
                    {
                        "multisurface.gml",
                        MultiPolygon.class,
                        "MULTIPOLYGON (((10.5 3.34, 100 123.456, 150 130, 10.5 3.34)))",
                        false
                    },
                    {
                        "multisurface_curve.gml",
                        MultiPolygon.class,
                        "MULTIPOLYGON (((25879.87 387915.774, 25880.336 387915.199, 25882.811 387912.147, 25888.868 387914.246, 25889.543528940994 387914.4529627376, 25890.820713326168 387914.70701050723, 25891.589 387914.778, 25892.120131109885 387914.7921788484, 25893.419548893602 387914.70701050723, 25894.351 387914.539, 25894.42 387914.848, 25893.844 387914.977, 25893.904 387915.98, 25896.156 387916.778, 25894.031 387922.9, 25886.656 387920.34, 25884.603 387919.628, 25884.646 387919.521, 25879.87 387915.774)))",
                        true
                    },
                    {
                        "multisurface_curve2.gml",
                        MultiPolygon.class,
                        "MULTIPOLYGON (((20716.496 371544.559, 20714.348583453197 371544.4182508586, 20712.23790942651 371543.99841169117, 20710.20009251658 371543.30666606233, 20708.27000033573 371542.3548499315, 20706.4806572963 371541.15924913663, 20704.862679553786 371539.7403207388, 20703.443751155955 371538.1223429963, 20702.248150361098 371536.3329999569, 20701.296334230294 371534.402907776, 20700.604588601436 371532.3650908661, 20700.18474943402 371530.2544168394, 20700.044 371528.107, 20700.044000292604 371528.10699991375, 20700.18474943402 371525.9595829881, 20700.604588601436 371523.8489089614, 20701.296334230294 371521.8110920515, 20702.248150361098 371519.88099987066, 20703.443751155955 371518.0916568312, 20704.862679553786 371516.4736790887, 20706.4806572963 371515.0547506909, 20708.27000033573 371513.859149896, 20710.20009251658 371512.9073337652, 20712.23790942651 371512.21558813634, 20714.348583453197 371511.7957489689, 20716.496 371511.655, 20718.643416349958 371511.79574914, 20720.754090354505 371512.215588303, 20722.791907243056 371512.9073339246, 20724.721999403657 371513.8591500454, 20726.511342424317 371515.0547508277, 20728.12932014986 371516.47367921064, 20729.548248532803 371518.0916569362, 20730.74384931512 371519.88099995686, 20731.69566543594 371521.81109211745, 20732.38741105754 371523.848909006, 20732.80725022055 371525.9595830106, 20732.94799936049 371528.1069999137, 20732.948 371528.107, 20732.80725022055 371530.2544168168, 20732.38741105754 371532.3650908214, 20731.69566543594 371534.40290770994, 20730.74384931512 371536.33299987053, 20729.548248532803 371538.1223428912, 20728.12932014986 371539.74032061675, 20726.511342424317 371541.15924899967, 20724.721999403657 371542.354849782, 20722.791907243056 371543.3066659028, 20720.754090354505 371543.9984115244, 20718.643416349958 371544.4182506874, 20716.496 371544.559)))",
                        true
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
    public void test()
            throws IOException, XMLStreamException, FactoryException, ParserConfigurationException,
                    SAXException {
        try (InputStream input =
                getClass()
                        .getClassLoader()
                        .getResourceAsStream("org/geotools/gml/stream/" + gmlResource)) {
            XMLInputFactory f = XMLInputFactory.newInstance();
            f.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
            XMLStreamReader r = f.createXMLStreamReader(input);
            XmlStreamGeometryReader geometryReader = new XmlStreamGeometryReader(r);
            r.nextTag();
            Geometry g = geometryReader.readGeometry();
            assertNotNull(g);
            assertEquals(expectedGeometryClass, g.getClass());
            assertEquals(expectedWKT, g.toString());

            if (compareXSD) {
                Geometry xsdGeometry = parseWithXSD();
                // Compare via default WKT toString()
                assertEquals(xsdGeometry.toString(), g.toString());
            }
        }
    }

    private Geometry parseWithXSD() throws IOException, ParserConfigurationException, SAXException {
        try (InputStream input =
                getClass()
                        .getClassLoader()
                        .getResourceAsStream("org/geotools/gml/stream/" + gmlResource)) {
            org.geotools.xsd.Parser parser =
                    new org.geotools.xsd.Parser(new org.geotools.gml3.GMLConfiguration());
            return (Geometry) parser.parse(input);
        }
    }
}
