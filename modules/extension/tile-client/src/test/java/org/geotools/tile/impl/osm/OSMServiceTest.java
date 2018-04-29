/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.tile.impl.osm;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.tile.ServiceTest;
import org.geotools.tile.Tile;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.bing.BingService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class OSMServiceTest extends ServiceTest {

    private static Map<String, List<String>> extentNameToUrlList;

    @BeforeClass
    public static void beforeClass() {
        ServiceTest.beforeClass();

        String urlPrefix = "http://tile.openstreetmap.org/";
        String urlSuffix = ".png";

        extentNameToUrlList = new HashMap<String, List<String>>();
        List<String> expectedIds_DE =
                Arrays.asList(new String[] {"5/16/11", "5/17/11", "5/16/10", "5/17/10"});

        enrichIdWithNameAndExtension(urlPrefix, expectedIds_DE, urlSuffix);
        extentNameToUrlList.put(DE_EXTENT_NAME, expectedIds_DE);

        List<String> expectedIds_BR =
                Arrays.asList(
                        new String[] {
                            "10/387/578",
                            "10/389/578",
                            "10/388/578",
                            "10/389/579",
                            "10/388/579",
                            "10/387/579"
                        });
        enrichIdWithNameAndExtension(urlPrefix, expectedIds_BR, urlSuffix);
        extentNameToUrlList.put(BR_EXTENT_NAME, expectedIds_BR);

        List<String> expectedIds_HAWAII =
                Arrays.asList(new String[] {"6/4/27", "6/3/28", "6/3/27", "6/4/28"});

        enrichIdWithNameAndExtension(urlPrefix, expectedIds_HAWAII, urlSuffix);
        extentNameToUrlList.put(HAWAII_EXTENT_NAME, expectedIds_HAWAII);
    }

    private static void enrichIdWithNameAndExtension(
            String prefix, List<String> expectedIds, String suffix) {
        for (int i = 0; i < expectedIds.size(); i++) {
            String oldValue = expectedIds.get(i);
            String newValue = prefix + oldValue + suffix;
            expectedIds.set(i, newValue);
        }
    }

    @Test
    public void testGetTilesInExtents() {

        testGetTilesInExtent(DE_EXTENT_NAME, 5957345);
        testGetTilesInExtent(BR_EXTENT_NAME, 500000);
        testGetTilesInExtent(HAWAII_EXTENT_NAME, 5957345);
    }

    @Test
    public void testGetName() {
        TileService service = createService();
        Assert.assertEquals("OSM", service.getName());
    }

    @Test
    public void testGetBaseURL() {
        TileService service = createService();
        Assert.assertEquals("http://tile.openstreetmap.org/", service.getBaseUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalName() {
        new BingService("", "http://localhost/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalNullName() {
        new OSMService(null, "http://localhost/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalURL() {
        new OSMService("Blah", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalNullURL() {
        new OSMService("Blah", null);
    }

    private void testGetTilesInExtent(final String extentName, int scale) {

        Collection<Tile> tiles = findTilesInExtent(getExtent(extentName), scale);

        List<String> expectedUrls = getUrlList(extentName);
        Assert.assertEquals(expectedUrls.size(), tiles.size());
        for (Tile t : tiles) {
            Assert.assertTrue(expectedUrls.contains(t.getUrl().toString()));
        }
    }

    private Collection<Tile> findTilesInExtent(ReferencedEnvelope extent, int scale) {

        TileService service = createService();
        Collection<Tile> tileList = service.findTilesInExtent(extent, scale, true, 28);

        return tileList;
    }

    public List<String> getUrlList(String extentName) {
        return extentNameToUrlList.get(extentName);
    }

    private TileService createService() {
        String baseURL = "http://tile.openstreetmap.org/";
        return new OSMService("OSM", baseURL);
    }
}
