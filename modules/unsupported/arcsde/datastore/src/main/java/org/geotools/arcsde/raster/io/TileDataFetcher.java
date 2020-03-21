/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.raster.io;

import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_16BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_16BIT_U;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_1BIT;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_32BIT_U;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_4BIT;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_64BIT_REAL;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_8BIT_S;
import static org.geotools.arcsde.raster.info.RasterCellType.TYPE_8BIT_U;

import com.esri.sde.sdk.client.SeRasterTile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.geotools.arcsde.raster.info.RasterCellType;

/**
 * Fills the raw tile data in a {@link TileInfo} out of a {@link SeRasterTile}, potentially
 * promoting the original tile data to a higher precision sample detph, and filling out the TileInfo
 * pixels with the no-data value where appropriate (determined by the {@link
 * SeRasterTile#getBitMaskData() bitmask data}).
 *
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 * @since 2.5.9
 * @see NativeTileReader
 */
abstract class TileDataFetcher {

    private static Map<RasterCellType, TileDataFetcher> tileDataSetters =
            new HashMap<RasterCellType, TileDataFetcher>();

    static {
        final ByteTileSetter byteTileSetter = new ByteTileSetter();
        tileDataSetters.put(TYPE_1BIT, new OneBitTileSetter());
        tileDataSetters.put(TYPE_4BIT, byteTileSetter);
        tileDataSetters.put(TYPE_8BIT_S, byteTileSetter);
        tileDataSetters.put(TYPE_8BIT_U, byteTileSetter);
        tileDataSetters.put(RasterCellType.TYPE_16BIT_U, new UShortTileSetter());
        tileDataSetters.put(RasterCellType.TYPE_16BIT_S, new ShortTileSetter());

        tileDataSetters.put(RasterCellType.TYPE_32BIT_S, new IntegerTileSetter());
        tileDataSetters.put(RasterCellType.TYPE_32BIT_U, new UnsignedIntegerTileSetter());

        tileDataSetters.put(RasterCellType.TYPE_32BIT_REAL, new FloatTileSetter());
        tileDataSetters.put(RasterCellType.TYPE_64BIT_REAL, new DoubleTileSetter());
    }

    /**
     * Returns a {@code TileDataFetcher} that knows how to translate the raw pixel data from an
     * {@link SeRasterTile} with pixel type {@code nativeType} to a {@link TileInfo}'s internal
     * array with a pixel type determined by {@code targetCellType}.
     *
     * @param nativeType the arcsde raster's native pixel type
     * @param targetCellType the TileInfo's target pixel type
     */
    public static TileDataFetcher getTileDataFetcher(
            final RasterCellType nativeType, final RasterCellType targetCellType) {
        final TileDataFetcher tileDataFetcher;
        if (nativeType == targetCellType) {
            tileDataFetcher = tileDataSetters.get(nativeType);
        } else {
            if (nativeType == TYPE_1BIT && targetCellType == RasterCellType.TYPE_8BIT_U) {
                return new OneBitTileSetter();
            } else if (nativeType == TYPE_8BIT_U && targetCellType == TYPE_16BIT_U) {
                return new UcharToUshort();
            } else if (nativeType == TYPE_16BIT_S && targetCellType == TYPE_32BIT_S) {
                return new ShortToInt();
            } else if (nativeType == TYPE_8BIT_S && targetCellType == TYPE_16BIT_S) {
                return new ByteToShort();
            } else if (nativeType == TYPE_16BIT_U && targetCellType == TYPE_32BIT_U) {
                return new UShortToUInt();
            } else if (nativeType == TYPE_32BIT_U && targetCellType == TYPE_64BIT_REAL) {
                return new UIntToDouble();
            } else if (nativeType == TYPE_32BIT_S && targetCellType == TYPE_64BIT_REAL) {
                return new IntToDouble();
            }
            throw new IllegalArgumentException(
                    "No registered TileDataFetcher for pixel type "
                            + nativeType
                            + " and target type "
                            + targetCellType);
        }
        if (tileDataFetcher == null) {
            throw new IllegalArgumentException(
                    "No registered TileDataFetcher for pixel type " + nativeType);
        }
        return tileDataFetcher;
    }

    /** Grabs the native pixel data out of {@code tile} */
    public abstract void setTileData(SeRasterTile tile, TileInfo tileInfo);

    /** Returns whether the sample N in the bitmask byte array is marked as a no-data pixel */
    protected final boolean isNoData(final int sampleN, final byte[] bitmaskData) {
        boolean isNoData = ((bitmaskData[sampleN / 8] >> (7 - (sampleN % 8))) & 0x01) == 0x00;
        return isNoData;
    }

    /** */
    private static final class OneBitTileSetter extends TileDataFetcher {

        @Override
        public void setTileData(final SeRasterTile tile, TileInfo tileInfo) {

            byte[] tileData = tileInfo.getTileDataAsBytes();

            final int numPixelsRead = tileInfo.getNumPixelsRead();
            final byte nodata = tileInfo.getNoDataValue().byteValue();

            if (numPixelsRead == 0) {
                Arrays.fill(tileData, nodata);
            } else {
                try {
                    tile.getPixels(tileData);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                // getPixels(byte[]) for a 1-bit raster sets set bits to 255 instead of 1, we want
                // them to be 1
                final byte bitSet = (byte) 0xFF;
                final boolean hasNoDataPixels = tileInfo.hasNoDataPixels();
                final byte[] bitmaskData = tileInfo.getBitmaskData();

                for (int pn = 0; pn < numPixelsRead; pn++) {
                    if (hasNoDataPixels && isNoData(pn, bitmaskData)) {
                        tileData[pn] = nodata;
                    } else {
                        if (bitSet == tileData[pn]) {
                            tileData[pn] = 1;
                        }
                    }
                }
            }
        }
    }

    /** */
    private static final class ByteTileSetter extends TileDataFetcher {
        @Override
        public void setTileData(final SeRasterTile tile, TileInfo tileInfo) {

            final int numPixelsRead = tileInfo.getNumPixelsRead();
            final byte nodata = tileInfo.getNoDataValue().byteValue();

            byte[] tileData = tileInfo.getTileDataAsBytes();

            if (numPixelsRead == 0) {
                Arrays.fill(tileData, nodata);
            } else {
                byte[] pixelData = tile.getPixelData();
                System.arraycopy(pixelData, 0, tileData, 0, numPixelsRead);
                if (tileInfo.hasNoDataPixels()) {
                    final byte[] bitmaskData = tileInfo.getBitmaskData();
                    for (int pn = 0; pn < numPixelsRead; pn++) {
                        if (isNoData(pn, bitmaskData)) {
                            tileData[pn] = nodata;
                        }
                    }
                }
            }
        }
    }

    private static final class UShortTileSetter extends TileDataFetcher {
        @Override
        public void setTileData(final SeRasterTile tile, TileInfo tileInfo) {

            short[] tileData = tileInfo.getTileDataAsUnsignedShorts();

            final int numPixelsRead = tileInfo.getNumPixelsRead();
            final int numPixels = tileInfo.getNumPixels();

            final short nodata = (short) (tileInfo.getNoDataValue().intValue() & 0xFFFF);

            if (numPixelsRead == 0) {
                Arrays.fill(tileData, nodata);
            } else {
                int[] ints = new int[numPixels];
                try {
                    tile.getPixels(ints);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                final boolean hasNoDataPixels = tileInfo.hasNoDataPixels();
                final byte[] bitmaskData = tileInfo.getBitmaskData();

                for (int pn = 0; pn < numPixels; pn++) {
                    if (hasNoDataPixels && isNoData(pn, bitmaskData)) {
                        tileData[pn] = nodata;
                    } else {
                        tileData[pn] = (short) ints[pn];
                    }
                }
            }
        }
    }

    private static final class ShortTileSetter extends TileDataFetcher {
        @Override
        public void setTileData(final SeRasterTile tile, TileInfo tileInfo) {

            short[] tileData = tileInfo.getTileDataAsShorts();

            final int numPixelsRead = tileInfo.getNumPixelsRead();
            final int numPixels = tileInfo.getNumPixels();
            final short nodata = tileInfo.getNoDataValue().shortValue();

            if (numPixelsRead == 0) {
                Arrays.fill(tileData, nodata);
            } else {
                int[] ints = new int[numPixels];
                try {
                    tile.getPixels(ints);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                final boolean hasNoDataPixels = tileInfo.hasNoDataPixels();
                final byte[] bitmaskData = tileInfo.getBitmaskData();

                for (int pn = 0; pn < numPixels; pn++) {
                    if (hasNoDataPixels && isNoData(pn, bitmaskData)) {
                        tileData[pn] = nodata;
                    } else {
                        tileData[pn] = (short) ints[pn];
                    }
                }
            }
        }
    }

    private static final class IntegerTileSetter extends TileDataFetcher {
        @Override
        public void setTileData(final SeRasterTile tile, TileInfo tileInfo) {

            int[] tileData = tileInfo.getTileDataAsIntegers();

            final int numPixelsRead = tileInfo.getNumPixelsRead();
            final int nodata = tileInfo.getNoDataValue().intValue();

            if (numPixelsRead == 0) {
                Arrays.fill(tileData, nodata);
            } else {
                try {
                    tile.getPixels(tileData);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (tileInfo.hasNoDataPixels()) {
                    byte[] bitmaskData = tileInfo.getBitmaskData();
                    for (int pn = 0; pn < numPixelsRead; pn++) {
                        if (isNoData(pn, bitmaskData)) {
                            tileData[pn] = nodata;
                        }
                    }
                }
            }
        }
    }

    private static final class UnsignedIntegerTileSetter extends TileDataFetcher {
        @Override
        public void setTileData(final SeRasterTile tile, TileInfo tileInfo) {

            double[] tileData = tileInfo.getTileDataAsDoubles();

            final int numPixelsRead = tileInfo.getNumPixelsRead();

            final double nodata = tileInfo.getNoDataValue().doubleValue();

            if (numPixelsRead == 0) {
                Arrays.fill(tileData, nodata);
            } else {
                try {
                    tile.getPixels(tileData);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                final boolean hasNoDataPixels = tileInfo.hasNoDataPixels();
                if (hasNoDataPixels) {
                    final byte[] bitmaskData = tileInfo.getBitmaskData();
                    for (int pn = 0; pn < numPixelsRead; pn++) {
                        if (isNoData(pn, bitmaskData)) {
                            tileData[pn] = nodata;
                        }
                    }
                }
            }
        }
    }

    private static final class FloatTileSetter extends TileDataFetcher {
        @Override
        public void setTileData(final SeRasterTile tile, TileInfo tileInfo) {

            float[] tileData = tileInfo.getTileDataAsFloats();

            final int numPixelsRead = tileInfo.getNumPixelsRead();
            final float nodata = tileInfo.getNoDataValue().floatValue();

            if (numPixelsRead == 0) {
                Arrays.fill(tileData, nodata);
            } else {
                try {
                    tile.getPixels(tileData);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (tileInfo.hasNoDataPixels()) {
                    byte[] bitmaskData = tileInfo.getBitmaskData();
                    for (int pn = 0; pn < numPixelsRead; pn++) {
                        if (isNoData(pn, bitmaskData)) {
                            tileData[pn] = nodata;
                        }
                    }
                }
            }
        }
    }

    private static final class DoubleTileSetter extends TileDataFetcher {
        @Override
        public void setTileData(final SeRasterTile tile, TileInfo tileInfo) {

            double[] tileData = tileInfo.getTileDataAsDoubles();

            final int numPixelsRead = tileInfo.getNumPixelsRead();
            final double nodata = tileInfo.getNoDataValue().doubleValue();

            if (numPixelsRead == 0) {
                Arrays.fill(tileData, nodata);
            } else {
                try {
                    tile.getPixels(tileData);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (tileInfo.hasNoDataPixels()) {
                    byte[] bitmaskData = tileInfo.getBitmaskData();
                    for (int pn = 0; pn < numPixelsRead; pn++) {
                        if (isNoData(pn, bitmaskData)) {
                            tileData[pn] = nodata;
                        }
                    }
                }
            }
        }
    }

    /** Converts native unsigned byte tile data to unsigned short data */
    private static final class UcharToUshort extends TileDataFetcher {
        @Override
        public void setTileData(final SeRasterTile tile, final TileInfo tileInfo) {
            final int numPixelsRead = tileInfo.getNumPixelsRead();

            final short nodata = (short) (tileInfo.getNoDataValue().intValue() & 0xFFFF);

            final short[] tileDataUShorts = tileInfo.getTileDataAsUnsignedShorts();

            // Arrays.fill(tileDataUShorts, nodata);
            if (numPixelsRead == 0) {
                Arrays.fill(tileDataUShorts, nodata);
            } else {
                /*
                 * getPixelData returns the SeRasterTile internal buffer with no extra copy. It may
                 * contain extra elements for the bitmask array in case there are no-data pixels
                 */
                final byte[] pixelData = tile.getPixelData();

                final boolean hasNoDataPixels = tileInfo.hasNoDataPixels();
                final byte[] bitmaskData = tileInfo.getBitmaskData();
                for (int pn = 0; pn < numPixelsRead; pn++) {
                    if (hasNoDataPixels && isNoData(pn, bitmaskData)) {
                        tileDataUShorts[pn] = nodata;
                    } else {
                        tileDataUShorts[pn] = (short) (pixelData[pn] & 0xFF);
                    }
                }
            }
        }
    }

    /** Converts native signed byte tile data to signed short data */
    private static final class ByteToShort extends TileDataFetcher {
        @Override
        public void setTileData(final SeRasterTile tile, final TileInfo tileInfo) {

            final int numPixelsRead = tileInfo.getNumPixelsRead();
            final short nodata = tileInfo.getNoDataValue().shortValue();

            if (numPixelsRead == 0) {
                Arrays.fill(tileInfo.getTileDataAsShorts(), nodata);
            } else {
                /*
                 * getPixelData returns the SeRasterTile internal buffer with no extra copy. It may
                 * contain extra elements for the bitmask array in case there are no-data pixels
                 */
                final byte[] pixelData = tile.getPixelData();
                final short[] tileDataShorts = tileInfo.getTileDataAsShorts();
                final byte[] bitmaskData = tileInfo.getBitmaskData();
                final boolean hasNoDataPixels = tileInfo.hasNoDataPixels();
                for (int pn = 0; pn < numPixelsRead; pn++) {
                    if (hasNoDataPixels && isNoData(pn, bitmaskData)) {
                        tileDataShorts[pn] = nodata;
                    } else {
                        tileDataShorts[pn] = (short) pixelData[pn];
                    }
                }
            }
        }
    }

    /** Converts native signed short tile data to signed int data */
    private static final class ShortToInt extends TileDataFetcher {

        @Override
        public void setTileData(final SeRasterTile tile, final TileInfo tileInfo) {
            final int numPixelsRead = tileInfo.getNumPixelsRead();

            final int nodata = tileInfo.getNoDataValue().intValue();

            if (numPixelsRead == 0) {
                Arrays.fill(tileInfo.getTileDataAsIntegers(), nodata);
            } else {
                int[] cache = tileInfo.getTileDataAsIntegers();
                try {
                    tile.getPixels(cache);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                final boolean hasNoDataPixels = tileInfo.hasNoDataPixels();
                if (hasNoDataPixels) {
                    final byte[] bitmaskData = tileInfo.getBitmaskData();
                    for (int pn = 0; pn < numPixelsRead; pn++) {
                        if (isNoData(pn, bitmaskData)) {
                            cache[pn] = nodata;
                        }
                    }
                }
            }
        }
    }

    /** Converts native integer tile data to double data */
    private static final class IntToDouble extends TileDataFetcher {

        private int[] cache = {};

        @Override
        public void setTileData(final SeRasterTile tile, final TileInfo tileInfo) {

            final int numPixelsRead = tileInfo.getNumPixelsRead();
            final int numPixels = tileInfo.getNumPixels();
            final double nodata = tileInfo.getNoDataValue().doubleValue();

            if (numPixelsRead == 0) {
                Arrays.fill(tileInfo.getTileDataAsDoubles(), nodata);
            } else {
                if (cache.length < numPixels) {
                    cache = new int[numPixels];
                }
                try {
                    tile.getPixels(cache);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                double[] tileData = tileInfo.getTileDataAsDoubles();
                final boolean hasNoDataPixels = tileInfo.hasNoDataPixels();
                final byte[] bitmaskData = tileInfo.getBitmaskData();

                for (int pn = 0; pn < numPixelsRead; pn++) {
                    if (hasNoDataPixels && isNoData(pn, bitmaskData)) {
                        tileData[pn] = nodata;
                    } else {
                        tileData[pn] = cache[pn];
                    }
                }
            }
        }
    }

    /** Converts native unsigned integer tile data to double data */
    private static final class UIntToDouble extends TileDataFetcher {
        @Override
        public void setTileData(final SeRasterTile tile, final TileInfo tileInfo) {
            final int numPixelsRead = tileInfo.getNumPixelsRead();

            final double nodata = tileInfo.getNoDataValue().doubleValue();

            if (numPixelsRead == 0) {
                Arrays.fill(tileInfo.getTileDataAsDoubles(), nodata);
            } else {
                double[] tileData = tileInfo.getTileDataAsDoubles();
                try {
                    tile.getPixels(tileData);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                final boolean hasNoDataPixels = tileInfo.hasNoDataPixels();
                if (hasNoDataPixels) {
                    final byte[] bitmaskData = tileInfo.getBitmaskData();
                    for (int pn = 0; pn < numPixelsRead; pn++) {
                        if (isNoData(pn, bitmaskData)) {
                            tileData[pn] = nodata;
                        }
                    }
                }
            }
        }
    }

    /** Converts native unsigned integer tile data to double data */
    private static final class UShortToUInt extends TileDataFetcher {
        @Override
        public void setTileData(final SeRasterTile tile, final TileInfo tileInfo) {
            final int numPixelsRead = tileInfo.getNumPixelsRead();

            final int nodata = tileInfo.getNoDataValue().intValue();

            if (numPixelsRead == 0) {
                Arrays.fill(tileInfo.getTileDataAsIntegers(), nodata);
            } else {
                int[] tileData = tileInfo.getTileDataAsIntegers();
                try {
                    tile.getPixels(tileData);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                final boolean hasNoDataPixels = tileInfo.hasNoDataPixels();
                if (hasNoDataPixels) {
                    final byte[] bitmaskData = tileInfo.getBitmaskData();
                    for (int pn = 0; pn < numPixelsRead; pn++) {
                        if (isNoData(pn, bitmaskData)) {
                            tileData[pn] = nodata;
                        }
                    }
                }
            }
        }
    }
}
