/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg;

import java.util.Arrays;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.factory.IdentifiedObjectFinder;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * This class makes sure we can find the ThreadedHsqlEpsgFactory
 * using ReferencingFactoryFinder.
 * 
 * @author Jody
 *
 * @source $URL$
 */
public class ThreadedHsqlEpsgFactoryTest extends TestCase {
    
    private static ThreadedHsqlEpsgFactory factory;
    private static IdentifiedObjectFinder finder;
    static final double EPS = 1e-06;
    
    protected void setUp() throws Exception {
        super.setUp();
        if( factory == null ){
            factory = (ThreadedHsqlEpsgFactory) ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null );
        }
        if( finder == null ){
            finder = factory.getIdentifiedObjectFinder(CoordinateReferenceSystem.class);
        }
    }

    public void testCreation() throws Exception {
        assertNotNull(factory);
        CoordinateReferenceSystem epsg4326 = factory.createCoordinateReferenceSystem("EPSG:4326");
        CoordinateReferenceSystem code4326 = factory.createCoordinateReferenceSystem("4326");

        assertEquals("4326 equals EPSG:4326", code4326, epsg4326);
        assertSame("4326 == EPSG:4326", code4326, epsg4326);
    }
    public void testFunctionality() throws Exception {
        CoordinateReferenceSystem crs1 = factory.createCoordinateReferenceSystem("4326");
        CoordinateReferenceSystem crs2 = factory.createCoordinateReferenceSystem("3005");

        // reproject
        MathTransform transform = CRS.findMathTransform(crs1, crs2,true);
        DirectPosition pos = new DirectPosition2D(48.417, 123.35);
        transform.transform(pos, null);        
    }
    public void testAuthorityCodes() throws Exception {
        Set authorityCodes = factory.getAuthorityCodes(CoordinateReferenceSystem.class);
        assertNotNull(authorityCodes);
        assertTrue(authorityCodes.size() > 3000);
    }

    public void testFindWSG84() throws FactoryException {
        String wkt;
        wkt = "GEOGCS[\"WGS 84\",\n"                                    +
              "  DATUM[\"World Geodetic System 1984\",\n"               +
              "    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563]],\n"  +
              "  PRIMEM[\"Greenwich\", 0.0],\n"                         +
              "  UNIT[\"degree\", 0.017453292519943295],\n"             +
              "  AXIS[\"Geodetic latitude\", NORTH],\n"                 +
              "  AXIS[\"Geodetic longitude\", EAST]]";
        
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        finder.setFullScanAllowed(false);
        
        assertNull("Should not find without a full scan, because the WKT contains no identifier " +
                   "and the CRS name is ambiguous (more than one EPSG object have this name).",
                   finder.find(crs));

        finder.setFullScanAllowed(true);
        IdentifiedObject find = finder.find(crs);
        
        assertNotNull("With full scan allowed, the CRS should be found.", find);
        
        assertTrue("Should found an object equals (ignoring metadata) to the requested one.",CRS.equalsIgnoreMetadata(crs, find));
        ReferenceIdentifier found = AbstractIdentifiedObject.getIdentifier(find, factory.getAuthority());
		//assertEquals("4326",found.getCode());
        assertNotNull( found );
        finder.setFullScanAllowed(false);
        String id = finder.findIdentifier(crs);
        assertEquals("The CRS should still be in the cache.","EPSG:4326", id);
    }
    public void testFindBeijing1954() throws FactoryException {
        /*
         * The PROJCS below intentionally uses a name different from the one found in the
         * EPSG database, in order to force a full scan (otherwise the EPSG database would
         * find it by name, but we want to test the scan).
         */
        String wkt = "PROJCS[\"Beijing 1954\",\n"                          +
              "   GEOGCS[\"Beijing 1954\",\n"                              +
              "     DATUM[\"Beijing 1954\",\n"                             +
              "       SPHEROID[\"Krassowsky 1940\", 6378245.0, 298.3]],\n" +
              "     PRIMEM[\"Greenwich\", 0.0],\n"                         +
              "     UNIT[\"degree\", 0.017453292519943295],\n"             +
              "     AXIS[\"Geodetic latitude\", NORTH],\n"                 +
              "     AXIS[\"Geodetic longitude\", EAST]],\n"                +
              "   PROJECTION[\"Transverse Mercator\"],\n"                  +
              "   PARAMETER[\"central_meridian\", 135.0],\n"               +
              "   PARAMETER[\"latitude_of_origin\", 0.0],\n"               +
              "   PARAMETER[\"scale_factor\", 1.0],\n"                     +
              "   PARAMETER[\"false_easting\", 500000.0],\n"               +
              "   PARAMETER[\"false_northing\", 0.0],\n"                   +
              "   UNIT[\"m\", 1.0],\n"                                     +
              "   AXIS[\"Northing\", NORTH],\n"                            +
              "   AXIS[\"Easting\", EAST]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        
        finder.setFullScanAllowed(false);
        assertNull("Should not find the CRS without a full scan.", finder.find(crs));
        
        finder.setFullScanAllowed(true);
        IdentifiedObject find = finder.find(crs);        
        assertNotNull("With full scan allowed, the CRS should be found.", find);
        
        assertTrue("Should found an object equals (ignoring metadata) to the requested one.",
                   CRS.equalsIgnoreMetadata(crs, find));
        
        assertEquals("2442", AbstractIdentifiedObject.getIdentifier(find, factory.getAuthority()).getCode());
        finder.setFullScanAllowed(false);
        String id = finder.findIdentifier(crs);
        assertEquals("The CRS should still be in the cache.","EPSG:2442", id);
    }
    
    public void testGoogleProjection() throws Exception {
        CoordinateReferenceSystem epsg4326 = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem epsg3785 = CRS.decode("EPSG:3857");
        
        String wkt900913 = "PROJCS[\"WGS84 / Google Mercator\", " +
        		"GEOGCS[\"WGS 84\", " +
        		"  DATUM[\"World Geodetic System 1984\", " +
        		"    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], " +
        		"    AUTHORITY[\"EPSG\",\"6326\"]], " +
        		"  PRIMEM[\"Greenwich\", 0.0, " +
        		"  AUTHORITY[\"EPSG\",\"8901\"]], " +
        		"  UNIT[\"degree\", 0.017453292519943295], AUTHORITY[\"EPSG\",\"4326\"]], " +
        		"PROJECTION[\"Mercator (1SP)\", " +
        		"AUTHORITY[\"EPSG\",\"9804\"]], " +
        		"PARAMETER[\"semi_major\", 6378137.0], " +
        		"PARAMETER[\"semi_minor\", 6378137.0], " +
        		"PARAMETER[\"latitude_of_origin\", 0.0], " +
        		"PARAMETER[\"central_meridian\", 0.0], " +
        		"PARAMETER[\"scale_factor\", 1.0], " +
        		"PARAMETER[\"false_easting\", 0.0], " +
        		"PARAMETER[\"false_northing\", 0.0], " +
        		"UNIT[\"m\", 1.0],  " +
        		"AUTHORITY[\"EPSG\",\"900913\"]]";
        CoordinateReferenceSystem epsg900913 = CRS.parseWKT(wkt900913);
        
        MathTransform t1 = CRS.findMathTransform(epsg4326, epsg3785); 
        MathTransform t2 = CRS.findMathTransform(epsg4326, epsg900913);

        // check the two equate each other, we know the above 900913 definition works
        double[][] points = new double[][] {{0,0}, {30.0, 30.0}, {-45.0, 45.0}, {-20, -20}, 
                {80,-80}, {85, 180}, {-85, -180}};
        double[][] points2 = new double[points.length][2];
        double[] tp1 = new double[2];
        double[] tp2= new double[2];
        for (double[] point : points) {
            t1.transform(point, 0, tp1, 0, 1);
            t2.transform(point, 0, tp2, 0, 1);
            assertEquals(tp1[0], tp2[0], EPS);
            assertEquals(tp1[1], tp2[1], EPS);
            // check inverse as well
            t1.inverse().transform(tp1, 0, tp1, 0, 1);
            t2.inverse().transform(tp2, 0, tp2, 0, 1);
            assertEquals(point[0], tp2[0], EPS);
            assertEquals(point[1], tp2[1], EPS);
        }
        
    }
}
