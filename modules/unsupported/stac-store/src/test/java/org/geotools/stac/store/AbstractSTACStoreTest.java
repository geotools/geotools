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
import java.net.URL;
import org.geotools.stac.STACOfflineTest;
import org.geotools.stac.client.STACClient;

public abstract class AbstractSTACStoreTest extends STACOfflineTest {

    protected static String MAJA_100 =
            BASE_URL + "/search?f=application%2Fgeo%2Bjson&collections=S2_L2A_MAJA&limit=100";
    protected static final String NS_URI = "https://stacspec.org/store";
    protected static final String MAJA = "S2_L2A_MAJA";
    protected static final String[] TYPE_NAMES = {"D4H", MAJA, "S2_L3A_WASP", "SUPERSITES", "WSF_2019"};
    protected STACDataStore store;

    @Override
    public void setup() throws IOException {
        super.setup();

        Class<?> cls = STACOfflineTest.class;
        httpClient.expectGet(new URL(MAJA_100), geojsonResponse("majaAll.json", cls));

        @SuppressWarnings("PMD.CloseResource") // managed by the store
        STACClient client = new STACClient(new URL(LANDING_PAGE_URL), httpClient);
        this.store = new STACDataStore(client);
        this.store.setSearchMode(STACClient.SearchMode.GET);
        this.store.setNamespaceURI(NS_URI);
    }
}
