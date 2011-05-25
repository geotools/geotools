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
 * An enumeration that mirrors the different possible raster compression types in Arcsde (ie,
 * {@code SeRaster#SE_COMPRESSION_*})
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.4
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/main/java/org
 *         /geotools/arcsde/raster/info/CompressionType.java $
 */
public enum CompressionType {
    COMPRESSION_JP2, COMPRESSION_JPEG, COMPRESSION_LZ77, COMPRESSION_NONE;
    static {
        COMPRESSION_JP2.setSdeTypeId(SeRaster.SE_COMPRESSION_JP2);
        COMPRESSION_JPEG.setSdeTypeId(SeRaster.SE_COMPRESSION_JPEG);
        COMPRESSION_LZ77.setSdeTypeId(SeRaster.SE_COMPRESSION_LZ77);
        COMPRESSION_NONE.setSdeTypeId(SeRaster.SE_COMPRESSION_NONE);
    }

    private int typeId;

    private void setSdeTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getSeCompressionType() {
        return this.typeId;
    }

    public static CompressionType valueOf(final int seCompressionType) {
        for (CompressionType type : CompressionType.values()) {
            if (type.getSeCompressionType() == seCompressionType) {
                return type;
            }
        }
        throw new NoSuchElementException("Compression type " + seCompressionType
                + " does not exist");
    }
}
