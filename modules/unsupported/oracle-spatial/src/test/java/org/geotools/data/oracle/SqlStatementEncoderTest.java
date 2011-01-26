/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.filter.SQLEncoderOracle;
import org.opengis.feature.simple.SimpleFeatureType;

public class SqlStatementEncoderTest extends TestCase {
	private SQLEncoderOracle encoder;

	private SqlStatementEncoder sql;

	protected void setUp() throws Exception {
		encoder = new SQLEncoderOracle(4326);
		sql = new SqlStatementEncoder(encoder, "table", "fid");
		super.setUp();
	}

	/*
	 * Test method for
	 * 'org.geotools.data.oracle.SqlStatementEncoder.makeCreateTableSQL(FeatureType)'
	 */
	public void testMakeCreateTableSQL() throws Exception {
		SimpleFeatureType schema = DataUtilities.createType("ignore",
				"name:String, line:MultiLineString, measure:Integer");
		String create = sql.makeCreateTableSQL(schema);
//		System.out.println(create);
		String expected = "CREATE TABLE table(fid NUMBER,name VARCHAR, line MDSYS.SDO_GEOMETRY, measure INTEGER)";
		assertEquals(expected, create);
	}

	/*
	 * Test method for
	 * 'org.geotools.data.oracle.SqlStatementEncoder.makeCreateTableSQL(FeatureType)'
	 */
	public void testMakeCreateFixIndex() throws Exception {
		String create = sql.makeCreateFidIndex();
		String expected = "CREATE UNIQUE INDEX table_index ON (fid )";
		assertEquals(expected, create);
	}

	/*
	 * Test method for
	 * 'org.geotools.data.oracle.SqlStatementEncoder.makeCreateGeomIndex()'
	 */
	public void testMakeCreateIndexSQL() throws SchemaException {
		SimpleFeatureType schema = DataUtilities.createType("ignore",
				"name:String, line:MultiLineString, measure:Integer");
		String create = sql.makeCreateGeomIndex(schema);
		String expected = "CREATE INDEX table_sidx ON table( line) INDEXTYPE IS mdsys.spatial_index";
		assertEquals(expected, create);
	}

}
