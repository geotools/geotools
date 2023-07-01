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
import static org.junit.Assert.assertThrows;

import java.net.URL;
import javax.swing.Icon;
import org.geotools.api.filter.FilterFactory2;
import org.geotools.api.filter.expression.Literal;
import org.geotools.data.ows.MockURLChecker;
import org.geotools.data.ows.URLCheckerException;
import org.geotools.data.ows.URLCheckers;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        URLCheckers.reset();
    }

    @Test
    public void testJsonCharset() throws Exception {
        Literal spriteExpression = getAerialWayLocation();
        SpriteGraphicFactory factory = new SpriteGraphicFactory();
        Icon icon = factory.getIcon(null, spriteExpression, "mbsprite", 15);
        assertNotNull(icon);
    }

    private static Literal getAerialWayLocation() {
        String urlStr = pngURL.toExternalForm();
        String spriteBaseUrl = urlStr.substring(0, urlStr.lastIndexOf(".png"));
        final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();
        Literal spriteExpression = FF.literal(spriteBaseUrl + "#aerialway_11");
        return spriteExpression;
    }

    @Test
    public void testURLCheckerAllowed() throws Exception {
        URLCheckers.register(new MockURLChecker(u -> u.contains("osm-liberty")));

        Literal spriteExpression = getAerialWayLocation();
        SpriteGraphicFactory factory = new SpriteGraphicFactory();
        Icon icon = factory.getIcon(null, spriteExpression, "mbsprite", 15);
        assertNotNull(icon);
    }

    @Test
    public void testURLCheckerDisallowed() throws Exception {
        URLCheckers.register(new MockURLChecker("nope", u -> false));

        Literal spriteExpression = getAerialWayLocation();
        SpriteGraphicFactory factory = new SpriteGraphicFactory();
        assertThrows(
                URLCheckerException.class,
                () -> factory.getIcon(null, spriteExpression, "mbsprite", 15));
    }
}
