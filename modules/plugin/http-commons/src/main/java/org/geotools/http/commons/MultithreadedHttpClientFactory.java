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
package org.geotools.http.commons;

import org.geotools.http.AbstractHTTPClientFactory;
import org.geotools.http.HTTPClient;

/**
 * Factory for MultithreadedHttpClient
 *
 * <p>To use client set Hints.HTTP_CLIENT_FACTORY=MultithreadedHttpClientFactory.class, or
 * Hints.HTTP_CLIENT=MultithreadedHttpClient.class
 *
 * @author Roar Brænden
 */
public class MultithreadedHttpClientFactory extends AbstractHTTPClientFactory {

    @Override
    public Class<? extends HTTPClient> getClientClass() {
        return MultithreadedHttpClient.class;
    }

    @Override
    public HTTPClient createClient() {
        return new MultithreadedHttpClient();
    }
}
