/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.data.DataAccess;
import org.geotools.data.DataUtilities;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.List;

public abstract class JDBCIAUOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCIAUTestSetup createTestSetup();

    @Test
    public void testCRS() throws Exception {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("mars_poi"));
        SimpleFeatureType ft = fs.getSchema();

        CoordinateReferenceSystem crs = ft.getCoordinateReferenceSystem();
        assertEquals("IAU:49900", CRS.lookupIdentifier(crs, true));
        CoordinateReferenceSystem expected = CRS.decode("IAU:49900", true);
        assertTrue(CRS.equalsIgnoreMetadata(expected, crs));
        
        // check CRS assignment to features
        List<SimpleFeature> features = DataUtilities.list(fs.getFeatures());
        assertEquals(3,features.size());
        for (SimpleFeature feature : features) {
            assertEquals(crs, feature.getFeatureType().getCoordinateReferenceSystem());
            Point p = (Point) feature.getDefaultGeometry();
            assertEquals(crs, p.getUserData());
            assertEquals(949900, p.getSRID());
        }
    }
}
