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

import java.util.List;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.BBOX3D;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

/** @author Stefan Uhrig, SAP SE */
public class HanaBBOXFilterOnlineTest extends JDBCTestSupport {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new HanaTestSetupPSPooling();
    }

    @Test
    public void testPrepare2DBBOXFilter() throws Exception {
        HanaDialect dialect = new HanaDialect(dataStore);
        HanaVersion version = new HanaVersion("2.00.60.0.0");
        HanaFilterToSQL filterToSQL = new HanaFilterToSQL(dialect, true, version);

        FilterFactory ff = dataStore.getFilterFactory();
        BBOX filter = ff.bbox(aname("GEOM"), 1, 2, 3, 4, "EPSG:4326");
        String s = filterToSQL.encodeToString(filter);
        assertEquals(
                "WHERE GEOM.ST_IntersectsRectPlanar(ST_GeomFromWKB(?, -1), ST_GeomFromWKB(?, -1)) = 1",
                s);

        GeometryFactory f = new GeometryFactory();
        List<Object> literals = filterToSQL.getLiteralValues();
        assertEquals(2, literals.size());
        assertEquals(f.createPoint(new Coordinate(1, 2)), literals.get(0));
        assertEquals(f.createPoint(new Coordinate(3, 4)), literals.get(1));
    }

    @Test
    public void testPrepare3DBBOXFilter() throws Exception {
        HanaDialect dialect = new HanaDialect(dataStore);
        HanaVersion version = new HanaVersion("2.00.60.0.0");
        HanaFilterToSQL filterToSQL = new HanaFilterToSQL(dialect, true, version);

        FilterFactory ff = dataStore.getFilterFactory();
        CoordinateReferenceSystem crs = decodeEPSG(4326);
        BBOX3D filter = ff.bbox("GEOM", new ReferencedEnvelope3D(1, 2, 3, 4, 5, 6, crs));
        String s = filterToSQL.encodeToString(filter);
        assertEquals(
                "WHERE GEOM.ST_IntersectsRectPlanar(ST_GeomFromWKB(?, -1), ST_GeomFromWKB(?, -1)) = 1 AND GEOM.ST_ZMin() <= ? AND GEOM.ST_ZMax() >= ?",
                s);

        GeometryFactory f = new GeometryFactory();
        List<Object> literals = filterToSQL.getLiteralValues();
        assertEquals(4, literals.size());
        assertEquals(f.createPoint(new Coordinate(1, 3)), literals.get(0));
        assertEquals(f.createPoint(new Coordinate(2, 4)), literals.get(1));
        assertEquals(6.0, literals.get(2));
        assertEquals(5.0, literals.get(3));
    }
}
