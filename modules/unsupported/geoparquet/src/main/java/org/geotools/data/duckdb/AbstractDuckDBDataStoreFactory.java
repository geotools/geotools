/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.duckdb.DuckDBDriver;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.SQLDialect;

/**
 * Abstract base class for DuckDB-powered datastores. Handles common DuckDB setup, configuration and initialization.
 *
 * <p>This factory provides the foundation for creating DataStore implementations that use DuckDB as their underlying
 * database engine. It manages:
 *
 * <ul>
 *   <li>Common parameter definition and handling
 *   <li>DuckDB JDBC connection setup and configuration
 *   <li>Extension management
 *   <li>DataStore configuration (simplification, etc.)
 * </ul>
 *
 * <p>Subclasses like GeoParquetDataStoreFactory extend this to provide format-specific implementation details while
 * inheriting the common DuckDB handling code. This separation allows reuse of the DuckDB infrastructure across multiple
 * datastore implementations.
 *
 * <p>DuckDB is an embedded analytical database that excels at reading and processing analytical data formats like
 * Parquet, and includes excellent built-in support for spatial operations.
 */
public abstract class AbstractDuckDBDataStoreFactory extends JDBCDataStoreFactory implements DataStoreFactorySpi {

    public static final Param SCREENMAP = new ParamBuilder("screenmap")
            .type(Boolean.class)
            .title("Support rendering screenmap")
            .description(
                    "Enables the rendering aid used to avoid painting tiny features over and over in the same pixel")
            .required(false)
            .defaultValue(true)
            .advancedLevel()
            .build();

    /**
     * Enables usage of a simplification function, when the queries contain geometry simplification hints. The
     * simplification function used depends on SIMPLIFICATION_METHOD setting, and is ST_Simplify by default.
     */
    public static final Param SIMPLIFY = new ParamBuilder("Support on the fly geometry simplification")
            .type(Boolean.class)
            .description(
                    "When enabled, operations such as map rendering will pass a hint that will enable the usage of a simplification function")
            .required(false)
            .defaultValue(true)
            .build();

    // NOTE: not giving an option, see note on
    // DuckdbDialect#encodeGeometryColumnSimplified()
    // /** Simplification method to use if SIMPLIFY is true. By default ST_Simplify
    // is used. */
    // public static final Param SIMPLIFICATION_METHOD = new Param(
    // "Method used to simplify geometries",
    // SimplificationMethod.class,
    // "Allows choosing the PostGIS simplification function to use, between
    // ST_Simplify and
    // ST_SimplifyPreserveTopology",
    // false,
    // SimplificationMethod.FAST,
    // new KVP(Parameter.OPTIONS, Arrays.asList(SimplificationMethod.values())));

    /** Allows subclasses to add their specific parameters */
    protected abstract void addDatabaseSpecificParameters(Map<String, Object> parameters);

    @Override
    protected abstract String getJDBCUrl(Map<String, ?> params) throws IOException;

    /** Allows subclasses to perform additional setup for their specific datastores */
    protected abstract JDBCDataStore setupDataStore(JDBCDataStore dataStore, Map<String, ?> params) throws IOException;

    @Override
    protected void setupParameters(Map<String, Object> parameters) {
        // Most parameters that don't apply to our datastore, just don't call
        // super.setupParameters() to avoid confusing
        // users

        // Keep namespace parameter
        parameters.put(NAMESPACE.key, NAMESPACE);

        // Add datastore-specific parameters (implemented by subclasses)
        addDatabaseSpecificParameters(parameters);

        parameters.put(SIMPLIFY.key, SIMPLIFY);
        parameters.put(SCREENMAP.key, SCREENMAP);
        // add these ones back, order matters for the webui
        parameters.put(JDBCDataStoreFactory.FETCHSIZE.key, JDBCDataStoreFactory.FETCHSIZE);
        // parameters.put(SIMPLIFICATION_METHOD.key, SIMPLIFICATION_METHOD);
    }

    @Override
    protected final String getDriverClassName() {
        return "org.duckdb.DuckDBDriver";
    }

    /**
     * Called by {@link #createDataStore(Map)} to set up the JDCB {@link DataSource}
     *
     * @param params the connection parameters
     * @param dialect the {@link DuckDBDialect} from {@link #createSQLDialect(JDBCDataStore, Map)}
     */
    @Override
    protected BasicDataSource createDataSource(Map<String, ?> params, SQLDialect dialect) throws IOException {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(getJDBCUrl(params));
        dataSource.setDriverClassName(getDriverClassName());
        dataSource.addConnectionProperty(DuckDBDriver.JDBC_STREAM_RESULTS, "true");

        DuckDBDialect duckDBDialect = (DuckDBDialect) dialect;
        Collection<String> connectionInitSqls = duckDBDialect.getConnectionInitSqls();
        dataSource.setConnectionInitSqls(connectionInitSqls);
        return dataSource;
    }

    @Override
    protected JDBCDataStore createDataStoreInternal(JDBCDataStore dataStore, Map<String, ?> params) throws IOException {
        DuckDBDialect dialect = (DuckDBDialect) dataStore.getSQLDialect();

        Boolean screenmap = (Boolean) SCREENMAP.lookUp(params);
        dialect.setScreenMapEnabled(screenmap == null || screenmap);

        // check geometry simplification (on by default)
        Boolean simplify = (Boolean) SIMPLIFY.lookUp(params);
        dialect.setSimplifyEnabled(simplify == null || simplify);
        // check preserving topology when simplifying geometries (off by default)
        // REVISIT: see note in DuckbDialect#encodeGeometryColumnSimplified()
        // SimplificationMethod simplificationMethod = (SimplificationMethod)
        // SIMPLIFICATION_METHOD.lookUp(params);
        // dialect.setTopologyPreserved(SimplificationMethod.PRESERVETOPOLOGY.equals(simplificationMethod));

        // Allow subclasses to perform additional setup
        return setupDataStore(dataStore, params);
    }

    @Override
    protected String getValidationQuery() {
        return "SELECT 1";
    }
}
