/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.egr;

import it.geosolutions.jaiext.iterators.RandomIterFactory;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.WritableRandomIter;
import org.geotools.geometry.jts.JTS;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Polygon;

/**
 * A tile of the whole grid space.
 *
 * <p>The related Raster is lazily allocated, so to skip allocation for fully covered tiles that do not need to be
 * drawn.
 *
 * @author Emanuele Tajariol <etj at geo-solutions.it>
 */
class Tile {
    private static final Logger LOGGER = Logging.getLogger(Tile.class);

    private static final boolean antiAliasing = false;

    private static final byte FF = (byte) 0xff;

    static Map<String, WritableRaster> solidRasterCache = new SoftValueHashMap<>();

    private static final ColorModel BINARY_COLOR_MODEL =
            new IndexColorModel(1, 2, new byte[] {0, FF}, new byte[] {0, FF}, new byte[] {0, FF});

    // the sample model used for internal "full size" tiles
    private static MultiPixelPackedSampleModel DEFAULT_PACKED_SAMPLE_MODEL = new MultiPixelPackedSampleModel(
            DataBuffer.TYPE_BYTE,
            ROIExcessGranuleRemover.DEFAULT_TILE_SIZE,
            ROIExcessGranuleRemover.DEFAULT_TILE_SIZE,
            1);

    // used for border tiles
    static Map<String, MultiPixelPackedSampleModel> mpSampleModelCache = new SoftValueHashMap<>();

    /** Width in pixels of this tile. */
    private final int tileWidth;

    /** Height in pixels of this tile. */
    private final int tileHeight;

    /**
     * Standard width in pixels of the tiles of this tileset. Tiles in last row or last column may have different size
     * than other tiles. We need the standard size for computing the grid translation when drawing geometries.
     */
    private final int stdTileWidth;

    /** Standard height in pixels of the tiles of this tileset. */
    private final int stdTileHeight;

    private final int col;

    private final int row;

    private Polygon tileBBox;

    private long coverageCount;

    // Lazy stuff
    private WritableRaster raster;

    private BufferedImage bi;

    private Graphics2D graphics;

    private BitSet rowFull;

    private Rectangle tileArea;

    public Tile(int tileWidth, int tileHeight, int col, int row, AffineTransform w2s) {
        this(tileWidth, tileHeight, col, row, w2s, tileWidth, tileHeight);
    }

    public Tile(
            int tileWidth, int tileHeight, int col, int row, AffineTransform w2s, int stdTileWidth, int stdTileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.col = col;
        this.row = row;
        this.stdTileWidth = stdTileWidth;
        this.stdTileHeight = stdTileHeight;
        this.rowFull = new BitSet(tileHeight);

        tileArea = new Rectangle(col * stdTileWidth, row * stdTileHeight, tileWidth, tileHeight);
        try {
            Envelope e = RendererUtilities.createMapEnvelope(tileArea, w2s);
            tileBBox = JTS.toGeometry(e);
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("TileBBox: " + tileBBox);
            }
        } catch (NoninvertibleTransformException ex) {
            LOGGER.log(Level.SEVERE, "Error creating tile", ex);
            tileBBox = null;
        }
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public long getCoverageCount() {
        return coverageCount;
    }

    public boolean isFullyCovered() {
        return tileWidth * tileHeight == coverageCount;
    }

    /** @return the bbox of this tile in world coordinates */
    public Polygon getTileBBox() {
        return tileBBox;
    }

    private void initGraphics(boolean inverted) {
        initRaster(inverted);
        // lazily allocate graphics
        if (graphics == null) {
            Color drawColor = inverted ? Color.BLACK : Color.WHITE;

            bi = new BufferedImage(BINARY_COLOR_MODEL, raster, false, null);
            bi.setAccelerationPriority(0);
            graphics = bi.createGraphics();

            final int offset = antiAliasing ? 2 : 0;

            graphics.setClip(-offset, -offset, tileWidth + offset * 2, tileHeight + offset * 2);
            graphics.translate(-this.col * stdTileWidth, -this.row * stdTileHeight);

            graphics.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    antiAliasing ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);

            graphics.setColor(drawColor);
        }
    }

    private void initRaster(boolean inverted) {
        // lazily allocate raster
        if (raster == null) {
            allocateRaster(inverted);
        }
    }

    /**
     * Set the coverageCount according to the current count of pixels set to 1.
     *
     * @return true if count changed.
     * @throws IllegalStateException if raster has not been initialized
     */
    public boolean refreshCoverageCount() throws IllegalStateException {
        if (raster == null) {
            throw new IllegalStateException("Raster not initialized");
        }

        long cnt = 0;

        raster.getSampleModel();

        int scanlineStride = ((MultiPixelPackedSampleModel) raster.getSampleModel()).getScanlineStride();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
        byte[] bytes = data.getData();

        int pos = 0;
        for (int row = 0; row < tileHeight; row++) {
            if (rowFull.get(row)) {
                cnt += tileWidth;
                pos += scanlineStride;
            } else {
                int rowCnt = 0;
                for (int col = 0; col < scanlineStride; col++) {
                    byte b = bytes[pos++];
                    int count = Integer.bitCount(0xFF & b);
                    rowCnt += count;
                }

                cnt += rowCnt;
                if (rowCnt >= tileWidth) {
                    rowFull.set(row);
                }
            }
        }

        if (coverageCount == cnt) {
            return false;
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Updating count for " + this + " to " + cnt);
            }

            coverageCount = cnt;

            return true;
        }
    }

    /**
     * Draws a binary image already in raster space.
     *
     * @return True if at least one pixel has been added
     */
    public boolean draw(PlanarImage binaryImage) {
        initRaster(false);
        final Rectangle imageBounds = binaryImage.getBounds();
        final Rectangle overlapArea = imageBounds.intersection(tileArea);
        if (overlapArea.isEmpty()) {
            return false;
        }
        int xOffset = tileArea.x;
        int yOffset = tileArea.y;
        final Rectangle tileOverlap =
                new Rectangle(overlapArea.x - xOffset, overlapArea.y - yOffset, overlapArea.width, overlapArea.height);

        RandomIter sourceIter = RandomIterFactory.create(binaryImage, overlapArea, true, true);
        WritableRandomIter rasterIter = RandomIterFactory.createWritable(raster, tileOverlap);

        boolean added = false;
        final int maxCol = overlapArea.x + overlapArea.width;
        final int maxRow = overlapArea.y + overlapArea.height;
        for (int row = overlapArea.y; row < maxRow; row++) {
            for (int col = overlapArea.x; col < maxCol; col++) {
                int maskValue = sourceIter.getSample(col, row, 0);
                int rasValue = rasterIter.getSample(col - xOffset, row - yOffset, 0);
                if (maskValue == 1 && rasValue == 0) {
                    rasterIter.setSample(col - xOffset, row - yOffset, 0, 1);
                    coverageCount++;
                    added = true;
                }
            }
        }

        return added;
    }

    /** Sets raster and sampleModel */
    private void allocateRaster(boolean inverted) {
        final int value = inverted ? 1 : 0;
        WritableRaster result;
        if (tileWidth != tileHeight || value == 0) {
            result = buildSolidRaster(tileWidth, tileHeight, value);
        } else {
            Raster template = getSolidRaster(tileWidth, tileHeight, value);
            result = template.createCompatibleWritableRaster();

            byte[] src = ((DataBufferByte) template.getDataBuffer()).getData();
            byte[] dst = ((DataBufferByte) result.getDataBuffer()).getData();
            System.arraycopy(src, 0, dst, 0, src.length);
        }

        raster = result;
    }

    Raster getSolidRaster(int tileWidth, int tileHeight, int value) {
        String key = tileWidth + "x" + tileHeight + '_' + value;
        WritableRaster result = solidRasterCache.get(key);
        if (result == null) {
            result = buildSolidRaster(tileWidth, tileHeight, value);
            solidRasterCache.put(key, result);
        }

        return result;
    }

    private WritableRaster buildSolidRaster(int tileWidth, int tileHeight, int value) {
        SampleModel sampleModel = getMPSampleModel(tileWidth, tileHeight);

        // build the raster
        WritableRaster newRaster = RasterFactory.createWritableRaster(sampleModel, new java.awt.Point(0, 0));

        // sanity checks
        int dataType = sampleModel.getTransferType();
        int numBands = sampleModel.getNumBands();
        if (dataType != DataBuffer.TYPE_BYTE) {
            throw new IllegalArgumentException("The code works only if the sample model data type is BYTE");
        }
        if (numBands != 1) {
            throw new IllegalArgumentException("The code works only for single band rasters!");
        }

        if (value != 0) {
            // flood fill
            int w = sampleModel.getWidth();
            int h = sampleModel.getHeight();
            int[] data = new int[w * h];

            Arrays.fill(data, value);
            newRaster.setSamples(0, 0, w, h, 0, data);
        }

        return newRaster;
    }

    private SampleModel getMPSampleModel(int tileWidth, int tileHeight) {
        SampleModel sampleModel;

        if (tileWidth == ROIExcessGranuleRemover.DEFAULT_TILE_SIZE
                && tileHeight == ROIExcessGranuleRemover.DEFAULT_TILE_SIZE) {
            sampleModel = DEFAULT_PACKED_SAMPLE_MODEL;
        } else {
            String key = tileWidth + "x" + tileHeight;
            sampleModel = mpSampleModelCache.get(key);
            if (sampleModel == null) {
                sampleModel = new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE, tileWidth, tileHeight, 1);
                mpSampleModelCache.put(key, (MultiPixelPackedSampleModel) sampleModel);
            }
        }

        return sampleModel;
    }

    public void draw(Shape projectedShape) {
        draw(projectedShape, false);
    }

    public void draw(Shape projectedShape, boolean inverted) {
        initGraphics(inverted);

        Color drawColor = inverted ? Color.BLACK : Color.WHITE;
        graphics.setColor(drawColor);
        graphics.fill(projectedShape);
    }

    public void draw(/* another Raster here I suppose? Maybe a tile, maybe mis-aligned with this one */ ) {
        initRaster(false);

        // flip bits here, if the tile is aligned we can do int math, otherwise bit by bit...
    }

    public void dispose() {
        if (graphics != null) {
            graphics.dispose();
        }
        bi = null;
        raster = null;
    }

    @Override
    public String toString() {
        return "Tile[" + col + 'x' + row + ':' + coverageCount + ']';
    }

    WritableRaster getRaster() {
        return raster;
    }

    public Rectangle getTileArea() {
        return tileArea;
    }
}
