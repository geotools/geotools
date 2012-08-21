package org.geotools.data.transform;

import static junit.framework.Assert.*;

import java.io.File;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.CRS;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

public class CRSEvaluatorTest {
    
    static SimpleFeatureType STATES_SCHEMA;
    private static CoordinateReferenceSystem WGS84;
    WKTReader wkt = new WKTReader();
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    
    @BeforeClass
    public static void beforeClass() throws Exception {
        PropertyDataStore pds = new PropertyDataStore(new File(
                "./src/test/resources/org/geotools/data/transform"));
        STATES_SCHEMA = pds.getFeatureSource("states").getSchema();
        pds.dispose();
        
        WGS84 = CRS.decode("EPSG:4326");
    }
    
    @Test
    public void testLiteralNull() throws Exception {
        Geometry g = wkt.read("POINT(0 0)");
        Literal literal = ff.literal(g);
        
        CRSEvaluator evaluator = new CRSEvaluator(null);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) literal.accept(evaluator, null);
        assertNull(crs);
    }

    @Test
    public void testLiteralSRID() throws Exception {
        Geometry g = wkt.read("POINT(0 0)");
        g.setSRID(4326);
        Literal literal = ff.literal(g);
        
        CRSEvaluator evaluator = new CRSEvaluator(null);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) literal.accept(evaluator, null);
        assertEquals(CRS.decode("EPSG:4326"), crs);
    }
    
    @Test
    public void testLiteralCRS() throws Exception {
        Geometry g = wkt.read("POINT(0 0)");
        g.setUserData(WGS84);
        Literal literal = ff.literal(g);
        
        CRSEvaluator evaluator = new CRSEvaluator(null);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) literal.accept(evaluator, null);
        assertSame(WGS84, crs);
    }
    
    @Test
    public void testAttribute() throws Exception {
        PropertyName pn = ff.property("the_geom");
        CRSEvaluator evaluator = new CRSEvaluator(STATES_SCHEMA);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) pn.accept(evaluator, null);
        assertEquals(WGS84, crs);
    }
    
    @Test
    public void testFunctionLiteral() throws Exception {
        Geometry g = wkt.read("POINT(0 0)");
        g.setUserData(WGS84);
        Literal literal = ff.literal(g);
        Function buffer = ff.function("buffer", literal, ff.literal(10));
        
        CRSEvaluator evaluator = new CRSEvaluator(null);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) buffer.accept(evaluator, null);
        assertSame(WGS84, crs);
    }
    
    @Test
    public void testFunctionAttribute() throws Exception {
        Geometry g = wkt.read("POINT(0 0)");
        g.setUserData(WGS84);
        PropertyName pn = ff.property("the_geom");
        Function buffer = ff.function("buffer", pn, ff.literal(10));
        
        CRSEvaluator evaluator = new CRSEvaluator(STATES_SCHEMA);
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) buffer.accept(evaluator, null);
        assertSame(WGS84, crs);
    }
    
    
}
