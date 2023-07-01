/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

import java.sql.Connection;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.cs.AxisDirection;
import org.geotools.jdbc.JDBCTestSupport;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;

/**
 * Check the handling of CRS when accessing PostGIS.
 *
 * <p>Specifically the function PostgisSQLDialect#createCRS
 *
 * @author Roar Brænden
 */
public class PostgisCRSOnlineTest extends JDBCTestSupport {

    @Override
    protected PostgisCRSTestSetup createTestSetup() {
        return new PostgisCRSTestSetup();
    }

    /** PostGIS are operating with a forceLongitudeFirst axis order. */
    @Test
    public void testCreateCRSwithNorthEastAxis() throws Exception {
        CoordinateReferenceSystem priorCrs = CRS.decode("EPSG:4326");
        Assert.assertEquals(
                AxisDirection.NORTH, priorCrs.getCoordinateSystem().getAxis(0).getDirection());

        try (Connection conn = dataStore.getDataSource().getConnection()) {
            CoordinateReferenceSystem crs = dialect.createCRS(4326, conn);
            Assert.assertEquals(
                    AxisDirection.EAST, crs.getCoordinateSystem().getAxis(0).getDirection());
        }
    }

    /** PostGIS uses ESRI codes. We prefer gt-epsg-extension definitions in esri.properties. */
    @Test
    public void testCreateCRSwithESRIProps() throws Exception {
        int northPoleLambertAzimuthalSRID = 102017;
        try (Connection conn = dataStore.getDataSource().getConnection()) {
            CoordinateReferenceSystem crs = dialect.createCRS(northPoleLambertAzimuthalSRID, conn);
            Assert.assertNull("We should'nt get a CRS because GT doesn't support the axis", crs);
        }
    }

    /** PostGIS do also have some WKT that could be used if we don't have anything else */
    @Test
    public void testCreateCRSwithPostgisWKT() throws Exception {
        int germanyZone1SRID = 31491;
        try (Connection conn = dataStore.getDataSource().getConnection()) {
            CoordinateReferenceSystem crs = dialect.createCRS(germanyZone1SRID, conn);
            Assert.assertNotNull("We should've gotten a CRS based on the WKT.", crs);
            Assert.assertEquals("ESRI:Germany_Zone_1", crs.getName().toString());
        }
    }
}
