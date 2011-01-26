/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_16BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_16BIT_U;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_1BIT;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_REAL;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_U;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_4BIT;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_64BIT_REAL;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_8BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_8BIT_U;
import static org.geotools.arcsde.raster.info.RasterUtils.determineNoDataValue;
import static org.geotools.arcsde.raster.info.RasterUtils.determineTargetCellType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Transparency;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class RasterUtilsTest {

    @Test
    public void testSdeColormapToJavaColorModel() {

        testSdeColormapToJavaColorModel(8, 256, 3, DataBuffer.TYPE_BYTE);

        testSdeColormapToJavaColorModel(8, 10, 3, DataBuffer.TYPE_BYTE);

        testSdeColormapToJavaColorModel(8, 256, 4, DataBuffer.TYPE_BYTE);

        testSdeColormapToJavaColorModel(8, 10, 4, DataBuffer.TYPE_BYTE);

        testSdeColormapToJavaColorModel(16, 65536, 3, DataBuffer.TYPE_USHORT);

        testSdeColormapToJavaColorModel(16, 10, 3, DataBuffer.TYPE_USHORT);

        testSdeColormapToJavaColorModel(16, 65536, 4, DataBuffer.TYPE_USHORT);

        testSdeColormapToJavaColorModel(16, 10, 4, DataBuffer.TYPE_USHORT);

        // what about a 16bit sample model with a data buffer byte with more than 256 entries?
        testSdeColormapToJavaColorModel(16, 530, 3, DataBuffer.TYPE_BYTE);
    }

    private void testSdeColormapToJavaColorModel(final int bitsPerSample, final int size,
            final int numBanks, final int transferType) {

        DataBuffer colorMapData;
        IndexColorModel colorModel;
        colorMapData = newColorMap(size, numBanks, transferType);

        colorModel = RasterUtils.sdeColorMapToJavaColorModel(colorMapData, bitsPerSample);

        assertColorModel(colorMapData, colorModel, bitsPerSample);
    }

    private void assertColorModel(DataBuffer expected, IndexColorModel actual, int bitsPerSample) {
        final int size = expected.getSize();
        final int numBanks = expected.getNumBanks();
        // final int dataType = expected.getDataType();

        assertEquals(bitsPerSample, actual.getPixelSize());
        assertEquals(bitsPerSample == 8 ? DataBuffer.TYPE_BYTE : DataBuffer.TYPE_USHORT, actual
                .getTransferType());

        assertEquals(numBanks, actual.getNumComponents());

        if (numBanks == 3) {
            assertEquals(-1, actual.getTransparentPixel());
            assertFalse(actual.hasAlpha());
            assertEquals(Transparency.OPAQUE, actual.getTransparency());
        } else if (numBanks == 4) {
            // transparent pixel is looked up by IndexColorModel
            assertTrue(actual.getTransparentPixel() > -1);
            assertTrue(actual.hasAlpha());
            assertEquals(Transparency.TRANSLUCENT, actual.getTransparency());
        }
        assertEquals(size, actual.getMapSize());

        for (int elem = 0; elem < size; elem++) {
            for (int bank = 0; bank < numBanks; bank++) {
                int actualValue = 0;
                switch (bank) {
                case 0:
                    actualValue = actual.getRed(elem);
                    break;
                case 1:
                    actualValue = actual.getGreen(elem);
                    break;
                case 2:
                    actualValue = actual.getBlue(elem);
                    break;
                case 3:
                    actualValue = actual.getAlpha(elem);
                    break;
                }
                assertEquals("at index " + elem + ", bank " + bank, expected.getElem(bank, elem),
                        actualValue);
            }
        }
    }

    private DataBuffer newColorMap(int size, int numBanks, int transferType) {
        DataBuffer colorMapData;
        switch (transferType) {
        case DataBuffer.TYPE_BYTE:
            colorMapData = new DataBufferByte(size, numBanks);
            break;
        case DataBuffer.TYPE_USHORT:
            colorMapData = new DataBufferUShort(size, numBanks);
            break;
        default:
            throw new IllegalArgumentException();
        }
        for (int elem = 0; elem < size; elem++) {
            for (int bank = 0; bank < numBanks; bank++) {
                // cast to byte
                int value = elem & 0xFF;
                colorMapData.setElem(bank, elem, value);
            }
        }
        return colorMapData;
    }

    @Test
    public void testDetermineTargetCellType() {
        assertDetermineTargetCellType(TYPE_1BIT, TYPE_1BIT, 0);
        assertDetermineTargetCellType(TYPE_1BIT, TYPE_8BIT_U, 2);
        assertDetermineTargetCellType(TYPE_1BIT, TYPE_8BIT_U, 0, 1, 2);

        assertDetermineTargetCellType(TYPE_4BIT, TYPE_4BIT, 0);
        assertDetermineTargetCellType(TYPE_4BIT, TYPE_4BIT, 15);
        assertDetermineTargetCellType(TYPE_4BIT, TYPE_8BIT_U, 16);

        assertDetermineTargetCellType(TYPE_8BIT_U, TYPE_8BIT_U, 0);
        assertDetermineTargetCellType(TYPE_8BIT_U, TYPE_8BIT_U, 255);
        assertDetermineTargetCellType(TYPE_8BIT_U, TYPE_16BIT_U, 256);
        assertDetermineTargetCellType(TYPE_8BIT_U, TYPE_16BIT_U, 0, 255, 256);

        assertDetermineTargetCellType(TYPE_8BIT_S, TYPE_8BIT_S, Byte.MIN_VALUE);
        assertDetermineTargetCellType(TYPE_8BIT_S, TYPE_8BIT_S, Byte.MAX_VALUE);
        assertDetermineTargetCellType(TYPE_8BIT_S, TYPE_16BIT_S, Byte.MAX_VALUE + 1);
        assertDetermineTargetCellType(TYPE_8BIT_S, TYPE_16BIT_S, 0, Byte.MIN_VALUE - 1,
                Byte.MAX_VALUE);

        assertDetermineTargetCellType(TYPE_16BIT_S, TYPE_16BIT_S, Short.MIN_VALUE);
        assertDetermineTargetCellType(TYPE_16BIT_S, TYPE_16BIT_S, Short.MAX_VALUE);
        assertDetermineTargetCellType(TYPE_16BIT_S, TYPE_32BIT_S, Short.MAX_VALUE + 1);
        assertDetermineTargetCellType(TYPE_16BIT_S, TYPE_32BIT_S, 0, Short.MIN_VALUE - 1,
                Short.MAX_VALUE);

        assertDetermineTargetCellType(TYPE_16BIT_U, TYPE_16BIT_U, 0);
        assertDetermineTargetCellType(TYPE_16BIT_U, TYPE_16BIT_U, 65535);
        assertDetermineTargetCellType(TYPE_16BIT_U, TYPE_32BIT_U, 65536);
        assertDetermineTargetCellType(TYPE_16BIT_U, TYPE_32BIT_U, 0, 65535, 65536);

        assertDetermineTargetCellType(TYPE_32BIT_U, TYPE_32BIT_U, 0);
        long max32bitU = TYPE_32BIT_U.getSampleValueRange().castTo(Long.class).getMaxValue();
        assertDetermineTargetCellType(TYPE_32BIT_U, TYPE_32BIT_U, max32bitU);
        assertDetermineTargetCellType(TYPE_32BIT_U, TYPE_64BIT_REAL, max32bitU + 1);
        assertDetermineTargetCellType(TYPE_32BIT_U, TYPE_64BIT_REAL, 0, max32bitU, max32bitU + 1);

        assertDetermineTargetCellType(TYPE_32BIT_S, TYPE_32BIT_S, Integer.MIN_VALUE);
        assertDetermineTargetCellType(TYPE_32BIT_S, TYPE_32BIT_S, Integer.MAX_VALUE);
        assertDetermineTargetCellType(TYPE_32BIT_S, TYPE_64BIT_REAL, (long) Integer.MIN_VALUE - 1L);
        assertDetermineTargetCellType(TYPE_32BIT_S, TYPE_64BIT_REAL, Integer.MIN_VALUE,
                (long) Integer.MAX_VALUE + 1L, (long) Integer.MIN_VALUE - 1L);
        {
            try {
                assertDetermineTargetCellType(TYPE_32BIT_REAL, TYPE_32BIT_REAL, Float.MIN_VALUE);
                fail("Expected IAE for 332BIT_REAL with no-data other than Float.NaN");
            } catch (IllegalArgumentException e) {
                assertTrue(true);
            }
            try {
                assertDetermineTargetCellType(TYPE_32BIT_REAL, TYPE_32BIT_REAL, Float.MAX_VALUE);
                fail("Expected IAE for 332BIT_REAL with no-data other than Float.NaN");
            } catch (IllegalArgumentException e) {
                assertTrue(true);
            }
            assertDetermineTargetCellType(TYPE_32BIT_REAL, TYPE_32BIT_REAL, Float.NaN);
        }
        {
            try {
                assertDetermineTargetCellType(TYPE_64BIT_REAL, TYPE_64BIT_REAL, Double.MIN_VALUE);
                fail("Expected IAE for TYPE_64BIT_REAL with no-data other than Float.NaN");
            } catch (IllegalArgumentException e) {
                assertTrue(true);
            }
            try {
                assertDetermineTargetCellType(TYPE_64BIT_REAL, TYPE_64BIT_REAL, Double.MAX_VALUE);
                fail("Expected IAE for TYPE_64BIT_REAL with no-data other than Float.NaN");
            } catch (IllegalArgumentException e) {
                assertTrue(true);
            }
            assertDetermineTargetCellType(TYPE_64BIT_REAL, TYPE_64BIT_REAL, Double.NaN);
        }
    }

    /**
     * 
     * @param nativeCellType
     *            the native cell type
     * @param expectedTargetCellType
     *            the expected cell type
     * @param noData
     *            list of no-data values, one per band in the raster
     */
    private void assertDetermineTargetCellType(RasterCellType nativeCellType,
            RasterCellType expectedTargetCellType, Number... noData) {

        List<Number> noDataValues = new ArrayList<Number>();
        for (Number bandNNoData : noData) {
            noDataValues.add(bandNNoData);
        }
        RasterCellType targetCellType = determineTargetCellType(nativeCellType, noDataValues);
        assertEquals(expectedTargetCellType, targetCellType);
    }

    @Test
    public void testDeterminaNoDataValueNonColorMapped() {

        assertDetermineNoDataValue(1, Double.NaN, Double.NaN, TYPE_1BIT, 2, null);
        assertDetermineNoDataValue(1, 0, 1, TYPE_1BIT, 2, null);
        assertDetermineNoDataValue(1, 0, 0, TYPE_1BIT, 2,
                "1BIT no-data should have been set to 2, "
                        + "regardless of the dataset not containing set pixels");

        assertDetermineNoDataValue(1, 0, 15, TYPE_4BIT, 16, null);
        assertDetermineNoDataValue(1, 0, 3, TYPE_4BIT, 16,
                "4BIT no-data should have been set to 16, "
                        + "regardless of the dataset used values");
        assertDetermineNoDataValue(1, Double.NaN, Double.NaN, TYPE_4BIT, 16, null);

        assertDetermineNoDataValue(1, Double.NaN, Double.NaN, TYPE_8BIT_U, 256, null);
        assertDetermineNoDataValue(1, 0, 255, TYPE_8BIT_U, 256, null);
        assertDetermineNoDataValue(1, 0, 254, TYPE_8BIT_U, 255, null);
        assertDetermineNoDataValue(1, 1, 254, TYPE_8BIT_U, 0, null);
        // 8bit unsigned rasters with 3 or 4 bands have a hardcoded nodata value of 255 so they
        // account for white/transparent
        assertDetermineNoDataValue(3, Double.NaN, Double.NaN, TYPE_8BIT_U, 255, null);
        assertDetermineNoDataValue(3, 0, 255, TYPE_8BIT_U, 255, null);
        assertDetermineNoDataValue(3, 0, 254, TYPE_8BIT_U, 255, null);
        assertDetermineNoDataValue(3, 1, 254, TYPE_8BIT_U, 255, null);

        assertDetermineNoDataValue(4, Double.NaN, Double.NaN, TYPE_8BIT_U, 255, null);
        assertDetermineNoDataValue(4, 0, 255, TYPE_8BIT_U, 255, null);
        assertDetermineNoDataValue(4, 0, 254, TYPE_8BIT_U, 255, null);
        assertDetermineNoDataValue(4, 1, 254, TYPE_8BIT_U, 255, null);

        assertDetermineNoDataValue(1, Double.NaN, Double.NaN, TYPE_8BIT_S, -129, null);
        assertDetermineNoDataValue(1, -128, 127, TYPE_8BIT_S, Byte.MIN_VALUE - 1, null);
        assertDetermineNoDataValue(1, -128, 126, TYPE_8BIT_S, 127, null);
        assertDetermineNoDataValue(1, -127, 127, TYPE_8BIT_S, -128, null);

        assertDetermineNoDataValue(1, Double.NaN, Double.NaN, TYPE_16BIT_U, 65536, null);
        assertDetermineNoDataValue(1, 0, 65535, TYPE_16BIT_U, 65536, null);
        assertDetermineNoDataValue(1, 1, 65535, TYPE_16BIT_U, 0, null);
        assertDetermineNoDataValue(1, 0, 65534, TYPE_16BIT_U, 65535, null);

        assertDetermineNoDataValue(1, Double.NaN, Double.NaN, TYPE_16BIT_S, Short.MIN_VALUE - 1,
                null);
        assertDetermineNoDataValue(1, Short.MIN_VALUE, Short.MAX_VALUE, TYPE_16BIT_S,
                Short.MIN_VALUE - 1, null);
        assertDetermineNoDataValue(1, Short.MIN_VALUE, Short.MAX_VALUE - 1, TYPE_16BIT_S,
                Short.MAX_VALUE, null);
        assertDetermineNoDataValue(1, Short.MIN_VALUE + 1, Short.MAX_VALUE, TYPE_16BIT_S,
                Short.MIN_VALUE, null);

        double max32bitU = TYPE_32BIT_U.getSampleValueRange().getMaximum();
        assertDetermineNoDataValue(1, Double.NaN, Double.NaN, TYPE_32BIT_U, max32bitU + 1, null);
        assertDetermineNoDataValue(1, 0, max32bitU, TYPE_32BIT_U, max32bitU + 1, null);
        assertDetermineNoDataValue(1, 1, max32bitU, TYPE_32BIT_U, 0, null);
        assertDetermineNoDataValue(1, 0, max32bitU - 1, TYPE_32BIT_U, max32bitU, null);

        assertDetermineNoDataValue(1, Double.NaN, Double.NaN, TYPE_32BIT_S, Integer.MIN_VALUE - 1L,
                null);
        assertDetermineNoDataValue(1, Integer.MIN_VALUE, Integer.MAX_VALUE, TYPE_32BIT_S,
                (long) Integer.MIN_VALUE - 1, null);
        assertDetermineNoDataValue(1, 1, Integer.MAX_VALUE, TYPE_32BIT_S, 0, null);
        assertDetermineNoDataValue(1, Integer.MIN_VALUE, Integer.MAX_VALUE - 1L, TYPE_32BIT_S,
                Integer.MAX_VALUE, null);

        assertDetermineNoDataValue(1, Double.NaN, Double.NaN, TYPE_32BIT_REAL, Float.NaN, null);
        assertDetermineNoDataValue(1, Float.MIN_VALUE, Float.MAX_VALUE, TYPE_32BIT_REAL, Float.NaN,
                null);
        assertDetermineNoDataValue(1, Float.MIN_VALUE + 1, Float.MAX_VALUE, TYPE_32BIT_REAL,
                Float.NaN, null);
        assertDetermineNoDataValue(1, Float.MIN_VALUE, Float.MAX_VALUE - 1L, TYPE_32BIT_REAL,
                Float.NaN, null);

        assertDetermineNoDataValue(1, Double.NaN, Double.NaN, TYPE_64BIT_REAL, Double.NaN, null);
        assertDetermineNoDataValue(1, Double.MIN_VALUE, Double.MAX_VALUE, TYPE_64BIT_REAL,
                Double.NaN, null);
        assertDetermineNoDataValue(1, Double.MIN_VALUE + 1, Double.MAX_VALUE, TYPE_64BIT_REAL,
                Double.NaN, null);
        assertDetermineNoDataValue(1, Double.MIN_VALUE, Double.MAX_VALUE - 1L, TYPE_64BIT_REAL,
                Double.NaN, null);
    }

    private void assertDetermineNoDataValue(final int numBands, final double statsMin,
            final double statsMax, final RasterCellType nativeCellType,
            final Number expectedNoDataValue, final String failureMsg) {

        Number noDataValue = determineNoDataValue(numBands, statsMin, statsMax, nativeCellType);
        if (failureMsg == null) {
            assertEquals(expectedNoDataValue.doubleValue(), noDataValue.doubleValue(), 0);
        } else {
            assertEquals(failureMsg, expectedNoDataValue.doubleValue(), noDataValue.doubleValue(),
                    0);
        }
    }
}
