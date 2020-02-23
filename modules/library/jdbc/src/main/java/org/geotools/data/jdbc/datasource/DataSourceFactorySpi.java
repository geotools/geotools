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
package org.geotools.data.jdbc.datasource;

import java.io.IOException;
import java.util.Map;
import javax.sql.DataSource;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.util.factory.Factory;

/**
 * Constructs a live DataSource from a set of parameters.
 *
 * <p>An instance of this interface should exist for all DataSource providers, common examples being
 * JNDI, DBCP, C3P0. In addition to implementing this interface data source providers should have a
 * services file:
 *
 * <p><code>META-INF/services/org.geotools.data.DataSourceFactorySpi</code>
 *
 * <p>The file should contain a single line which gives the full name of the implementing class.
 *
 * <p>example:<br>
 * <code>e.g.
 * org.geotools.data.dbpc.DBCPDataSourceFactory</code>
 *
 * <p>The factories are never called directly by client code, instead the DataSourceFinder class is
 * used.
 *
 * <p>The following example shows how a user might setup a DBCP connection pool::
 *
 * <p>
 *
 * <pre><code>
 * HashMap params = new HashMap();
 * params.put("url","jdbc:postgresql://localhost/dbname");
 * params.put("driverClassName","org.postgresql.Driver");
 * params.put("username", "5432");
 * params.put("password","postgis_test");
 * params.put("maxActive", "10");
 *
 * DataSource ds = DataSourceFinder.getDataSource(params);
 * </code></pre>
 *
 * @author Andrea Aime - TOPP
 */
public interface DataSourceFactorySpi extends Factory {
    /**
     * Construct a live data source using the params specifed. The returned DataSource may be
     * pooled.
     *
     * @param params The full set of information needed to construct a live DataSource.
     * @return The created DataSource, this may be null if the required resource was not found or if
     *     insufficent parameters were given. Note that canProcess() should have returned false if
     *     the problem is to do with insuficent parameters.
     * @throws IOException if there were any problems setting up (creating or connecting) the
     *     datasource.
     */
    DataSource createDataSource(Map params) throws IOException;

    /** Same as {@link #createDataSource(Map)}, but forces the creation of a new DataSource */
    DataSource createNewDataSource(Map params) throws IOException;

    /**
     * Name suitable for display to end user.
     *
     * <p>A non localized display name for this data source type.
     *
     * @return A short name suitable for display in a user interface.
     */
    String getDisplayName();

    /**
     * Describe the nature of the data source constructed by this factory.
     *
     * <p>A non localized description of this data store type.
     *
     * @return A human readable description that is suitable for inclusion in a list of available
     *     datasources.
     */
    String getDescription();

    /**
     * MetaData about the required Parameters (for {@link #createDataSource(Map)}).
     *
     * @return Param array describing the Map for createDataStore
     */
    Param[] getParametersInfo();

    /**
     * Test to see if this factory is suitable for processing the data pointed to by the params map.
     *
     * <p>If this data source requires a number of parameters then this mehtod should check that
     * they are all present and that they are all valid.
     *
     * @param params The full set of information needed to construct a live data source.
     * @return booean true if and only if this factory can process the resource indicated by the
     *     param set and all the required params are pressent.
     */
    boolean canProcess(Map params);

    /**
     * Test to see if this data source is available, if it has all the appropriate libraries to
     * construct a datastore.
     *
     * @return <tt>true</tt> if and only if this factory has all the appropriate jars on the
     *     classpath to create DataSource.
     */
    boolean isAvailable();
}
