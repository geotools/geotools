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
package org.geotools.arcsde.raster.info;

import java.awt.Point;
import java.awt.image.IndexColorModel;

/**
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.4
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/main/java/org
 *         /geotools/arcsde/raster/info/RasterBandInfo.java $
 */
public class RasterBandInfo {

    long bandId;

    String bandName;

    int bandNumber;

    /**
     * the color map as it is on the database, except that we always create a color map with alpha
     * channel regardless of whether the native on has alpha or not, to account for the no-data
     * pixel to be a transparent one where appropriate
     */
    IndexColorModel nativeColorMap;

    /**
     * The color map as it is going to be used by this library. May differ from the native one
     * either in the number of elements (when the native color map is not full and it has no
     * transparent pixel to be used as no-data value), or in the pixel depth (when the native
     * colormap is full, has no no-data pixel, and hence it needs to be promoted to a higher
     * transfer type to make room for a no-data index)
     */
    IndexColorModel colorMap;

    /**
     * The band's no-data value.
     */
    Number noDataValue;

    CompressionType compressionType;

    RasterCellType cellType;

    InterleaveType interleaveType;

    InterpolationType interpolationType;

    boolean hasStats;

    Point tileOrigin;

    double statsMin;

    double statsMax;

    double statsMean;

    double statsStdDev;

    public RasterBandInfo() {
        // do nothing
    }

    public RasterBandInfo(long bandId, RasterCellType nativeType, Number noDataValue,
            double statsMin, double statsMax) {
        this.bandId = bandId;
        this.cellType = nativeType;
        this.noDataValue = noDataValue;
        this.statsMin = statsMin;
        this.statsMax = statsMax;
    }

    /**
     * @return the ArcSDE identifier for the band
     */
    public long getBandId() {
        return bandId;
    }

    public String getBandName() {
        return bandName;
    }

    public int getBandNumber() {
        return bandNumber;
    }

    public boolean isColorMapped() {
        return nativeColorMap != null;
    }

    public CompressionType getCompressionType() {
        return compressionType;
    }

    public RasterCellType getCellType() {
        return cellType;
    }

    public InterleaveType getInterleaveType() {
        return interleaveType;
    }

    public InterpolationType getInterpolationType() {
        return interpolationType;
    }

    public boolean isHasStats() {
        return hasStats;
    }

    public Point getTileOrigin() {
        return tileOrigin;
    }

    public IndexColorModel getNativeColorMap() {
        return nativeColorMap;
    }

    public IndexColorModel getColorMap() {
        return colorMap;
    }

    public double getStatsMin() {
        return statsMin;
    }

    public double getStatsMax() {
        return statsMax;
    }

    public double getStatsMean() {
        return statsMean;
    }

    public double getStatsStdDev() {
        return statsStdDev;
    }

    public Number getNoDataValue() {
        return noDataValue;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getBandName());
        sb.append("[ id:").append(getBandId());
        sb.append(", type:").append(getCellType());
        sb.append(", samples: nodata=").append(getNoDataValue()).append(" min=").append(
                getStatsMin()).append(" max=").append(getStatsMax()).append(" mean=").append(
                getStatsMean()).append(" stddev=").append(getStatsStdDev());
        /*
         * sb.append(", tile origin: ").append((int) getTileOrigin().x).append(",").append( (int)
         * getTileOrigin().y);
         */
        sb.append(", compression:").append(getCompressionType());
        sb.append(", interpolation:").append(getInterpolationType());
        sb.append(", Color Map: ").append(isColorMapped() ? "YES" : "NO");
        return sb.toString();
    }

}
