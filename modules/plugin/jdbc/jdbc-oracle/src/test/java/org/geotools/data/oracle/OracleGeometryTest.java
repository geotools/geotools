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

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.jdbc.JDBCGeometryTest;
import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.io.WKTReader;

public class OracleGeometryTest extends JDBCGeometryTest {

    @Override
    protected JDBCGeometryTestSetup createTestSetup() {
        return new OracleGeometryTestSetup();
    }

    public void testLinearRing() throws Exception {
        assertEquals(LineString.class, checkGeometryType(LinearRing.class));
    }
    
    public void testComplexGeometryFallback() throws Exception {
        SimpleFeatureIterator fi = dataStore.getFeatureSource("COLA_MARKETS_CS").getFeatures().features();
        assertTrue(fi.hasNext());
        SimpleFeature sf = fi.next();
        assertNotNull(sf.getDefaultGeometry());
        Geometry expected = new WKTReader().read("POLYGON((6 4, 12 4, 12 12, 6 12, 6 4))");
        assertTrue(expected.equals((Geometry) sf.getDefaultGeometry()));
        fi.close();
    }
}
