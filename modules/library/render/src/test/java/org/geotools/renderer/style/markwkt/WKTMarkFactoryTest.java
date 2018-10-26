/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style.markwkt;

import java.io.File;
import java.net.URL;
import junit.framework.TestCase;
import org.geotools.TestData;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.GeoTools;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;

/**
 * Unit tests for WKT mark factory NOTE: To make it work, a properties wkt.properties, containing:
 * ls=LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25) and located in /tmp has to be
 * provided
 *
 * @author Luca Morandini lmorandini@ieee.org
 */
public class WKTMarkFactoryTest extends TestCase {

    private static final SimpleFeature feature;
    private static final FilterFactory ff;
    private static final URL rootDir;

    // This inner class is used to make some members public in
    // order to carry out testing
    final class WKTMarkFactoryPublic extends WKTMarkFactory {
        public String getFromCachePublic(String urlLib, String wktName) {
            return this.getFromCache(urlLib, wktName);
        }

        public void addToCachePublic(String urlLib) {
            this.addToCache(urlLib);
        }
    };

    static {
        try {
            ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
            SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
            featureTypeBuilder.setName("TestType");
            featureTypeBuilder.add("geom", LineString.class, DefaultGeographicCRS.WGS84);
            SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();
            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
            feature = featureBuilder.buildFeature(null);

            // Sets the root dir for WKT libraries to the dir where wkt.properties is
            File fl = TestData.file(WKTMarkFactoryTest.class, "");
            assert fl.isDirectory() && fl.exists();
            rootDir = fl.toURI().toURL();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void testWellKnownTextLineString() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            Literal exp =
                    ff.literal(
                            WKTMarkFactory.WKT_PREFIX
                                    + "LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)");
            wmf.getShape(null, exp, feature);
        } catch (Exception e) {
            assertTrue(false);
            return;
        }

        assertTrue(true);
    }

    public void testWellKnownTextMultiLineString() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            Literal exp =
                    ff.literal(
                            WKTMarkFactory.WKT_PREFIX
                                    + "MULTILINESTRING((0.25 0.25, 0.5 0.75, 0.75 0.25, 0.25 0.25), (0.25 0.75, 0.5 0.25, 0.75 0.75, 0.25 0.75))");
            wmf.getShape(null, exp, feature);
        } catch (Exception e) {
            assertTrue(false);
            return;
        }

        assertTrue(true);
    }

    public void testWellKnownTextPolygon() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            Literal exp =
                    ff.literal(
                            WKTMarkFactory.WKT_PREFIX
                                    + "POLYGON((0.25 0.25, 0.5 0.75, 0.75 0.25, 0.25 0.25))");
            wmf.getShape(null, exp, feature);
        } catch (Exception e) {
            assertTrue(false);
            return;
        }

        assertTrue(true);
    }

    public void testWellKnownTextCurve() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            Literal exp =
                    ff.literal(
                            WKTMarkFactory.WKT_PREFIX
                                    + "CURVEPOLYGON(COMPOUNDCURVE(CIRCULARSTRING(0 0,2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0)))");
            wmf.getShape(null, exp, feature);
        } catch (Exception e) {
            assertTrue(false);
            return;
        }

        assertTrue(true);
    }

    public void testWellKnownTextPolygonError() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            Literal exp =
                    ff.literal(
                            WKTMarkFactory.WKT_PREFIX
                                    + "POLYGON((0.25 0.25, 0.5 0.75, 0.75 0.25,     ))");
            wmf.getShape(null, exp, feature);
        } catch (Exception e) {
            assertTrue(true);
            return;
        }

        assertTrue(false);
    }

    public void testUnknownProtocol() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            Literal exp = ff.literal("xxx://POLYGON((0.25 0.25, 0.5 0.75, 0.75 0.25,))");
            if (wmf.getShape(null, exp, feature) == null) {
                assertTrue(true);
                return;
            }
        } catch (Exception e) {
            assertTrue(false);
            return;
        }
        assertTrue(false);
    }

    public void testWellKnownTextFromNotExistingFile() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        wmf.setRoot(rootDir);
        Literal exp = ff.literal(WKTMarkFactory.WKTLIB_PREFIX + "xxx.properties#xxx");
        try {
            wmf.getShape(null, exp, feature);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testNotExistingWellKnownTextFromFile() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        wmf.setRoot(rootDir);
        Literal exp = ff.literal(WKTMarkFactory.WKTLIB_PREFIX + "wkt.properties#xxx");
        try {
            wmf.getShape(null, exp, feature);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testWellKnownTextFromFile() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        wmf.setRoot(rootDir);
        Literal exp = ff.literal(WKTMarkFactory.WKTLIB_PREFIX + "wkt.properties#ls");
        try {
            wmf.getShape(null, exp, feature);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    public void testCacheSingleLibrary() {

        WKTMarkFactoryPublic wmfp = new WKTMarkFactoryPublic();
        wmfp.setRoot(rootDir);

        try {
            // Adds library to cache
            Literal exp = ff.literal("wkt.properties");
            wmfp.addToCachePublic(exp.evaluate(feature, String.class));

            // Check "ls" has been loaded
            assertEquals(
                    wmfp.getFromCachePublic("wkt.properties", "ls"),
                    "LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)");

            // Check "xx" has not been loaded
            assertEquals(wmfp.getFromCachePublic("wkt.properties", "xx"), null);

            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    public void testCacheMultipleLibraries() {

        WKTMarkFactoryPublic wmfp = new WKTMarkFactoryPublic();
        wmfp.setRoot(rootDir);

        try {
            // Adds 1st library to cache
            Literal exp = ff.literal("wkt.properties");
            wmfp.addToCachePublic(exp.evaluate(feature, String.class));

            // Adds 2nd library to cache
            exp = ff.literal("wkt2.properties");
            wmfp.addToCachePublic(exp.evaluate(feature, String.class));

            // Check "ls" has been loaded
            assertEquals(
                    wmfp.getFromCachePublic("wkt.properties", "ls"),
                    "LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)");

            // Check "ls2" has been loaded
            assertEquals(
                    wmfp.getFromCachePublic("wkt2.properties", "ls2"),
                    "LINESTRING(0.0 0.5, 0.5 0.5, 0.75 1.0, 1.0 0.5, 1.25 0.5)");

            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
