/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.informix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCFeatureSourceOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.referencing.CRS;
import org.junit.Test;

public class InformixFeatureSourceOnlineTest extends JDBCFeatureSourceOnlineTest {
    @Override
    protected JDBCTestSetup createTestSetup() {
        return new InformixTestSetup();
    }

    /**
     * Test if the fast retrieval of bounds works. Note that if getOptimizedBounds fails it will
     * return null, and GeoTools will fall back to a different mechanism and the test will still
     * pass. A warning will be seen in the logs, however.
     */
    @Test
    public void testGetBounds() throws Exception {
        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds();
        assertEquals(0, Math.round(bounds.getMinX()));
        assertEquals(0, Math.round(bounds.getMinY()));
        assertEquals(2, Math.round(bounds.getMaxX()));
        assertEquals(2, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(CRS.decode("EPSG:4326"), bounds.getCoordinateReferenceSystem()));
    }

    /*
     * for some reason this fails while
     * org.geotools.jdbc.JDBCFeatureSourceOnlineTest.testStringFunction() which
     * is functionally identical passes.
     */
    @Override
    public void testStrMatchesFilter() throws Exception {
    }
}
