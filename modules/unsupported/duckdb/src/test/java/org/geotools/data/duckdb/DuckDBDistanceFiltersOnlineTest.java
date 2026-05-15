/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import org.geotools.jdbc.JDBCDistanceFiltersTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Ignore;
import org.junit.Test;

public class DuckDBDistanceFiltersOnlineTest extends JDBCDistanceFiltersTest {
    private static final String GEOGRAPHIC_DISTANCE_SRID_UNSUPPORTED =
            "DuckDB native GEOMETRY does not expose SRID metadata, so GeoTools cannot safely convert geographic distance units";

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new DuckDBTestSetup();
    }

    @Override
    @Test
    @Ignore(GEOGRAPHIC_DISTANCE_SRID_UNSUPPORTED)
    public void testDWithinGeographicKm() {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    @Ignore(GEOGRAPHIC_DISTANCE_SRID_UNSUPPORTED)
    public void testDWithinGeographicMeter() {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    @Ignore(GEOGRAPHIC_DISTANCE_SRID_UNSUPPORTED)
    public void testDWithinGeographicMile() {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    @Ignore(GEOGRAPHIC_DISTANCE_SRID_UNSUPPORTED)
    public void testDWithinGeographicFeet() {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    @Ignore(GEOGRAPHIC_DISTANCE_SRID_UNSUPPORTED)
    public void testBeyondGeographicKm() {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    @Ignore(GEOGRAPHIC_DISTANCE_SRID_UNSUPPORTED)
    public void testBeyondGeographicMeter() {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    @Ignore(GEOGRAPHIC_DISTANCE_SRID_UNSUPPORTED)
    public void testBeyondGeographicMile() {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    @Ignore(GEOGRAPHIC_DISTANCE_SRID_UNSUPPORTED)
    public void testBeyondGeographicFeet() {
        // Ignored on purpose. See @Ignore reason.
    }
}
