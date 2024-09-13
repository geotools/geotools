/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.api.data.Query;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCFeatureSourceOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Test;

/** @author Stefan Uhrig, SAP SE */
public class HanaFeatureSourceOnlineTest extends JDBCFeatureSourceOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new HanaTestSetupDefault();
    }

    @Test
    public void testEstimatedBounds() throws Exception {
        // enable fast bbox
        ((HanaDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);

        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds();
        assertEquals(0L, Math.round(bounds.getMinX()));
        assertEquals(0L, Math.round(bounds.getMinY()));
        assertEquals(2L, Math.round(bounds.getMaxX()));
        assertEquals(2L, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(decodeEPSG(4326), bounds.getCoordinateReferenceSystem()));
    }

    @Test
    public void testEstimatedBoundsWithQuery() throws Exception {
        // enable fast bbox
        ((HanaDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter =
                ff.equals(ff.property(aname("stringProperty")), ff.literal("one"));

        Query query = new Query();
        query.setFilter(filter);

        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds(query);
        assertEquals(1L, Math.round(bounds.getMinX()));
        assertEquals(1L, Math.round(bounds.getMinY()));
        assertEquals(1L, Math.round(bounds.getMaxX()));
        assertEquals(1L, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(decodeEPSG(4326), bounds.getCoordinateReferenceSystem()));
    }

    @Test
    public void testEstimatedBoundsWithLimit() throws Exception {
        ((HanaDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);
        super.testBoundsWithLimit();
    }

    @Test
    public void testEstimatedBoundsWithOffset() throws Exception {
        ((HanaDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);
        super.testBoundsWithOffset();
    }
}
