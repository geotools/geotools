package org.geotools.geometry.jts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.junit.Test;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;

public class DecimatorTest {

    GeometryFactory gf = new GeometryFactory();
    LiteCoordinateSequenceFactory csf = new LiteCoordinateSequenceFactory();

    /**
     * http://jira.codehaus.org/browse/GEOT-1923
     */
    @Test
    public void testDecimateRing() {
        // a long rectangle made of 5 coordinates
        LinearRing g = gf.createLinearRing(csf.create(new double[] {0,0,0,10,2,10,2,0,0,0}));
        assertTrue(g.isValid());
        
        Decimator d = new Decimator(4, 4);
        d.decimate(g);
        g.geometryChanged();
        assertTrue(g.isValid());
        assertEquals(4, g.getCoordinateSequence().size());
    }
    
    /**
     * http://jira.codehaus.org/browse/GEOT-2937
     */
    @Test
    public void testDecimatePseudoRing() {
        // a long rectangle made of 3 coordinates
        LineString g = gf.createLineString(csf.create(new double[] {0,0,0,10,0,0}));
        assertTrue(g.isValid());
        
        Decimator d = new Decimator(4, 4);
        d.decimate(g);
        g.geometryChanged();
        assertTrue(g.isValid());
        assertEquals(3, g.getCoordinateSequence().size());
    }
    
    @Test
    public void testDecimateOpenTriangle() throws Exception {
        LineString g = gf.createLineString(csf.create(new double[] {0,0,0,2,2,2,0,0}));
        assertTrue(g.isValid());
        
        Decimator d = new Decimator(3, 3);
        d.decimateTransformGeneralize(g, new AffineTransform2D(new AffineTransform()));
        g.geometryChanged();
        assertTrue(g.isValid());
        assertEquals(4, g.getCoordinateSequence().size());
    }
    
    /**
     * http://jira.codehaus.org/browse/GEOT-1923
     */
    @Test
    public void testDecimateRingEnvelope() {
        // acute triangle
        LinearRing g = gf.createLinearRing(csf.create(new double[] {0,0,0,10,2,10,2,0,0,0}));
        assertTrue(g.isValid());
        
        Decimator d = new Decimator(20, 20);
        d.decimate(g);
        g.geometryChanged();
        assertTrue(g.isValid());
        assertEquals(4, g.getCoordinateSequence().size());
    }
    
    @Test
    public void testNoDecimation() {
        // acute triangle
        LinearRing g = gf.createLinearRing(csf.create(new double[] {0,0,0,10,2,10,2,0,0,0}));
        LinearRing original = (LinearRing) g.clone();
        assertTrue(g.isValid());
        
        Decimator d = new Decimator(-1, -1);
        d.decimate(g);
        g.geometryChanged();
        assertTrue(g.isValid());
        assertTrue(original.equals(g));
    }
    
    @Test
    public void testDistance() throws Exception {
        LineString ls = gf.createLineString(csf.create(new double[] {0,0,1,1,2,2,3,3,4,4,5,5}));
        
        MathTransform identity = new AffineTransform2D(new AffineTransform());
        
        Decimator d = new Decimator(identity, new Rectangle(0,0,5,5), 0.8);
        d.decimateTransformGeneralize((Geometry) ls.clone(), identity);
        assertEquals(6, ls.getNumPoints());
        
        d = new Decimator(identity, new Rectangle(0,0,5,5), 1);
        d.decimateTransformGeneralize(ls, identity);
        assertEquals(4, ls.getNumPoints());
        
        d = new Decimator(identity, new Rectangle(0,0,5,5), 6);
        d.decimateTransformGeneralize(ls, identity);
        assertEquals(2, ls.getNumPoints());
    }
    
    @Test
    public void testDecimate3DPoint() throws Exception {
        Point p = gf.createPoint(csf.create(new double[] {0,1,2}, 3));
        
        MathTransform identity = new AffineTransform2D(new AffineTransform());
        
        Decimator d = new Decimator(identity, new Rectangle(0,0,5,5), 0.8);
        d.decimateTransformGeneralize(p, identity);
        assertEquals(1, p.getNumPoints());
        assertEquals(2, p.getCoordinateSequence().getDimension());
    }
    
    @Test
    public void testDecimate3DLine() throws Exception {
    	LineString ls = gf.createLineString(csf.create(new double[] {0,0,1, 1,2,1, 3,3,4 ,4,5,5}, 3));
    	assertEquals(4, ls.getNumPoints());
        
        MathTransform identity = new AffineTransform2D(new AffineTransform());
        
        Decimator d = new Decimator(identity, new Rectangle(0,0,5,5), 0.8);
        d.decimateTransformGeneralize(ls, identity);
        assertEquals(4, ls.getNumPoints());
        assertEquals(2, ls.getCoordinateSequence().getDimension());
    }
}
