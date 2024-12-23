/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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

public abstract class JDBCIAUTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCIAUTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void setUpData() throws Exception {
        try {
            dropMarsPoiTable();
            dropMarsGeology();
            removeIAU49901();
        } catch (Exception e) {
        }

        createMarsPoiTable();
    }

    /**
     * Creates a table named 'mars_poi' which has a variety of temporal attributes, with the following schema: *
     *
     * <p>The table has the following data: <code>
     * _=geom:Point:authority=IAU;srid=49900,name:String,diameter:Double
     * mars.1=POINT (-36.897 -27.2282)|Blunck|66.485
     * mars.2=POINT (-36.4134 -30.3621)|Martynov|61
     * mars.3=POINT (-2.75999999999999 -86.876)|Australe Mensa|172
     * </code> The srid for the CRS should be 949900.
     */
    protected abstract void createMarsPoiTable() throws Exception;

    /** Drops the 'mars_poi' table and removes srid 949900 */
    protected abstract void dropMarsPoiTable() throws Exception;

    /** Drops the 'mars_geology' table, created via "createSchema" */
    protected abstract void dropMarsGeology() throws Exception;

    /** Removes the registration of IAU:49901 from the database, if any was created */
    protected abstract void removeIAU49901() throws Exception;
}
