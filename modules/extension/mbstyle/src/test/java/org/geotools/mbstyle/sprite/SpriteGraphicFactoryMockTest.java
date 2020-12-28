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

import java.io.IOException;
import java.net.URL;
import javax.swing.Icon;
import org.geotools.data.ows.MockHttpClient;
import org.geotools.data.ows.MockHttpResponse;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;

public class SpriteGraphicFactoryMockTest {

    private static final URL pngURL =
            SpriteGraphicFactoryMockTest.class.getResource("test-data/liberty/osm-liberty.png");
    private static final URL jsonURL =
            SpriteGraphicFactoryMockTest.class.getResource("test-data/liberty/osm-liberty.json");

    public static class SpriteMockHttpClient extends MockHttpClient {

        public SpriteMockHttpClient() {

            try {
                final MockHttpResponse jsonResponse =
                        new MockHttpResponse(toByteArray(jsonURL), "application/json");
                expectGet(jsonURL, jsonResponse);

                final MockHttpResponse pngResponse =
                        new MockHttpResponse(toByteArray(pngURL), "image/png");
                expectGet(pngURL, pngResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Before
    public void setup() throws Exception {
        Hints.putSystemDefault(Hints.HTTP_CLIENT, SpriteMockHttpClient.class);
    }

    @After
    public void tearDown() throws Exception {
        Hints.removeSystemDefault(Hints.HTTP_CLIENT);
    }

    @Test
    public void testJsonCharset() throws Exception {
        String urlStr = pngURL.toExternalForm();
        String spriteBaseUrl = urlStr.substring(0, urlStr.lastIndexOf(".png"));
        final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

        SpriteGraphicFactory factory = new SpriteGraphicFactory();
        Icon icon =
                factory.getIcon(null, FF.literal(spriteBaseUrl + "#aerialway_11"), "mbsprite", 15);
        assertNotNull(icon);
    }
}
