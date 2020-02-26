/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs;

import static org.geotools.data.wfs.internal.URIs.buildURL;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wfs.impl.WFSDataAccessFactory;
import org.geotools.data.wfs.internal.Versions;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.ows.ServiceException;
import org.geotools.util.Version;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;

/**
 * A {@link DataStoreFactorySpi} to connect to a Web Feature Service.
 *
 * <p>Produces a {@link WFSDataStore} if the correct set of connection parameters are provided. For
 * instance, the only mandatory one is {@link #URL}.
 *
 * <p>As with all the DataStoreFactorySpi implementations, this one is not intended to be used
 * directly but through the {@link DataStoreFinder} mechanism, hence client applications should not
 * have strong dependencies over this module.
 *
 * <p>Upon a valid URL to a WFS GetCapabilities document, this factory will perform version
 * negotiation between the server supported protocol versions and this plugin supported ones, and
 * will return a {@link DataStore} capable of communicating with the server using the agreed WFS
 * protocol version.
 *
 * <p>In the case the provided GetCapabilities URL explicitly contains a VERSION parameter and both
 * the server and client support that version, that version will be used.
 *
 * @see WFSDataStore
 * @see WFSClient
 */
@SuppressWarnings({"unchecked", "nls"})
public class WFSDataStoreFactory extends WFSDataAccessFactory implements DataStoreFactorySpi {

    private static int GMLComplianceLevel = 0;

    /**
     * Requests the WFS Capabilities document from the {@link WFSDataStoreFactory#URL url} parameter
     * in {@code params} and returns a {@link WFSDataStore} according to the version of the
     * GetCapabilities document returned.
     *
     * <p>Note the {@code URL} provided as parameter must refer to the actual {@code
     * GetCapabilities} request. If you need to specify a preferred version or want the
     * GetCapabilities request to be generated from a base URL build the URL with the {@link
     * #createGetCapabilitiesRequest} first.
     *
     * @see org.geotools.data.DataStoreFactorySpi#createDataStore(java.util.Map)
     */
    @Override
    public WFSDataStore createDataStore(final Map<String, Serializable> params) throws IOException {

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

        final URL capabilitiesURL = (URL) URL.lookUp(params);

        final HTTPClient http = getHttpClient(params);
        http.setTryGzip(config.isTryGZIP());
        http.setUser(config.getUser());
        http.setPassword(config.getPassword());
        int timeoutMillis = config.getTimeoutMillis();
        http.setConnectTimeout(timeoutMillis / 1000);
        http.setReadTimeout(timeoutMillis / 1000);

        // WFSClient performs version negotiation and selects the correct strategy
        WFSClient wfsClient;
        try {
            wfsClient = new WFSClient(capabilitiesURL, http, config);
        } catch (ServiceException e) {
            throw new IOException(e);
        }

        WFSDataStore dataStore = new WFSDataStore(wfsClient);
        // factories
        dataStore.setFilterFactory(CommonFactoryFinder.getFilterFactory(null));
        dataStore.setGeometryFactory(
                new GeometryFactory(PackedCoordinateSequenceFactory.DOUBLE_FACTORY));
        dataStore.setFeatureTypeFactory(new FeatureTypeFactoryImpl());
        dataStore.setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
        dataStore.setDataStoreFactory(this);
        dataStore.setNamespaceURI(config.getNamespaceOverride());

        return dataStore;
    }

    /**
     * Creates the HttpClient instance used to connect to the WFS service, compatible with the given
     * parameters.
     *
     * @param params wfs service connection parameters
     * @return the HttpClient instance
     */
    public HTTPClient getHttpClient(final Map<String, Serializable> params) throws IOException {
        final URL capabilitiesURL = (URL) URL.lookUp(params);
        final WFSConfig config = WFSConfig.fromParams(params);
        return config.isUseHttpConnectionPooling() && isHttp(capabilitiesURL)
                ? new MultithreadedHttpClient(config)
                : new SimpleHttpClient();
    }

    private static boolean isHttp(java.net.URL capabilitiesURL) {
        return capabilitiesURL.getProtocol().toLowerCase().matches("http(s)?");
    }

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
        return "Provides access to the Features published a Web Feature Service, "
                + "and the ability to perform transactions on the server (when supported / allowed).";
    }

    /**
     * Checks whether {@code params} contains a valid set of parameters to connecto to a WFS.
     *
     * <p>Rules are:
     *
     * <ul>
     *   <li>The mandatory {@link #URL} is provided.
     *   <li>Either both {@link #USERNAME} and {@link #PASSWORD} are provided, or none.
     * </ul>
     */
    @Override
    public boolean canProcess(@SuppressWarnings("rawtypes") final Map params) {
        return super.canProcess(params, GMLComplianceLevel);
    }

    /**
     * Creates a HTTP GET Method based WFS {@code GetCapabilities} request for the given protocol
     * version.
     *
     * <p>If the query string in the {@code host} URL already contains a VERSION number, that
     * version is <b>discarded</b>.
     *
     * @param host non null URL from which to construct the WFS {@code GetCapabilities} request by
     *     discarding the query string, if any, and appending the propper query string.
     */
    public static URL createGetCapabilitiesRequest(URL host, Version version) {
        if (host == null) {
            throw new NullPointerException("null url");
        }
        if (version == null) {
            throw new NullPointerException("version");
        }

        Map<String, String> getCapsKvp = new HashMap<String, String>();
        getCapsKvp.put("SERVICE", "WFS");
        getCapsKvp.put("REQUEST", "GetCapabilities");
        getCapsKvp.put("VERSION", version.toString());
        return buildURL(host, getCapsKvp);
    }

    /**
     * Creates a HTTP GET Method based WFS {@code GetCapabilities} request.
     *
     * <p>If the query string in the {@code host} URL already contains a VERSION number, that
     * version is used, otherwise the queried version will be 1.0.0.
     *
     * <p><b>NOTE</b> the default version will be 1.0.0 until the support for 1.1.0 gets stable
     * enough for general use. If you want to use a 1.1.0 WFS you'll have to explicitly provide the
     * VERSION=1.1.0 parameter in the GetCapabilities request meanwhile.
     *
     * @param host non null URL pointing either to a base WFS service access point, or to a full
     *     {@code GetCapabilities} request.
     */
    public static URL createGetCapabilitiesRequest(final URL host) {
        if (host == null) {
            throw new NullPointerException("url");
        }

        String queryString = host.getQuery();
        queryString =
                queryString == null || "".equals(queryString.trim())
                        ? ""
                        : queryString.toUpperCase();

        final Version defaultVersion = Versions.highest();

        // which version to use
        Version requestVersion = defaultVersion;

        if (queryString.length() > 0) {

            Map<String, String> params = new HashMap<String, String>();
            String[] split = queryString.split("&");
            for (String kvp : split) {
                int index = kvp.indexOf('=');
                String key = index > 0 ? kvp.substring(0, index) : kvp;
                String value = index > 0 ? kvp.substring(index + 1) : null;
                params.put(key, value);
            }

            String request = params.get("REQUEST");
            if ("GETCAPABILITIES".equals(request)) {
                String version = params.get("VERSION");
                if (version != null) {
                    requestVersion = Versions.find(version);
                    if (requestVersion == null) {
                        requestVersion = defaultVersion;
                    }
                }
            }
        }
        return createGetCapabilitiesRequest(host, requestVersion);
    }

    /**
     * Defaults to true, only a few datastores need to check for drivers.
     *
     * @return <code>true</code>
     */
    public boolean isAvailable() {
        return true;
    }
}
