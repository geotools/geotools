/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005-2007. All rights reserved.
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
 *
 */
package org.geotools.data.db2;

import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.data.db2.filter.SQLEncoderDB2;
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.IllegalFilterException;
import org.geotools.referencing.CRS;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

/**
 * DOCUMENT ME!
 * 
 * @author David Adler
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/unsupported/db2/src/test/java/org/geotools/data/db2/SQLEncoderDB2Test.java $
 */
public class SQLEncoderDB2Test extends TestCase {
//	private static Map DB2_SPATIAL_PREDICATES = new HashMap();

	private SQLEncoderDB2 encoder;

	FilterFactory2 ff = null;

	Literal bboxLiteral = null;

	String sqlString = null;

	PropertyName spatialColumn = null;

	Literal doubleLiteral = null;

	Literal geometryLiteral = null;

	Literal polygonLiteral = null;
	Envelope bboxEnv = null;

	/**
	 * Setup creates an encoder and an expression to encode
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void setUp() throws Exception {
		super.setUp();
		encoder = new SQLEncoderDB2();
		encoder.setSqlNameEscape("\"");
		ff = (FilterFactory2) CommonFactoryFinder.getFilterFactory(null);
		bboxLiteral = null;

		spatialColumn = ff.property("Geom");
		doubleLiteral = ff.literal(1);

		WKTReader reader = new WKTReader();
		LineString line = (LineString) reader.read("LINESTRING (0 0, 300 300)");
		bboxEnv = line.getEnvelopeInternal();
		geometryLiteral = ff.literal(line);
		polygonLiteral = ff.literal((Polygon) reader
				.read("POLYGON ((-76 41, -76 42, -74 42, -74 41, -76 41))"));
		
		// stuff below not currently used - was testing opengis geometries
		CoordinateReferenceSystem crs = null;
		crs = CRS.decode("EPSG:4269");
		try {
			//       Geometry geom = JTSUtils.jtsToGo1(line,crs); // causes
			// FactoryNotFoundException
			Object obj = ff.contains(spatialColumn, geometryLiteral);
			//        Object obj2 = ff.contains("Geom", geom);
			System.out.println(obj);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Creates a geometry filter that uses the left and right geometries created
	 * by the setup method.
	 * 
	 * @param opClass Class of the filter (Contains, Intersects, etc)
	 * 
	 * @return a BinarySpatialOperator
	 * 
	 * @throws IllegalFilterException
	 */
	private BinarySpatialOperator createGeometryFilter(Class opClass)
			throws IllegalFilterException {
		BinarySpatialOperator gf = null;
		gf = createGeometryFilter(opClass, spatialColumn, polygonLiteral);
		return gf;
	}

	/**
	 * Creates a geometry filter with the specified filter type, left and right
	 * expressions.
	 * 
	 * @param opClass Class of the filter (Contains, Intersects, etc)
	 * @param left
	 * @param right
	 * 
	 * @return a BinarySpatialOperato
	 * 
	 * @throws IllegalFilterException
	 */
	private BinarySpatialOperator createGeometryFilter(Class opClass,
			PropertyName left, Literal right) throws IllegalFilterException {
		BinarySpatialOperator gf = null;

		if (opClass == Contains.class) {
			gf = ff.contains(left, right);
		} else if (opClass == Crosses.class) {
			gf = ff.crosses(left, right);
		} else if (opClass == Disjoint.class) {
			gf = ff.disjoint(left, right);
		} else if (opClass == Equals.class) {
			gf = ff.equal(left, right);
		} else if (opClass == Intersects.class) {
			gf = ff.intersects(left, right);
		} else if (opClass == Overlaps.class) {
			gf = ff.overlaps(left, right);
		} else if (opClass == Touches.class) {
			gf = ff.touches(left, right);
		} else if (opClass == Within.class) {
			gf = ff.within(left, right);
		} else {
			// This shouldn't happen since we will have pulled out
			// the unsupported parts before invoking this method
			String msg = "unsupported filter type" + opClass.toString();
			System.out.println(msg);
		}
		return gf;
	}

	/**
	 * Creates a distance filter with the specified left and right
	 * expressions and distance.
	 * 
	 * @param left
	 * @param right
	 * @param distance
	 * 
	 * @return
	 * 
	 * @throws IllegalFilterException
	 */
	private BinarySpatialOperator createDistanceFilter(PropertyName left,
			Literal right, double distance) throws IllegalFilterException {

		BinarySpatialOperator gf = ff.dwithin(left, right, distance, "");
		return gf;
	}

	private BinarySpatialOperator createBeyondFilter(PropertyName left,
			Literal right, double distance) throws IllegalFilterException {

		BinarySpatialOperator gf = ff.beyond(left, right, distance, "");
		return gf;
	}
	private BBOX createBBOXFilter(PropertyName left,
			Envelope env) throws IllegalFilterException {

    	double xmin = env.getMinX();
    	double ymin = env.getMinY();
    	double xmax = env.getMaxX();
    	double ymax = env.getMaxY();
    	BBOX bbox = ff.bbox(left,xmin,ymin,xmax,ymax,"");
		return bbox;
	}
	
	public void testDistance() throws IllegalFilterException,
			FilterToSQLException {
		BinarySpatialOperator gf = null;
		encoder.setSelectivityClause(null);
		gf = createBeyondFilter(spatialColumn, geometryLiteral, 10.0);
		encoder.setSelectivityClause(null);
		sqlString = this.encoder.encodeToString(gf);

		assertEquals(
				"Beyond",
				"WHERE db2gse.ST_Distance(\"Geom\", db2gse.ST_LINESTRING('LINESTRING (0 0, 300 300)', 1)) > 10.0",
				sqlString);
		gf = createDistanceFilter(spatialColumn, geometryLiteral, 10.0);
		encoder.setSelectivityClause(null);
		sqlString = this.encoder.encodeToString(gf);
		assertEquals(
				"DWithin",
				"WHERE db2gse.ST_Distance(\"Geom\", db2gse.ST_LINESTRING('LINESTRING (0 0, 300 300)', 1)) < 10.0",
				sqlString);
		
		encoder.setSelectivityClause("SELECTIVITY 0.01");
		sqlString = this.encoder.encodeToString(gf);
		assertEquals(
				"DWithin",
				"WHERE db2gse.ST_Distance(\"Geom\", db2gse.ST_LINESTRING('LINESTRING (0 0, 300 300)', 1)) < 10.0 SELECTIVITY 0.01",
				sqlString);		
	}

	public void testBBOX() throws IllegalFilterException, FilterToSQLException {

		encoder.setSelectivityClause(null);

        BBOX bbox = createBBOXFilter(spatialColumn, bboxEnv);
		encoder.setSelectivityClause(null);
		sqlString = this.encoder.encodeToString(bbox);
		assertEquals(
				"geometry literal",
				"WHERE db2gse.EnvelopesIntersect(\"Geom\", 0.0, 0.0, 300.0, 300.0, 1) = 1",
				sqlString);

		encoder.setSelectivityClause("SELECTIVITY 0.001");
		sqlString = this.encoder.encodeToString(bbox);
		assertEquals(
				"geometry literal",
				"WHERE db2gse.EnvelopesIntersect(\"Geom\", 0.0, 0.0, 300.0, 300.0, 1) = 1 SELECTIVITY 0.001",
				sqlString);

	}

	public void testCommonPredicates() throws IllegalFilterException,
			FilterToSQLException {

		BinarySpatialOperator gf = null;
		Set keys = encoder.getPredicateMap().keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			Class filterType = (Class) it.next();
			String predicateName = (String) encoder.getPredicateMap()
					.get(filterType);

			if (predicateName.equals("EnvelopesIntersect")
					|| predicateName.equals("ST_Distance")) { // skip - tested separately
				continue;
			}

			System.out.println("Testing predicate '" + predicateName
					+ "' type = " + filterType);
			gf = createGeometryFilter(filterType);
			encoder.setSelectivityClause(null);
			sqlString = this.encoder.encodeToString(gf);
			String expected = "WHERE db2gse."
					+ predicateName
					+ "(\"Geom\", db2gse.ST_POLYGON('POLYGON ((-76 41, -76 42, -74 42, -74 41, -76 41))', 1)) = 1";
			assertEquals("Testing predicate: " + predicateName, expected,
					sqlString);
		}
	}

	public void testGetCapabilities() throws IllegalFilterException {
		        FilterCapabilities fc = encoder.getCapabilities();

		        assertTrue("Check if filter supported",
		 fc.fullySupports(createGeometryFilter(Contains.class)));
	}
}
