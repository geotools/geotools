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
package org.geotools.data.db2;

import static org.junit.Assert.assertEquals;

import org.geotools.api.filter.spatial.BBOX3D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.jdbc.JDBC3DOnlineTest;
import org.geotools.jdbc.JDBC3DTestSetup;
import org.geotools.util.factory.Hints;

public class DB23DOnlineTest extends JDBC3DOnlineTest {

    @Override
    protected void connect() throws Exception {
        Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        super.connect();
    }

    @Override
    protected Integer getNativeSRID() {
        return Integer.valueOf(DB2TestUtil.SRID);
    }

    @Override
    protected JDBC3DTestSetup createTestSetup() {
        return new DB23DTestSetup();
    }

    @Override
    public void testSchema() throws Exception {
        // do nothing, not applicable
    }

    @Override
    public void testBBOX3DOutsideLine() throws Exception {
        // a bbox 3d well outside the line footprint
        BBOX3D bbox3d = FF.bbox("", new ReferencedEnvelope3D(1, 2, 1, 2, 100, 101, crs));
        SimpleFeatureCollection fc =
                dataStore.getFeatureSource(tname(getLine3d())).getFeatures(bbox3d);
        assertEquals(1, fc.size());
    }
}
