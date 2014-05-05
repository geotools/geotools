package org.geotools.filter.spatial;

import static org.junit.Assert.*;

import java.util.ArrayList;


import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * 
 *
 * @source $URL$
 */
public class BBOXImplTest {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    @Test
    public void testBbox() {
        
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        Coordinate coords[] = new Coordinate[6];
        coords[0] = new Coordinate(0, 1.5);
        coords[1] = new Coordinate(0, 2.5);
        coords[2] = new Coordinate(1.5, 3);
        coords[3] = new Coordinate(4, 2.5);
        coords[4] = new Coordinate(0.5, 1);
        coords[5] = coords[0];
        Polygon p = gf.createPolygon(gf.createLinearRing(coords), null);
        SimpleFeatureType type = null;
        try {
            type = DataUtilities.createType("testSchema", "name:String,*geom:Geometry");
        } catch (SchemaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ArrayList<Object> attributes = new ArrayList<Object>();
        attributes.add("testFeature");
        attributes.add(p);
        Feature f = null;
        f = SimpleFeatureBuilder.build(type, new Object[] { "testFeature", p }, null);
        Envelope e1 = new Envelope(3, 6, 0, 2);
        Envelope e2 = new Envelope(3.25, 3.75, 1.25, 1.75);
        assertTrue(e1.contains(e2));
        assertTrue(p.getEnvelopeInternal().contains(e2));
        assertTrue(p.getEnvelopeInternal().intersects(e1));
        BBOXImpl bbox1 = (BBOXImpl) ff.bbox(ff.property("geom"), e1.getMinX(),
                e1.getMinY(), e1.getMaxX(), e1.getMaxY(), "");
        BBOXImpl bbox2 = (BBOXImpl) ff.bbox(ff.property("geom"), e2.getMinX(),
                e2.getMinY(), e2.getMaxX(), e2.getMaxY(), "");
        assertFalse(bbox2.evaluate(f));
        assertFalse(bbox1.evaluate(f));
    }
    
    @Test
    public void testPreserveOriginalSRS() throws NoSuchAuthorityCodeException, FactoryException {
        String srs = "AUTO:42004,9001,0,33";
        CoordinateReferenceSystem crs = CRS.decode(srs);
        BBOX bbox = ff.bbox(ff.property(""), 0, 1000, 2000, 3000, srs);
        Geometry geom = bbox.getExpression2().evaluate(null, Geometry.class);
        assertEquals(crs, geom.getUserData());
        assertEquals(srs, bbox.getSRS());
    }

}
