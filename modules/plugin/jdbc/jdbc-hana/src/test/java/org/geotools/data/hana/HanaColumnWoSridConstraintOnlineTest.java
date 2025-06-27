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
package org.geotools.data.hana;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import java.sql.Connection;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;

/** @author Stefan Uhrig, SAP SE */
public class HanaColumnWoSridConstraintOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new HanaColumnWoSridConstraintTestSetup();
    }

    private boolean supportsColumnsWoSridConstraint(HanaVersion version) {
        return version.getVersion() > 2 || version.getVersion() == 2 && version.getRevision() >= 60;
    }

    @Test
    public void testFindSridOnColumn() throws Exception {
        try (Connection conn = dataStore.getDataSource().getConnection()) {
            HanaVersion version = new HanaVersion(conn.getMetaData().getDatabaseProductVersion());
            assumeTrue(supportsColumnsWoSridConstraint(version));

            HanaDialect dialect = (HanaDialect) this.dialect;
            dialect.initializeConnection(conn);
            Integer srid =
                    dialect.getGeometrySRID(dataStore.getDatabaseSchema(), tname("tabwosc"), aname("geom"), conn);
            assertEquals(Integer.valueOf(3857), srid);
        }
    }

    @Test
    public void testFilterOnColumn() throws Exception {
        try (Connection conn = dataStore.getDataSource().getConnection()) {
            HanaVersion version = new HanaVersion(conn.getMetaData().getDatabaseProductVersion());
            assumeTrue(supportsColumnsWoSridConstraint(version));
        }

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        BBOX bbox = ff.bbox(aname("geom"), 0, 0, 10, 10, "EPSG:3857");
        ContentFeatureCollection features =
                dataStore.getFeatureSource(tname("tabwosc")).getFeatures(bbox);
        assertEquals(1, features.size());
    }
}
