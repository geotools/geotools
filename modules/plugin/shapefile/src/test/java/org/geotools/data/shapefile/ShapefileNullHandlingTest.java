package org.geotools.data.shapefile;

import java.io.File;
import java.io.IOException;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class ShapefileNullHandlingTest extends TestCaseSupport {
	
	SimpleFeatureType schema;
	SimpleFeatureCollection collection;
	private SimpleFeature[] features;
	
	public ShapefileNullHandlingTest(String name) throws IOException {
		super(name);
	}


	@Before
	protected void setUp() throws Exception {
		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.add("geom", Point.class, 4326);
		tb.add("name", String.class);
		tb.setName("testnulls");
		schema = tb.buildFeatureType();
		
		GeometryFactory gf = new GeometryFactory();
		
		features = new SimpleFeature[4];
		features[0] =  SimpleFeatureBuilder.build(schema, new Object[] {null, "zero"} , "1");
		features[1] =  SimpleFeatureBuilder.build(schema, new Object[] {gf.createPoint(new Coordinate(0, 10)), "one"} , "2");
		features[2] =  SimpleFeatureBuilder.build(schema, new Object[] {null, "two"} , "3");
		features[3] =  SimpleFeatureBuilder.build(schema, new Object[] {gf.createPoint(new Coordinate(10, 10)), null} , "4");

		collection = DataUtilities.collection(features);
	}
	
	@Test
	public void testWriteNulls() throws Exception {
		File tempShape = getTempFile();
		ShapefileDataStore store = new ShapefileDataStore(tempShape.toURI().toURL());
		store.createSchema(schema);
		
		// write out the features
		FeatureStore fs = (FeatureStore) store.getFeatureSource();
		fs.addFeatures(collection);

		// read it back
	    SimpleFeature[] readfc =  (SimpleFeature[]) fs.getFeatures().toArray(new SimpleFeature[3]);
	    
	    // check the first geometry
	    Geometry read = (Geometry) features[0].getDefaultGeometry();
        assertNull(read);
	    
	    Geometry orig = (Geometry) features[1].getDefaultGeometry();
	    read = (Geometry) features[1].getDefaultGeometry();
		assertTrue(orig.equals(read));
		
		// check the null geometry
		read = (Geometry) features[2].getDefaultGeometry();
		assertNull(read);
		
		// make sure the third is ok as well
		orig = (Geometry) features[3].getDefaultGeometry();
	    read = (Geometry) features[3].getDefaultGeometry();
		assertTrue(orig.equals(read));
		
		store.dispose();
	}
	

}
