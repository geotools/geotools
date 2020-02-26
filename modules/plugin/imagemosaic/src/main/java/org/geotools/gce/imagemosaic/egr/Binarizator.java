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

import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.RasterFactory;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteShape;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;

/**
 * Creates a quantized binary representation of a Bounding Box.
 *
 * <p><br>
 * The BBOx is represented by a grid of pixels, which are then split in a set of tiles. <br>
 *
 * <p>By {@link #add(org.locationtech.jts.geom.Geometry) adding} Geometries to the Binarizator, they
 * will be rendered on the grid.<br>
 *
 * <p>You can check if the grid has been fully drawn using the {@link #isComplete() } method.<br>
 *
 * <p>When a Tile has been completely drawn, it is removed from the list.
 *
 * @author Emanuele Tajariol <etj at geo-solutions dot it>
 */
class Binarizator {

    private static final Logger LOGGER = Logging.getLogger(Binarizator.class);

    private final AffineTransform w2gTransform;

    private final int tileWidth, tileHeight;

    // only used for debug
    private final int origW, origH;

    /** Tiles not yet fully covered by the input geometries */
    private List<Tile> activeTiles;

    Binarizator(Polygon bbox, int pxWidth, int pxHeight, int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.origW = pxWidth;
        this.origH = pxHeight;

        final ReferencedEnvelope env = JTS.toEnvelope(bbox);
        w2gTransform =
                RendererUtilities.worldToScreenTransform(env, new Rectangle(pxWidth, pxHeight));

        if (w2gTransform == null) {
            LOGGER.info("Null transformer, possible bad bbox requested " + env);
            activeTiles = Collections.emptyList();
        } else {
            createTiles(pxWidth, pxHeight, tileWidth, tileHeight, w2gTransform);
        }
    }

    private void createTiles(int w, int h, int tw, int th, AffineTransform w2s) {
        int colnum = w / tw;

        int rownum = h / th;

        // create main tiles list
        activeTiles = new LinkedList<>();
        for (int col = 0; col < colnum; col++) {
            for (int row = 0; row < rownum; row++) {
                Tile tile = new Tile(tileWidth, tileHeight, col, row, w2s);
                activeTiles.add(tile);
            }
        }

        // add smaller last row and column if needed
        if ((w % tw) != 0) {
            for (int row = 0; row < rownum; row++) {
                Tile tile = new Tile(w % tw, tileHeight, colnum, row, w2s, tileWidth, tileHeight);
                activeTiles.add(tile);
            }
        }

        if ((h % th) != 0) {
            for (int col = 0; col < colnum; col++) {
                Tile tile = new Tile(tileWidth, h % th, col, rownum, w2s, tileWidth, tileHeight);
                activeTiles.add(tile);
            }
        }

        // add the rightmost lower cut tile
        if (((w % tw) != 0) && ((h % th) != 0)) {
            Tile tile = new Tile(w % tw, h % th, colnum, rownum, w2s, tileWidth, tileHeight);
            activeTiles.add(tile);
        }
    }

    /** Tells if the grid has been fully drawn. */
    public boolean isComplete() {
        return activeTiles.isEmpty();
    }

    /**
     * Adds a ROI to the binarizator. This assumes the operation is already fully working in raster
     * space, in other words, the world to grid transformation is the identity
     */
    public boolean add(ROI roi) {
        // do we need to transform it?
        // roi.transform(at)
        // look for intercepted tiles
        // if roi is shape or polygon base go the vector path?

        // avoid using the raster path if possible, the vector one has
        // some useful optimizations and we'll avod generating the raster version
        // of
        if (roi instanceof ROIGeometry) {
            Geometry geometry = ((ROIGeometry) roi).getAsGeometry();
            return add(geometry);
        } else if (roi instanceof ROIShape) {
            Shape shape = ((ROIShape) roi).getAsShape();
            Geometry geometry = JTS.toGeometry(shape);
            return add(geometry);
        }

        // ok, fully raster addition then
        final PlanarImage roiImage = roi.getAsImage();
        final Rectangle roiBounds = roiImage.getBounds();
        boolean added = false;
        for (Iterator<Tile> it = activeTiles.iterator(); it.hasNext(); ) {
            Tile tile = it.next();

            Rectangle tileBounds = tile.getTileArea();
            if (tileBounds.intersects(roiBounds)) {
                if (tile.draw(roiImage)) {
                    added = true;
                    if (tile.isFullyCovered()) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.fine(
                                    "Removing covered tile "
                                            + tile
                                            + " ("
                                            + activeTiles.size()
                                            + " left)");
                        }
                        it.remove();
                        tile.dispose();
                    }
                }
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Skipping tile " + tile);
                }
            }
        }

        return added;
    }

    /**
     * Render a Geometry on the grid.
     *
     * @param geometry the geometry to add
     * @return true if the added geometry turned on at least one pixel
     */
    public boolean add(Geometry geometry) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Adding geometry " + geometry);
        }

        PreparedGeometry pg = PreparedGeometryFactory.prepare(geometry);

        // transform the JTS object into AWT, and project on grid space
        LiteShape shape = new LiteShape(geometry, null, false);
        Shape projectedShape = w2gTransform.createTransformedShape(shape);
        boolean added = false;

        for (Iterator<Tile> it = activeTiles.iterator(); it.hasNext(); ) {
            Tile tile = it.next();

            Polygon tileBBox = tile.getTileBBox();
            Envelope geometryEnvelope = geometry.getEnvelopeInternal();
            if (geometryEnvelope.intersects(tileBBox.getEnvelopeInternal())
                    && pg.intersects(tileBBox)) // geometry partially covers the tile: draw it
            {
                if (pg.contains(tileBBox)) // geometry fully covers the tile?
                {
                    added = true;
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(
                                "Removing fully covered tile "
                                        + tile
                                        + " ("
                                        + activeTiles.size()
                                        + " left)");
                    }
                    it.remove();
                    tile.dispose();
                } else {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("drawing " + pg + " on " + tile + ' ' + tileBBox);
                    }

                    tile.draw(projectedShape);

                    if (tile.refreshCoverageCount()) {
                        added = true;
                        if (tile.isFullyCovered()) {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.fine(
                                        "Removing covered tile "
                                                + tile
                                                + " ("
                                                + activeTiles.size()
                                                + " left)");
                            }
                            it.remove();
                            tile.dispose();
                        }
                    }
                }
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Skipping tile " + tile);
                }
            }
        }

        return added;
    }

    List<Tile> getActiveTiles() {
        return activeTiles;
    }

    BufferedImage getDebugImage() {

        // "The code works only if the sample model data type is BYTE");
        SampleModel sampleModel =
                new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE, origW, origH, 1);

        // build the raster
        WritableRaster mainRaster =
                RasterFactory.createWritableRaster(sampleModel, new java.awt.Point(0, 0));

        // fill with 0 the whole raster
        int[] data = new int[origW * origH];
        Arrays.fill(data, 0);
        mainRaster.setSamples(0, 0, origW, origH, 0, data);

        // Draw white crosses as palceholder for all the tiles.
        // Existing tiles will overwrite the cross.

        int colnum = origW / tileWidth;
        int rownum = origH / tileHeight;

        final byte[] x00FF = {0, (byte) 0xff};
        final ColorModel binaryCM = new IndexColorModel(1, 2, x00FF, x00FF, x00FF);

        BufferedImage mainBI = new BufferedImage(binaryCM, mainRaster, false, null);
        Graphics2D graphics = mainBI.createGraphics();

        for (int col = 0; col <= colnum; col++) {
            for (int row = 0; row <= rownum; row++) {
                graphics.setColor(Color.WHITE);
                graphics.drawRect(col * tileWidth, row * tileHeight, tileWidth - 1, tileHeight - 1);
                drawChecks(graphics, col, row);
                graphics.setColor(Color.BLACK);
            }
        }
        // todo add crossed for last row an col if they exist

        // Draw the tiles

        for (Tile tile : activeTiles) {
            Raster tileRaster = tile.getRaster();
            int col = tile.getCol();
            int row = tile.getRow();

            if (tileRaster != null) {
                mainRaster.setDataElements(col * tileWidth, row * tileHeight, tileRaster);
            } else {
                // tile exists, but no raster yet: not still drawn, so basically
                // it's all black:

                graphics.setColor(Color.BLACK);
                graphics.fillRect(col * tileWidth, row * tileHeight, tileWidth - 1, tileHeight - 1);

                graphics.setColor(Color.WHITE);
                drawCross(graphics, col, row);
            }
        }

        // for (int col = 0; col <= colnum; col++)
        // {
        // for (int row = 0; row <= rownum; row++)
        // {
        // graphics.setColor(Color.WHITE);
        // graphics.drawString(col + "x" + row, (int) ((col + 0.5) * tileWidth), (int) ((row - 0.5)
        // * tileWidth));
        // }
        // }

        return mainBI;
    }

    private void drawCross(Graphics2D graphics, int col, int row) {
        graphics.drawRect(col * tileWidth, row * tileHeight, tileWidth - 1, tileHeight - 1);

        graphics.drawLine(
                col * tileWidth,
                row * tileHeight,
                (col * tileWidth) + tileWidth - 1,
                (row * tileHeight) + tileHeight - 1);

        graphics.drawLine(
                (col * tileWidth) + tileWidth - 1,
                row * tileWidth,
                col * tileWidth,
                (row * tileHeight) + tileHeight - 1);
    }

    private void drawChecks(Graphics2D graphics, int col, int row) {
        graphics.setColor(Color.WHITE);

        for (int y = 0; y < tileHeight; y++) {
            for (int x = y % 2; x < tileWidth; x += 2) {
                graphics.drawRect((col * tileWidth) + x, (row * tileWidth) + y, 0, 0);
            }
        }
    }
}
