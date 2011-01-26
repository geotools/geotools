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

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.SQLEncoderException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Intersects;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * DB2 Feature Source implementation. Overrides functionality in
 * JDBCFeatureSource to provide more efficient or more appropriate DB2-specific
 * implementation.
 * 
 * @author David Adler - IBM Corporation
 * @source $URL:
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/unsupported/db2/src/main/java/org/geotools/data/db2/DB2FeatureSource.java $
 */
public class DB2FeatureSource extends JDBCFeatureSource {

	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.db2");

	/**
	 * Constructs a feature source based on a DB2 data store for a specified
	 * feature type.
	 * 
	 * @param dataStore
	 * @param featureType
	 */
	public DB2FeatureSource(DB2DataStore dataStore, SimpleFeatureType featureType) {
		super(dataStore, featureType);

	}

	/**
	 * Closes everything associated with a query, the ResultSet, Statement and
	 * Connection.
	 * 
	 * @param rs
	 *            the ResultSet
	 * @param stmt
	 *            the Statement
	 * @param conn
	 *            the Connection
	 * @param transaction
	 *            the Transaction
	 * @param e
	 *            the SQLException, if any, or null
	 */
	protected void closeAll(ResultSet rs, Statement stmt, Connection conn,
			Transaction transaction, SQLException e) {
		close(rs);
		close(stmt);
		close(conn, transaction, e);
	}

	/**
	 * Gets the bounds of the feature using the specified query.
	 * 
	 * @param query
	 *            a query object.
	 * 
	 * @return the envelope representing the bounds of the features.
	 * 
	 * @throws IOException
	 *             if there was an encoder problem.
	 * @throws DataSourceException
	 *             if there was an error executing the query to get the bounds.
	 */
	public ReferencedEnvelope getBounds(Query query) throws IOException {
		ReferencedEnvelope env = new ReferencedEnvelope();
		CoordinateReferenceSystem crs = null;
		LOGGER.fine("Query: " + query.toString());

		if (getSchema() != null) {
			String typeName = getSchema().getTypeName();
			GeometryDescriptor geomType = getSchema().getGeometryDescriptor();

			if (geomType != null) {
				Filter filter = query.getFilter();
				Class filterClass = filter.getClass();

				if (filterClass == Intersects.class
						|| filterClass == BBOX.class) {
					filter = fixNullGeomFilter(filter);
				}
				if (filter != Filter.EXCLUDE) {
					String sqlStmt = null;
					try {
						DB2SQLBuilder builder = (DB2SQLBuilder) ((DB2DataStore) this
								.getDataStore()).getSqlBuilder(typeName);
						LOGGER.fine("Filter: " + filter.toString());
						sqlStmt = builder.buildSQLBoundsQuery(typeName,
								geomType, filter);
					} catch (SQLEncoderException e) {
						throw new IOException("SQLEncoderException: " + e);
					}

					Connection conn = null;
					Transaction transaction = null;
					Statement statement = null;
					ResultSet results = null;

					try {
						conn = getConnection();
						transaction = getTransaction();
						statement = conn.createStatement();
						results = statement.executeQuery(sqlStmt);
						if (results.next()) {
							double minx = results.getDouble(1);
							double miny = results.getDouble(2);
							double maxx = results.getDouble(3);
							double maxy = results.getDouble(4);
							env = new ReferencedEnvelope(minx, maxx, miny, maxy, null);
						} else {
							env = new ReferencedEnvelope();
						}
					} catch (SQLException e) {
						closeAll(results, statement, conn, transaction, e);
						System.out.println(e);
						throw new DataSourceException("Could not get bounds "
								+ query.getHandle(), e);
					}

					closeAll(results, statement, conn, transaction, null);
				}

				crs = geomType.getCoordinateReferenceSystem();
				env = new ReferencedEnvelope(env, crs);
			}
		}

		LOGGER.fine("Bounds: " + env.toString());

		return env;
	}

	/*
	 * Sometimes getBounds gets a filter with no geometry column - we substitute
	 * the default geometry column, if one can be found.
	 */
	private Filter fixNullGeomFilter(Filter inFilter) {
		BinarySpatialOperator operator = (BinarySpatialOperator) inFilter;
		Filter filter = (Filter) inFilter;
		Class filterClass = inFilter.getClass();
		double xmin = 0, ymin = 0, xmax = 0, ymax = 0;
		if (operator.getExpression1() == null) {
			String attName = null;
			GeometryDescriptor dg = getSchema().getGeometryDescriptor();
			if (dg != null)
				attName = dg.getLocalName();
			if (attName != null) {
				FilterFactory2 ff = (FilterFactory2) CommonFactoryFinder
						.getFilterFactory(null);
				if (filterClass == Intersects.class) {
					Intersects intersects = (Intersects) inFilter;
					Literal geomLiteral = (Literal) intersects.getExpression2();
					Geometry geom = (Geometry) geomLiteral.getValue();
					Envelope envelope = geom.getEnvelopeInternal();
					xmin = envelope.getMinX();
					ymin = envelope.getMinY();
					xmax = envelope.getMaxX();
					ymax = envelope.getMaxY();
					PropertyName name = ff.property(attName);
					filter = ff.intersects(name,geomLiteral);
				} else if (filterClass == BBOX.class) {
					BBOX bbox = (BBOX) inFilter;
					xmin = bbox.getMinX();
					ymin = bbox.getMinY();
					xmax = bbox.getMaxX();
					ymax = bbox.getMaxY();
				}
				filter = ff.bbox(attName, xmin, ymin, xmax, ymax, "");
			}
		}
	return filter;
	}
}
