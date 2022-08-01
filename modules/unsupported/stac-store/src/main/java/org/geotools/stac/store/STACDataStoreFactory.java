/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.store;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.Parameter;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPClientFinder;
import org.geotools.http.HTTPConnectionPooling;
import org.geotools.http.SimpleHttpClient;
import org.geotools.stac.client.STACClient;
import org.geotools.stac.client.STACClient.SearchMode;
import org.geotools.util.KVP;
import org.geotools.util.factory.Hints;

public class STACDataStoreFactory implements DataStoreFactorySpi {

    public static final Param DBTYPE =
            new Param(
                    "dbtype",
                    String.class,
                    "Type",
                    true,
                    "stac",
                    Collections.singletonMap(Parameter.LEVEL, "program"));

    public static final Param LANDING_PAGE =
            new Param("landingPage", URL.class, "The STAC server landing page URL", true);

    public static final Param NAMESPACE =
            new Param(
                    "namespace",
                    URI.class,
                    "uri to the namespace",
                    false,
                    null,
                    new KVP(Param.LEVEL, "advanced"));

    public static final Param SEARCH_MODE =
            new Param(
                    "searchMode",
                    SearchMode.class,
                    "How to perform search queries, GET, POST or decide based on conformance classes",
                    false,
                    SearchMode.GET,
                    Collections.singletonMap(
                            Parameter.OPTIONS, Arrays.asList(SearchMode.values())));

    public static final Param FETCH_SIZE =
            new Param(
                    "fetchSize",
                    Integer.class,
                    "How many items to fetch in a single request (just a suggestion to the server)",
                    false,
                    STACDataStore.DEFAULT_FETCH_SIZE,
                    Collections.singletonMap(Parameter.MIN, 1));

    public static final Param HARD_LIMIT =
            new Param(
                    "hardLimit",
                    Integer.class,
                    "How many items to fetch from a collection, tops, even while paging. Set to zero or negative to disable.",
                    false,
                    STACDataStore.DEFAULT_HARD_LIMIT,
                    Collections.singletonMap(Parameter.MIN, 1));

    public static final Param USE_CONNECTION_POOLING =
            new Param(
                    "useConnectionPooling",
                    Boolean.class,
                    "Use HTTP connection pooling",
                    false,
                    Boolean.TRUE);

    public static final Param MAX_CONNECTIONS =
            new Param(
                    "maxConnections",
                    Integer.class,
                    "Maximum number of connections",
                    false,
                    Integer.valueOf(6),
                    Collections.singletonMap(Parameter.MIN, 1));

    public static final Param TRY_GZIP =
            new Param(
                    "tryGZIP",
                    Boolean.class,
                    "Use GZIP compression, if available",
                    false,
                    Boolean.TRUE);

    public static final Param USERNAME =
            new Param("username", String.class, "User name for HTTP basic authentication", false);

    public static final Param PASSWORD =
            new Param(
                    "password",
                    String.class,
                    "Password for HTTP basic authentication",
                    false,
                    Boolean.TRUE);

    public static final Param CONNECTION_TIMEOUT =
            new Param("connectionTimeout", Integer.class, "Connection timeout (sec)", false, 10);

    public static final Param READ_TIMEOUT =
            new Param("readTimeout", Integer.class, "Read timeout (sec)", false, 10);

    @Override
    public String getDisplayName() {
        return "STAC-API";
    }

    @Override
    public String getDescription() {
        return "Spatio-Temporal Asset Catalog API datastore";
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {
            NAMESPACE,
            DBTYPE,
            LANDING_PAGE,
            SEARCH_MODE,
            FETCH_SIZE,
            HARD_LIMIT,
            USE_CONNECTION_POOLING,
            MAX_CONNECTIONS,
            TRY_GZIP,
            USERNAME,
            PASSWORD,
            CONNECTION_TIMEOUT,
            READ_TIMEOUT
        };
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public DataStore createDataStore(Map<String, ?> params) throws IOException {

        // build the client
        HTTPClient http = buildHttpClient(params);

        // fetch the store params and build the STAC client
        URL landingPage = (URL) LANDING_PAGE.lookUp(params);
        @SuppressWarnings("PMD.CloseResource") // will be closed by the store
        STACClient client = new STACClient(landingPage, http);

        // now build the actual store
        URI namespace = (URI) NAMESPACE.lookUp(params);
        SearchMode mode = (SearchMode) SEARCH_MODE.lookUp(params);
        Integer fetchSize = (Integer) FETCH_SIZE.lookUp(params);
        Integer hardLimit = (Integer) HARD_LIMIT.lookUp(params);
        STACDataStore store = new STACDataStore(client);
        if (namespace != null) store.setNamespaceURI(namespace.toString());
        if (mode != null) store.setSearchMode(mode);
        if (fetchSize != null) store.setFetchSize(fetchSize);
        if (hardLimit != null) store.setHardLimit(hardLimit);
        return store;
    }

    @SuppressWarnings("PMD.CloseResource") // the store will handle
    private HTTPClient buildHttpClient(Map<String, ?> params) throws IOException {
        Boolean pooling = (Boolean) USE_CONNECTION_POOLING.lookUp(params);
        HTTPClient client;
        if (Boolean.TRUE.equals(pooling)) {
            client = HTTPClientFinder.createClient(HTTPConnectionPooling.class);
            HTTPConnectionPooling pc = (HTTPConnectionPooling) client;

            Integer maxConnections = (Integer) MAX_CONNECTIONS.lookUp(params);
            if (maxConnections != null) pc.setMaxConnections(maxConnections);
        } else {
            client =
                    HTTPClientFinder.createClient(
                            new Hints(Hints.HTTP_CLIENT, SimpleHttpClient.class));
        }

        Boolean gzip = (Boolean) TRY_GZIP.lookUp(params);
        if (gzip != null) {
            client.setTryGzip(gzip);
        }

        Integer ct = (Integer) CONNECTION_TIMEOUT.lookUp(params);
        if (ct != null) client.setConnectTimeout(ct);

        Integer rt = (Integer) READ_TIMEOUT.lookUp(params);
        if (rt != null) client.setReadTimeout(rt);

        String username = (String) USERNAME.lookUp(params);
        if (username != null) {
            client.setUser(username);
            client.setPassword((String) PASSWORD.lookUp(params));
        }

        return client;
    }

    @Override
    public DataStore createNewDataStore(Map<String, ?> params) throws IOException {
        throw new UnsupportedOperationException();
    }
}
