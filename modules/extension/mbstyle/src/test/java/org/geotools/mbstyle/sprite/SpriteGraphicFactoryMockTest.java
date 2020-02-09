/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import javax.swing.*;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.MockHttpClient;
import org.geotools.data.ows.MockHttpResponse;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;

public class SpriteGraphicFactoryMockTest {

    static class TestSpriteGraphicFactory extends SpriteGraphicFactory {

        URL jsonResource;
        URL pngResource;

        public TestSpriteGraphicFactory(URL jsonResource, URL pngResource) {
            this.jsonResource = jsonResource;
            this.pngResource = pngResource;
        }

        @Override
        protected HTTPClient getHttpClient() {
            MockHttpClient client = new MockHttpClient();
            try {
                MockHttpResponse jsonResponse =
                        new MockHttpResponse(toByteArray(jsonResource), "application/json");
                jsonResponse.setResponseCharset("UTF-8");
                client.expectGet(jsonResource, jsonResponse);

                MockHttpResponse pngResponse =
                        new MockHttpResponse(toByteArray(pngResource), "image/png");
                client.expectGet(pngResource, pngResponse);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return client;
        }
    }

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    @Test
    public void testJsonCharset() throws Exception {
        URL pngURL = this.getClass().getResource("test-data/liberty/osm-liberty.png");
        URL jsonURL = this.getClass().getResource("test-data/liberty/osm-liberty.json");
        String urlStr = pngURL.toExternalForm();
        String spriteBaseUrl = urlStr.substring(0, urlStr.lastIndexOf(".png"));

        SpriteGraphicFactory factory = new TestSpriteGraphicFactory(jsonURL, pngURL);
        Icon icon =
                factory.getIcon(null, FF.literal(spriteBaseUrl + "#aerialway_11"), "mbsprite", 15);
        assertNotNull(icon);
    }
}
