/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2008, Refractions Research Inc.
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

import org.geotools.tile.TileFactory;
import org.geotools.tile.impl.WebMercatorTileService;

/**
 * The tile service for the OpenStreetMap family.
 *
 * @author Tobias Sauerwein
 * @author Ugo Taddei
 * @since 12
 */
public class OSMService extends WebMercatorTileService {

    private static final TileFactory tileFactory = new OSMTileFactory();

    private static double[] scaleList = {
        Double.NaN,
        Double.NaN,
        147914381,
        73957190,
        36978595,
        18489297,
        9244648,
        4622324,
        2311162,
        1155581,
        577790,
        288895,
        144447,
        72223,
        36111,
        18055,
        9027,
        4513,
        2256,
        1128,
        564,
        282,
        141,
        70
    };

    public OSMService(String name, String baseUrl) {
        super(name, baseUrl);
    }

    @Override
    public double[] getScaleList() {
        return scaleList;
    }

    @Override
    public TileFactory getTileFactory() {
        return tileFactory;
    }
}
