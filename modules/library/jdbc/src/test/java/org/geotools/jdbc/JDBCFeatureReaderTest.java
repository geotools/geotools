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
package org.geotools.jdbc;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

public abstract class JDBCFeatureReaderTest extends JDBCTestSupport {

    public void testNext() throws Exception {
        Query query = new DefaultQuery( tname("ft1") );
        FeatureReader reader = dataStore.getFeatureReader( query, Transaction.AUTO_COMMIT );
        
        assertTrue( reader.hasNext() );
        SimpleFeature feature = (SimpleFeature) reader.next();
        
        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull( g );
        
        assertTrue( g.getUserData() instanceof CoordinateReferenceSystem );
        reader.close();
    }

}
