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
package org.geotools.data;

import java.io.IOException;
import java.util.Map;

/**
 * Factory used to construct a DataStore from a set of parameters.
 * <p>
 * The following example shows how a user might connect to a PostGIS database,
 * and maintain the resulting DataStore in a Registry:
 * </p>
 *
 * <p>
 * <pre><code>
 * HashMap params = new HashMap();
 * params.put("namespace", "leeds");
 * params.put("dbtype", "postgis");
 * params.put("host","feathers.leeds.ac.uk");
 * params.put("port", "5432");
 * params.put("database","postgis_test");
 * params.put("user","postgis_ro");
 * params.put("passwd","postgis_ro");
 *
 * DefaultRegistry registry = new DefaultRegistry();
 * registry.addDataStore("leeds", params);
 *
 * DataStore postgis = registry.getDataStore( "leeds" );
 * SimpleFeatureSource = postgis.getFeatureSource( "table" );
 * </code></pre>
 * </p> 
 * <h2>Implementation Notes</h2>
 * <p>
 * An instance of this interface should exist for all data stores which want to
 * take advantage of the dynamic plug-in system. In addition to implementing
 * this factory interface each DataStore implementation should have a services file:
 * </p>
 *
 * <p>
 * <code>META-INF/services/org.geotools.data.DataStoreFactorySpi</code>
 * </p>
 *
 * <p>
 * The file should contain a single line which gives the full name of the
 * implementing class.
 * </p>
 *
 * <p>
 * example:<br/><code>e.g.
 * org.geotools.data.mytype.MyTypeDataSourceFacotry</code>
 * </p>
 *
 * <p>
 * The factories are never called directly by client code, instead the
 * DataStoreFinder class is used.
 * </p>
 * 
 * @author Jody Garnett, Refractions Research
 * @source $URL$
 */
public interface DataStoreFactorySpi extends DataAccessFactory {
    /**
     * Construct a live data source using the params specifed.
     *
     * <p>
     * You can think of this as setting up a connection to the back end data
     * source.
     * </p>
     *
     * <p>
     * Magic Params: the following params are magic and are honoured by
     * convention by the GeoServer and uDig application.
     *
     * <ul>
     * <li>
     * "user": is taken to be the user name
     * </li>
     * <li>
     * "passwd": is taken to be the password
     * </li>
     * <li>
     * "namespace": is taken to be the namespace prefix (and will be kept in
     * sync with GeoServer namespace management.
     * </li>
     * </ul>
     *
     * When we eventually move over to the use of OpperationalParam we will
     * have to find someway to codify this convention.
     * </p>
     *
     * @param params The full set of information needed to construct a live
     *        data store. Typical key values for the map include: url -
     *        location of a resource, used by file reading datasources. dbtype
     *        - the type of the database to connect to, e.g. postgis, mysql
     *
     * @return The created DataStore, this may be null if the required resource
     *         was not found or if insufficent parameters were given. Note
     *         that canProcess() should have returned false if the problem is
     *         to do with insuficent parameters.
     *
     * @throws IOException if there were any problems setting up (creating or
     *         connecting) the datasource.
     */
    DataStore createDataStore(Map<String, java.io.Serializable> params) throws IOException;

    //    /**
    //     * Construct a simple MetadataEntity providing internationlization information
    //     * for the data source that *would* be created by createDataStore.
    //     * <p>
    //     * Suitable for use by CatalogEntry, unknown if this will make
    //     * a DataStore behind the scenes or not. It is possible it will
    //     * communicate with the data source though (hense the IOException).
    //     * </p>
    //     * @param params The full set of information needed to construct a live
    //     *        data store
    //     * @return MetadataEntity with descriptive information (including
    //     *         internationlization support). 
    //     * @throws IOException
    //     */
    //    DataSourceMetadataEnity createMetadata( Map params ) throws IOException;
    DataStore createNewDataStore(Map<String, java.io.Serializable> params) throws IOException;
}
