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

import org.geotools.tile.TileFactory;
import org.geotools.tile.impl.WebMercatorTileService;

/**
 * The Bing tile service.
 *
 * <p>Note that Bing requires a key, which you can generate <a
 * href="http://www.microsoft.com/maps/create-a-bing-maps-key.aspx">here</a>. This service follows
 * the documentation for the <a
 * href="https://msdn.microsoft.com/en-us/library/ff701721.aspx">Imagery API</a>
 *
 * <p>In order for the BingService to work correctly, you must use the URL fragment provided by <a
 * href="https://msdn.microsoft.com/en-us/library/ff701716.aspx">Get Imagery Metadata</a>. In
 * particular, you need to instantiate a BingService with a URL template such as <code>
 * http://ecn.subdomain.tiles.virtualearth.net/tiles/r${code}.jpeg?key=YOUR_BING_KEY&g=129&mkt={culture}&shading=hill&stl=H
 * </code> .
 *
 * <pre>
 * String baseURL = &quot;http://ecn.subdomain.tiles.virtualearth.net/tiles/r${code}.jpeg?key=YOUR_BING_KEY&g=129&mkt={culture}&shading=hill&stl=H;&quot;
 * TileService service = new BingService(&quot;Road&quot;, baseURL);
 *
 * // you may add to a map:
 * map.addLayer(new TileLayer(service));
 *
 * // or do some hard work to fetch the tiles
 *  Collection<Tile> tiles = service.findTilesInExtent(viewportExtent,
 *          scale, false, 128);
 * </pre>
 *
 * The "${code}" value will be substituted by the tile code (the quadkey) when the BingTile creates
 * its URL.
 *
 * @author Ugo Taddei
 * @since 12
 */
public class BingService extends WebMercatorTileService {

    private static final TileFactory tileFactory = new BingTileFactory();

    // from https://msdn.microsoft.com/en-us/library/bb259689.aspx
    private static double[] SCALE_LIST = {
        295829355.45,
        147914677.73,
        73957338.86,
        36978669.43,
        18489334.72,
        9244667.36,
        4622333.68,
        2311166.84,
        1155583.42,
        577791.71,
        288895.85,
        144447.93,
        72223.96,
        36111.98,
        18055.99,
        9028.0,
        4514.0,
        2257.0,
        1128.50,
        564.25,
        282.12,
        141.06,
        70.53
    };

    public BingService(String name, String baseUrl) {
        super(name, baseUrl);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.geotools.tile.TileService
     */
    @Override
    public double[] getScaleList() {
        return SCALE_LIST;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.TileService.udig.catalog.internal.wmt.wmtsource.WMTSource#getTileFactory()
     */
    public TileFactory getTileFactory() {
        return tileFactory;
    }
}
