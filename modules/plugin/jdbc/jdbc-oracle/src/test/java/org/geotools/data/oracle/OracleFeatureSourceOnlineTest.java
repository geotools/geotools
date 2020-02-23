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
package org.geotools.data.oracle;

import org.geotools.data.Query;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCFeatureSourceOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.referencing.CRS;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

public class OracleFeatureSourceOnlineTest extends JDBCFeatureSourceOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleTestSetup();
    }

    /**
     * Test if the fast retrieval of bounds out of oracle metadata tables works
     *
     * @author Hendrik Peilke
     */
    public void testSDOGeomMetadataBounds() throws Exception {
        // enable fast bounds retrieval from SDO_GEOM_METADATA table
        ((OracleDialect) ((JDBCDataStore) dataStore).getSQLDialect()).setMetadataBboxEnabled(true);
        ;

        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds();
        assertEquals(-180l, Math.round(bounds.getMinX()));
        assertEquals(-90l, Math.round(bounds.getMinY()));
        assertEquals(180l, Math.round(bounds.getMaxX()));
        assertEquals(90l, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(CRS.decode("EPSG:4326"), bounds.getCoordinateReferenceSystem()));
    }

    public void testEstimatedBounds() throws Exception {
        // enable fast bbox
        ((OracleDialect) ((JDBCDataStore) dataStore).getSQLDialect())
                .setEstimatedExtentsEnabled(true);

        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds();
        assertEquals(0l, Math.round(bounds.getMinX()));
        assertEquals(0l, Math.round(bounds.getMinY()));
        assertEquals(2l, Math.round(bounds.getMaxX()));
        assertEquals(2l, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(CRS.decode("EPSG:4326"), bounds.getCoordinateReferenceSystem()));
    }

    public void testEstimatedBoundsWithQuery() throws Exception {
        // enable fast bbox
        ((OracleDialect) ((JDBCDataStore) dataStore).getSQLDialect())
                .setEstimatedExtentsEnabled(true);

        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter =
                ff.equals(ff.property(aname("stringProperty")), ff.literal("one"));

        Query query = new Query();
        query.setFilter(filter);

        ReferencedEnvelope bounds = dataStore.getFeatureSource(tname("ft1")).getBounds(query);
        assertEquals(1l, Math.round(bounds.getMinX()));
        assertEquals(1l, Math.round(bounds.getMinY()));
        assertEquals(1l, Math.round(bounds.getMaxX()));
        assertEquals(1l, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(CRS.decode("EPSG:4326"), bounds.getCoordinateReferenceSystem()));
    }
}
