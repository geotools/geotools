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
import org.geotools.TestData;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Literal;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.GeoTools;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.LineString;

/**
 * Unit tests for WKT mark factory NOTE: To make it work, a properties wkt.properties, containing: ls=LINESTRING(0.0
 * 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25) and located in /tmp has to be provided
 *
 * @author Luca Morandini lmorandini@ieee.org
 */
public class WKTMarkFactoryTest {

    private static final SimpleFeature feature;
    private static final FilterFactory ff;
    private static final URL rootDir;

    // This inner class is used to make some members public in
    // order to carry out testing
    static final class WKTMarkFactoryPublic extends WKTMarkFactory {
        public String getFromCachePublic(String urlLib, String wktName) {
            return this.getFromCache(urlLib, wktName);
        }

        public void addToCachePublic(String urlLib) {
            this.addToCache(urlLib);
        }
    }

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

    @Test
    public void testWellKnownTextLineString() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            Literal exp = ff.literal(
                    WKTMarkFactory.WKT_PREFIX + "LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)");
            wmf.getShape(null, exp, feature);
        } catch (Exception e) {
            Assert.fail();
            return;
        }

        Assert.assertTrue(true);
    }

    @Test
    public void testWellKnownTextMultiLineString() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            Literal exp = ff.literal(
                    WKTMarkFactory.WKT_PREFIX
                            + "MULTILINESTRING((0.25 0.25, 0.5 0.75, 0.75 0.25, 0.25 0.25), (0.25 0.75, 0.5 0.25, 0.75 0.75, 0.25 0.75))");
            wmf.getShape(null, exp, feature);
        } catch (Exception e) {
            Assert.fail();
            return;
        }

        Assert.assertTrue(true);
    }

    @Test
    public void testWellKnownTextPolygon() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            Literal exp =
                    ff.literal(WKTMarkFactory.WKT_PREFIX + "POLYGON((0.25 0.25, 0.5 0.75, 0.75 0.25, 0.25 0.25))");
            wmf.getShape(null, exp, feature);
        } catch (Exception e) {
            Assert.fail();
            return;
        }

        Assert.assertTrue(true);
    }

    @Test
    public void testWellKnownTextCurve() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            Literal exp = ff.literal(WKTMarkFactory.WKT_PREFIX
                    + "CURVEPOLYGON(COMPOUNDCURVE(CIRCULARSTRING(0 0,2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0)))");
            wmf.getShape(null, exp, feature);
        } catch (Exception e) {
            Assert.fail();
            return;
        }

        Assert.assertTrue(true);
    }

    @Test
    public void testWellKnownTextPolygonError() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            Literal exp = ff.literal(WKTMarkFactory.WKT_PREFIX + "POLYGON((0.25 0.25, 0.5 0.75, 0.75 0.25,     ))");
            wmf.getShape(null, exp, feature);
        } catch (Exception e) {
            Assert.assertTrue(true);
            return;
        }

        Assert.fail();
    }

    @Test
    public void testUnknownProtocol() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            Literal exp = ff.literal("xxx://POLYGON((0.25 0.25, 0.5 0.75, 0.75 0.25,))");
            if (wmf.getShape(null, exp, feature) == null) {
                Assert.assertTrue(true);
                return;
            }
        } catch (Exception e) {
            Assert.fail();
            return;
        }
        Assert.fail();
    }

    @Test
    public void testWellKnownTextFromNotExistingFile() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        wmf.setRoot(rootDir);
        Literal exp = ff.literal(WKTMarkFactory.WKTLIB_PREFIX + "xxx.properties#xxx");
        try {
            wmf.getShape(null, exp, feature);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testNotExistingWellKnownTextFromFile() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        wmf.setRoot(rootDir);
        Literal exp = ff.literal(WKTMarkFactory.WKTLIB_PREFIX + "wkt.properties#xxx");
        try {
            wmf.getShape(null, exp, feature);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testWellKnownTextFromFile() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        wmf.setRoot(rootDir);
        Literal exp = ff.literal(WKTMarkFactory.WKTLIB_PREFIX + "wkt.properties#ls");
        try {
            wmf.getShape(null, exp, feature);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testCacheSingleLibrary() {

        WKTMarkFactoryPublic wmfp = new WKTMarkFactoryPublic();
        wmfp.setRoot(rootDir);

        try {
            // Adds library to cache
            Literal exp = ff.literal("wkt.properties");
            wmfp.addToCachePublic(exp.evaluate(feature, String.class));

            // Check "ls" has been loaded
            Assert.assertEquals(
                    wmfp.getFromCachePublic("wkt.properties", "ls"),
                    "LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)");

            // Check "xx" has not been loaded
            Assert.assertNull(wmfp.getFromCachePublic("wkt.properties", "xx"));

            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
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
            Assert.assertEquals(
                    wmfp.getFromCachePublic("wkt.properties", "ls"),
                    "LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)");

            // Check "ls2" has been loaded
            Assert.assertEquals(
                    wmfp.getFromCachePublic("wkt2.properties", "ls2"),
                    "LINESTRING(0.0 0.5, 0.5 0.5, 0.75 1.0, 1.0 0.5, 1.25 0.5)");

            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
