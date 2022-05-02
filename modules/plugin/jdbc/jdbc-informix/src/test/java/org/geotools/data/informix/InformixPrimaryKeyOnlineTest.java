/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.informix;

import org.geotools.jdbc.JDBCPrimaryKeyOnlineTest;
import org.geotools.jdbc.JDBCPrimaryKeyTestSetup;

public class InformixPrimaryKeyOnlineTest extends JDBCPrimaryKeyOnlineTest {

    @Override
    protected JDBCPrimaryKeyTestSetup createTestSetup() {
        return new InformixPrimaryKeyTestSetup();
    }

    @Override
    public void testSequencedPrimaryKey() throws Exception {
        // Skip - in Informix the sequences are not associated with the table, so we cannot discover
        // them unless they conform to a naming convention of some kind. That cannot be relied upon
        // in a 3rd-party database. Maybe this can be improved in the future.
    }
}
