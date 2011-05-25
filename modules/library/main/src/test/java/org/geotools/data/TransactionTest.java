/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;


/**
 *
 *
 * @source $URL$
 */
public class TransactionTest extends TestCase {
    MemoryDataStore ds;
    SimpleFeatureType type;
    Geometry geom;
    
    protected void setUp() throws Exception {
        super.setUp();
        type=DataUtilities.createType("default", "name:String,*geom:Geometry");
        GeometryFactory fac=new GeometryFactory();
        geom=fac.createPoint(new Coordinate(10,10));
        SimpleFeature f1=SimpleFeatureBuilder.build(type,new Object[]{ "original", geom },null);
        ds=new MemoryDataStore(new SimpleFeature[]{f1});
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testAddFeature() throws Exception{
        SimpleFeature f1=SimpleFeatureBuilder.build(type,new Object[]{ "one",geom },null);
        SimpleFeature f2=SimpleFeatureBuilder.build(type,new Object[]{ "two", geom },null);
        
        SimpleFeatureStore store=(SimpleFeatureStore) ds.getFeatureSource("default");
        store.setTransaction(new DefaultTransaction());
        store.addFeatures( DataUtilities.collection( f1 ) );
        store.addFeatures( DataUtilities.collection(f2) );
        
        count( store, 3);
//        assertEquals("Number of known feature as obtained from getCount",3, store.getCount(Query.ALL));
    }

    public void testRemoveFeature() throws Exception{
        SimpleFeature f1=SimpleFeatureBuilder.build(type,new Object[]{ "one",geom },null);
        
        SimpleFeatureStore store=(SimpleFeatureStore) ds.getFeatureSource("default");
        store.setTransaction(new DefaultTransaction());
        List<FeatureId> fid=store.addFeatures( DataUtilities.collection(f1) );

        count(store, 2);
        FeatureId identifier = fid.iterator().next();
        String next = identifier.getID();
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
        Filter f = filterFactory.id(Collections.singleton(filterFactory.featureId(next)));
        store.removeFeatures(f);
        
        count(store, 1);
//        assertEquals("Number of known feature as obtained from getCount",3, store.getCount(Query.ALL));
    }

	private void count(SimpleFeatureStore store, int expected) throws IOException, IllegalAttributeException {
		int i=0;
        for( SimpleFeatureIterator reader=store.getFeatures().features();
        reader.hasNext();){
            reader.next();
            i++;
        }
        assertEquals("Number of known feature as obtained from reader",expected, i);
	}
}
