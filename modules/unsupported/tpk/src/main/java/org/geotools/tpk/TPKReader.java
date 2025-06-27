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
 *
 */

package org.geotools.tpk;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;

public class TPKReader extends AbstractGridCoverage2DReader {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(TPKReader.class);

    static final CoordinateReferenceSystem SPHERICAL_MERCATOR;

    static final CoordinateReferenceSystem WGS_84;

    static {
        try {
            SPHERICAL_MERCATOR = CRS.decode("EPSG:3857", true);
            WGS_84 = CRS.decode("EPSG:4326", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static final ReferencedEnvelope WORLD_ENVELOPE =
            new ReferencedEnvelope(-20037508.34, 20037508.34, -20037508.34, 20037508.34, SPHERICAL_MERCATOR);

    protected static final int DEFAULT_TILE_SIZE = 256;

    protected static final int ZOOM_LEVEL_BASE = 2;

    protected ReferencedEnvelope bounds;

    protected String imageFormat;

    protected File sourceFile;

    protected Map<Long, TPKZoomLevel> zoomLevelMap;

    public TPKReader(Object source, Hints hints) {
        long startConstructor = System.currentTimeMillis();
        sourceFile = TPKFormat.getFileFromSource(source);
        zoomLevelMap = new HashMap<>();

        TPKFile file = new TPKFile(sourceFile, zoomLevelMap);

        try {
            bounds = ReferencedEnvelope.create(file.getBounds(), WGS_84).transform(SPHERICAL_MERCATOR, true);
        } catch (Exception e) {
            bounds = null;
        }
        originalEnvelope = new GeneralBounds(bounds == null ? WORLD_ENVELOPE : bounds);

        imageFormat = file.getImageFormat();

        long maxZoom = file.getMaxZoomLevel();

        long size = Math.round(Math.pow(ZOOM_LEVEL_BASE, maxZoom)) * DEFAULT_TILE_SIZE;

        highestRes = new double[] {WORLD_ENVELOPE.getSpan(0) / size, WORLD_ENVELOPE.getSpan(1) / size};

        originalGridRange = new GridEnvelope2D(new Rectangle((int) size, (int) size));

        coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(this.hints);

        crs = SPHERICAL_MERCATOR;

        file.close();

        String msg = String.format(
                "TPKReader constructor finished in %d milliseconds", System.currentTimeMillis() - startConstructor);

        LOGGER.fine(msg);
    }

    @Override
    public Format getFormat() {
        return new TPKFormat();
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue... parameters) throws IllegalArgumentException {

        long startRead = System.currentTimeMillis();

        TPKFile file = new TPKFile(sourceFile, zoomLevelMap, bounds, imageFormat);
        ReferencedEnvelope requestedEnvelope = null;
        Rectangle dim = null;

        if (parameters != null) {
            for (GeneralParameterValue parameter : parameters) {
                final ParameterValue param = (ParameterValue) parameter;
                final ReferenceIdentifier name = param.getDescriptor().getName();
                if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
                    final GridGeometry2D gg = (GridGeometry2D) param.getValue();
                    try {
                        requestedEnvelope = ReferencedEnvelope.create(
                                        gg.getEnvelope(), gg.getCoordinateReferenceSystem())
                                .transform(SPHERICAL_MERCATOR, true);
                    } catch (Exception e) {
                        requestedEnvelope = null;
                    }

                    dim = gg.getGridRange2D().getBounds();
                }
            }
        }

        if (requestedEnvelope == null) {
            requestedEnvelope = bounds;
        }

        long zoomLevel = 0;

        if (requestedEnvelope != null && dim != null) {
            // find the closest zoom based on horizontal resolution
            double ratioWidth = requestedEnvelope.getSpan(0)
                    / WORLD_ENVELOPE.getSpan(0); // proportion of total width that is being requested
            double propWidth = dim.getWidth() / ratioWidth; // this is the width in pixels that the whole world would
            // have in the requested resolution
            zoomLevel = Math.round(Math.log(propWidth / DEFAULT_TILE_SIZE) / Math.log(ZOOM_LEVEL_BASE));
            // the closest zoom level to the resolution, based on the formula width =
            // zoom_base^zoom_level * tile_size -> zoom_level = log(width /
            // tile_size)/log(zoom_base)
        }

        // now take a zoom level that is available in the TPK file
        zoomLevel = file.getClosestZoom(zoomLevel);

        long numberOfTiles =
                Math.round(Math.pow(ZOOM_LEVEL_BASE, zoomLevel)); // number of tile columns/rows for chosen zoom level
        double resX = WORLD_ENVELOPE.getSpan(0) / numberOfTiles; // points per tile
        double resY = WORLD_ENVELOPE.getSpan(1) / numberOfTiles; // points per tile
        double offsetX = WORLD_ENVELOPE.getMinimum(0);
        double offsetY = WORLD_ENVELOPE.getMinimum(1);

        long leftTile = file.getMinColumn(zoomLevel);
        long rightTile = file.getMaxColumn(zoomLevel);
        long bottomTile = file.getMinRow(zoomLevel);
        long topTile = file.getMaxRow(zoomLevel);

        if (requestedEnvelope != null) { // crop tiles to requested envelope
            leftTile = boundMax(leftTile, (requestedEnvelope.getMinimum(0) - offsetX) / resX);
            bottomTile = boundMax(bottomTile, (requestedEnvelope.getMinimum(1) - offsetY) / resY);
            rightTile = boundMinMax(leftTile, rightTile, (requestedEnvelope.getMaximum(0) - offsetX) / resX);
            topTile = boundMinMax(bottomTile, topTile, (requestedEnvelope.getMaximum(1) - offsetY) / resY);
        }

        int width = (int) (rightTile - leftTile + 1) * DEFAULT_TILE_SIZE;
        int height = (int) (topTile - bottomTile + 1) * DEFAULT_TILE_SIZE;

        // recalculate the envelope we are actually returning
        ReferencedEnvelope resultEnvelope = new ReferencedEnvelope(
                offsetX + leftTile * resX,
                offsetX + (rightTile + 1) * resX,
                offsetY + bottomTile * resY,
                offsetY + (topTile + 1) * resY,
                SPHERICAL_MERCATOR);

        String imageFormat = file.getImageFormat();

        // go get all of the raw data for each tile creating a list of Tile objects
        List<TPKTile> tiles = file.getTiles(zoomLevel, topTile, bottomTile, leftTile, rightTile, imageFormat);

        // now construct the complete image
        BufferedImage image = getStartImage(BufferedImage.TYPE_INT_ARGB, width, height);
        final Graphics graphics = image.getGraphics();

        final long originLeft = leftTile;
        final long originTop = topTile;

        // use parallel processing to create individual tile images
        tiles.parallelStream()
                .map(TileImage::new) // it's this conversion to image that we are parallelizing
                .forEach(tileImage -> {
                    if (tileImage.image != null) {
                        // calc tile position
                        int posx = (int) (tileImage.col - originLeft) * DEFAULT_TILE_SIZE;
                        int posy = (int) (originTop - tileImage.row) * DEFAULT_TILE_SIZE;

                        // use drawImage() to stitch the individual tile images together
                        graphics.drawImage(tileImage.image, posx, posy, null);
                    }
                });

        file.close();

        String msg = String.format(
                "At zoom level %d TPK read completed in %d milliseconds",
                zoomLevel, System.currentTimeMillis() - startRead);

        LOGGER.fine(msg);

        return coverageFactory.create("unnamed", image, resultEnvelope);
    }

    private long boundMinMax(long max, long min, double value) {
        return Math.max(max, Math.min(min, Math.round(Math.floor(value))));
    }

    private long boundMax(long bound, double value) {
        return Math.max(bound, Math.round(Math.floor(value)));
    }

    public enum ImageFormats {

        // add formats as required
        FMT_JPG("jpg", new byte[] {(byte) 0xff, (byte) 0xd8}),
        FMT_PNG("png", new byte[] {(byte) 0x89, (byte) 0x50, (byte) 0x4e, (byte) 0x47});

        private final String format;
        private final byte[] signature;

        ImageFormats(String format, byte[] signature) {
            this.format = format;
            this.signature = signature;
        }

        // Scan the set of defined formats looking for a matching "signature"
        public static String inferFormatFromImageData(byte[] imageData) {
            for (ImageFormats format : ImageFormats.values()) {
                boolean matches = true;
                try {
                    for (int index = 0; index < format.signature.length; index++) {
                        if (imageData[index] != format.signature[index]) {
                            matches = false;
                            break;
                        }
                    }
                } catch (Exception ex) {
                    matches = false;
                }

                if (matches) {
                    return format.format;
                }
            }
            return null;
        }
    }

    /**
     * Infer file type from file data if possible -- In case TPK files allow tiles with mixed formats we don't want to
     * be entirely dependent on the Conf.xml "CacheTileFormat" element
     *
     * @param imageData -- reference to the raw byte data of the image
     * @param format -- format derived from metadata table
     * @return -- the inferred file type
     */
    private static String getImageFormat(byte[] imageData, String format) {
        String inferred = ImageFormats.inferFormatFromImageData(imageData);
        if (inferred != null && !inferred.equalsIgnoreCase(format)) {
            LOGGER.fine(String.format("Overriding tile format: was %s, set to %s", format, inferred));
        }
        return inferred != null ? inferred : format;
    }

    protected static BufferedImage readImage(byte[] data, String format) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        Iterator<?> readers = ImageIO.getImageReadersByFormatName(getImageFormat(data, format));
        ImageReader reader = (ImageReader) readers.next();
        try (ImageInputStream iis = ImageIO.createImageInputStream(bis)) {
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();

            return reader.read(0, param);
        }
    }

    @SuppressWarnings("PMD.ReplaceHashtableWithMap")
    protected BufferedImage getStartImage(BufferedImage copyFrom, int width, int height) {
        Hashtable<String, Object> properties = null;

        if (copyFrom.getPropertyNames() != null) {
            properties = new Hashtable<>();
            for (String name : copyFrom.getPropertyNames()) {
                properties.put(name, copyFrom.getProperty(name));
            }
        }

        SampleModel sm = copyFrom.getSampleModel().createCompatibleSampleModel(width, height);
        WritableRaster raster = Raster.createWritableRaster(sm, null);

        BufferedImage image =
                new BufferedImage(copyFrom.getColorModel(), raster, copyFrom.isAlphaPremultiplied(), properties);

        setBackground(image, new Color(0x00000000, true)); // transparent background

        return image;
    }

    protected BufferedImage getStartImage(int imageType, int width, int height) {
        if (imageType == BufferedImage.TYPE_CUSTOM) imageType = BufferedImage.TYPE_3BYTE_BGR;

        BufferedImage image = new BufferedImage(width, height, imageType);
        setBackground(image, new Color(0x00000000, true)); // transparent background

        return image;
    }

    protected BufferedImage getStartImage(int width, int height) {
        return getStartImage(BufferedImage.TYPE_CUSTOM, width, height);
    }

    protected void setBackground(BufferedImage image, Color bgColor) {
        Graphics2D g2D = (Graphics2D) image.getGraphics();
        Color save = g2D.getColor();
        g2D.setColor(bgColor);
        g2D.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2D.setColor(save);
    }

    /** Tile converted into a BufferedImage -- this conversion is done in parallel members: row, column and image */
    static class TileImage {
        long col;
        long row;
        BufferedImage image;

        TileImage(TPKTile tile) {
            this.col = tile.col;
            this.row = tile.row;
            if (tile.tileData != null && tile.tileData.length > 0) {
                try {
                    image = readImage(tile.tileData, tile.imageFormat);
                } catch (Exception ex) {
                    String template = "Bad tile data, zl=%d, row=%d, col=%d ==> %s";
                    LOGGER.info(String.format(template, tile.zoomLevel, row, col, ex.getMessage()));
                    image = null;
                }
            }
        }
    }
}
