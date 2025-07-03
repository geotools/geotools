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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCGeographyOnlineTest;
import org.geotools.jdbc.JDBCGeographyTestSetup;
import org.geotools.jdbc.VirtualTable;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;

/** @author Stefan Uhrig, SAP SE */
public class HanaGeographyOnlineTest extends JDBCGeographyOnlineTest {

    @Override
    protected JDBCGeographyTestSetup createTestSetup() {
        return new HanaGeographyTestSetup(new HanaTestSetupPSPooling());
    }

    @Override
    public void testDistanceGreatCircle() throws Exception {
        // Currently, HANA only supports point-point distances
    }

    @Override
    public void testVirtualTable() throws Exception {
        // We have to override to use the proper schema in the select-statement.
        String schema = getFixture().getProperty("schema", "geotools");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        HanaTestUtil.encodeIdentifiers(sb, schema, "geopoint");
        VirtualTable vt = new VirtualTable("geopoint_vt", sb.toString());
        dataStore.createVirtualTable(vt);

        SimpleFeatureType featureType = dataStore.getSchema("geopoint_vt");
        assertNotNull(featureType);
        assertNotNull(featureType.getGeometryDescriptor());
    }

    @Override
    public void testSchema() throws Exception {
        // We have to override, because HANA always binds to Geometry.class, but not to Point.class
        SimpleFeatureType ft = dataStore.getFeatureSource(tname("geopoint")).getSchema();
        assertNotNull(ft);

        assertTrue(ft.getDescriptor(aname("geo")) instanceof GeometryDescriptor);
        assertEquals(Geometry.class, ft.getDescriptor("geo").getType().getBinding());

        int epsg = CRS.lookupEpsgCode(
                ((GeometryDescriptor) ft.getDescriptor(aname("geo"))).getCoordinateReferenceSystem(), false);
        assertEquals(4326, epsg);
    }

    @Override
    public void testBounds() throws Exception {
        assumeTrue(isGeographySupportAvailable());

        HanaDialect hanaDialect = (HanaDialect) dataStore.getSQLDialect();
        ReferencedEnvelope expected = new ReferencedEnvelope(-110, 0, 29, 49, decodeEPSG(4326));
        try {
            // Test with estimation disabled
            ReferencedEnvelope env =
                    dataStore.getFeatureSource(tname("geopoint")).getBounds();
            assertTrue(env.boundsEquals2D(expected, Math.ulp(2.0)));

            // Test with estimation enabled
            hanaDialect.setEstimatedExtentsEnabled(true);
            env = dataStore.getFeatureSource(tname("geopoint")).getBounds();
            assertTrue(env.boundsEquals2D(expected, Math.ulp(2.0)));
        } finally {
            hanaDialect.setEstimatedExtentsEnabled(false);
        }
    }
}
