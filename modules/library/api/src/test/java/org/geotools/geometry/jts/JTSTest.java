/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.DirectPosition;

import com.vividsolutions.jts.geom.Envelope;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import static org.junit.Assert.*;

import org.opengis.referencing.operation.MathTransform;

/**
 * Unit tests for the JTS utility class.
 * 
 * @author Michael Bedward
 *
 *
 * @source $URL$
 * @version $Id$
 * @since 2.8
 */
public class JTSTest extends JTSTestBase {

    @Test
    public void toGeometry_Shape_Poly() {
        Shape shape = new Polygon(XPOINTS, YPOINTS, NPOINTS);
        Geometry geom = JTS.toGeometry(shape);
        assertTrue(geom instanceof LinearRing);

        Coordinate[] coords = geom.getCoordinates();
        assertEquals(NPOINTS + 1, coords.length);

        CoordList list = new CoordList(coords);
        Coordinate c = new Coordinate();
        for (int i = 0; i < NPOINTS; i++) {
            c.x = XPOINTS[i];
            c.y = YPOINTS[i];
            assertTrue(list.contains(c));
        }
    }

    @Test
    public void toGeometry_Shape_Line() {
        GeneralPath path = new GeneralPath();
        
        path.moveTo(XPOINTS[0], YPOINTS[0]);
        for (int i = 1; i < NPOINTS; i++) {
            path.lineTo(XPOINTS[i], YPOINTS[i]);
        }
                
        Geometry geom = JTS.toGeometry(path);
        assertTrue(geom instanceof LineString);

        Coordinate[] coords = geom.getCoordinates();
        assertEquals(NPOINTS, coords.length);

        CoordList list = new CoordList(coords);
        Coordinate c = new Coordinate();
        for (int i = 0; i < NPOINTS; i++) {
            c.x = XPOINTS[i];
            c.y = YPOINTS[i];
            assertTrue(list.contains(c));
        }
    }

    @Test
    public void getEnvelope2D() {
        ReferencedEnvelope refEnv = new ReferencedEnvelope(
                -10, 10, -5, 5, DefaultGeographicCRS.WGS84);
        
        Envelope2D env2D = JTS.getEnvelope2D(refEnv, refEnv.getCoordinateReferenceSystem());
        
        CRS.equalsIgnoreMetadata(
                refEnv.getCoordinateReferenceSystem(),
                env2D.getCoordinateReferenceSystem());
        
        assertTrue(env2D.boundsEquals(refEnv, 0, 1, TOL));
    }

    @Test
    public void toGeometry_Envelope() {
        Envelope refEnv = new Envelope(-10, 10, -5, 5);
        Geometry geom = JTS.toGeometry(refEnv);
        assertTrue(geom instanceof com.vividsolutions.jts.geom.Polygon);
        
        Envelope geomEnv = geom.getEnvelopeInternal();
        assertEquals(-10.0, geomEnv.getMinX(), TOL);
        assertEquals(10.0, geomEnv.getMaxX(), TOL);
        assertEquals(-5.0, geomEnv.getMinY(), TOL);
        assertEquals(5.0, geomEnv.getMaxY(), TOL);
    }
    
    @Test
    public void toGeometry_ReferencedEnvelope() {
        ReferencedEnvelope refEnv = new ReferencedEnvelope(-10, 10, -5, 5, DefaultGeographicCRS.WGS84);
        Geometry geom = JTS.toGeometry(refEnv);
        assertTrue(geom instanceof com.vividsolutions.jts.geom.Polygon);
        
        Envelope geomEnv = geom.getEnvelopeInternal();
        assertEquals(-10.0, geomEnv.getMinX(), TOL);
        assertEquals(10.0, geomEnv.getMaxX(), TOL);
        assertEquals(-5.0, geomEnv.getMinY(), TOL);
        assertEquals(5.0, geomEnv.getMaxY(), TOL);
    }


    @Test
    public void toEnvelope() {
        Coordinate[] coords = getPolyCoords();
        GeometryFactory gf = new GeometryFactory();
        Geometry geom = gf.createPolygon(gf.createLinearRing(coords), null);
        
        ReferencedEnvelope refEnv = JTS.toEnvelope(geom);
        assertTrue(geom.getEnvelopeInternal().equals(refEnv));
    }
    
    @Test
    public void toDirectPosition() {
        Coordinate c = new Coordinate(40,40);
        DirectPosition wrapper = JTS.toDirectPosition(c, DefaultGeographicCRS.WGS84 );
        
        GeneralDirectPosition expected = new GeneralDirectPosition( DefaultGeographicCRS.WGS84);
        expected.setOrdinate(0,40);
        expected.setOrdinate(1,40);
        
        assertEquals( expected, wrapper );
    }
    @Test
    public void toGeometry_BoundingBox() {
        BoundingBox bbox = new ReferencedEnvelope(-10, 10, -5, 5, null);
        Geometry geom = JTS.toGeometry(bbox);
        assertTrue(geom instanceof com.vividsolutions.jts.geom.Polygon);
        
        Envelope geomEnv = geom.getEnvelopeInternal();
        assertEquals(-10.0, geomEnv.getMinX(), TOL);
        assertEquals(10.0, geomEnv.getMaxX(), TOL);
        assertEquals(-5.0, geomEnv.getMinY(), TOL);
        assertEquals(5.0, geomEnv.getMaxY(), TOL);
    }
 
    
    /**
     * Added this test after a bug was reported in JTS.transform for converting
     * between WGS84 (2D) and DefaultGeocentric.CARTESIAN (3D).
     */
    @Test
    public void transformCoordinate2DCRSTo3D() throws Exception {
        CoordinateReferenceSystem srcCRS = DefaultGeographicCRS.WGS84;
        CoordinateReferenceSystem targetCRS = DefaultGeocentricCRS.CARTESIAN;
        MathTransform transform = CRS.findMathTransform(srcCRS, targetCRS);

        Coordinate srcCoord = new Coordinate(0, 0);
        Coordinate dest0 = JTS.transform(srcCoord, null, transform);

        srcCoord.x = 180;
        Coordinate dest180 = JTS.transform(srcCoord, null, transform);

        // Only a perfunctory check on the return values - mostly we
        // just wanted to make sure there was no exception
        assertEquals(dest0.x, -dest180.x, TOL);
        assertEquals(dest0.y, dest180.y, TOL);
        assertEquals(dest0.z, dest180.z, TOL);
    }
    
    @Test
    public void testTransformToWGS84() throws Exception {
        String wkt = "GEOGCS[\"GDA94\","
                + " DATUM[\"Geocentric Datum of Australia 1994\","
                + "  SPHEROID[\"GRS 1980\", 6378137.0, 298.257222101, AUTHORITY[\"EPSG\",\"7019\"]],"
                + "  TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], "
                + " AUTHORITY[\"EPSG\",\"6283\"]], "
                + " PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],"
                + " UNIT[\"degree\", 0.017453292519943295], "
                + " AXIS[\"Geodetic longitude\", EAST], " + " AXIS[\"Geodetic latitude\", NORTH], "
                + " AXIS[\"Ellipsoidal height\", UP], " + " AUTHORITY[\"EPSG\",\"4939\"]]";

        CoordinateReferenceSystem gda94 = CRS.parseWKT(wkt);
        ReferencedEnvelope bounds = new ReferencedEnvelope3D(130.875825803896, 130.898939990319,
                -16.4491956225999, -16.4338185791628, 0.0, 0.0, gda94 );

        ReferencedEnvelope worldBounds = JTS.toGeographic( bounds );
        assertEquals( DefaultGeographicCRS.WGS84, worldBounds.getCoordinateReferenceSystem() );
        
        Envelope envelope = new Envelope(130.875825803896, 130.898939990319,
                -16.4491956225999, -16.4338185791628);
        
        Envelope worldBounds2 = JTS.toGeographic( envelope, gda94 );
        if( worldBounds2 instanceof BoundingBox){
            assertEquals( DefaultGeographicCRS.WGS84, ((BoundingBox)worldBounds2).getCoordinateReferenceSystem() );
        }
    }
}
