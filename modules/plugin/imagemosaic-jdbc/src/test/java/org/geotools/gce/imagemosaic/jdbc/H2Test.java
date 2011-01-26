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

import java.awt.Color;
import java.net.URL;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * @author mcr
 * 
 */
/**
 * @author mcr
 * 
 */
// 
public class H2Test extends AbstractTest {
	public static String EPSG_31287_TOWGS84 = "PROJCS[\"MGI / Austria Lambert\",GEOGCS[\"MGI\", DATUM[\"Militar-Geographische Institut\","
			+ "SPHEROID[\"Bessel 1841\", 6377397.155, 299.1528128, AUTHORITY[\"EPSG\",\"7004\"]], "
			+ "TOWGS84[577.326,90.129,463.919,5.137,1.474,5.297,2.4232],AUTHORITY[\"EPSG\",\"6312\"]],"
			+ "PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], UNIT[\"degree\", 0.017453292519943295],"
			+ "AXIS[\"Geodetic longitude\", EAST], AXIS[\"Geodetic latitude\", NORTH], AUTHORITY[\"EPSG\",\"4312\"]],"
			+ "PROJECTION[\"Lambert Conic Conformal (2SP)\", AUTHORITY[\"EPSG\",\"9802\"]], PARAMETER[\"central_meridian\", 13.333333333333334],"
			+ "PARAMETER[\"latitude_of_origin\", 47.5], PARAMETER[\"standard_parallel_1\", 49.0], PARAMETER[\"false_easting\", 400000.0],"
			+ "PARAMETER[\"false_northing\", 400000.0], PARAMETER[\"standard_parallel_2\", 46.0], UNIT[\"m\", 1.0],"
			+ "AXIS[\"Easting\", EAST], AXIS[\"Northing\", NORTH], AUTHORITY[\"EPSG\",\"31287\"]] ";

	protected static CoordinateReferenceSystem SOURCE;

	protected static CoordinateReferenceSystem TARGET;

	static DBDialect dialect = null;

	{
		try {
			TARGET = CRS.parseWKT(EPSG_31287_TOWGS84);
			SOURCE = CRS.decode("EPSG:4326");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public H2Test(String test) {
		super(test);
	}

	@Override
	public String getConfigUrl() {
		return "file:target/resources/oek.h2.xml";
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();

		H2Test test = new H2Test("");

		if (test.checkPreConditions() == false) {
			return suite;
		}

		suite.addTest(new H2Test("testScripts"));
		suite.addTest(new H2Test("testIsSameFile"));
		suite.addTest(new H2Test("testImportParamList"));
		suite.addTest(new H2Test("testGetConnection"));
		suite.addTest(new H2Test("testDrop"));
		suite.addTest(new H2Test("testCreate"));
		suite.addTest(new H2Test("testImage1"));
		suite.addTest(new H2Test("testFullExtent"));
		suite.addTest(new H2Test("testNoData"));
		suite.addTest(new H2Test("testPartial"));
		suite.addTest(new H2Test("testVienna"));
		suite.addTest(new H2Test("testViennaEnv"));
		suite.addTest(new H2Test("testOutputTransparentColor"));
		suite.addTest(new H2Test("testOutputTransparentColor2"));
		suite.addTest(new H2Test("testReproject1"));
		suite.addTest(new H2Test("testDrop"));
		suite.addTest(new H2Test("testCreateJoined"));
		suite.addTest(new H2Test("testImage1Joined"));
		suite.addTest(new H2Test("testFullExtentJoined"));
		suite.addTest(new H2Test("testNoDataJoined"));
		suite.addTest(new H2Test("testPartialJoined"));
		suite.addTest(new H2Test("testViennaJoined"));
		suite.addTest(new H2Test("testViennaEnvJoined"));
		suite.addTest(new H2Test("testDrop"));
		suite.addTest(new H2Test("testCloseConnection"));

		return suite;
	}

	@Override
	protected String getSubDir() {
		return "h2";
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

	public void testReproject1() {
		JDBCAccess access = getJDBCAccess();
		ImageLevelInfo li = access.getLevelInfo(access.getNumOverviews());

		GeneralEnvelope env = new GeneralEnvelope(new double[] {
				li.getExtentMaxY() - DELTA, li.getExtentMaxX() - DELTA },
				new double[] { li.getExtentMaxY() + DELTA,
						li.getExtentMaxX() + DELTA });

		try {
			env.setCoordinateReferenceSystem(SOURCE);

			MathTransform t = CRS.findMathTransform(SOURCE, TARGET);
			GeneralEnvelope tenv = CRS.transform(t, env);
			// GeneralEnvelope tenv=new GeneralEnvelope(new
			// Rectangle2D.Double(300000,300000,400000,400000));
			// TARGET=CRS.decode("EPSG:31287");
			tenv.setCoordinateReferenceSystem(TARGET);
			imageMosaic("partialgreen_reprojected", getConfigUrl(), tenv, 400,
					400, Color.GREEN, null);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}


	public void setUp() throws Exception {
		// No fixture check needed
	}

	protected String getFixtureId() {
		return null;
	}

	protected String getXMLConnectFragmentName() {
		return "connect.h2.xml.inc";
	}

	protected String getDriverClassName() {
		return "org.h2.Driver";
	}

	protected String getJDBCUrl(String host, Integer port, String dbName) {
		return "jdbc:h2:target/h2/testdata";
	}

}
