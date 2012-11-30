package org.geotools.geometry.jts;

import static org.junit.Assert.*;

import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.geometry.MismatchedReferenceSystemException;

import com.vividsolutions.jts.geom.Coordinate;
import java.awt.geom.Rectangle2D;

/**
 * 
 *
 * @source $URL$
 */
public class ReferencedEnvelopeTest {

    @Test
    public void testEverything() {
        ReferencedEnvelope everything = ReferencedEnvelope.EVERYTHING;
        ReferencedEnvelope world = new ReferencedEnvelope( ReferencedEnvelope.EVERYTHING );
        
        assertSame( everything, ReferencedEnvelope.EVERYTHING );
        assertNotSame( everything, world );
        assertEquals( everything, world );
        assertEquals( world, everything );
        
        assertFalse( "This is not an empty envelope", everything.isEmpty() );
        assertTrue( "This is a null envelope", everything.isNull() );        
        
        Coordinate center = everything.centre();
        assertNotNull( center );
        
        double area = everything.getArea();
        assertTrue( "area="+area, Double.isInfinite( area ) );
        
        area = world.getArea();
        assertTrue( "area="+area, Double.isInfinite( area ) );
        
        try {
            everything.setBounds( new ReferencedEnvelope() );
            fail("Expected IllegalStateException");
        }
        catch( IllegalStateException expected ){
            // ignore
        }
        everything.setToNull();
        everything.translate(1.0, 1.0);
        
        assertEquals( everything, world );
        assertEquals( world, everything );     
        
        assertEquals( world.getMaximum(0), everything.getMaximum(0),0.0);
        assertEquals( world.getMaximum(1), everything.getMaximum(1),0.0);
        
        assertEquals( world.getMinimum(0), everything.getMinimum(0),0.0);
        assertEquals( world.getMinimum(1), everything.getMinimum(1),0.0);
        
        assertEquals( world.getMedian(0), everything.getMedian(0),0.0);
        assertEquals( world.getMedian(1), everything.getMedian(0),0.0);
    }
    
    @Test
    public void intersection() throws Exception {
        ReferencedEnvelope australia = new ReferencedEnvelope( DefaultGeographicCRS.WGS84 );
        australia.include( 40, 110);
        australia.include( 10, 150);
        
        ReferencedEnvelope newZealand = new ReferencedEnvelope( DefaultEngineeringCRS.CARTESIAN_2D );        
        newZealand.include( 50, 165);
        newZealand.include( 33, 180);
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
        ReferencedEnvelope australia = new ReferencedEnvelope( DefaultGeographicCRS.WGS84 );
        australia.include( 40, 110);
        australia.include( 10, 150);
        
        ReferencedEnvelope newZealand = new ReferencedEnvelope( DefaultEngineeringCRS.CARTESIAN_2D );        
        newZealand.include( 50, 165);
        newZealand.include( 33, 180);
        
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
        ReferencedEnvelope bbox = new ReferencedEnvelope(); // this is empty
        assertNull(bbox.getCoordinateReferenceSystem());

        ReferencedEnvelope australia = new ReferencedEnvelope(DefaultGeographicCRS.WGS84);
        australia.include(40, 110);
        australia.include(10, 150);

        bbox.include(australia);
        
        assertEquals( australia.getCoordinateReferenceSystem(), bbox.getCoordinateReferenceSystem() );
  
    }

    @Test
    public void testBoundsEquals2D() {
        Rectangle2D bounds = new Rectangle2D.Double(-20.0, -20.0, 40.0, 40.0);

        ReferencedEnvelope env1 = new ReferencedEnvelope(bounds, null);
        ReferencedEnvelope env2 = new ReferencedEnvelope(bounds, null);
        double eps = 1.0e-4d;
        assertTrue(env1.boundsEquals2D(env2, eps));

        bounds = new Rectangle2D.Double(-20.01, -20.01, 40.0, 40.0);
        env2 = new ReferencedEnvelope(bounds, null);

        assertFalse(env1.boundsEquals2D(env2, eps));

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

        ReferencedEnvelope bounds = new ReferencedEnvelope(130.875825803896, 130.898939990319,
                -16.4491956225999, -16.4338185791628, DefaultGeographicCRS.WGS84 );
                
        ReferencedEnvelope worldBounds2D = bounds.transform( DefaultGeographicCRS.WGS84, true );
        assertEquals( DefaultGeographicCRS.WGS84, worldBounds2D.getCoordinateReferenceSystem() );
        
        ReferencedEnvelope worldBounds3D = bounds.transform( DefaultGeographicCRS.WGS84_3D, true );
        assertEquals( DefaultGeographicCRS.WGS84_3D, worldBounds3D.getCoordinateReferenceSystem() );

        ReferencedEnvelope gda94Bounds3D = bounds.transform( gda94, true );
        assertEquals( gda94, gda94Bounds3D.getCoordinateReferenceSystem() );
        
        
    }
}
