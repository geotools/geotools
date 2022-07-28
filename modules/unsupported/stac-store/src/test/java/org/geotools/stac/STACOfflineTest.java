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
package org.geotools.stac;

import static org.geotools.stac.client.STACClient.GEOJSON_MIME;
import static org.geotools.stac.client.STACClient.JSON_MIME;

import java.io.IOException;
import java.net.URL;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;
import org.junit.Before;

public class STACOfflineTest {

    protected static final String BASE_URL = "https://geoservice.dlr.de/eoc/ogc/stac";
    protected static final String LANDING_PAGE_URL = BASE_URL;
    protected static final String COLLECTIONS_URL = BASE_URL + "/collections?f=application%2Fjson";
    protected static final String MAJA_ALL_URL =
            BASE_URL + "/search?f=application%2Fgeo%2Bjson&collections=S2_L2A_MAJA";
    protected static final String MAJA_ONE =
            BASE_URL
                    + "/search?f=application%2Fgeo%2Bjson&collections=S2_L2A_MAJA&datetime=2022"
                    + "-07-14T10:46:29.0240000Z";
    protected MockHttpClient httpClient;

    @Before
    public void setup() throws IOException {
        this.httpClient = new MockHttpClient();
        Class<?> cls = STACOfflineTest.class;
        httpClient.expectGet(new URL(LANDING_PAGE_URL), jsonResponse("landingPage.json", cls));
        httpClient.expectGet(new URL(COLLECTIONS_URL), jsonResponse("collections.json", cls));
        httpClient.expectGet(new URL(MAJA_ALL_URL), geojsonResponse("majaAll.json", cls));
        httpClient.expectGet(new URL(MAJA_ONE), geojsonResponse("majaOne.json", cls));
    }

    protected HTTPResponse jsonResponse(String fileName, Class<?> cls) {
        return new MockHttpResponse(cls.getResourceAsStream(fileName), JSON_MIME);
    }

    protected HTTPResponse geojsonResponse(String fileName, Class<?> cls) {
        return new MockHttpResponse(cls.getResourceAsStream(fileName), GEOJSON_MIME);
    }
}
