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

import org.geotools.tile.Tile;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.WebMercatorZoomLevel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OSMTileTest {

    private Tile tile;

    @Before
    public void beforeTest() {

        String baseURL = "http://tile.openstreetmap.org/";
        TileService service = new OSMService("Mapnik", baseURL);
        OSMTileIdentifier tileIdentifier =
                new OSMTileIdentifier(10, 12, new WebMercatorZoomLevel(5), service.getName());

        this.tile = new OSMTile(tileIdentifier, service);
    }

    @Test
    public void testConstructor() {

        Assert.assertNotNull(this.tile);
    }

    @Test
    public void testGetURL() {
        Assert.assertEquals(
                "http://tile.openstreetmap.org/5/10/12.png", this.tile.getUrl().toString());
    }
}
