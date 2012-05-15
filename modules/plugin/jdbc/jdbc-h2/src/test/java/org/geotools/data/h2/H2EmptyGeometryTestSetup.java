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
package org.geotools.data.h2;

import org.geotools.jdbc.JDBCEmptyGeometryTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/plugin/jdbc/jdbc-postgis/src/test/java/org/geotools/data/postgis/PostGISBooleanTestSetup.java $
 */
public class H2EmptyGeometryTestSetup extends JDBCEmptyGeometryTestSetup {

    public H2EmptyGeometryTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

	@Override
	protected void createEmptyGeometryTable() throws Exception {
		//create table schema
		 run("CREATE TABLE \"geotools\".\"empty\"(" //
	                + "\"fid\" serial primary key, " //
	                + "\"id\" integer, " //
	                + "\"geom_point\" POINT, " //
	                + "\"geom_linestring\" LINESTRING, " //
	                + "\"geom_polygon\" POLYGON, " //
                    + "\"geom_multipoint\" MULTIPOINT, " //	                
                    + "\"geom_multilinestring\" MULTILINESTRING, " //
                    + "\"geom_multipolygon\" MULTIPOLYGON, " //                   
	                + "\"name\" varchar" //
	                + ")");
		 
        run("CALL AddGeometryColumn('geotools', 'empty', 'geom_point', 4326, 'POINT', 2)");
        run("CALL AddGeometryColumn('geotools', 'empty', 'geom_linestring', 4326, 'LINESTRING', 2)");
        run("CALL AddGeometryColumn('geotools', 'empty', 'geom_polygon', 4326, 'POLYGON', 2)");
        run("CALL AddGeometryColumn('geotools', 'empty', 'geom_multipoint', 4326, 'MULTIPOINT', 2)");
        run("CALL AddGeometryColumn('geotools', 'empty', 'geom_multilinestring', 4326, 'MULTILINESTRING', 2)");
        run("CALL AddGeometryColumn('geotools', 'empty', 'geom_multipolygon', 4326, 'MULTIPOLYGON', 2)");
	}

	@Override
	protected void dropEmptyGeometryTable() throws Exception {
         run("CALL DropGeometryColumn('geotools', 'empty', 'geom_point')");
         run("CALL DropGeometryColumn('geotools', 'empty', 'geom_linestring')");
         run("CALL DropGeometryColumn('geotools', 'empty', 'geom_polygon')");
         run("CALL DropGeometryColumn('geotools', 'empty', 'geom_multipoint')");
         run("CALL DropGeometryColumn('geotools', 'empty', 'geom_multilinestring')");
         run("CALL DropGeometryColumn('geotools', 'empty', 'geom_multipolygon')");
		 run( "DROP TABLE \"geotools\".\"empty\"");
		
	}
    

}
