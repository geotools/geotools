/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.FeatureTypeInfo;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.SQLBuilder;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.factory.Hints;
import org.geotools.filter.RegfuncFilterFactoryImpl;
import org.geotools.filter.SQLEncoderPostgis;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * {@link DataStore} with support for registered functions, that is, functions that can be executed
 * on the database server side.
 * 
 * <p>
 * 
 * TODO: everything in this class should be pulled up into ancestor classes.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class RegfuncPostgisDataStore extends PostgisDataStore {

    /*
     * SPI mechanism cannot be used as we only want one FilterFactory implementation system wide.
     * Why provide more than one? Which one should be used? This would be SPI misuse.
     * 
     * FIXME: This should go away when registered function support is refactored into core. At the
     * moment, this is required because otherwise DuplicatingFilterVisitor gets the wrong
     * FeatureFactory.
     */
    static {
        Hints.putSystemDefault(Hints.FILTER_FACTORY, new RegfuncFilterFactoryImpl());
    }

    /**
     * Constructor.
     * 
     * @see org.geotools.data.postgis.PostgisDataStore#PostgisDataStore(DataSource)
     * 
     * @param dataSource
     * @throws IOException
     */
    public RegfuncPostgisDataStore(DataSource dataSource) throws IOException {
        super(dataSource);
    }

    /**
     * Constructor.
     * 
     * @see org.geotools.data.postgis.PostgisDataStore#PostgisDataStore(DataSource, String)
     * 
     * @param dataSource
     * @param namespace
     * @throws IOException
     */
    public RegfuncPostgisDataStore(DataSource dataSource, String namespace) throws IOException {
        super(dataSource, namespace);
    }

    /**
     * Constructor.
     * 
     * @see org.geotools.data.postgis.PostgisDataStore#PostgisDataStore(DataSource, String, String)
     * 
     * @param dataSource
     * @param schema
     * @param namespace
     * @throws IOException
     */
    public RegfuncPostgisDataStore(DataSource dataSource, String schema, String namespace)
            throws IOException {
        super(dataSource, schema, namespace);
    }

    /**
     * Constructor.
     * 
     * @see org.geotools.data.postgis.PostgisDataStore#PostgisDataStore(DataSource,
     *      JDBCDataStoreConfig, int)
     * 
     * @param dataSource
     * @param config
     * @param optimizeMode
     * @throws IOException
     */
    public RegfuncPostgisDataStore(DataSource dataSource, JDBCDataStoreConfig config,
            int optimizeMode) throws IOException {
        super(dataSource, config, optimizeMode);
    }

    /**
     * Constructor.
     * 
     * @see org.geotools.data.postgis.PostgisDataStore#PostgisDataStore(DataSource, String, String,
     *      int)
     * 
     * @param dataSource
     * @param schema
     * @param namespace
     * @param optimizeMode
     * @throws IOException
     */
    public RegfuncPostgisDataStore(DataSource dataSource, String schema, String namespace,
            int optimizeMode) throws IOException {
        super(dataSource, schema, namespace, optimizeMode);
    }

    /**
     * Creates a new sql builder for encoding raw sql statements.
     * 
     * <p>
     * 
     * This method duplicates the implementation in {@link PostgisDataStore}, except that it
     * ensures a {@link RegfuncSQLEncoderPostgis} is passed to {@link PostgisSQLBuilder}, enabling
     * support for registered functions.
     * 
     * @see org.geotools.data.postgis.PostgisDataStore#createSQLBuilder()
     */
    // @Override
    protected PostgisSQLBuilder createSQLBuilder() {
        PostgisSQLBuilder builder = new PostgisSQLBuilder(new RegfuncSQLEncoderPostgis(), config);
        initBuilder(builder);
        return builder;
    }

    /**
     * This method duplicates the implementation in {@link PostgisDataStore}, except that it
     * ensures a {@link RegfuncSQLEncoderPostgis} is passed to {@link PostgisSQLBuilder}, enabling
     * support for registered functions.
     * 
     * @see org.geotools.data.postgis.PostgisDataStore#getSqlBuilder(java.lang.String)
     */
    // @Override
    public SQLBuilder getSqlBuilder(String typeName) throws IOException {
        FeatureTypeInfo info = typeHandler.getFeatureTypeInfo(typeName);
        int srid = -1;
        SQLEncoderPostgis encoder = new RegfuncSQLEncoderPostgis();
        encoder.setSupportsGEOS(useGeos);
        encoder.setFIDMapper(typeHandler.getFIDMapper(typeName));
        if (info.getSchema().getGeometryDescriptor() != null) {
            String geom = info.getSchema().getGeometryDescriptor().getLocalName();
            srid = info.getSRID(geom);
            encoder.setDefaultGeometry(geom);
        }
        encoder.setFeatureType(info.getSchema());
        encoder.setSRID(srid);
        encoder.setLooseBbox(looseBbox);
        PostgisSQLBuilder builder = new PostgisSQLBuilder(encoder, config, info.getSchema());
        initBuilder(builder);
        return builder;
    }

    /**
     * At the moment, this method does the same as the method of the superclass.
     * 
     * @see org.geotools.data.postgis.PostgisDataStore#buildSchema(java.lang.String,
     *      org.geotools.data.jdbc.fidmapper.FIDMapper)
     */
    // Should be OK to allow super to use its SQLEncoderPostgis (no Regfunc)
    // because only used for permission checks.
    // @Override
    protected SimpleFeatureType buildSchema(String typeName, FIDMapper mapper) throws IOException {
        if (false) {
            // kept this code as probing for registered_functions goes here
            Connection conn = getConnection(Transaction.AUTO_COMMIT);
            try {
                Statement st = conn.createStatement();
                try {
                    st.execute("SELECT * FROM geometry_columns LIMIT 0;");
                } catch (Throwable t) {
                    String msg = "Error querying relation: geometry_columns." + " Possible cause:"
                            + t.getLocalizedMessage();
                    throw new DataSourceException(msg, t);
                }
                try {
                    SQLEncoderPostgis encoder = new RegfuncSQLEncoderPostgis(-1);
                    encoder.setSupportsGEOS(useGeos);
                    PostgisSQLBuilder builder = new PostgisSQLBuilder(encoder, config);
                    initBuilder(builder);

                    st.execute("SELECT * FROM " + builder.encodeTableName(typeName) + " LIMIT 0;");
                } catch (Throwable t) {
                    String msg = "Error querying relation: " + typeName + "." + " Possible cause:"
                            + t.getLocalizedMessage();
                    throw new DataSourceException(msg, t);
                }
                st.close();
            } catch (SQLException e) {
                JDBCUtils.close(conn, Transaction.AUTO_COMMIT, e);
                throw new DataSourceException(e);
            } finally {
                JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
            }
            // everything is cool, keep going
        }
        return super.buildSchema(typeName, mapper);
    }

}
