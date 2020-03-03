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
package org.geotools.jdbc;

import java.util.Arrays;
import java.util.List;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public abstract class JDBCGeometryTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCGeometryTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        // clean up all the tables and their metadata
        for (Class geomClass : getGeometryClasses()) {
            try {
                dropSpatialTable("t" + geomClass.getSimpleName());
            } catch (Exception e) {
                // it's ok, the table might not be there
            }
        }
    }

    @Override
    protected void setUpData() throws Exception {
        // nothing to do, the tables are setup within the test
        // this method is overridden so that we avoid calling the delegate setUpData method
    }

    /** The list of geometry classes the datastore will be tested against. */
    protected List<Class> getGeometryClasses() {
        return Arrays.asList(
                new Class[] {
                    Point.class,
                    LineString.class,
                    LinearRing.class,
                    Polygon.class,
                    MultiPoint.class,
                    MultiLineString.class,
                    MultiPolygon.class,
                    Geometry.class,
                    GeometryCollection.class
                });
    }

    /**
     * Must remove the spatial table and all metadata associated to it, leaving the DBMS in a state
     * where the same table can be re-created without issues
     */
    protected abstract void dropSpatialTable(String tableName) throws Exception;
}
