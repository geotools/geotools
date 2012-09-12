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

import com.vividsolutions.jts.geom.Point;

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

//	public void testGeometryMetadataTable() throws Exception {
//        testSetup.setupGeometryColumns(dataStore);
//        
//        GeometryDescriptor gd = dataStore.getFeatureSource("GTMETA").getSchema().getGeometryDescriptor();
//        assertEquals(Point.class, gd.getType().getBinding());
//        assertEquals(4269, (int) CRS.lookupEpsgCode(gd.getCoordinateReferenceSystem(), false));
//    }
}
