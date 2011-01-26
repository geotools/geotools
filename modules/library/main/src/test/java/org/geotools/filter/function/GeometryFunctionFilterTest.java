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
package org.geotools.filter.function;

import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Function;
import org.opengis.filter.spatial.Equals;


public class GeometryFunctionFilterTest extends FunctionTestSupport {

    public GeometryFunctionFilterTest() {
        super("GeometryFunctionFilterTest");
    }
    
    public void testBasicTest() throws Exception {
        Function exp = ff.function("geometryType", ff.property("geom"));
        FeatureIterator<SimpleFeature> iter=featureCollection.features();
        while( iter.hasNext() ){
            SimpleFeature feature = iter.next();
            assertEquals( "Point", exp.evaluate(feature) );
        }
        
        iter.close();
    }
    
    public void testNullTest() throws Exception {
        Function exp = ff.function("geometryType", ff.property("geom"));
        FeatureIterator<SimpleFeature> iter=featureCollection.features();
        while( iter.hasNext() ){
        	SimpleFeature feature = iter.next();
            feature.setAttribute("geom",null);
            assertNull( exp.evaluate(feature) );
        }
        
        iter.close();
    }
    
    public void testFilterEqualsTest() throws Exception {
        Function exp = ff.function("geometryType", ff.property("geom"));
        PropertyIsEqualTo filter = ff.equals(exp, ff.literal("Point"));
        
        FeatureIterator<SimpleFeature> iter=featureCollection.features();
        while( iter.hasNext() ){
            SimpleFeature feature = iter.next();
            assertTrue( filter.evaluate( feature ));
        }
        
        iter.close();
    }
}
