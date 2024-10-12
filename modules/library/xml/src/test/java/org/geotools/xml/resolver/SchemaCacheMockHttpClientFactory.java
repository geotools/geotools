/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml.resolver;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.geotools.http.AbstractHTTPClientFactory;
import org.geotools.http.HTTPBehavior;
import org.geotools.http.HTTPClient;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;

/**
 * Factory for mock http client.
 *
 * <p>Registered in META-INF/services, when used make sure to set Hints.HTTP_CLIENT_FACTORY.
 *
 * @author Cécile Vuilleumier (camptocamp)
 */
public class SchemaCacheMockHttpClientFactory extends AbstractHTTPClientFactory {

    @Override
    public List<Class<?>> clientClasses() {
        return Collections.singletonList(SchemaCacheMockHttpClient.class);
    }

    @Override
    public HTTPClient createClient(List<Class<? extends HTTPBehavior>> behaviors) {
        return new SchemaCacheMockHttpClient();
    }

    private static class SchemaCacheMockHttpClient extends MockHttpClient {

        public SchemaCacheMockHttpClient() {
            try {
                final MockHttpResponse response =
                        new MockHttpResponse(SchemaCacheTest.MOCK_HTTP_RESPONSE_BODY, "application/xml");
                expectGet(new URL(SchemaCacheTest.MOCK_SCHEMA_LOCATION), response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
