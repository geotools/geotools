package org.geotools.filter;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.GeometryTransformationVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Function;

public class GeometryFilterVisitorTest extends TestCase {
    
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    public void testSimpleBuffer() {
        org.opengis.filter.expression.Expression geomTx = ff.function("buffer", ff.property("the_geom"), ff.literal(2));
        
        ReferencedEnvelope re = new ReferencedEnvelope(0, 2, 0, 2, null);
        
        GeometryTransformationVisitor visitor = new GeometryTransformationVisitor();
        ReferencedEnvelope result = (ReferencedEnvelope) geomTx.accept(visitor, re);
        
        ReferencedEnvelope expected = new ReferencedEnvelope(-2, 4, -2, 4, null);
        assertEquals(expected, result);
    }
    
    public void testChainBuffer() {
        // check buffer chaining
        Function innerBuffer = ff.function("buffer", ff.property("the_geom"), ff.literal(3));
        Function geomTx = ff.function("buffer", innerBuffer, ff.literal(2));
        
        ReferencedEnvelope re = new ReferencedEnvelope(0, 2, 0, 2, null);
        
        GeometryTransformationVisitor visitor = new GeometryTransformationVisitor();
        ReferencedEnvelope result = (ReferencedEnvelope) geomTx.accept(visitor, re);
        
        ReferencedEnvelope expected = new ReferencedEnvelope(-5, 7, -5, 7, null);
        assertEquals(expected, result);
    }
    
    public void testChainIntersection() {
        Function innerBuffer1 = ff.function("buffer", ff.property("the_geom"), ff.literal(3));
        Function innerBuffer2 = ff.function("buffer", ff.property("other_geom"), ff.literal(2));
        Function geomTx = ff.function("intersection", innerBuffer1, innerBuffer2);
        
        ReferencedEnvelope re = new ReferencedEnvelope(0, 2, 0, 2, null);
        
        GeometryTransformationVisitor visitor = new GeometryTransformationVisitor();
        ReferencedEnvelope result = (ReferencedEnvelope) geomTx.accept(visitor, re);
        
        ReferencedEnvelope expected = new ReferencedEnvelope(-3, 5, -3, 5, null);
        assertEquals(expected, result);
        
    }
}
