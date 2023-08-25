/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbstyle.sprite;

import static org.apache.commons.io.IOUtils.toByteArray;

import java.io.IOException;
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
 * @author Roar Brænden
 */
public class SpriteMockHttpClientFactory extends AbstractHTTPClientFactory {

    @Override
    public List<Class<?>> clientClasses() {
        return Collections.singletonList(SpriteMockHttpClient.class);
    }

    @Override
    public HTTPClient createClient(List<Class<? extends HTTPBehavior>> behaviors) {
        return new SpriteMockHttpClient();
    }

    private static class SpriteMockHttpClient extends MockHttpClient {

        public SpriteMockHttpClient() {

            try {
                final MockHttpResponse jsonResponse =
                        new MockHttpResponse(
                                toByteArray(SpriteGraphicFactoryMockTestImpl.jsonURL),
                                "application/json");
                expectGet(SpriteGraphicFactoryMockTestImpl.jsonURL, jsonResponse);

                final MockHttpResponse pngResponse =
                        new MockHttpResponse(
                                toByteArray(SpriteGraphicFactoryMockTestImpl.pngURL), "image/png");
                expectGet(SpriteGraphicFactoryMockTestImpl.pngURL, pngResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
