package org.geotools.process.feature.gs;

import static org.junit.Assert.*;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class FeatureProcessTest {

    @Test
    public void testFeatureCollection() throws Exception {
        Geometry poly = new WKTReader().read("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))");
        CoordinateReferenceSystem utm32n = CRS.decode("EPSG:32632");
        poly.setUserData(utm32n);
        
        SimpleFeatureCollection fc = new FeatureProcess().execute(poly, null, "testft");
        assertNotNull(fc);
        assertEquals(1, fc.size());
        SimpleFeatureType schema = fc.getSchema();
        assertEquals("testft", schema.getTypeName());
        assertEquals(1, schema.getAttributeCount());
        GeometryDescriptor ad = (GeometryDescriptor) schema.getDescriptor("geom");
        assertEquals(Polygon.class, ad.getType().getBinding());
        assertEquals(utm32n, ad.getCoordinateReferenceSystem());
        SimpleFeature sf = fc.features().next();
        assertEquals(poly, sf.getDefaultGeometry());
    }
}
