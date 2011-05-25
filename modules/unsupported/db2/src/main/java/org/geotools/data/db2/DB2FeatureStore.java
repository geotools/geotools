/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005. All rights reserved.
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
/*
 * Created on May 16, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
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
import org.geotools.data.jdbc.JDBCFeatureStore;
import org.geotools.filter.SQLEncoderException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * DB2 FeatureStore implementation. Overrides functionality in
 * JDBCFeatureStore to provide more efficient or more appropriate DB2-specific
 * implementation.
 * 
 * @author David Adler - IBM Corporation
 *
 * @source $URL$
 */
public class DB2FeatureStore extends JDBCFeatureStore{
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
    "org.geotools.data.db2");	
	
    /**
     * Basic constructor for DB2FeatureStore
     *
     * @param dataStore
     * @param featureType
     */
    public DB2FeatureStore(DB2DataStore dataStore, SimpleFeatureType featureType) {
        super(dataStore, featureType);
    }
    /**
     * Gets the bounds of the feature using the specified query.
     *
     * @param query a query object.
     *
     * @return the envelope representing the bounds of the features.
     *
     * @throws IOException if there was an encoder problem.
     * @throws DataSourceException if there was an error executing the query to
     *         get the bounds.
     */
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        ReferencedEnvelope env = new ReferencedEnvelope();
        CoordinateReferenceSystem crs = null;
        String typeName = "null";

        if (getSchema() != null) {
            typeName = getSchema().getTypeName();
            GeometryDescriptor geomType = getSchema()
                .getGeometryDescriptor();

            if (query.getFilter() != Filter.EXCLUDE) {
                String sqlStmt = null;

                try {
                    DB2SQLBuilder builder = (DB2SQLBuilder) ((DB2DataStore)this.getDataStore())
                    .getSqlBuilder(typeName);
                    sqlStmt = builder.buildSQLBoundsQuery(typeName, geomType,
                            query.getFilter());
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

        LOGGER.finer(typeName + " bounds: " + env.toString());

        return env;
    }
    /**
     * Closes everything associated with a query, the ResultSet, Statement and
     * Connection.
     *
     * @param rs the ResultSet
     * @param stmt the Statement
     * @param conn the Connection
     * @param transaction the Transaction
     * @param e the SQLException, if any, or null
     */
    protected void closeAll(ResultSet rs, Statement stmt, Connection conn,
        Transaction transaction, SQLException e) {
        close(rs);
        close(stmt);
        close(conn, transaction, e);
    }    

    /**
     * Direct SQL query number of rows in query.
     * 
     * <p>
     * Note this is a low level SQL statement and if it fails the provided
     * Transaction will be rolled back.
     * </p>
     * <p>
     * SQL: SELECT COUNT(*) as cnt FROM table WHERE filter
     * </p>
     * @param query
     * @param transaction
     *
     * @return Number of rows in query, or -1 if not optimizable.
     *
     * @throws IOException Usual on the basis of a filter error
     */
    public int count(Query query, Transaction transaction) throws IOException {
		int count = 0; // we may return this as default if some tests fail
		String typeName = "null";
		Filter filter = query.getFilter();

		if (getSchema() != null) {
			typeName = getSchema().getTypeName();
			GeometryDescriptor geomType = (GeometryDescriptor)getSchema().getGeometryDescriptor();

			if (filter != Filter.EXCLUDE) {
				DB2SQLBuilder builder = (DB2SQLBuilder) ((DB2DataStore) 
						getDataStore()).getSqlBuilder(typeName);

				Connection conn = null;
				Statement statement = null;
				ResultSet results = null;

				try {
					conn = getConnection();
					StringBuffer sql = new StringBuffer();
					//chorner: we should hit an indexed column, * will likely tablescan
					sql.append("SELECT COUNT(*) as cnt");
					builder.sqlFrom(sql, typeName);
					builder.sqlWhere(sql, filter); //safe to assume filter = prefilter
					statement = conn.createStatement();
					results = statement.executeQuery(sql.toString());

					if (results.next()) {
						count = results.getInt("cnt");
					}
				} catch (SQLException e) {
					closeAll(results, statement, conn, transaction, e);
					System.out.println(e);
					throw new DataSourceException("Could not get count "
							+ query.getHandle(), e);
				} catch (SQLEncoderException e) {
					closeAll(results, statement, conn, transaction, null);
					System.out.println(e);
					throw new DataSourceException("Could not get count "
							+ query.getHandle(), e);
				}
				closeAll(results, statement, conn, transaction, null);
			}
		}
		LOGGER.finer(typeName + " count: " + count);
		return count;
	}        
}
