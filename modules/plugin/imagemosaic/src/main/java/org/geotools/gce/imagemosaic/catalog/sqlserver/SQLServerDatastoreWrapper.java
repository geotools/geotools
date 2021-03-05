/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog.sqlserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.commons.beanutils.BeanUtils;
import org.geotools.data.DataStore;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.gce.imagemosaic.catalog.oracle.DataStoreWrapper;
import org.geotools.gce.imagemosaic.catalog.oracle.FeatureTypeMapper;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.SQLDialect;
import org.opengis.feature.simple.SimpleFeatureType;

/** Specific SQLServer implementation for a {@link DataStoreWrapper} */
public class SQLServerDatastoreWrapper extends DataStoreWrapper {

    public static final String DEFAULT_METADATA_TABLE = "mosaic_geometry_metadata";
    private static final String METADATA_TABLE_PROPERTY = "geometryMetadataTable";
    private static final String CREATE_METADATA_TABLE =
            "CREATE TABLE %s(\n"
                    + "   F_TABLE_SCHEMA VARCHAR(128),\n"
                    + "   F_TABLE_NAME VARCHAR(128) NOT NULL,\n"
                    + "   F_GEOMETRY_COLUMN VARCHAR(128) NOT NULL,\n"
                    + "   COORD_DIMENSION INTEGER,\n"
                    + "   SRID INTEGER NOT NULL,\n"
                    + "   TYPE VARCHAR(30) NOT NULL,\n"
                    + "   UNIQUE(F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN),\n"
                    + "   CHECK(TYPE IN ('POINT','LINE', 'POLYGON', 'COLLECTION', 'MULTIPOINT', 'MULTILINE', 'MULTIPOLYGON', 'GEOMETRY') ))";

    public SQLServerDatastoreWrapper(DataStore datastore, String location) {
        super(datastore, location);

        // check if the geometry metadata table is present, if not, create and configure
        // it, to make usage of SQL Server transparent (otherwise the property file would have
        // needed the setup of the geometry metadata table for mosaic to work at all, the
        // CRS of geometries is lost otherwise at the default isolation level)
        try {
            if (datastore instanceof JDBCDataStore) {
                JDBCDataStore jdbcDataStore = (JDBCDataStore) this.datastore;

                SQLDialect dialect = jdbcDataStore.getSQLDialect();
                String metadataTable = BeanUtils.getProperty(dialect, METADATA_TABLE_PROPERTY);
                if (metadataTable == null) {
                    metadataTable = DEFAULT_METADATA_TABLE;
                    BeanUtils.setProperty(dialect, METADATA_TABLE_PROPERTY, DEFAULT_METADATA_TABLE);
                }
                String databaseSchema = jdbcDataStore.getDatabaseSchema();

                try (Connection cx = jdbcDataStore.getConnection(Transaction.AUTO_COMMIT);
                        Statement st = cx.createStatement()) {
                    boolean createMetadataTable = true;
                    DatabaseMetaData metaData = cx.getMetaData();

                    // get the schema.metadata qualified and quoted string for use in direct SQL
                    StringBuffer qualifiedTableName = new StringBuffer();
                    if (databaseSchema != null) {
                        dialect.encodeTableName(databaseSchema, qualifiedTableName);
                        qualifiedTableName.append(".");
                    }
                    dialect.encodeTableName(metadataTable, qualifiedTableName);

                    // check if it's already there
                    try (ResultSet res =
                            metaData.getTables(
                                    null, databaseSchema, metadataTable, new String[] {"TABLE"})) {
                        createMetadataTable = !res.next();
                    }
                    // it not create
                    if (createMetadataTable) {
                        st.executeUpdate(String.format(CREATE_METADATA_TABLE, qualifiedTableName));
                    }
                }
            }
        } catch (SQLException
                | IOException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            LOGGER.log(
                    Level.FINE,
                    "Failed to assess/create the metadata geometry table, this could lead to lost geometry SRIDs on table creation",
                    e);
        }
    }

    /**
     * Return a specific {@link FeatureTypeMapper} by parsing mapping properties contained within
     * the specified {@link Properties} object
     */
    @Override
    protected FeatureTypeMapper getFeatureTypeMapper(final Properties props) throws Exception {
        FeatureTypeMapper mapper = super.getFeatureTypeMapper(props);
        return mapper;
    }

    @Override
    protected FeatureTypeMapper getFeatureTypeMapper(SimpleFeatureType featureType)
            throws Exception {
        return new SQLServerTypeMapper(featureType);
    }

    @Override
    protected SimpleFeatureSource transformFeatureStore(
            SimpleFeatureStore store, FeatureTypeMapper mapper) throws IOException {
        SimpleFeatureSource transformedSource = mapper.getSimpleFeatureSource();
        if (transformedSource != null) {
            return transformedSource;
        } else {
            transformedSource =
                    new SQLServerTransformFeatureStore(
                            store, mapper.getName(), mapper.getDefinitions(), datastore);
            ((SQLServerTypeMapper) mapper).setSimpleFeatureSource(transformedSource);
            return transformedSource;
        }
    }
}
