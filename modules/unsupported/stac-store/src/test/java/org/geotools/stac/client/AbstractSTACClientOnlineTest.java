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
package org.geotools.stac.client;

import java.net.URL;
import org.geotools.http.commons.MultithreadedHttpClient;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;

public abstract class AbstractSTACClientOnlineTest {

    protected STACClient client;

    @Before
    public void setup() {
        try {
            this.client = new STACClient(new URL(getServerURL()), new MultithreadedHttpClient());
            Assume.assumeNotNull("Could not find landing page", client.getLandingPage());
        } catch (Exception e) {
            Assume.assumeNoException("Could not connect to STAC test server, skipping", e);
        }
    }

    protected abstract String getServerURL();

    @After
    public void releaseClient() throws Exception {
        if (this.client != null) this.client.close();
    }
}
