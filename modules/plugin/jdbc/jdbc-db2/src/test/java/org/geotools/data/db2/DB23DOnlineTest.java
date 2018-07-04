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

import org.geotools.jdbc.JDBC3DOnlineTest;
import org.geotools.jdbc.JDBC3DTestSetup;

/** @source $URL$ */
public class DB23DOnlineTest extends JDBC3DOnlineTest {

    @Override
    protected Integer getNativeSRID() {
        return new Integer(DB2TestUtil.SRID);
    }

    @Override
    protected JDBC3DTestSetup createTestSetup() {
        return new DB23DTestSetup();
    }

    @Override
    public void testSchema() throws Exception {
        // do nothing, not applicable
    }
}
