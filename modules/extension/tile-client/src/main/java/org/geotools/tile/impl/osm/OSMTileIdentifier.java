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

import org.geotools.tile.TileIdentifier;
import org.geotools.tile.impl.ZoomLevel;

/**
 * The TileIdentifier implementation for the OpenStreetMap family. This identifier follows the grid logic of similar
 * implementations. Please refer to the <a href="http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames">OpenStreetMap
 * Wiki</a> for the exact description.
 *
 * @author Ugo Taddei
 * @since 12
 */
public class OSMTileIdentifier extends TileIdentifier {

    public OSMTileIdentifier(int x, int y, ZoomLevel zoomLevel, String serviceName) {
        super(x, y, zoomLevel, serviceName);
    }

    @Override
    public String getId() {
        final String separator = "_";
        StringBuilder sb = createGenericCodeBuilder(separator);
        sb.insert(0, separator).insert(0, getServiceName());
        return sb.toString();
    }

    @Override
    public String getCode() {
        final String separator = "/";
        return createGenericCodeBuilder(separator).toString();
    }

    private StringBuilder createGenericCodeBuilder(final String separator) {
        StringBuilder sb = new StringBuilder(50);
        sb.append(getZ()).append(separator).append(getX()).append(separator).append(getY());

        return sb;
    }

    @Override
    public TileIdentifier getRightNeighbour() {
        return new OSMTileIdentifier(
                TileIdentifier.arithmeticMod(getX() + 1, getZoomLevel().getMaxTilePerRowNumber()),
                getY(),
                getZoomLevel(),
                getServiceName());
    }

    @Override
    public TileIdentifier getLowerNeighbour() {
        return new OSMTileIdentifier(
                getX(),
                TileIdentifier.arithmeticMod(getY() + 1, getZoomLevel().getMaxTilePerRowNumber()),
                getZoomLevel(),
                getServiceName());
    }
}
