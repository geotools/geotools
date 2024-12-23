/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.image.BufferedImage;
import org.geotools.test.OnlineTestCase;
import org.geotools.tile.Tile;
import org.geotools.tile.Tile.RenderState;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.WebMercatorZoomLevel;
import org.junit.Assert;
import org.junit.Test;

/**
 * Online test against tile.openstreetmap.org Activate by adding empty file "osm_tile_client.properties"
 *
 * @author Roar Brænden
 */
public class OSMServiceOnlineTest extends OnlineTestCase {

    @Override
    protected String getFixtureId() {
        return "osm_tile_client";
    }

    @Test
    public void testImageLoadTileOnline() throws Exception {
        final TileService service = new OSMService("OSM", "http://tile.openstreetmap.org/");
        final Tile tile = new OSMTile(2166, 1189, new WebMercatorZoomLevel(12), service);
        final BufferedImage image = tile.getBufferedImage();
        Assert.assertNotNull("getBufferedImage should return image even if fail.", image);
        Assert.assertEquals(
                "getRenderState will be INVALID if image fetch has failed.",
                RenderState.RENDERED,
                tile.getRenderState());
    }
}
