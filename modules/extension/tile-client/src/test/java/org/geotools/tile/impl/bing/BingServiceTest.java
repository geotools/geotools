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
package org.geotools.tile.impl.bing;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.tile.ServiceTest;
import org.geotools.tile.Tile;
import org.geotools.tile.TileService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BingServiceTest extends ServiceTest {

    private static Map<String, List<String>> extentNameToUrlList;

    @BeforeClass
    public static void beforeClass() {
        ServiceTest.beforeClass();

        String urlPrefix = "http://ak.dynamic.t2.tiles.virtualearth.net/comp/ch/";
        String urlSuffix = "?mkt=de-de&it=G,VE,BX,L,LA&shading=hill&og=78&n=z";

        extentNameToUrlList = new HashMap<String, List<String>>();
        List<String> expectedIds_DE =
                Arrays.asList(new String[] {"12022", "12021", "12023", "12020"});

        enrichIdWithNameAndExtension(urlPrefix, expectedIds_DE, urlSuffix);
        extentNameToUrlList.put(DE_EXTENT_NAME, expectedIds_DE);

        List<String> expectedIds_BR =
                Arrays.asList(
                        new String[] {
                            "2112000123",
                            "2112000031",
                            "2112000120",
                            "2112000121",
                            "2112000033",
                            "2112000122"
                        });
        enrichIdWithNameAndExtension(urlPrefix, expectedIds_BR, urlSuffix);
        extentNameToUrlList.put(BR_EXTENT_NAME, expectedIds_BR);

        List<String> expectedIds_HAWAII =
                Arrays.asList(new String[] {"022211", "022300", "022033", "022122"});

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
        Assert.assertEquals("RoadLayerService", service.getName());
    }

    @Test
    public void testGetBaseURL() {
        TileService service = createService();
        Assert.assertEquals(
                "http://ak.dynamic.t2.tiles.virtualearth.net/comp/ch/${code}?mkt=de-de&it=G,VE,BX,L,LA&shading=hill&og=78&n=z",
                service.getBaseUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalName() {
        new BingService("", "http://localhost/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalNullName() {
        new BingService(null, "http://localhost/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalURL() {
        new BingService("Blah", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalNullURL() {
        new BingService("Blah", null);
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
        Collection<Tile> tiles = service.findTilesInExtent(extent, scale, true, 28);

        return tiles;
    }

    public List<String> getUrlList(String extentName) {
        return extentNameToUrlList.get(extentName);
    }

    private TileService createService() {
        String baseURL =
                "http://ak.dynamic.t2.tiles.virtualearth.net/comp/ch/${code}?mkt=de-de&it=G,VE,BX,L,LA&shading=hill&og=78&n=z";
        return new BingService("RoadLayerService", baseURL);
    }
}
