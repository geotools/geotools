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

import static org.junit.Assert.assertTrue;

import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.Hints;

import org.junit.Test;
/**
 * 
 * @author Roar Brænden
 *
 */
public class CommonFactoryFinderTest {
	
	@Test
	public void findingSimpleHttpClientAsDefault() throws Exception {
        HTTPClient client = CommonFactoryFinder.getHttpClientFactory().getClient();

        assertTrue(client != null);
        assertTrue(client instanceof SimpleHttpClient);
	}

    @Test
    public void findingSimpleHttpClientTestByHints() throws Exception {

        HTTPClient client =
                CommonFactoryFinder.getHttpClientFactory()
                        .getClient(new Hints(
                                Hints.HTTP_CLIENT,
                                SimpleHttpClient.class));

        assertTrue(client instanceof SimpleHttpClient);
 
    }
    
    @Test
    public void findingMultithreadedHttpClientTestByHints() throws Exception {

        HTTPClient client =
                CommonFactoryFinder.getHttpClientFactory()
                        .getClient(new Hints(
                                Hints.HTTP_CLIENT,
                                MultithreadedHttpClient.class));

        assertTrue(client instanceof MultithreadedHttpClient);
    }
}
