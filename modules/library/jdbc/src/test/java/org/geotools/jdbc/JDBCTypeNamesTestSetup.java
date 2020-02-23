/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public abstract class JDBCTypeNamesTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCTypeNamesTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    protected final void setUpData() throws Exception {
        delegate.setUpData();

        // kill all the data
        try {
            dropTypes();
        } catch (SQLException e) {
        }

        // create all the data
        createTypes();
    }

    /**
     * Creates a table with the following schema:
     *
     * <p><code>ftntable( id:Integer; name:String; geom:POLYGON )</code> Creates a view with the
     * following schema:
     *
     * <p><code>create view ftnview as select id, geom from ft_table</code> In Addition to that,
     * there should be some database specific type structures like synonyms or aliases, if
     * available. (should be )
     */
    protected abstract void createTypes() throws Exception;

    protected abstract void dropTypes() throws Exception;

    /**
     * Returns expected type names as created in {@link #createTypes()}. At least <code>ftntable
     * </code> and <code>ftnview</code>.
     */
    protected List<String> getExpectedTypeNames() {
        List<String> expectedTypeNames = new LinkedList<String>();
        expectedTypeNames.add("ftntable");
        expectedTypeNames.add("ftnview");
        return expectedTypeNames;
    }
}
