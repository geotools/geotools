package org.geotools.filter.spatial;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.BoundingBox3D;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * A test for the 3D BBOX Filter.
 *
 * @source $URL$
 * @author Niels Charlier
 */
public class BBOX3DImplTest extends TestCase {

    public static Test suite() {
        return new TestSuite(BBOX3DImplTest.class);
    }

    public void testBbox3D() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
                
        ;
        SimpleFeatureType type = null;
        try {
            type = DataUtilities.createType("testSchema", "name:String,*geom:Geometry");
        } catch (SchemaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
       
        
        Feature f1 = SimpleFeatureBuilder.build(type, new Object[] { "testFeature1", gf.createPoint(new Coordinate(10, 20, 30)) }, null);
        Feature f2 = SimpleFeatureBuilder.build(type, new Object[] { "testFeature2", gf.createPoint(new Coordinate(10, 10, 60)) }, null);
        
        BoundingBox3D envelope1 = new ReferencedEnvelope3D( 0, 50, 0, 50, 0, 50, null);
        Filter bbox1 = ff.bbox(ff.property("geom"), envelope1);
        BoundingBox3D envelope2 = new ReferencedEnvelope3D( 0, 50, 0, 50, 50, 100, null);
        Filter bbox2 = ff.bbox(ff.property("geom"), envelope2);
        
        assertTrue(bbox1.evaluate(f1));
        assertFalse(bbox1.evaluate(f2));
        assertFalse(bbox2.evaluate(f1));
        assertTrue(bbox2.evaluate(f2));
    }

}
