/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
 
package org.geotools.data.store;

import java.io.IOException;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.collection.FilteringSimpleFeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * 
 *
 * @source $URL$
 */
public class FilteringSimpleFeatureCollectionTest extends FeatureCollectionWrapperTestSupport {
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    
    public void testNext() {
        Filter filter = ff.equal(ff.property("someAtt"), ff.literal("1"), false);
        SimpleFeatureCollection collection = new FilteringSimpleFeatureCollection(delegate, filter);
        assertNotNull(collection.features().next());
    }
    public void testCount() {
        Filter filter = ff.equal(ff.property("someAtt"), ff.literal("1"), false);
        SimpleFeatureCollection collection = new FilteringSimpleFeatureCollection(delegate, filter);
        assertEquals(1, collection.size());
    }
    
    public void testVisitor() throws IOException {
        Filter filter = ff.equal(ff.property("someAtt"), ff.literal("1"), false);
        SimpleFeatureCollection collection = new FilteringSimpleFeatureCollection(delegate, filter);
        collection.accepts(new FeatureVisitor() {
            
            public void visit(Feature feature) {
                assertEquals(1, feature.getProperty("someAtt").getValue());
                
            }
        }, null);
    }
}
