/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.crs;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author Jody Garnett
 * @source $URL$
 */
public class CRSTest extends TestCase {
    /**
     * Makes sure that the transform between two EPSG:4326 is the identity transform.
     */
    public void testFindMathTransformIdentity() throws FactoryException {
        CoordinateReferenceSystem crs1default = CRS.decode("EPSG:4326",false);
        CoordinateReferenceSystem crs2default = CRS.decode("EPSG:4326",false);
        MathTransform tDefault = CRS.findMathTransform(crs1default, crs2default);
        assertTrue("WSG84 transformed to WSG84 should be Identity", tDefault.isIdentity());
        
        CoordinateReferenceSystem crs1force = CRS.decode("EPSG:4326",true);
        CoordinateReferenceSystem crs2force = CRS.decode("EPSG:4326",true);
        MathTransform tForce = CRS.findMathTransform(crs1force, crs2force);
        assertTrue("WSG84 transformed to WSG84 should be Identity", tForce.isIdentity());
    }
    
    public void testEPSG42102() throws Exception {
	    CoordinateReferenceSystem bc = CRS.decode("EPSG:42102");
		assertNotNull( "bc", bc );
	}
    
	public void testAUTO4200() throws Exception {	
	    CoordinateReferenceSystem utm = CRS.decode("AUTO:42001,0.0,0.0");
		assertNotNull( "auto-utm", utm );		
	}

    public void test4269() throws Exception {
        CoordinateReferenceSystem latlong = CRS.decode("EPSG:4269");        
        assertNotNull( "latlong", latlong );
        try {
            latlong = CRS.decode("4269");
            fail( "Shoudl not be able to decode 4269 without EPSG authority");
        } catch (NoSuchAuthorityCodeException e) {
            // expected
        }
        assertNotNull( "latlong", latlong );
    }
    
    public void testManditoryTranform() throws Exception {                
        CoordinateReferenceSystem WGS84 = (CoordinateReferenceSystem) CRS.decode("EPSG:4326"); // latlong
        CoordinateReferenceSystem NAD83 = (CoordinateReferenceSystem) CRS.decode("EPSG:4269");
        CoordinateReferenceSystem NAD83_UTM10 = (CoordinateReferenceSystem) CRS.decode("EPSG:26910");
        CoordinateReferenceSystem BC_ALBERS = (CoordinateReferenceSystem) CRS.decode("EPSG:42102");
                
        CoordinateOperation op = ReferencingFactoryFinder.getCoordinateOperationFactory(null).createOperation( WGS84, WGS84 );
        MathTransform math = op.getMathTransform();
                
        DirectPosition pt1 = new GeneralDirectPosition(0.0,0.0);        
        DirectPosition pt2 = math.transform( pt1, null );
        assertNotNull( pt2 );
          
        double pts[] = new double[] {
                1187128,395268, 1187128,396027,
                1188245,396027, 1188245,395268,
                1187128,395268};
        double tst[] = new double[ pts.length ];                        
        math.transform( pts, 0, new double[ pts.length ], 0, pts.length/2 );
        for( int i=0; i<pts.length;i++)
            assertTrue( "pts["+i+"]", pts[i] != tst[i] );
    }
    /**
     * Taken from empty udig map calculation of scale.
     * 
     * @throws Exception
     */
    public void testSamplePixel() throws Exception {
        Map map = new HashMap();
        //map.put( Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, true );
        //map.put( Hints.FORCE_STANDARD_AXIS_DIRECTIONS, true );
        //map.put( Hints.FORCE_STANDARD_AXIS_UNITS, true );

        Hints global = new Hints(map);
        GeoTools.init( global );

       // ReferencedEnvelope[-0.24291497975705742 : 0.24291497975711265, -0.5056179775280899 : -0.0]
        CoordinateReferenceSystem EPSG4326 = CRS.decode("EPSG:4326");
        ReferencedEnvelope pixelBounds = new ReferencedEnvelope( -0.24291497975705742, 0.24291497975711265, -0.5056179775280899, 0.0, EPSG4326 );
        CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        
        ReferencedEnvelope latLong = pixelBounds.transform( WGS84, true );  
        assertNotNull( latLong );
    }
    
    public void testReprojection() throws Exception{
        // origional bc alberts
        Polygon poly1 = poly( new double[] {
                1187128,395268, 1187128,396027,
                1188245,396027, 1188245,395268,
                1187128,395268} );

        // transformed     
        Polygon poly3 = poly( new double[]{
                -123.47009555832284,48.543261561072285,
                -123.46972894676578,48.55009592117936,
                -123.45463828850829,48.54973520267305,
                -123.4550070827961,48.54290089070186,
                -123.47009555832284,48.543261561072285
        });
        
        CoordinateReferenceSystem WGS84 = (CoordinateReferenceSystem) CRS.decode("EPSG:4326"); // latlong
        CoordinateReferenceSystem BC_ALBERS = (CoordinateReferenceSystem) CRS.decode("EPSG:42102");
        
        MathTransform transform = CRS.findMathTransform(BC_ALBERS, WGS84 );
        
        Polygon polyAfter = (Polygon) JTS.transform(poly1, transform);
        System.out.println( polyAfter );
        
        assertTrue( poly3.equals( polyAfter ));
        
        Envelope before = poly1.getEnvelopeInternal();
        Envelope expected = poly3.getEnvelopeInternal();
        
        Envelope after = JTS.transform( before, transform );
        assertEquals( expected, after );                 
    }
    public void testReprojectionDefault() throws Exception{
        // origional bc alberts
        Polygon poly1 = poly( new double[] {
                1187128,395268, 1187128,396027,
                1188245,396027, 1188245,395268,
                1187128,395268} );

        // transformed     
        Polygon poly3 = poly( new double[]{
                -123.47009555832284,48.543261561072285,
                -123.46972894676578,48.55009592117936,
                -123.45463828850829,48.54973520267305,
                -123.4550070827961,48.54290089070186,
                -123.47009555832284,48.543261561072285
        });
        
        CoordinateReferenceSystem WGS84 = DefaultGeographicCRS.WGS84;
        CoordinateReferenceSystem BC_ALBERS = (CoordinateReferenceSystem) CRS.decode("EPSG:42102");
        
        MathTransform transform = CRS.findMathTransform(BC_ALBERS, WGS84 );
        
        Polygon polyAfter = (Polygon) JTS.transform(poly1, transform);
        System.out.println( polyAfter );
        
        assertTrue( poly3.equals( polyAfter ));
        
        Envelope before = poly1.getEnvelopeInternal();
        Envelope expected = poly3.getEnvelopeInternal();
        
        Envelope after = JTS.transform( before, transform );
        assertEquals( expected, after );                 
    }
    public static GeometryFactory factory = new GeometryFactory();
    
    public static Polygon poly( double coords[] ) {
        return factory.createPolygon( ring( coords ), null );
    }
    public static LinearRing ring( double coords[] ) {
        return factory.createLinearRing( coords( coords ) );
    }
    public static CoordinateSequence coords( double coords[] ) {
        Coordinate array[] = new Coordinate[ coords.length/2 ];
        for( int i=0; i<array.length; i++ ) {
            array[i] = new Coordinate( coords[i*2], coords[i*2+1] );
        }
        return factory.getCoordinateSequenceFactory().create( array );
    }
    
}
