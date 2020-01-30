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
package org.geotools.renderer.style;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.awt.*;
import java.util.List;
import org.geotools.renderer.lite.StreamingRendererTest;
import org.junit.After;
import org.junit.Assume;
import org.junit.Test;

public class FontCacheTest {

    @After
    public void cleanCache() {
        FontCache.getDefaultInstance().resetCache();
    }

    @Test
    public void testUnregisterdAlternatives() {
        // if the system already contains droid sans, this test will fail
        FontCache fc = FontCache.getDefaultInstance();
        Assume.assumeThat(fc.getAvailableFonts(), not(hasItem(startsWith("Droid"))));

        List<String> alternatives = fc.getAlternatives("Droid");
        assertThat(alternatives, empty());
    }

    @Test
    public void testManualAlternatives() {
        FontCache fc = FontCache.getDefaultInstance();
        fc.registerFont(loadFont("DroidSansArmenian.ttf"));
        fc.registerFont(loadFont("DroidSansFallback.ttf"));
        fc.registerFont(loadFont("DroidNaskh-Regular.ttf"));

        List<String> alternatives = fc.getAlternatives("Droid");
        assertThat(
                alternatives,
                hasItems("Droid Arabic Naskh", "Droid Sans Armenian", "Droid Sans Fallback"));
    }

    private Font loadFont(String fontName) {
        return FontCache.loadFromUrl(
                StreamingRendererTest.class.getResource("test-data/" + fontName).toExternalForm());
    }
}
