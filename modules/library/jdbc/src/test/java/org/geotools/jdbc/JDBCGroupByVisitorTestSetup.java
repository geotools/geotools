/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

public abstract class JDBCGroupByVisitorTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCGroupByVisitorTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected final void setUpData() throws Exception {
        try {
            dropBuildingsTable();
        } finally {
            createBuildingsTable();
        }

        try {
            dropFt1GroupByTable();
        } finally {
            createFt1GroupByTable();
        }
    }

    protected abstract void createBuildingsTable() throws Exception;

    protected abstract void dropBuildingsTable() throws Exception;

    /**
     * It's unclear if all databases support grouping on geometry attributes. The default for this
     * method returns false. When returning true, also implement the creation/drop of the
     * ft1_group_by table.
     *
     * @return True if the database supports grouping on geometry attributes.
     */
    public boolean supportsGeometryGroupBy() {
        return false;
    }

    protected abstract void createFt1GroupByTable() throws Exception;

    protected abstract void dropFt1GroupByTable() throws Exception;
}
