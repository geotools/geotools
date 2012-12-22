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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Unit tests for {@link FilterFunction_isWithinDistance3D}
 * 
 * @author Martin Davis
 */
public class FilterFunction_isWithinDistance3DTest extends TestCase{

    public void testDistance3D() {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
                
        SimpleFeatureType type = null;
        try {
            type = DataUtilities.createType("testSchema", "name:String,*geom:Geometry");
        } catch (SchemaException e) {
            e.printStackTrace();
        }        
       
        Feature f = SimpleFeatureBuilder.build(type, new Object[] { "testFeature1", gf.createPoint(new Coordinate(10, 20, 30)) }, null);
        Literal literal_geom = ff.literal(gf.createPoint(new Coordinate(10, 30, 40)));
        Literal literal_num = ff.literal(15.0);

        Function exp = ff.function("isWithinDistance3D", ff.property("geom"), literal_geom, literal_num);
        Object value = exp.evaluate(f);
        assertTrue(value instanceof Boolean);
        assertTrue((Boolean) value);
    }
}