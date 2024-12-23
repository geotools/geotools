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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.geotools.stac.store.STACDataStoreFactory.CONNECTION_TIMEOUT;
import static org.geotools.stac.store.STACDataStoreFactory.DBTYPE;
import static org.geotools.stac.store.STACDataStoreFactory.LANDING_PAGE;
import static org.geotools.stac.store.STACDataStoreFactory.MAX_CONNECTIONS;
import static org.geotools.stac.store.STACDataStoreFactory.NAMESPACE;
import static org.geotools.stac.store.STACDataStoreFactory.PASSWORD;
import static org.geotools.stac.store.STACDataStoreFactory.READ_TIMEOUT;
import static org.geotools.stac.store.STACDataStoreFactory.TRY_GZIP;
import static org.geotools.stac.store.STACDataStoreFactory.USERNAME;
import static org.geotools.stac.store.STACDataStoreFactory.USE_CONNECTION_POOLING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPConnectionPooling;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class STACDataStoreFactoryTest {

    @ClassRule
    public static WireMockClassRule classRule =
            new WireMockClassRule(WireMockConfiguration.options().dynamicPort());

    @Rule
    public WireMockClassRule service = classRule;

    private static final String NS_URI = "http://www.geotools.org";

    @Test
    @SuppressWarnings("PMD.CloseResource") // pooling HTTP client closed via store
    public void testConnect() throws IOException {
        service.stubFor(get(urlEqualTo("/stac"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Authentication", "dXNlcjpwYXNzd29yZA==")
                        .withBody(IOUtils.toString(
                                getClass().getResourceAsStream("../landingPage.json"), StandardCharsets.UTF_8))));

        Map<String, Object> params = new HashMap<>();
        params.put(DBTYPE.key, DBTYPE.sample);
        params.put(LANDING_PAGE.key, new URL("http://localhost:" + service.port() + "/stac"));
        params.put(NAMESPACE.key, NS_URI);
        params.put(USE_CONNECTION_POOLING.key, true);
        params.put(MAX_CONNECTIONS.key, 10);
        params.put(TRY_GZIP.key, false);
        params.put(USERNAME.key, "user");
        params.put(PASSWORD.key, "password");
        params.put(CONNECTION_TIMEOUT.key, 20);
        params.put(READ_TIMEOUT.key, 30);
        DataStore store = DataStoreFinder.getDataStore(params);
        try {
            assertThat(store, Matchers.instanceOf(STACDataStore.class));

            STACDataStore stac = (STACDataStore) store;
            assertEquals(NS_URI, stac.getNamespaceURI());

            HTTPClient http = stac.getClient().getHttp();
            assertFalse(http.isTryGzip());
            assertEquals("user", http.getUser());
            assertEquals("password", http.getPassword());

            assertThat(http, Matchers.instanceOf(HTTPConnectionPooling.class));
            HTTPConnectionPooling pooling = (HTTPConnectionPooling) http;
            assertEquals(10, pooling.getMaxConnections());
        } finally {
            if (store != null) store.dispose();
        }
    }
}
