/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.http;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * Default factory for generating HTTP client's. Will deliver a SimpleHttpClient, or a HTTPClient
 * given by the hint HTTP_CLIENT.
 *
 * @author Roar Brænden
 */
public class DefaultHTTPClientFactory extends AbstractHTTPClientFactory {

    private static final Logger LOGGER = Logging.getLogger(DefaultHTTPClientFactory.class);

    @SuppressWarnings("rawtypes")
    @Override
    protected HTTPClient createClient(Hints hints) {
        if (!hints.containsKey(Hints.HTTP_CLIENT)) {
            return new SimpleHttpClient();
        } else {
            try {
                Class cls = (Class) hints.get(Hints.HTTP_CLIENT);
                return (HTTPClient) cls.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                LOGGER.log(Level.SEVERE, "Exception when initiating new HTTP client.", ex);
                throw new RuntimeException(
                        String.format(
                                "A class couldn't be initiated: %s", hints.get(Hints.HTTP_CLIENT)));
            }
        }
    }
}
