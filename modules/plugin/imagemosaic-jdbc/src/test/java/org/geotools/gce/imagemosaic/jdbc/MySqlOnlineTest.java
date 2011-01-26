/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.jdbc;

import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;

public class MySqlOnlineTest extends AbstractTest {
	static DBDialect dialect = null;

	public MySqlOnlineTest(String test) {
		super(test);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();

		MySqlOnlineTest test = new MySqlOnlineTest("");

		if (test.checkPreConditions() == false) {
			return suite;
		}

		suite.addTest(new MySqlOnlineTest("testScripts"));
		suite.addTest(new MySqlOnlineTest("testGetConnection"));
		suite.addTest(new MySqlOnlineTest("testDrop"));
		suite.addTest(new MySqlOnlineTest("testCreate"));
		suite.addTest(new MySqlOnlineTest("testImage1"));
		suite.addTest(new MySqlOnlineTest("testFullExtent"));
		suite.addTest(new MySqlOnlineTest("testNoData"));
		suite.addTest(new MySqlOnlineTest("testPartial"));
		suite.addTest(new MySqlOnlineTest("testVienna"));
		suite.addTest(new MySqlOnlineTest("testViennaEnv"));
		suite.addTest(new MySqlOnlineTest("testDrop"));
		suite.addTest(new MySqlOnlineTest("testCreateJoined"));
		suite.addTest(new MySqlOnlineTest("testImage1Joined"));
		suite.addTest(new MySqlOnlineTest("testFullExtentJoined"));
		suite.addTest(new MySqlOnlineTest("testNoDataJoined"));
		suite.addTest(new MySqlOnlineTest("testPartialJoined"));
		suite.addTest(new MySqlOnlineTest("testViennaJoined"));
		suite.addTest(new MySqlOnlineTest("testViennaEnvJoined"));
		suite.addTest(new MySqlOnlineTest("testDrop"));
		suite.addTest(new MySqlOnlineTest("testCloseConnection"));

		return suite;
	}

	@Override
	public String getConfigUrl() {
		return "file:target/resources/oek.mysql.xml";
	}

	@Override
	protected String getSubDir() {
		return "mysql";
	}

	@Override
	protected DBDialect getDBDialect() {
		if (dialect != null) {
			return dialect;
		}

		Config config = null;

		try {
			config = Config.readFrom(new URL(getConfigUrl()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		dialect = DBDialect.getDBDialect(config);

		return dialect;
	}

	protected String getXMLConnectFragmentName() {
		return "connect.mysql.xml.inc";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.AbstractTest#getDriverClassName()
	 */
	protected String getDriverClassName() {
		return "com.mysql.jdbc.Driver";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.AbstractTest#getJDBCUrl(java.lang.String,
	 *      java.lang.Integer, java.lang.String)
	 */
	protected String getJDBCUrl(String host, Integer port, String dbName) {
		return "jdbc:mysql://" + host + ":" + port + "/" + dbName;
	}

}
