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

import static org.junit.Assert.assertNotNull;

import java.net.URL;
import javax.swing.Icon;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;

public class SpriteGraphicFactoryMockTest {

    static final URL pngURL =
            SpriteGraphicFactoryMockTest.class.getResource("test-data/liberty/osm-liberty.png");
    static final URL jsonURL =
            SpriteGraphicFactoryMockTest.class.getResource("test-data/liberty/osm-liberty.json");

    @Before
    public void setup() throws Exception {
        Hints.putSystemDefault(Hints.HTTP_CLIENT_FACTORY, SpriteMockHttpClientFactory.class);
    }

    @After
    public void tearDown() throws Exception {
        Hints.removeSystemDefault(Hints.HTTP_CLIENT_FACTORY);
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
