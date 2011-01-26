package org.geotools.caching.grid.featurecache;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.geotools.caching.featurecache.FeatureCache;
import org.geotools.caching.featurecache.FeatureCacheException;
import org.geotools.caching.grid.spatialindex.store.MemoryStorage;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class StreamingGridFeatureCacheTest extends TestCase {

	FilterFactory filterFactory = new FilterFactoryImpl();
	
	private DataStore rawDataset;
	private FeatureCache cacheDataset;
	private SimpleFeatureSource cacheFS;

	private int numFeatures = 100;
	
	protected void setUp() throws Exception {
		// first
		resetDatasets();
	}
  
    
	private void resetDatasets() throws FeatureCacheException, IOException {
		SimpleFeatureType type = createFeatureType();

		DefaultFeatureCollection dfc = new DefaultFeatureCollection(
				"mycollection", type);
		for (int i = 0; i < numFeatures; i++) {
			dfc.add(makeFeature(type, 0, 500));
		}

		rawDataset = new MemoryDataStore(dfc);

		// build up a cache
		// for testing purposes limit the cache
		// to 10 features.
		cacheFS = rawDataset.getFeatureSource("mycollection");
		ReferencedEnvelope bounds = new ReferencedEnvelope(0, 500, 0, 500, type
				.getCoordinateReferenceSystem());
		cacheDataset = new StreamingGridFeatureCache(cacheFS, bounds, 5000, 1000,
				MemoryStorage.createInstance());
//		cacheDataset = new GridFeatureCache(cacheFS, bounds, 5, 1000,
//				MemoryStorage.createInstance());
	}

	 public void testGet() throws Exception {
		double x_min = 0;
		double x_max = 100;
		double y_min = 0;
		double y_max = 100;

		resetDatasets();

		FilterFactory filterFactory = new FilterFactoryImpl();
		FeatureType ft = rawDataset.getFeatureSource("mycollection")
				.getSchema();
		String localname = ft.getGeometryDescriptor().getLocalName();
		String srs = ft.getGeometryDescriptor().getCoordinateReferenceSystem()
				.toString();

		Filter bb = filterFactory.bbox(localname, x_min, y_min, x_max, y_max,
				srs);

		int cnt = rawDataset.getFeatureSource("mycollection").getFeatures(bb)
				.size();
		assertEquals(numFeatures, rawDataset.getFeatureSource("mycollection")
				.getFeatures().size());

		assertEquals(cnt, cacheDataset.getFeatures(bb).size());

		// this time the features should be in the cache
		assertEquals(cnt, cacheDataset.getFeatures(bb).size());
	}
	 
	 /*
	  * This test is setup to test that
	  * the feature collection doesn't return duplicate
	  * features when a feature is cached
	  * but also returned as part of the
	  * feature source query. 
	  * 
	  */
	 public void testGetDuplicateFeatures() throws FeatureCacheException,
			IOException {

		/*
		 * Create a cache with a grid that looks like this
		 * 
		 * 
		 * x = feature in the cache
		 * 
		 * 10+------+-------+ | | | | xxxxxx | 5 +--x---+-------+ | x | | | | |
		 * 0 +------+-------+ 0 5 10
		 */

		//
		// ---- BUILD FEATURE -----
		//
		SimpleFeatureTypeBuilder bb = new SimpleFeatureTypeBuilder();
		bb.add("ID", Integer.class);
		bb.add("the_geom", Geometry.class, DefaultEngineeringCRS.CARTESIAN_2D);
		bb.setDefaultGeometry("the_geom");
		bb.setName("My Feature Type");

		// bb.setSRS(DefaultEngineeringCRS.CARTESIAN_2D.toString());
		SimpleFeatureType featureType = bb.buildFeatureType();

		SimpleFeatureBuilder fb = new SimpleFeatureBuilder(featureType);
		GeometryFactory gf = new GeometryFactory();

		LineString[] geoms = new LineString[2];

		int feature1[] = new int[] { 2, 4, 2, 8, 8, 8 };
		int feature2[] = new int[] { 0, 0, 0, 10, 10, 10, 10, 0, 0, 0 }; // bound feature to ensure the tiles are built as shown above

		Object featurescoords[] = new Object[] { feature1, feature2 };
		for (int i = 0; i < featurescoords.length; i++) {
			int[] values = (int[]) featurescoords[i];
			Coordinate c[] = new Coordinate[values.length / 2];
			for (int j = 0; j < values.length; j = j + 2) {
				int x = values[j];
				int y = values[j + 1];
				c[j / 2] = new Coordinate(x, y);
			}
			geoms[i] = gf.createLineString(c);
		}

		SimpleFeature features[] = new SimpleFeature[geoms.length];

		MemoryFeatureCollection mm = new MemoryFeatureCollection(featureType);
		for (int i = 0; i < geoms.length; i++) {
			features[i] = fb.buildFeature(i + "", new Object[] {
					new Integer(i), geoms[i] });
			mm.add(features[i]);
		}

		//
		// ---- CACHING FEATURE COLLECTION -----
		//
		DataStore ds = new MemoryDataStore(mm);
		SimpleFeatureSource fs = ds.getFeatureSource(featureType.getTypeName());
		FeatureCache cache = new StreamingGridFeatureCache(fs, 4, 4,MemoryStorage.createInstance());
		
		Filter upperLeft = filterFactory.bbox(featureType.getGeometryDescriptor().getLocalName(), 0, 5.1, 4.9, 9.9,featureType.getCoordinateReferenceSystem().toString());
		SimpleFeatureCollection fc = cache.getFeatures(upperLeft);
		assertEquals(2, fc.size());

		//this should end up getting the same features from the source that are already in the cache
		//however we only want one copy of those feature returned;
		Filter upperHalf = filterFactory.bbox(featureType.getGeometryDescriptor().getLocalName(), 0, 5.1, 9.9, 9.9,featureType.getCoordinateReferenceSystem().toString());
		fc = cache.getFeatures(upperHalf);
		assertEquals(2, fc.size());

	}

	public void testPut() throws Exception {
		resetDatasets();

		double x_min = 450;
		double x_max = 500;
		double y_min = 450;
		double y_max = 500;

		FilterFactory filterFactory = new FilterFactoryImpl();
		FeatureType ft = rawDataset.getFeatureSource("mycollection").getSchema();
		String localname = ft.getGeometryDescriptor().getLocalName();
		String srs = ft.getGeometryDescriptor().getCoordinateReferenceSystem().toString();

		Filter bb = filterFactory.bbox(localname, x_min, y_min, x_max, y_max,srs);
		assertEquals(numFeatures, rawDataset.getFeatureSource("mycollection").getFeatures().size());

		//cacheFS.resetDidRead();
		assertEquals(numFeatures, cacheDataset.getFeatures().size());
		//assertTrue(cacheFS.didRead());

		int size = rawDataset.getFeatureSource("mycollection").getFeatures(bb).size();
		//cacheFS.resetDidRead();
		assertEquals(size, cacheDataset.getFeatures(bb).size());
		//assertTrue(cacheFS.didRead()); // features aren't cached if all features
										// are requested??

		SimpleFeature newFeature = makeFeature(rawDataset.getFeatureSource("mycollection").getSchema(), 450, 500);
		DefaultFeatureCollection dfc = new DefaultFeatureCollection("mycollection", rawDataset.getFeatureSource("mycollection").getSchema());
		dfc.add(newFeature);

		// put features which is okay but doesn't not add it back to the raw
		// dataset
		try{
			cacheDataset.put(dfc);
		}catch (Exception ex){
			assertTrue(true);
		}
		//cacheFS.resetDidRead();
		//assertEquals(size + 1, cacheDataset.getFeatures(bb).size());
		//assertTrue(!cacheFS.didRead());
	}

	public void testWrite() {
		try {
			resetDatasets();

			String feattype = "mycollection";
			SimpleFeatureSource raw = rawDataset.getFeatureSource("mycollection");

			FeatureType ft = rawDataset.getFeatureSource("mycollection").getSchema();
			String localname = ft.getGeometryDescriptor().getLocalName();
			String srs = ft.getGeometryDescriptor().getCoordinateReferenceSystem().toString();
			Filter bb = filterFactory.bbox(localname, 0, 0, 10, 10, srs);

			assertEquals(numFeatures, rawDataset.getFeatureSource(feattype).getFeatures().size());
			assertEquals(numFeatures, cacheDataset.getFeatures().size());

			int beforebboxcnt = raw.getFeatures(bb).size();
			assertEquals(beforebboxcnt, cacheDataset.getFeatures(bb).size());

			SimpleFeature newFeature2 = makeFeature(raw.getSchema(), 0, 10);

			Transaction t = new DefaultTransaction();
			try {
				FeatureWriter<SimpleFeatureType, SimpleFeature> writer = rawDataset.getFeatureWriter("mycollection", t);
				while (writer.hasNext())
					writer.next();

				SimpleFeature feature = writer.next();
				feature.setAttributes(newFeature2.getAttributes());
				writer.write();
				t.commit();
			} finally {
				t.close();
			}
			//cacheFS.resetDidRead();
			assertEquals(numFeatures+1, cacheDataset.getFeatures().size());
			//assertTrue(cacheFS.didRead());

			//cacheFS.resetDidRead();
			assertEquals(beforebboxcnt + 1, cacheDataset.getFeatures(bb).size());
			//assertTrue(!cacheFS.didRead());

			assertEquals(beforebboxcnt + 1, raw.getFeatures(bb).size());
			assertEquals(numFeatures+1, raw.getFeatures().size());
			assertEquals(numFeatures+1, cacheDataset.getFeatures().size());

		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	private SimpleFeatureType createFeatureType() {
		SimpleFeatureTypeBuilder sb = new SimpleFeatureTypeBuilder();
		sb.setName("mycollection");
		sb.add("id", Integer.class);
		sb.add("name", String.class);
		sb.add("the_geom", Geometry.class, DefaultGeographicCRS.WGS84);

		return sb.buildFeatureType();

	}

	private SimpleFeature makeFeature(SimpleFeatureType type, int min, int max) {

		SimpleFeatureBuilder sb = new SimpleFeatureBuilder(type);
		SimpleFeature sf = sb.buildFeature(null);
		sf.setDefaultGeometry(createLine(min, max));
		int id = (int) (Math.random() * 10000);
		sf.setAttribute("id", id);
		sf.setAttribute("name", "some name:" + Math.random());

		return sf;

	}

	private LineString createLine(double min, double max) {
		int numpoints = (int) (Math.random() * 100);
		if (numpoints <= 1)
			numpoints = 2;

		Coordinate[] coords = new Coordinate[numpoints];
		for (int i = 0; i < numpoints; i++) {
			double x = Math.random() * (max - min) + min;
			double y = Math.random() * (max - min) + min;
			coords[i] = new Coordinate(x, y);
		}

		GeometryFactory gf = new GeometryFactory();
		return gf.createLineString(coords);
	}
	
	
	
	public void testEviction() throws IOException, FeatureCacheException{
    	
    	SimpleFeatureTypeBuilder bb = new SimpleFeatureTypeBuilder();
    	bb.add("ID", Integer.class);
    	bb.add("the_geom", Geometry.class, DefaultEngineeringCRS.CARTESIAN_2D);
    	bb.setDefaultGeometry("the_geom");
    	bb.setName("My Feature Type");
    	
    	//bb.setSRS(DefaultEngineeringCRS.CARTESIAN_2D.toString());
    	SimpleFeatureType featureType = bb.buildFeatureType();
    	
    	SimpleFeatureBuilder fb = new SimpleFeatureBuilder(featureType);
    	GeometryFactory gf = new GeometryFactory();
    	
    	LineString[] geoms = new LineString[8];
    	
    	int feature1[] = new int[]{1,1, 1,2, 2,2, 2,1, 1,1};
    	int feature2[] = new int[]{3,3, 3,4, 4,4, 4,3, 3,3};
    	
    	int feature3[] = new int[]{5,1, 5,2, 6,2, 6,1, 5,1};
    	int feature4[] = new int[]{7,3, 7,4, 8,4, 8,3, 7,3};
    	
    	int feature5[] = new int[]{1,5, 1,6, 2,6, 2,5, 1,5};
    	int feature6[] = new int[]{3,7, 3,8, 4,8, 4,7, 3,7};
    	
    	int feature7[] = new int[]{5,5, 5,6, 6,6, 6,5, 5,5};
    	int feature8[] = new int[]{7,7, 7,8, 8,8, 8,7, 7,7};
    	
    	Object featurescoords[] = new Object[]{feature1, feature2, feature3, feature4, feature5, feature6, feature7, feature8};
    	for (int i = 0; i < featurescoords.length; i ++){
    		int[] values = (int[])featurescoords[i];
    		Coordinate c[] = new Coordinate[5];
    		for (int j = 0; j < values.length; j = j + 2){
    			int x = values[j];
    			int y = values[j+1];
    			c[j/2] = new Coordinate(x,y);
    		}
    		geoms[i] = gf.createLineString(c);
    	}
    	
    	SimpleFeature features[] = new SimpleFeature[geoms.length];
    	MemoryFeatureCollection mm = new MemoryFeatureCollection(featureType);
    	for (int i = 0; i < geoms.length; i ++){
    		features[i] = fb.buildFeature(i +"", new Object[]{new Integer(i), geoms[i]});
    		mm.add(features[i]);
    	}
    	
    	DataStore ds = new MemoryDataStore(mm);
    	FeatureCache cache = new StreamingGridFeatureCache(ds.getFeatureSource(featureType.getTypeName()), 4, 4, MemoryStorage.createInstance());
    	
    	FilterFactory ff = new FilterFactoryImpl();
    	Filter f1 = ff.bbox("the_geom", 0, 0, 4.4, 4.4, DefaultEngineeringCRS.CARTESIAN_2D.toString());
    	Envelope e1 = new Envelope(0, 4.4, 0, 4.4);
    	
    	Filter f2 = ff.bbox("the_geom", 0, 4.6, 4.4, 8.5, DefaultEngineeringCRS.CARTESIAN_2D.toString());
    	Envelope e2 = new Envelope(0, 4.4, 4.6, 8.5);
    	
    	Filter f3 = ff.bbox("the_geom", 4.6, 0, 8.5, 4.4, DefaultEngineeringCRS.CARTESIAN_2D.toString());
    	Envelope e3 = new Envelope(4.6, 8.5, 0, 4.4);
    	
    	Filter f4 = ff.bbox("the_geom", 4.6, 4.6, 8.5, 8.5, DefaultEngineeringCRS.CARTESIAN_2D.toString());
    	Envelope e4 = new Envelope(4.6, 8.5, 4.6, 8.5);    	

    	//there should be two features in each region
    	FeatureCollection fc = cache.getFeatures(f1);
    	assertEquals(2, fc.size());
    	
    	fc = cache.getFeatures(f2);
    	assertEquals(2, fc.size());
    	
    	fc = cache.getFeatures(f3);
    	assertEquals(2, fc.size());

    	//at this point the cache should contain feature from area 2 & 3 and features from area 1
    	//should have been evicted.
    	assertEquals(0, cache.peek(e4).size());
    	assertEquals(0, cache.peek(e1).size());
    	assertEquals(2, cache.peek(e2).size());
    	assertEquals(2, cache.peek(e3).size());
    	
    	fc = cache.getFeatures(f4);
    	assertEquals(2, fc.size());
    	
    	//at this point the cache should contain features from areas 3 & 4 and area 2 should 
    	//have been evicted;
    	assertEquals(2, cache.peek(e3).size());
    	assertEquals(2, cache.peek(e4).size());
    	assertEquals(0, cache.peek(e2).size());
    	assertEquals(0, cache.peek(e1).size());
    	
    	//lets query region 4 again
    	fc = cache.getFeatures(f4);
    	assertEquals(2, fc.size());
    	
    	//at this point the cache should contain features from areas 3 & 4 and area 2 should 
    	//have been evicted;
    	assertEquals(2, cache.peek(e3).size());
    	assertEquals(2, cache.peek(e4).size());
    	assertEquals(0, cache.peek(e2).size());
    	assertEquals(0, cache.peek(e1).size());    	
    	
    	//now lets go back to 1
    	fc = cache.getFeatures(f1);
    	assertEquals(2, fc.size());

    	assertEquals(2, cache.peek(e4).size());
    	assertEquals(2, cache.peek(e1).size());
    	assertEquals(0, cache.peek(e2).size());
    	assertEquals(0, cache.peek(e3).size());
    	
    	//if we peek at 4
    	fc = cache.peek(e4);
    	fc.size();  //(note area isn't actually visited until features are iterated through
    	//now the cache should be order (least recently used to most recently used) 1, 4

    	//ask for 2
    	//fc = cache.peek(e2);
    	fc  = cache.getFeatures(f2);
    	fc.size();
    	//cache: 4,2
    	
    	assertEquals(2, cache.peek(e4).size());
    	assertEquals(2, cache.peek(e2).size());
    	assertEquals(0, cache.peek(e1).size());
    	assertEquals(0, cache.peek(e3).size());
    }
	
	
	 public void testPeek() throws FeatureCacheException, IOException {
		resetDatasets();

		StreamingGridFeatureCache cache = (StreamingGridFeatureCache) cacheDataset;

		Filter bb = filterFactory.bbox(cacheFS.getSchema()
				.getGeometryDescriptor().getLocalName(), 100, 100, 200, 200,
				cacheFS.getSchema().getGeometryDescriptor()
						.getCoordinateReferenceSystem().toString());

		FeatureCollection fc = cache.getFeatures(bb);
		int size = fc.size();

		assertEquals(size, cache.peek(new Envelope(100, 200, 100, 200)).size());
		assertTrue(size <= cache.peek(new Envelope(0, 1000, 0, 1000)).size());	//because of the grid sizes there may actually be more features in the cache at this point
		
	}
	
	/**
     * A test that queries the dataset for a given set of attributes and a maximum number
     * of features (Skips the geometry attribute)
	 * @throws FeatureCacheException 
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 * @throws TransformException 
	 * @throws MismatchedDimensionException 
     */
    public void testQuery () throws IOException, FeatureCacheException, NoSuchAuthorityCodeException, FactoryException, MismatchedDimensionException, TransformException{
    	int maxfeatures = 10;
    	//build up query
    	resetDatasets();
    	
    	//tests a query with a given number of attributes
    	//and a select number of attributes
    	SimpleFeatureType schema = (SimpleFeatureType)cacheDataset.getSchema();
    	ArrayList<String> attributes = new ArrayList<String>();
    	 for( int i = 0; i < schema.getAttributeCount(); i++ ) {
             AttributeDescriptor attr = schema.getDescriptor(i);
             if( !(attr instanceof GeometryDescriptor) ){
            	 attributes.add(attr.getName().getLocalPart());
             }
         }
    	DefaultQuery query = new DefaultQuery(schema.getTypeName(),Filter.INCLUDE, maxfeatures, attributes.toArray(new String[0]), null);
    	    	
    	FeatureCollection features = cacheDataset.getFeatures(query);
    	//ensure feature count is < maximum
    	assertEquals(maxfeatures, features.size());
    	//ensure attribute count correct
    	SimpleFeatureType ftype = (SimpleFeatureType)features.getSchema();
    	assertEquals(attributes.size(), ftype.getAttributeCount());
    	
    	//test a query with a different CRS
    	Filter f = filterFactory.bbox(schema.getGeometryDescriptor().getLocalName(), 0, 0, 100, 100, schema.getCoordinateReferenceSystem().toString());
    	features = cacheDataset.getFeatures(f);
    	int size = features.size();
    	CoordinateReferenceSystem targetCRS = getBCAlbers();

    	//same crs
    	query = new DefaultQuery(schema.getTypeName(), f);
    	query.setCoordinateSystem(schema.getCoordinateReferenceSystem());
    	query.setCoordinateSystemReproject(schema.getCoordinateReferenceSystem());
    	query.setFilter(f);
    	features = cacheDataset.getFeatures(query);
    	assertEquals(size, features.size());
    	
    	
    	//different crs
    	query = new DefaultQuery(schema.getTypeName(), Filter.INCLUDE);
    	query.setCoordinateSystem(schema.getCoordinateReferenceSystem());
    	query.setCoordinateSystemReproject(targetCRS);
    	query.setFilter(f);    	
    	features = cacheDataset.getFeatures(query);
    	assertEquals(targetCRS,features.getSchema().getCoordinateReferenceSystem());
    	assertEquals(size, features.size());

    	//now lets try start index
    	query = new DefaultQuery(schema.getTypeName());
    	query.setStartIndex(new Integer(4));
    	boolean error = false;
    	try{
    		features = cacheDataset.getFeatures(query);
    	}catch(IOException ex){
    		error = true;
    	}
    	assertTrue(error);

    	//sort by
    	String prop = schema.getAttributeDescriptors().get(1).getLocalName();
    	SortBy sb =filterFactory.sort(prop, SortOrder.ASCENDING); 
    	query = new DefaultQuery(schema.getTypeName());
    	query.setSortBy(new SortBy[]{sb});
    	error = false;
    	try{
    		features = cacheDataset.getFeatures(query);
    	}catch (IOException ex){
    		error = true;
    	}
    	assertTrue(error);
    }

    private CoordinateReferenceSystem getBCAlbers() throws FactoryException{
    	String wkt = "PROJCS[\"NAD83 / BC Albers\","+
    	  "GEOGCS[\"NAD83\", "+
    	  "  DATUM[\"North_American_Datum_1983\", "+
    	  "    SPHEROID[\"GRS 1980\", 6378137.0, 298.257222101, AUTHORITY[\"EPSG\",\"7019\"]], "+
    	  "    TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], "+
    	  "    AUTHORITY[\"EPSG\",\"6269\"]], "+
    	  "  PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], "+
    	  "  UNIT[\"degree\", 0.017453292519943295], "+
    	  "  AXIS[\"Lon\", EAST], "+
    	  "  AXIS[\"Lat\", NORTH], "+
    	  "  AUTHORITY[\"EPSG\",\"4269\"]], "+
    	  "PROJECTION[\"Albers_Conic_Equal_Area\"], "+
    	  "PARAMETER[\"central_meridian\", -126.0], "+
    	  "PARAMETER[\"latitude_of_origin\", 45.0], "+
    	  "PARAMETER[\"standard_parallel_1\", 50.0], "+
    	  "PARAMETER[\"false_easting\", 1000000.0], "+
    	  "PARAMETER[\"false_northing\", 0.0], "+
    	  "PARAMETER[\"standard_parallel_2\", 58.5], "+
    	  "UNIT[\"m\", 1.0], "+
    	  "AXIS[\"x\", EAST], "+
    	  "AXIS[\"y\", NORTH], "+
    	  "AUTHORITY[\"EPSG\",\"3005\"]]";
    	
    	return CRS.parseWKT(wkt);
    	  
    }
}
