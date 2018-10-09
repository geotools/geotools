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

import org.geotools.jdbc.JDBCGeographyOnlineTest;
import org.geotools.jdbc.JDBCGeographyTestSetup;
import org.geotools.jdbc.VirtualTable;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;

/** @author Stefan Uhrig, SAP SE */
public class HanaGeographyOnlineTest extends JDBCGeographyOnlineTest {

    @Override
    protected JDBCGeographyTestSetup createTestSetup() {
        return new HanaGeographyTestSetup(new HanaTestSetup());
    }

    @Override
    public void testDistanceGreatCircle() throws Exception {
        // Currently, HANA only supports point-point distances
    }

    @SuppressWarnings("deprecation")
    @Override
    public void testVirtualTable() throws Exception {
        // We have to override to use the proper schema in the select-statement.
        VirtualTable vt =
                new VirtualTable("geopoint_vt", "SELECT * FROM \"geotools\".\"geopoint\"");
        dataStore.addVirtualTable(vt);

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

        int epsg =
                CRS.lookupEpsgCode(
                        ((GeometryDescriptor) ft.getDescriptor(aname("geo")))
                                .getCoordinateReferenceSystem(),
                        false);
        assertEquals(4326, epsg);
    }
}
