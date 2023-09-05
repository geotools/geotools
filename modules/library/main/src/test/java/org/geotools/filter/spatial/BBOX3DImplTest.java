package org.geotools.filter.spatial;

import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.geometry.BoundingBox3D;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.junit.Assert;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * A test for the 3D BBOX Filter.
 *
 * @author Niels Charlier
 */
public class BBOX3DImplTest {

    @org.junit.Test
    public void testBbox3D() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());

        SimpleFeatureType type = null;
        try {
            type = DataUtilities.createType("testSchema", "name:String,*geom:Geometry");
        } catch (SchemaException e) {
            // TODO Auto-generated catch block
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        Feature f1 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {"testFeature1", gf.createPoint(new Coordinate(10, 20, 30))},
                        null);
        Feature f2 =
                SimpleFeatureBuilder.build(
                        type,
                        new Object[] {"testFeature2", gf.createPoint(new Coordinate(10, 10, 60))},
                        null);

        BoundingBox3D envelope1 = new ReferencedEnvelope3D(0, 50, 0, 50, 0, 50, null);
        Filter bbox1 = ff.bbox(ff.property("geom"), envelope1);
        BoundingBox3D envelope2 = new ReferencedEnvelope3D(0, 50, 0, 50, 50, 100, null);
        Filter bbox2 = ff.bbox(ff.property("geom"), envelope2);

        Assert.assertTrue(bbox1.evaluate(f1));
        Assert.assertFalse(bbox1.evaluate(f2));
        Assert.assertFalse(bbox2.evaluate(f1));
        Assert.assertTrue(bbox2.evaluate(f2));
    }
}
