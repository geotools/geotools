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
package org.geotools.data.wfs.impl;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.ows.ServiceException;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

/**
 * A {@link DataStoreFactorySpi} to connect to a Web Feature Service.
 * <p>
 * Produces a {@link WFSDataStore} if the correct set of connection parameters are provided. For instance, the only mandatory one is {@link #URL}.
 * </p>
 * <p>
 * As with all the DataStoreFactorySpi implementations, this one is not intended to be used directly but through the {@link DataStoreFinder}
 * mechanism, hence client applications should not have strong dependencies over this module.
 * </p>
 * <p>
 * Upon a valid URL to a WFS GetCapabilities document, this factory will perform version negotiation between the server supported protocol versions
 * and this plugin supported ones, and will return a {@link DataStore} capable of communicating with the server using the agreed WFS protocol version.
 * </p>
 * <p>
 * In the case the provided GetCapabilities URL explicitly contains a VERSION parameter and both the server and client support that version, that
 * version will be used.
 * </p>
 * 
 * @see WFSContentDataStore
 * @see WFSClient
 */
@SuppressWarnings({ "unchecked", "nls" })
public class WFSDataStoreFactory extends WFSDataAccessFactory implements DataStoreFactorySpi {

    private static int GMLComplianceLevel = 0;

    /**
     * Requests the WFS Capabilities document from the {@link WFSDataStoreFactory#URL url} parameter
     * in {@code params} and returns a {@link WFSDataStore} according to the version of the
     * GetCapabilities document returned.
     * <p>
     * Note the {@code URL} provided as parameter must refer to the actual {@code GetCapabilities}
     * request. If you need to specify a preferred version or want the GetCapabilities request to be
     * generated from a base URL build the URL with the {@link #createGetCapabilitiesRequest} first.
     * </p>
     * 
     * @see org.geotools.data.DataStoreFactorySpi#createDataStore(java.util.Map)
     */
    @Override
    public WFSContentDataStore createDataStore(final Map<String, Serializable> params)
            throws IOException {

        final WFSConfig config = WFSConfig.fromParams(params);

        {
            String user = config.getUser();
            String password = config.getPassword();
            if (((user == null) && (password != null))
                    || ((config.getPassword() == null) && (config.getUser() != null))) {
                throw new IOException(
                        "Cannot define only one of USERNAME or PASSWORD, must define both or neither");
            }
        }

        final HTTPClient http = new SimpleHttpClient();// new MultithreadedHttpClient();
        // TODO: let HTTPClient be configured for gzip
        // http.setTryGzip(tryGZIP);
        http.setUser(config.getUser());
        http.setPassword(config.getPassword());
        int timeoutMillis = config.getTimeoutMillis();
        http.setConnectTimeout(timeoutMillis / 1000);

        final URL capabilitiesURL = (URL) URL.lookUp(params);

        // WFSClient performs version negotiation and selects the correct strategy
        WFSClient wfsClient;
        try {
            wfsClient = new WFSClient(capabilitiesURL, http, config);
        } catch (ServiceException e) {
            throw new IOException(e);
        }

        WFSContentDataStore dataStore = new WFSContentDataStore(wfsClient);
        // factories
        dataStore.setFilterFactory(CommonFactoryFinder.getFilterFactory(null));
        dataStore.setGeometryFactory(new GeometryFactory(
                PackedCoordinateSequenceFactory.DOUBLE_FACTORY));
        dataStore.setFeatureTypeFactory(new FeatureTypeFactoryImpl());
        dataStore.setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
        dataStore.setDataStoreFactory(this);
        return dataStore;
    }

    /**
     * Unsupported operation, can't create a WFS service.
     * 
     * @throws UnsupportedOperationException
     *             always, as this operation is not applicable to WFS.
     */
    @Override
    public DataStore createNewDataStore(final Map<String, Serializable> params) throws IOException {
        throw new UnsupportedOperationException("Operation not applicable to a WFS service");
    }

    @Override
    public String getDisplayName() {
        return "Web Feature Server (NG)";
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    /**
     * Checks whether {@code params} contains a valid set of parameters to connecto to a WFS.
     * <p>
     * Rules are:
     * <ul>
     * <li>The mandatory {@link #URL} is provided.
     * <li>Either both {@link #USERNAME} and {@link #PASSWORD} are provided, or none.
     * </ul>
     * </p>
     */
    @Override
    public boolean canProcess(@SuppressWarnings("rawtypes") final Map params) {
        /*
         * check required params exist and are of the correct type
         */
        boolean canProcess = super.canProcess(params);
        if (!canProcess) {
            return false;
        }

        // Check compliance level
        if (params.containsKey(GML_COMPLIANCE_LEVEL.key)) {
            if ((Integer) params.get(GML_COMPLIANCE_LEVEL.key) > GMLComplianceLevel) {
                return false;
            }
        }

        return true;
    }

}
