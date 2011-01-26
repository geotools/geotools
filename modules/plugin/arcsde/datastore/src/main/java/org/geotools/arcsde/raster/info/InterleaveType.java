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

import java.util.NoSuchElementException;

import com.esri.sde.sdk.client.SeRaster;

/**
 * An enumeration that mirrors the different possible band interleave types in Arcsde (ie, {@code
 * SeRaster#SE_RASTER_INTERLEAVE_*})
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.4
 * @version $Id$
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/main/java/org
 *         /geotools/arcsde/raster/info/InterleaveType.java $
 */
public enum InterleaveType {
    INTERLEAVE_BIL, INTERLEAVE_BIL_91, INTERLEAVE_BIP, INTERLEAVE_BIP_91, INTERLEAVE_BSQ, INTERLEAVE_BSQ_91, INTERLEAVE_NONE;
    static {
        INTERLEAVE_BIL.setSdeTypeId(SeRaster.SE_RASTER_INTERLEAVE_BIL);
        INTERLEAVE_BIL_91.setSdeTypeId(SeRaster.SE_RASTER_INTERLEAVE_BIL_91);
        INTERLEAVE_BIP.setSdeTypeId(SeRaster.SE_RASTER_INTERLEAVE_BIP);
        INTERLEAVE_BIP_91.setSdeTypeId(SeRaster.SE_RASTER_INTERLEAVE_BIP_91);
        INTERLEAVE_BSQ.setSdeTypeId(SeRaster.SE_RASTER_INTERLEAVE_BSQ);
        INTERLEAVE_BSQ_91.setSdeTypeId(SeRaster.SE_RASTER_INTERLEAVE_BSQ_91);
        INTERLEAVE_NONE.setSdeTypeId(SeRaster.SE_RASTER_INTERLEAVE_NONE);
    }

    private int typeId;

    private void setSdeTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getSeRasterInterleaveType() {
        return this.typeId;
    }

    public static InterleaveType valueOf(final int seRasterInterleaveType) {
        for (InterleaveType type : InterleaveType.values()) {
            if (type.getSeRasterInterleaveType() == seRasterInterleaveType) {
                return type;
            }
        }
        throw new NoSuchElementException("Raster interleave type " + seRasterInterleaveType
                + " does not exist");
    }
}
