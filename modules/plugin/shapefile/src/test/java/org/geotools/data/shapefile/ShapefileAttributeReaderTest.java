package org.geotools.data.shapefile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;

import org.geotools.TestData;
import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

public class ShapefileAttributeReaderTest extends TestCaseSupport {

	public final String STATEPOP = "shapes/statepop.shp";

	public ShapefileAttributeReaderTest(String name) throws IOException {
		super(name);
	}

	public void testAttributeReader() throws IOException {
	        File shpFile =  copyShapefiles(STATEPOP);
	        URL url = shpFile.toURI().toURL();
		ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
		
		//creates both indexed and regular shapefile data store
		IndexedShapefileDataStore indexedstore = new IndexedShapefileDataStore(url);
		ShapefileDataStore store = new ShapefileDataStore(url);

		//get a random feature id from one of the stores
		FeatureIterator<SimpleFeature> it = indexedstore.getFeatureSource().getFeatures().features();
                FeatureId fid = it.next().getIdentifier();
                it.close();
		
		//query the datastore
		FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
		Filter idFilter = ff.id(Collections.singleton(fid));
		final Query query = new DefaultQuery(indexedstore.getSchema().getName().getLocalPart(), idFilter, new String[] { "STATE_NAME"});
		final FeatureCollection<SimpleFeatureType, SimpleFeature> indexedfeatures = indexedstore.getFeatureSource().getFeatures(query);
		final FeatureCollection<SimpleFeatureType, SimpleFeature> features = store.getFeatureSource().getFeatures(query);
		
		// compare the results
		FeatureIterator<SimpleFeature> indexIterator = indexedfeatures.features();
		SimpleFeature indexedFeature = indexIterator.next();
		indexIterator.close();
		
		FeatureIterator<SimpleFeature> iterator = features.features();
		SimpleFeature feature = iterator.next();
		iterator.close();
		
		String indexedStateName = (String) indexedFeature.getAttribute("STATE_NAME");
		String stateName = (String) feature.getAttribute("STATE_NAME");

		System.out.println(indexedStateName);
		System.out.println(stateName);

		assertEquals(indexedStateName, stateName);
	}

}
