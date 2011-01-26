package org.geotools.data.crs;

import junit.framework.TestCase;

import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class ForceCoordinateFeatureResultsTest extends TestCase {
    
    private static final String FEATURE_TYPE_NAME = "testType";
    private MemoryDataStore store;
    private CoordinateReferenceSystem wgs84;
    private CoordinateReferenceSystem utm32n;

    protected void setUp() throws Exception {
        wgs84 = CRS.decode("EPSG:4326");
        utm32n = CRS.decode("EPSG:32632");
        
        GeometryFactory fac=new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10,10) );
        
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(FEATURE_TYPE_NAME);
        builder.setCRS(wgs84);
        builder.add("geom", Point.class );
        
        SimpleFeatureType ft = builder.buildFeatureType();
        
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(ft);
        b.add( p );
        
        SimpleFeature[] features=new SimpleFeature[]{
           b.buildFeature(null) 
        };
        
        store = new MemoryDataStore(features);
    }

    public void testSchema() throws Exception {
        SimpleFeatureCollection original = store.getFeatureSource(FEATURE_TYPE_NAME).getFeatures();
        assertEquals(wgs84, original.getSchema().getCoordinateReferenceSystem());
        
        SimpleFeatureCollection forced = new ForceCoordinateSystemFeatureResults(original, utm32n);
        assertEquals(utm32n, forced.getSchema().getCoordinateReferenceSystem());
    }
    
    public void testBounds() throws Exception {
        SimpleFeatureCollection original = store.getFeatureSource(FEATURE_TYPE_NAME).getFeatures();
        
        SimpleFeatureCollection forced = new ForceCoordinateSystemFeatureResults(original, utm32n);
        assertEquals(new ReferencedEnvelope(10,10,10,10, utm32n), forced.getBounds());
    }
}