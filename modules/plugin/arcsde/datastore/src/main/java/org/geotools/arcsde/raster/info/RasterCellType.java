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

import java.awt.image.DataBuffer;
import java.util.NoSuchElementException;

import org.geotools.util.NumberRange;

import com.esri.sde.sdk.client.SeRaster;

/**
 * An enumeration that mirrors the different possible cell resolutions in Arcsde (ie, {@code
 * SeRaster#SE_PIXEL_TYPE_*})
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.4
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/main/java/org
 *         /geotools/arcsde/raster/info/RasterCellType.java $
 */
public enum RasterCellType {
    TYPE_16BIT_S(16, DataBuffer.TYPE_SHORT, true, NumberRange.create(Short.MIN_VALUE,
            Short.MAX_VALUE)), //
    TYPE_16BIT_U(16, DataBuffer.TYPE_USHORT, false, NumberRange.create((int) 0, (int) 65535)), //
    TYPE_1BIT(1, DataBuffer.TYPE_BYTE, false, NumberRange.create((byte) 0, (byte) 1)), //
    TYPE_32BIT_REAL(32, DataBuffer.TYPE_FLOAT, true, NumberRange.create(Float.MIN_VALUE,
            Float.MAX_VALUE)), //
    TYPE_32BIT_S(32, DataBuffer.TYPE_INT, true, NumberRange.create(Integer.MIN_VALUE,
            Integer.MAX_VALUE)), //
    TYPE_32BIT_U(64, DataBuffer.TYPE_DOUBLE, false, NumberRange.create(0D,
            (double) Math.pow(2, 32) - 1)), //
    TYPE_4BIT(4, DataBuffer.TYPE_BYTE, false, NumberRange.create((byte) 0,
            (byte) Math.pow(2, 4) - 1)), //
    TYPE_64BIT_REAL(64, DataBuffer.TYPE_DOUBLE, true, NumberRange.create(Double.MIN_VALUE,
            Double.MAX_VALUE)), //
    TYPE_8BIT_S(8, DataBuffer.TYPE_BYTE, true, NumberRange.create(Byte.MIN_VALUE, Byte.MAX_VALUE)), //
    TYPE_8BIT_U(8, DataBuffer.TYPE_BYTE, false, NumberRange.create((int) 0, (int) 255));
    static {
        // Setting the se_pixel_type in a static initializer to avoid having them linked to the fake
        // values in the dummy api
        TYPE_16BIT_S.setSdeTypeId(SeRaster.SE_PIXEL_TYPE_16BIT_S);
        TYPE_16BIT_U.setSdeTypeId(SeRaster.SE_PIXEL_TYPE_16BIT_U);
        TYPE_1BIT.setSdeTypeId(SeRaster.SE_PIXEL_TYPE_1BIT);
        TYPE_32BIT_REAL.setSdeTypeId(SeRaster.SE_PIXEL_TYPE_32BIT_REAL);
        TYPE_32BIT_S.setSdeTypeId(SeRaster.SE_PIXEL_TYPE_32BIT_S);
        TYPE_32BIT_U.setSdeTypeId(SeRaster.SE_PIXEL_TYPE_32BIT_U);
        TYPE_4BIT.setSdeTypeId(SeRaster.SE_PIXEL_TYPE_4BIT);
        TYPE_64BIT_REAL.setSdeTypeId(SeRaster.SE_PIXEL_TYPE_64BIT_REAL);
        TYPE_8BIT_S.setSdeTypeId(SeRaster.SE_PIXEL_TYPE_8BIT_S);
        TYPE_8BIT_U.setSdeTypeId(SeRaster.SE_PIXEL_TYPE_8BIT_U);
    }

    private int typeId;

    private final int bitsPerSample;

    private final int dataBufferType;

    private final boolean signed;

    private final NumberRange<?> sampleValueRange;

    private RasterCellType(final int bitsPerSample, final int dataBufferType, final boolean signed,
            final NumberRange<?> sampleValueRange) {
        this.bitsPerSample = bitsPerSample;
        this.dataBufferType = dataBufferType;
        this.signed = signed;
        this.sampleValueRange = sampleValueRange;
    }

    private void setSdeTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getSeRasterPixelType() {
        return this.typeId;
    }

    public int getBitsPerSample() {
        return bitsPerSample;
    }

    public int getDataBufferType() {
        return dataBufferType;
    }

    public boolean isSigned() {
        return signed;
    }

    public static RasterCellType valueOf(final int seRasterPixelType) {
        for (RasterCellType type : RasterCellType.values()) {
            if (type.getSeRasterPixelType() == seRasterPixelType) {
                return type;
            }
        }
        throw new NoSuchElementException("Raster pixel type " + seRasterPixelType
                + " does not exist");
    }

    public NumberRange<?> getSampleValueRange() {
        return sampleValueRange;
    }

}
