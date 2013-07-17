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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCTypeNamesTestSetup;

public class PostgisTypeNamesTestSetup extends JDBCTypeNamesTestSetup {

	protected PostgisTypeNamesTestSetup() {
		super(new PostGISTestSetup());
	}

	@Override
	protected void createTypes() throws Exception {
		run("CREATE TABLE \"ftntable\" ("
				+ "\"id\" INT, \"name\" VARCHAR, \"geom\" GEOMETRY)");
		run("CREATE VIEW \"ftnview\" AS SELECT \"id\", \"geom\" FROM \"ftntable\"");
	}

	@Override
	protected void dropTypes() throws Exception {
		runSafe("DROP VIEW \"ftnview\"");
		runSafe("DROP TABLE \"ftntable\"");
	}

}
