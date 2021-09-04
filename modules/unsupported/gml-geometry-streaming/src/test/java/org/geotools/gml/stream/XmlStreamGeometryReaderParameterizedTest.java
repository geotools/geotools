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
                    },
                    {
                        "surface.gml",
                        MultiPolygon.class,
                        "MULTIPOLYGON (((1 0, 0 0, 0 1, 1 1, 1 0)))",
                        // gml 3.2 xsd test fails because it returns an empty string
                        false
                    },
                    {
                        "surface2.gml",
                        MultiPolygon.class,
                        "MULTIPOLYGON (((214606.115 581137.695, 214593.637 581184.181, 214586.404 581200.432, 214582.757 581198.853, 214579.699 581197.328, 214595.919 581135.491, 214597.599 581135.854, 214606.115 581137.695)))",
                        // gml 3.2 xsd test fails because it returns an empty string
                        false
                    },
                    {
                        "surface3.gml",
                        MultiPolygon.class,
                        "MULTIPOLYGON (((83017.684 436256.656, 83017.75 436256.62, 83018.089 436257.232, 83018.658 436256.917, 83018.318 436256.305, 83019.517 436255.639, 83023.106 436253.648, 83023.445 436254.261, 83023.795 436254.066, 83023.455 436253.454, 83028.352 436250.738, 83028.692 436251.35, 83029.042 436251.156, 83028.702 436250.543, 83033.489 436247.887, 83033.829 436248.5, 83034.397 436248.184, 83034.058 436247.572, 83038.845 436244.916, 83039.185 436245.528, 83039.535 436245.334, 83039.195 436244.722, 83044.092 436242.005, 83044.432 436242.617, 83044.781 436242.423, 83044.442 436241.811, 83049.339 436239.094, 83049.678 436239.706, 83050.028 436239.512, 83049.688 436238.9, 83054.476 436236.244, 83054.815 436236.856, 83055.384 436236.54, 83055.044 436235.928, 83059.832 436233.272, 83060.171 436233.884, 83060.521 436233.69, 83060.182 436233.078, 83065.078 436230.361, 83065.418 436230.973, 83065.768 436230.779, 83065.428 436230.167, 83070.325 436227.45, 83070.665 436228.062, 83071.014 436227.868, 83070.675 436227.256, 83075.462 436224.6, 83075.802 436225.212, 83076.37 436224.896, 83076.031 436224.284, 83080.818 436221.628, 83081.158 436222.24, 83081.508 436222.046, 83081.168 436221.434, 83086.065 436218.717, 83086.404 436219.329, 83086.754 436219.135, 83086.415 436218.523, 83091.311 436215.806, 83091.651 436216.418, 83092.001 436216.224, 83091.661 436215.612, 83096.449 436212.956, 83096.788 436213.568, 83097.357 436213.253, 83097.017 436212.641, 83101.805 436209.984, 83102.144 436210.596, 83102.494 436210.402, 83102.154 436209.79, 83107.051 436207.073, 83107.391 436207.686, 83107.741 436207.491, 83107.401 436206.879, 83112.189 436204.223, 83112.528 436204.835, 83113.097 436204.52, 83112.757 436203.908, 83113.99 436203.225, 83114.089 436203.17, 83114.156 436203.283, 83114.373 436203.159, 83114.625 436203.599, 83114.408 436203.723, 83115.525 436205.958, 83115.53 436205.967, 83116.556 436208.023, 83116.658 436207.977, 83116.784 436207.92, 83116.994 436208.383, 83116.908 436208.422, 83116.766 436208.486, 83118.579 436212.989, 83118.819 436212.918, 83118.966 436213.409, 83118.726 436213.48, 83118.736 436213.514, 83118.239 436213.789, 83117.9 436213.177, 83117.331 436213.492, 83117.671 436214.105, 83112.883 436216.76, 83112.544 436216.148, 83112.194 436216.342, 83112.534 436216.955, 83107.637 436219.671, 83107.297 436219.059, 83106.947 436219.253, 83107.287 436219.865, 83102.499 436222.522, 83102.16 436221.91, 83101.591 436222.225, 83101.931 436222.838, 83097.143 436225.493, 83096.804 436224.881, 83096.454 436225.075, 83096.794 436225.687, 83091.897 436228.404, 83091.557 436227.792, 83091.207 436227.986, 83091.547 436228.598, 83086.65 436231.315, 83086.311 436230.703, 83085.961 436230.897, 83086.3 436231.509, 83081.513 436234.165, 83081.173 436233.553, 83080.605 436233.869, 83080.945 436234.48, 83076.157 436237.137, 83075.817 436236.525, 83075.468 436236.719, 83075.807 436237.331, 83070.91 436240.048, 83070.571 436239.436, 83070.221 436239.63, 83070.561 436240.242, 83065.664 436242.959, 83065.324 436242.347, 83064.974 436242.541, 83065.314 436243.153, 83060.527 436245.809, 83060.187 436245.197, 83059.619 436245.512, 83059.958 436246.125, 83055.171 436248.781, 83054.831 436248.169, 83054.481 436248.363, 83054.821 436248.975, 83049.924 436251.692, 83049.584 436251.08, 83049.235 436251.274, 83049.574 436251.886, 83044.677 436254.603, 83044.338 436253.991, 83043.988 436254.185, 83044.328 436254.797, 83039.54 436257.452, 83039.2 436256.841, 83038.632 436257.156, 83038.972 436257.768, 83034.184 436260.425, 83033.845 436259.812, 83033.495 436260.007, 83033.834 436260.619, 83028.938 436263.335, 83028.598 436262.723, 83028.248 436262.917, 83028.588 436263.53, 83025.27 436265.371, 83023.8 436266.186, 83023.461 436265.574, 83022.892 436265.889, 83023.232 436266.502, 83023.171 436266.536, 83017.684 436256.656), (83020.321 436261.255, 83020.661 436261.866, 83021.229 436261.551, 83020.889 436260.939, 83020.321 436261.255), (83025.677 436258.283, 83026.016 436258.895, 83026.366 436258.701, 83026.027 436258.089, 83025.677 436258.283), (83030.923 436255.372, 83031.263 436255.984, 83031.613 436255.79, 83031.273 436255.178, 83030.923 436255.372), (83036.061 436252.522, 83036.4 436253.134, 83036.969 436252.818, 83036.629 436252.206, 83036.061 436252.522), (83041.417 436249.55, 83041.756 436250.162, 83042.106 436249.968, 83041.766 436249.356, 83041.417 436249.55), (83046.663 436246.639, 83047.003 436247.251, 83047.353 436247.057, 83047.013 436246.445, 83046.663 436246.639), (83051.91 436243.728, 83052.25 436244.34, 83052.599 436244.146, 83052.26 436243.534, 83051.91 436243.728), (83057.047 436240.878, 83057.387 436241.49, 83057.955 436241.175, 83057.616 436240.563, 83057.047 436240.878), (83062.403 436237.906, 83062.743 436238.518, 83063.093 436238.324, 83062.753 436237.712, 83062.403 436237.906), (83067.65 436234.995, 83067.989 436235.608, 83068.339 436235.413, 83067.999 436234.801, 83067.65 436234.995), (83072.896 436232.084, 83073.236 436232.697, 83073.586 436232.503, 83073.246 436231.89, 83072.896 436232.084), (83078.034 436229.234, 83078.373 436229.846, 83078.942 436229.531, 83078.602 436228.919, 83078.034 436229.234), (83083.39 436226.263, 83083.729 436226.875, 83084.079 436226.681, 83083.739 436226.069, 83083.39 436226.263), (83088.636 436223.352, 83088.976 436223.964, 83089.326 436223.77, 83088.986 436223.158, 83088.636 436223.352), (83093.883 436220.441, 83094.222 436221.053, 83094.572 436220.859, 83094.233 436220.247, 83093.883 436220.441), (83099.02 436217.591, 83099.36 436218.202, 83099.928 436217.887, 83099.588 436217.275, 83099.02 436217.591), (83104.376 436214.619, 83104.716 436215.231, 83105.065 436215.037, 83104.726 436214.425, 83104.376 436214.619), (83109.623 436211.708, 83109.962 436212.32, 83110.312 436212.126, 83109.972 436211.514, 83109.623 436211.708), (83114.76 436208.858, 83115.099 436209.47, 83115.668 436209.154, 83115.328 436208.542, 83114.76 436208.858)))",
                        // gml 3.2 xsd test fails because it returns an empty string
                        false
                    },
                    {
                        "multisurface_surface.gml",
                        // because Surface is parsed to MultiPolygon
                        MultiPolygon.class,
                        "MULTIPOLYGON (((0 0, 0 1, 1 1, 1 0, 0 0)))",
                        false
                    },
                    {
                        "multisurface_s_surface.gml",
                        // because Surface is parsed to MultiPolygon
                        MultiPolygon.class,
                        "MULTIPOLYGON (((0 0, 0 1, 1 1, 1 0, 0 0)))",
                        false
                    },
                    {
                        "surface_multipatch.gml",
                        MultiPolygon.class,
                        "MULTIPOLYGON (((1 0, 0 0, 0 1, 1 1, 1 0)), ((0 -1, 1 -1, 1 0, 0 0, 0 -1)))",
                        false
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
