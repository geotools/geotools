/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.view;

import java.io.IOException;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.factory.CommonFactoryFinder;

import org.geotools.feature.FeatureIterator;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class DefaultViewTest extends TestCase {

    String typeName = "type1";
    private MemoryDataStore ds;

    protected void setUp() throws Exception {
        super.setUp();
        SimpleFeatureType ft=DataUtilities.createType(typeName, "geom:Point,name:String,id:int");
        ds=new MemoryDataStore();
        ds.addFeature(createFeatures(ft,1));
        ds.addFeature(createFeatures(ft,2));
        ds.addFeature(createFeatures(ft,3));
        ds.addFeature(createFeatures(ft,4));
    }

    private SimpleFeature createFeatures(SimpleFeatureType ft, int i) throws IllegalAttributeException {
        GeometryFactory fac=new GeometryFactory();
        return SimpleFeatureBuilder.build(ft,new Object[]{
            fac.createPoint(new Coordinate(i,i)),
            "name"+i, 
            new Integer(i)
        }, null);
    }

    public void testGetFeatures() throws Exception {

        FeatureSource<SimpleFeatureType, SimpleFeature> view = getView();
        
        FeatureIterator<SimpleFeature> features = view.getFeatures().features();
        int count=0;
        while( features.hasNext() ){
            count++;
            features.next();
        }
        
        assertEquals(2, count);
    }

    public void testGetFeaturesQuery() throws Exception {

        FeatureSource<SimpleFeatureType, SimpleFeature> view = getView();
        
        FeatureIterator<SimpleFeature> features = view.getFeatures(getQuery()).features();
        int count=0;
        while( features.hasNext() ){
            count++;
            features.next();
        }
        
        assertEquals(1, count);
    }
    public void testGetFeaturesFilter() throws Exception {

        FeatureSource<SimpleFeatureType, SimpleFeature> view = getView();
        Filter f = getFilter();
        FeatureIterator<SimpleFeature> features = view.getFeatures(f).features();
        int count=0;
        while( features.hasNext() ){
            count++;
            features.next();
        }
        
        assertEquals(1, count);
    }
  
    public void testGetCount() throws Exception {
        FeatureSource<SimpleFeatureType, SimpleFeature> view = getView();
        
        DefaultQuery defaultQuery = getQuery();
        int count = view.getCount(defaultQuery);
        assertEquals(1, count);
    }

    private DefaultQuery getQuery() throws IllegalFilterException {
        Filter f = getFilter();
        DefaultQuery defaultQuery = new DefaultQuery(typeName, f, new String[0]);
        return defaultQuery;
    }

    private Filter getFilter() throws IllegalFilterException {
        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);
        Filter f = fac.equals(fac.property("name"), fac.literal("name2"));
        return f;
    }

    private FeatureSource<SimpleFeatureType, SimpleFeature> getView() throws IllegalFilterException, IOException, SchemaException {
        FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);
        Filter f = fac.less(fac.property("id"), fac.literal(3));

        FeatureSource<SimpleFeatureType, SimpleFeature> view = ds.getView(new DefaultQuery(typeName, f));
        return view;
    }

}
