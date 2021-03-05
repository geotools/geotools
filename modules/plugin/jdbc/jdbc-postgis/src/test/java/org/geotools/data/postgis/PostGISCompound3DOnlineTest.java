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
package org.geotools.data.postgis;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCCompound3DOnlineTest;
import org.geotools.jdbc.JDBCCompound3DTestSetup;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;

public class PostGISCompound3DOnlineTest extends JDBCCompound3DOnlineTest {

    @Override
    protected JDBCCompound3DTestSetup createTestSetup() {
        return new PostGISCompound3DTestSetup(new PostGISTestSetup());
    }

    /** Make sure we can properly retrieve the bounds of 3d layers */
    @Override
    public void testBounds() throws Exception {
        ((PostGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(true);

        ReferencedEnvelope env = dataStore.getFeatureSource(tname(getLine3d())).getBounds();

        // check we got the right 2d component
        Envelope expected = new Envelope(1, 5, 0, 4);
        assertEquals(expected.getMinX(), env.getMinX(), 0.05);
        assertEquals(expected.getMaxX(), env.getMaxX(), 0.05);
        assertEquals(expected.getMinY(), env.getMinY(), 0.05);
        assertEquals(expected.getMaxY(), env.getMaxY(), 0.05);

        // check the srs the expected one
        assertEquals(CRS.getHorizontalCRS(crs), env.getCoordinateReferenceSystem());
    }
}
