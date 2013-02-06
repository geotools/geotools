/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  https://github.com/nfms4redd/nfms-geobatch
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCGeometryTest;
import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.geotools.referencing.CRS;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author DamianoG
 * 
 */
public class SQLServerGeometryTest extends JDBCGeometryTest {

    private SQLServerGeometryTestSetup testSetup;

    @Override
    protected JDBCGeometryTestSetup createTestSetup() {
        testSetup = new SQLServerGeometryTestSetup();
        return testSetup;
    }

    @Override
    public void testPoint() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(Point.class, checkGeometryType(Point.class));
    }

    @Override
    public void testLineString() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(LineString.class, checkGeometryType(LineString.class));
    }

    @Override
    public void testLinearRing() throws Exception {
        // LinearRing is not supported by SQLServer
    }

    @Override
    public void testPolygon() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(Polygon.class, checkGeometryType(Polygon.class));
    }

    @Override
    public void testMultiPoint() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(MultiPoint.class, checkGeometryType(MultiPoint.class));
    }

    @Override
    public void testMultiLineString() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(MultiLineString.class, checkGeometryType(MultiLineString.class));
    }

    @Override
    public void testMultiPolygon() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(MultiPolygon.class, checkGeometryType(MultiPolygon.class));
    }

    @Override
    public void testGeometry() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(Geometry.class, checkGeometryType(Geometry.class));
    }

    @Override
    public void testGeometryCollection() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        assertEquals(GeometryCollection.class, checkGeometryType(GeometryCollection.class));
    }
    
    public void testGeometryMetadataTable() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        
        GeometryDescriptor gd = dataStore.getFeatureSource(tname("gtmeta")).getSchema().getGeometryDescriptor();
        assertEquals(Point.class, gd.getType().getBinding());
        assertEquals(4326, (int) CRS.lookupEpsgCode(gd.getCoordinateReferenceSystem(), false));
    }

}
