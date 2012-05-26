package org.geotools.filter.visitor;

import static org.junit.Assert.*;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;


public class SpatialFilterVisitorTest {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    private SpatialFilterVisitor visitor;
    
    
    @Before
    public void setUp() throws Exception {
        visitor = new SpatialFilterVisitor();
    }

    @Test
    public void testInclude() {
        Filter.INCLUDE.accept(visitor, null);
        assertFalse(visitor.hasSpatialFilter);
    }
    
    @Test
    public void testExclude() {
        Filter.EXCLUDE.accept(visitor, null);
        assertFalse(visitor.hasSpatialFilter);
    }
    
    @Test
    public void testBBOX() {
        ff.bbox("geom", 0, 0, 10, 10, "EPSG:4326").accept(visitor, null);
        assertTrue(visitor.hasSpatialFilter);
    }
    
    @Test
    public void testIntersects() {
        ff.intersects(ff.property("geom"), ff.literal(null)).accept(visitor, null);
        assertTrue(visitor.hasSpatialFilter);
    }
    
    @Test
    public void testOverlaps() {
        ff.overlaps(ff.property("geom"), ff.literal(null)).accept(visitor, null);
        assertTrue(visitor.hasSpatialFilter);
    }
    
}
