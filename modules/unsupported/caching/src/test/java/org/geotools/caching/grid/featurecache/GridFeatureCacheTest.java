/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.caching.grid.featurecache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.caching.CacheOversizedException;
import org.geotools.caching.FeatureCollectingVisitor;
import org.geotools.caching.featurecache.AbstractFeatureCache;
import org.geotools.caching.featurecache.AbstractFeatureCacheTest;
import org.geotools.caching.featurecache.FeatureCache;
import org.geotools.caching.featurecache.FeatureCacheException;
import org.geotools.caching.grid.spatialindex.store.MemoryStorage;
import org.geotools.caching.util.CacheUtil;
import org.geotools.caching.util.Generator;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;


public class GridFeatureCacheTest extends AbstractFeatureCacheTest {
    static boolean testEviction_holistic = false;
    GridFeatureCache cache;

    public static Test suite() {
        return new TestSuite(GridFeatureCacheTest.class);
    }

    @Override
    protected AbstractFeatureCache createInstance(int capacity)
        throws FeatureCacheException, IOException {
        this.cache = new GridFeatureCache((SimpleFeatureStore) ds.getFeatureSource(
                    dataset.getSchema().getName()), 100, capacity,
                MemoryStorage.createInstance());

        return this.cache;
    }

    @Override
    public void testEviction() throws IOException, FeatureCacheException {
        super.cache = createInstance(numdata / 2);

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                Filter f = Generator.createBboxFilter(new Coordinate(i * 0.1, j * 0.1), 0.1, 0.1);
                SimpleFeatureCollection control = ds.getFeatureSource(dataset.getSchema().getName()).getFeatures(f);
                SimpleFeatureCollection c = cache.getFeatures(f);
                
                assertEquals(control.size(), c.size());

                if (!testEviction_holistic && (cache.tracker.getEvictions() > 10)) { // wait to generate a fair amount of evictions,
                                                                                     // and see everything is still working

                    return;
                }
            }
        }

        System.out.println(cache.tracker.getStatistics());
        System.out.println(cache.sourceAccessStats());

        if (!testEviction_holistic) {
            fail("Did not got enough evictions : " + cache.tracker.getEvictions());
        }
    }
    
    public void testEviction2() throws IOException, FeatureCacheException{
    	
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
    	FeatureCache cache = new GridFeatureCache(ds.getFeatureSource(featureType.getTypeName()), 4, 4, MemoryStorage.createInstance());
    	
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
    	assertEquals(0, ((GridFeatureCache)cache).peek(e4).size());
    	assertEquals(0, ((GridFeatureCache)cache).peek(e1).size());
    	assertEquals(2, ((GridFeatureCache)cache).peek(e2).size());
    	assertEquals(2, ((GridFeatureCache)cache).peek(e3).size());
    	
    	//lets test the last region
    	fc = cache.getFeatures(f4);
    	assertEquals(2, fc.size());
    	
    	//at this point the cache should contain features from areas 3 & 4 and area 2 should 
    	//have been evicted;
    	assertEquals(2, ((GridFeatureCache)cache).peek(e3).size());
    	assertEquals(2, ((GridFeatureCache)cache).peek(e4).size());
    	assertEquals(0, ((GridFeatureCache)cache).peek(e2).size());
    	assertEquals(0, ((GridFeatureCache)cache).peek(e1).size());
    	
    	//lets query region 4 again
    	fc = cache.getFeatures(f4);
    	assertEquals(2, fc.size());
    	
    	//at this point the cache should contain features from areas 3 & 4 and area 2 should 
    	//have been evicted;
    	assertEquals(2, ((GridFeatureCache)cache).peek(e3).size());
    	assertEquals(2, ((GridFeatureCache)cache).peek(e4).size());
    	assertEquals(0, ((GridFeatureCache)cache).peek(e2).size());
    	assertEquals(0, ((GridFeatureCache)cache).peek(e1).size());    	
    	
    	//now lets go back to 1
    	fc = cache.getFeatures(f1);
    	assertEquals(2, fc.size());

    	assertEquals(2, ((GridFeatureCache)cache).peek(e4).size());
    	assertEquals(2, ((GridFeatureCache)cache).peek(e1).size());
    	assertEquals(0, ((GridFeatureCache)cache).peek(e2).size());
    	assertEquals(0, ((GridFeatureCache)cache).peek(e3).size());
    	
    	//if we peek at 4
    	fc = ((GridFeatureCache)cache).peek(e4);
    	//now the cache should be order (least recently used to most recently used) 1, 4
    	//ask for 2
    	fc  = cache.getFeatures(f2);
    	//cache: 4,2
    	assertEquals(2, ((GridFeatureCache)cache).peek(e4).size());
    	assertEquals(2, ((GridFeatureCache)cache).peek(e2).size());
    	assertEquals(0, ((GridFeatureCache)cache).peek(e1).size());
    	assertEquals(0, ((GridFeatureCache)cache).peek(e3).size());
    }


    @Override
    public void testPut() throws CacheOversizedException {
        cache.put(dataset);

        FeatureCollectingVisitor v = new FeatureCollectingVisitor((SimpleFeatureType)dataset.getSchema());
        cache.tracker.intersectionQuery(CacheUtil.convert(unitsquare), v);

        //assertEquals(0, v.getCollection().size());
        List<Envelope> matches = cache.match(unitsquare);

        for (Iterator<Envelope> it = matches.iterator(); it.hasNext();) {
            cache.register(it.next());
        }

        cache.put(dataset);

        v = new FeatureCollectingVisitor((SimpleFeatureType)dataset.getSchema());
        cache.tracker.intersectionQuery(CacheUtil.convert(unitsquare), v);

        assertEquals(dataset.size(), v.getCollection().size());
    }
    
    /**
     * A test that queries the dataset for a given set of attributes and a maximum number
     * of features (Skips the geometry attribute)
     */
    public void testQuery () throws IOException{
    	int maxfeatures = 10;
    	//build up query
    	SimpleFeatureType schema = dataset.getSchema();
    	ArrayList<String> attributes = new ArrayList<String>();
    	 for( int i = 0; i < schema.getAttributeCount(); i++ ) {
             AttributeDescriptor attr = schema.getDescriptor(i);
             if( !(attr instanceof GeometryDescriptor) ){
            	 attributes.add(attr.getName().getLocalPart());
             }
         }
    	DefaultQuery query = new DefaultQuery(schema.getTypeName(),Filter.INCLUDE, maxfeatures, attributes.toArray(new String[0]), null);
    	    	
    	FeatureCollection features = cache.getFeatures(query);
    	
    	//ensure feature count is < maximum
    	assertTrue(features.size() <= maxfeatures);
    	
    	//ensure attribute count correct
    	SimpleFeatureType ftype = (SimpleFeatureType)features.getSchema();
    	assertEquals(attributes.size(), ftype.getAttributeCount());
    }
}
