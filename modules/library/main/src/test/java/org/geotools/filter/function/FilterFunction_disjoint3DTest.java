package org.geotools.filter.function;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.FilterFactoryImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Unit tests for FilterFunction_disjoint3D
 * 
 * @author Martin Davis
 */
public class FilterFunction_disjoint3DTest extends TestCase{

    public void testIntersects() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();
                
        SimpleFeatureType type = null;
        try {
            type = DataUtilities.createType("testSchema", "name:String,*geom:Geometry");
        } catch (SchemaException e) {
            e.printStackTrace();
        }        
       
        WKTReader reader = new WKTReader();
        Geometry geom1 = reader.read("LINESTRING(0 0 0, 10 10 10)");
        Feature f = SimpleFeatureBuilder.build(type, new Object[] { "testFeature1", geom1 }, null);
        
        Geometry geom2 = reader.read("LINESTRING(10 0 0, 0 10 10)");
        Literal literal_geom = ff.literal(geom2);

        Function exp = ff.function("disjoint3D", ff.property("geom"), literal_geom);
        Object value = exp.evaluate(f);
        assertTrue(value instanceof Boolean);
        assertTrue(! (Boolean) value);
    }
}