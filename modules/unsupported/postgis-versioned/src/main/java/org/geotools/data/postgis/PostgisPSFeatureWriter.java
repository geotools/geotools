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
package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.jdbc.FeatureTypeInfo;
import org.geotools.data.jdbc.JDBCPSFeatureWriter;
import org.geotools.data.jdbc.QueryData;
import org.geotools.data.postgis.fidmapper.PostGISAutoIncrementFIDMapper;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.SQLEncoderException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.io.WKTWriter;

/**
 * An implementation of FeatureWriter for PostGIS using {@link PreparedStatement}
 * 
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/postgis/src/main/java/org/geotools/data/postgis/PostgisFeatureWriter.java $
 */
class PostgisPSFeatureWriter extends JDBCPSFeatureWriter {

    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.jdbc");

    /** Well Known Text writer (from JTS). */
    protected static WKTWriter geometryWriter = new WKTWriter();

    private boolean WKBEnabled;

    private boolean byteaWKB;

    private PostgisSQLBuilder sqlBuilder;

    /**
     * @param fReader
     * @param queryData
     * @param WKBEnabled
     * @param byteaWKB -- true if you're using postgis 1.0+. they changed how to do wkb writing.
     * @throws IOException
     */
    public PostgisPSFeatureWriter(FeatureReader<SimpleFeatureType, SimpleFeature> fReader,
                                  QueryData queryData,
                                  boolean WKBEnabled,
                                  boolean byteaWKB,
                                  PostgisSQLBuilder sqlBuilder) throws IOException {

        super(fReader, queryData);
        this.WKBEnabled = WKBEnabled;
        this.byteaWKB = byteaWKB;
        this.sqlBuilder = sqlBuilder;
    }

    /**
     * Overrides to add the <code>"RETURNING pk"</code> statement to the insert request, so
     * {@link PostGISAutoIncrementFIDMapper#retriveId} can get the generated pk without issueing an
     * extra select to the db.
     */
    @Override
    protected PreparedStatement prepareInsertStatement(Connection conn,
            StringBuffer statementSQL,
            SimpleFeatureType featureType) throws SQLException {

        if (mapper.hasAutoIncrementColumns()) {
            statementSQL.append(" RETURNING ");
            for (int i = 0; i < mapper.getColumnCount(); i++) {
                if (mapper.isAutoIncrement(i)) {
                    statementSQL.append(mapper.getColumnName(i));
                    statementSQL.append(",");
                }
            }
            // remove trailing comma
            statementSQL.deleteCharAt(statementSQL.length() - 1);
        }

        return super.prepareInsertStatement(conn, statementSQL, featureType);
    }

    /**
     * Overrides to call exectuteQuery instead of executeUpdate if and only if we're using an
     * autoincrement primary key, so the result can be retrieved through getResultset()
     * 
     * @see #prepareInsertStatement
     */
    @Override
    protected void executeInsert(final PreparedStatement insertStatement) throws IOException {
        if (mapper.hasAutoIncrementColumns()) {
            try {
                insertStatement.executeQuery();
            } catch (SQLException sqle) {
                String msg = "SQL Exception executing insert statement";
                LOGGER.log(Level.SEVERE, msg, sqle);
                queryData.close(sqle);
                throw new DataSourceException(msg, sqle);
            }
        } else {
            super.executeInsert(insertStatement);
        }
    }

    @Override
    protected String getGeometryPlaceHolder(final AttributeDescriptor type) {
        String placeHolder;
        final FeatureTypeInfo ftInfo = queryData.getFeatureTypeInfo();
        final int srid = ftInfo.getSRID(type.getLocalName());

        if (WKBEnabled) {
            // String wkb = WKBWriter.bytesToHex(new WKBWriter().write(geom));
            if (byteaWKB) {
                placeHolder = "setSRID( ?::geometry," + srid + ")";
            } else {
                placeHolder = "GeomFromWKB( ? , " + srid + ")";
            }
        } else {
            // String geoText = geometryWriter.write(geom);
            placeHolder = "GeometryFromText( ?, " + srid + ")";
        }
        return placeHolder;
    }

    /**
     * Returns true if the WKB format is used to transfer geometries, false otherwise
     */
    public boolean isWKBEnabled() {
        return WKBEnabled;
    }

    /**
     * If turned on, WKB will be used to transfer geometry data instead of WKT
     * 
     * @param enabled
     */
    public void setWKBEnabled(boolean enabled) {
        WKBEnabled = enabled;
    }

    /**
     * DJB: this is the javadoc from the superclass, but this wasnt being done. Encodes the
     * tableName, default is to do nothing, but postgis will override and put double quotes around
     * the tablename.
     */
    protected String encodeName(String tableName) {
        return sqlBuilder.encodeTableName(tableName);
    }

    protected String encodeColumnName(String colName) {
        return sqlBuilder.encodeColumnName(colName);
    }

    /**
     * For postgres >= 8.1 NOWAIT is used (meaning you get a response). Prior versions will block
     * during concurrent editing.
     */
    protected String makeSelectForUpdateSql(SimpleFeature current) {
        FeatureTypeInfo ftInfo = queryData.getFeatureTypeInfo();
        SimpleFeatureType featureType = ftInfo.getSchema();
        String tableName = featureType.getTypeName();

        FilterFactory ff = FilterFactoryFinder.createFilterFactory();
        Filter fid = ff.createFidFilter(current.getID());

        StringBuffer sql = new StringBuffer("SELECT ");
        // fid will be picked up automatically
        sqlBuilder.sqlColumns(sql, mapper, new AttributeDescriptor[] {});
        sqlBuilder.sqlFrom(sql, tableName);
        try {
            sqlBuilder.sqlWhere(sql, fid);
        } catch (SQLEncoderException e) {
            e.printStackTrace();
        }
        sql.append(" FOR UPDATE");

        // determine if "NOWAIT" is supported (postgres >= 8.1)
        try {
            int major = queryData.getConnection().getMetaData().getDatabaseMajorVersion();
            int minor = queryData.getConnection().getMetaData().getDatabaseMinorVersion();
            if ((major > 8) || ((major == 8) && minor >= 1)) {
                sql.append(" NOWAIT"); // horray, no blocking!
            } else {
                LOGGER
                        .warning("To fully support concurrent edits, please upgrade to postgres >= 8.1; the version currently in use will block");
            }
        } catch (SQLException e) { // we couldn't get the version :(
            LOGGER.warning("Failed to determine postgres version; assuming < 8.1");
        }

        return (sql.toString());
    }
}
