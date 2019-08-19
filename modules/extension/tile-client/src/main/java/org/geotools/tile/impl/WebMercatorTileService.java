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
package org.geotools.tile.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.tile.TileService;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The WebMercatorTileService is an abstract class that holds some of the tile service logic for
 * Mercator-based tile services.
 *
 * @author Ugo Taddei
 * @since 12
 */
public abstract class WebMercatorTileService extends TileService {

    private static final Logger LOGGER = Logging.getLogger(WebMercatorTileService.class);

    public static final double MIN_LONGITUDE = -180;

    public static final double MIN_LATITUDE = -85.05112878;

    public static final double MAX_LONGITUDE = 180;

    public static final double MAX_LATITUDE = 85.05112878;

    public static final CoordinateReferenceSystem WEB_MERCATOR_CRS;

    static {
        CoordinateReferenceSystem tmpCrs = null;

        try {
            tmpCrs = CRS.decode("EPSG:3857");
        } catch (FactoryException e) {
            LOGGER.log(Level.SEVERE, "Failed to create Web Mercator CRS EPSG:3857", e);
            throw new RuntimeException(e);
        }

        WEB_MERCATOR_CRS = tmpCrs;
    }

    protected WebMercatorTileService(String name, String baseURL) {
        super(name, baseURL);
    }

    public ReferencedEnvelope getBounds() {
        return new ReferencedEnvelope(
                MIN_LONGITUDE,
                MAX_LONGITUDE,
                MIN_LATITUDE,
                MAX_LATITUDE,
                DefaultGeographicCRS.WGS84);
    }

    public CoordinateReferenceSystem getProjectedTileCrs() {
        return WEB_MERCATOR_CRS;
    }
}
