/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.arcsde.raster.io;

import java.io.IOException;

/**
 * Offers a random access interface to the tile data for a raster request.
 * <p>
 * Implementations are expected to perform better when the tiles are requested in sequential x/y
 * order (e.g., 0,0; 1,0; 2,0; 0,1; 1,1; 2,1 for 3x2 tile set), though they're required to be able
 * to return any randomly requested tile, probably by being forced to issue a separate request to
 * the server or do some cacheing.
 * </p>
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.4
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/main/java/org
 *         /geotools/arcsde/gce/TileReader.java $
 */
public interface TileReader {

    /**
     * @return number of bits per sample
     */
    public abstract int getBitsPerSample();

    /**
     * @return number of samples per tile
     */
    public abstract int getPixelsPerTile();

    /**
     * @return numbre of bands being fetched
     */
    public abstract int getNumberOfBands();

    /**
     * @return number of pixels per tile over the X axis
     */
    public abstract int getTileWidth();

    /**
     * @return number of pixels per tile over the Y axis
     */
    public abstract int getTileHeight();

    /**
     * @return number of tiles being fetched over the X axis
     */
    public abstract int getTilesWide();

    /**
     * @return number of tiles being fetched over the Y axis
     */
    public abstract int getTilesHigh();

    /**
     * @return number of bytes in the raw pixel content of a tile, not taking into account any
     *         trailing bitmask data.
     */
    public abstract int getBytesPerTile();

    /**
     * Disposes any resource being held by this TileReader, making the TileReader unusable and the
     * behaviour of {@link #hasNext()} and {@link #next} unpredictable
     */
    public abstract void dispose();

    public String getServerName();

    public String getRasterTableName();

    public abstract long getRasterId();

    public abstract int getPyramidLevel();

    public abstract int getMinTileX();

    public abstract int getMinTileY();

    public abstract void getTile(int tileX, int tileY, byte[][] data) throws IOException;

    public abstract void getTile(int tileX, int tileY, short[][] data) throws IOException;

    public abstract void getTile(int tileX, int tileY, int[][] data) throws IOException;

    public abstract void getTile(int tileX, int tileY, float[][] data) throws IOException;

    public abstract void getTile(int tileX, int tileY, double[][] data) throws IOException;

    public abstract int getMaxTileX();

    public abstract int getMaxTileY();

}
