/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ows;

/**
 * ControlledHttpClientFactory provides static method to enable URL/URI evaluation on any
 * implementation of org.geotools.data.ows.HTTPClient
 *
 * @author Imran Rajjad - Geo Solutions
 */
public class ControlledHttpClientFactory {

    /**
     * This method accepts an instance org.geotools.data.ows.HTTPClient and wraps it in
     * ControlledHttpClient to enabled URL evaluation
     *
     * <p>If no URLCheckers are configured or there are no enabled URLCheckers, then no wrapping
     * will occur
     *
     * @param client instance of org.geotools.data.ows.HTTPClient
     * @return ControlledHttpClient
     */
    public static HTTPClient wrap(HTTPClient client) {
        // only wrap if URLCheckers are configured
        if (URLCheckers.getEnabledURLCheckerList().isEmpty()) return client;
        return new ControlledHttpClient(client);
    }
}
