package org.geotools.geometry.jts;

import static org.junit.Assert.*;

import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.geometry.MismatchedReferenceSystemException;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Test for ReferencedEnvelope3D
 * 
 * @author Niels Charlier
 * @source $URL$
 */
public class ReferencedEnvelope3DTest {

    @Test
    public void testEverything() {
        ReferencedEnvelope3D everything = ReferencedEnvelope3D.EVERYTHING;
        ReferencedEnvelope3D world = new ReferencedEnvelope3D( ReferencedEnvelope3D.EVERYTHING );
        assertEquals(world.getDimension(), 3);
        
        assertSame( everything, ReferencedEnvelope3D.EVERYTHING );
        assertNotSame( everything, world );
        assertEquals( everything, world );
        assertEquals( world, everything );
        
        assertFalse( "This is not an empty 3d envelope", everything.isEmpty() );
        assertTrue( "This is a null 3d envelope", everything.isNull() );        
        
        Coordinate center = everything.centre();
        assertNotNull( center );
        
        double volume = everything.getVolume();
        assertTrue( "volume="+volume, Double.isInfinite( volume ) );
        
        volume = world.getVolume();
        assertTrue( "volume="+volume, Double.isInfinite( volume ) );
        
        double area = everything.getArea();
        assertTrue( "area="+area, Double.isInfinite( area ) );
        
        area = world.getArea();
        assertTrue( "area="+area, Double.isInfinite( area ) );
        
        try {
            everything.setBounds( new ReferencedEnvelope3D() );
            fail("Expected IllegalStateException");
        }
        catch( IllegalStateException expected ){
            // ignore
        }
        everything.setToNull();
        everything.translate(1.0, 1.0, 1.0);
        
        assertEquals( everything, world );
        assertEquals( world, everything );     
        
        assertEquals( world.getMaximum(0), everything.getMaximum(0),0.0);
        assertEquals( world.getMaximum(1), everything.getMaximum(1),0.0);
        assertEquals( world.getMaximum(2), everything.getMaximum(2),0.0);
        
        assertEquals( world.getMinimum(0), everything.getMinimum(0),0.0);
        assertEquals( world.getMinimum(1), everything.getMinimum(1),0.0);
        assertEquals( world.getMinimum(2), everything.getMinimum(2),0.0);
        
        assertEquals( world.getMedian(0), everything.getMedian(0),0.0);
        assertEquals( world.getMedian(1), everything.getMedian(1),0.0);
        assertEquals( world.getMedian(2), everything.getMedian(2),0.0);
    }
    
    @Test
    public void intersection() throws Exception {
        ReferencedEnvelope3D australia = new ReferencedEnvelope3D( DefaultGeographicCRS.WGS84_3D );
        australia.include( 40, 110, 0);
        australia.include( 10, 150, 10);
        
        ReferencedEnvelope3D newZealand = new ReferencedEnvelope3D( DefaultEngineeringCRS.CARTESIAN_3D );        
        newZealand.include( 50, 165, 0);
        newZealand.include( 33, 180, 5);
        try {
            australia.intersection(newZealand);
            fail( "Expected a missmatch of CoordianteReferenceSystem");
        }
        catch (MismatchedReferenceSystemException t){
            // expected
        }
    }
    @Test
    public void include() throws Exception {
        ReferencedEnvelope3D australia = new ReferencedEnvelope3D( DefaultGeographicCRS.WGS84_3D );
        australia.include( 40, 110, 0);
        australia.include( 10, 150, 10);
        
        ReferencedEnvelope3D newZealand = new ReferencedEnvelope3D( DefaultEngineeringCRS.CARTESIAN_3D );        
        newZealand.include( 50, 165, 0);
        newZealand.include( 33, 180, 5);
        
        try {
            australia.expandToInclude( newZealand);
            fail( "Expected a missmatch of CoordianteReferenceSystem");
        }
        catch (MismatchedReferenceSystemException t){
            // expected
        }
        try {
            australia.include( newZealand);
            fail( "Expected a missmatch of CoordianteReferenceSystem");
        }
        catch (MismatchedReferenceSystemException t){
            // expected
        }
    }
    
    @Test
    public void empty() {
        // ensure empty can grab a default CRS when starting from nothing
        ReferencedEnvelope3D bbox = new ReferencedEnvelope3D(); // this is empty
        assertNull(bbox.getCoordinateReferenceSystem());

        ReferencedEnvelope3D australia = new ReferencedEnvelope3D(DefaultGeographicCRS.WGS84_3D);
        australia.include(40, 110, 0);
        australia.include(10, 150, 10);

        bbox.include(australia);
        
        assertEquals( australia.getCoordinateReferenceSystem(), bbox.getCoordinateReferenceSystem() );
  
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
        
        ReferencedEnvelope worldBounds3D = bounds.transform( DefaultGeographicCRS.WGS84_3D, true );
        assertEquals( DefaultGeographicCRS.WGS84_3D, worldBounds3D.getCoordinateReferenceSystem() );
        
        ReferencedEnvelope worldBounds2D = bounds.transform( DefaultGeographicCRS.WGS84, true );
        assertEquals( DefaultGeographicCRS.WGS84, worldBounds2D.getCoordinateReferenceSystem() );
    }
}
