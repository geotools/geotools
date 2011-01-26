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
package org.geotools.filter;

import java.util.HashSet;
import java.util.LinkedHashSet;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;


public class FilterTransformerTest extends TestCase {
    FilterTransformer transform = new FilterTransformer();
    
    public void testIdEncode() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        HashSet<FeatureId> set = new LinkedHashSet<FeatureId>();
        set.add( ff.featureId("FID.1"));
        set.add( ff.featureId("FID.2"));        
        Filter filter=ff.id( set );
        
        String output = transform.transform( filter );
        assertNotNull( "got xml", output );
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ogc:FeatureId xmlns=\"http://www.opengis.net/ogc\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\" fid=\"FID.1\"/><ogc:FeatureId fid=\"FID.2\"/>";
        assertEquals( "expected id filters", xml, output );
    }
}
