/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.FeatureReader;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.GeometryFilter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * This test should be run against a postgis instance that does not 
 * have GEOS installed.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 */
public class PostgisWithoutGeosOnlineTest extends AbstractPostgisDataTestCase {

	public PostgisWithoutGeosOnlineTest(String name) {
		super(name);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public String getFixtureFile() {
		return "nogeos.properties"; 
	}
	
	public void _testBboxQuery() throws Exception {
//		get the bounding box for each feature
		List bbox = new ArrayList();
		List fids = new ArrayList();
		SimpleFeatureCollection fc = data.getFeatureSource("road").getFeatures();
		for (Iterator itr = fc.iterator(); itr.hasNext();) {
			SimpleFeature f = (SimpleFeature)itr.next();
			bbox.add(((Geometry) f.getDefaultGeometry()).getEnvelopeInternal());
			fids.add(f.getID());
		}
		
		//query each feature
		SimpleFeatureType type = data.getSchema("road");
		FilterFactory ff = FilterFactoryFinder.createFilterFactory();
		
		for (int i = 0; i < bbox.size(); i++) {
			Envelope box = (Envelope)bbox.get(i);
			String fid = (String)fids.get(i);
			
			GeometryFilter filter = 
				ff.createGeometryFilter(GeometryFilter.GEOMETRY_BBOX);
			
			filter.addLeftGeometry(ff.createAttributeExpression(type,"geom"));
			filter.addRightGeometry(ff.createBBoxExpression(box));
			
			FeatureReader<SimpleFeatureType, SimpleFeature> reader;
            reader = ((PostgisDataStore) data).getFeatureReader(type,filter,Transaction.AUTO_COMMIT);
			boolean found = false;
			for (; reader.hasNext();) {
				SimpleFeature f = reader.next();
				if (fid.equals(f.getID()))
					found = true;
			}
			reader.close();
			assertTrue(found);
		}
	}
	
	public void testBboxQueryWithLooseBBOX() throws Exception {
		((PostgisDataStore) data).setLooseBbox(true);
		_testBboxQuery();
	}
	
	public void testBboxQueryWithoutLooseBBOX() throws Exception {
            ((PostgisDataStore) data).setLooseBbox(false);
		_testBboxQuery();
	}
}
