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
package org.geotools.renderer.markwkt.test;

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Expression;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.filter.FilterFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.markwkt.WKTMarkFactory;
import org.geotools.util.SoftValueHashMap;

import com.vividsolutions.jts.geom.LineString;

import junit.framework.TestCase;

/**
 * Unit tests for WKT mark factory
 * NOTE: To make it work, a properties wkt.properties, containing:
 * ls=LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)
 * and located in /tmp has to be provided 
 * 
 * @author Luca Morandini lmorandini@ieee.org
 * 
 *
 *
 * @source $URL$
 */
public class WKTMarkFactoryTest extends TestCase {

    private SimpleFeature feature;
    private Expression exp;
    private FilterFactory ff;
    private URL rootDir;

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

    {
	try {
	    ff = CommonFactoryFinder.getFilterFactory(null);
	    SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
	    featureTypeBuilder.setName("TestType");
	    featureTypeBuilder.add("geom", LineString.class,
		    DefaultGeographicCRS.WGS84);
	    SimpleFeatureType featureType = featureTypeBuilder
		    .buildFeatureType();
	    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(
		    featureType);
	    this.feature = featureBuilder.buildFeature(null);
	    
	    // Sets the root dir for WKT libraries to the dir where wkt.properties is
	    File fl= new File(this.getClass().getClassLoader().getResource("wkt.properties").toString().replace("wkt.properties", ""));
	    this.rootDir= new URL(fl.toString());
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void testWellKnownTextLineString() {
	WKTMarkFactory wmf = new WKTMarkFactory();
	try {
	    this.exp = ff
		    .literal(WKTMarkFactory.WKT_PREFIX + "LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)");
	    wmf.getShape(null, this.exp, this.feature);
	} catch (Exception e) {
	    assertTrue(false);
	    return;
	}

	assertTrue(true);
    }

    public void testWellKnownTextMultiLineString() {
	WKTMarkFactory wmf = new WKTMarkFactory();
	try {
	    this.exp = ff
		    .literal(WKTMarkFactory.WKT_PREFIX + "MULTILINESTRING((0.25 0.25, 0.5 0.75, 0.75 0.25, 0.25 0.25), (0.25 0.75, 0.5 0.25, 0.75 0.75, 0.25 0.75))");
	    wmf.getShape(null, this.exp, this.feature);
	} catch (Exception e) {
	    assertTrue(false);
	    return;
	}

	assertTrue(true);
    }

    public void testWellKnownTextPolygon() {
	WKTMarkFactory wmf = new WKTMarkFactory();
	try {
	    this.exp = ff
		    .literal(WKTMarkFactory.WKT_PREFIX + "POLYGON((0.25 0.25, 0.5 0.75, 0.75 0.25, 0.25 0.25))");
	    wmf.getShape(null, this.exp, this.feature);
	} catch (Exception e) {
	    assertTrue(false);
	    return;
	}

	assertTrue(true);
    }

    public void testWellKnownTextCurve() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            this.exp = ff
                    .literal(WKTMarkFactory.WKT_PREFIX + "CURVEPOLYGON(COMPOUNDCURVE(CIRCULARSTRING(0 0,2 0, 2 1, 2 3, 4 3),(4 3, 4 5, 1 4, 0 0)))");
            wmf.getShape(null, this.exp, this.feature);
        } catch (Exception e) {
            assertTrue(false);
            return;
        }

        assertTrue(true);
    }

    public void testWellKnownTextPolygonError() {
	WKTMarkFactory wmf = new WKTMarkFactory();
	try {
	    this.exp = ff
		    .literal(WKTMarkFactory.WKT_PREFIX + "POLYGON((0.25 0.25, 0.5 0.75, 0.75 0.25,     ))");
	    wmf.getShape(null, this.exp, this.feature);
	} catch (Exception e) {
	    assertTrue(true);
	    return;
	}

	assertTrue(false);
    }

    public void testUnknownProtocol() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        try {
            this.exp = ff
                    .literal("xxx://POLYGON((0.25 0.25, 0.5 0.75, 0.75 0.25,))");
            if (wmf.getShape(null, this.exp, this.feature) == null) {
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
        wmf.setRoot(this.rootDir);
        this.exp = ff.literal(WKTMarkFactory.WKTLIB_PREFIX + "xxx.properties#xxx");
        try {
            wmf.getShape(null, this.exp, this.feature);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testNotExistingWellKnownTextFromFile() {
        WKTMarkFactory wmf = new WKTMarkFactory();
        wmf.setRoot(this.rootDir);
        this.exp = ff.literal(WKTMarkFactory.WKTLIB_PREFIX + "wkt.properties#xxx");
        try {
            wmf.getShape(null, this.exp, this.feature);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void testWellKnownTextFromFile() {
	WKTMarkFactory wmf = new WKTMarkFactory();
        wmf.setRoot(this.rootDir);
	this.exp = ff.literal(WKTMarkFactory.WKTLIB_PREFIX + "wkt.properties#ls");
	try {
	    wmf.getShape(null, this.exp, this.feature);
	    assertTrue(true);
	} catch (Exception e) {
	    assertTrue(false);
	}
    }

    public void testCacheSingleLibrary() {

        WKTMarkFactoryPublic wmfp= new WKTMarkFactoryPublic();
        wmfp.setRoot(this.rootDir);
        
        try {
            // Adds library to cache
            this.exp = ff.literal("wkt.properties");
            wmfp.addToCachePublic(this.exp.evaluate(this.feature, String.class));
            
            // Check "ls" has been loaded
            assertEquals(wmfp.getFromCachePublic("wkt.properties", "ls"),
                    "LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)");
            
            // Check "xx" has not been loaded
            assertEquals(wmfp.getFromCachePublic("wkt.properties", "xx"),
                    null);
            
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
    public void testCacheMultipleLibraries() {

        WKTMarkFactoryPublic wmfp= new WKTMarkFactoryPublic();
        wmfp.setRoot(this.rootDir);
        
        try {
            // Adds 1st library to cache
            this.exp = ff.literal("wkt.properties");
            wmfp.addToCachePublic(this.exp.evaluate(this.feature, String.class));
            
            // Adds 2nd library to cache
            this.exp = ff.literal("wkt2.properties");
            wmfp.addToCachePublic(this.exp.evaluate(this.feature, String.class));
            
            // Check "ls" has been loaded
            assertEquals(wmfp.getFromCachePublic("wkt.properties", "ls"),
                    "LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)");
            
            // Check "ls2" has been loaded
            assertEquals(wmfp.getFromCachePublic("wkt2.properties", "ls2"),
                    "LINESTRING(0.0 0.5, 0.5 0.5, 0.75 1.0, 1.0 0.5, 1.25 0.5)");
            
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    
}
