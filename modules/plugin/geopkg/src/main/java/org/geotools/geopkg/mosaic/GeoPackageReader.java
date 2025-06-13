/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.mosaic;

import it.geosolutions.jaiext.mosaic.MosaicRIF;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.PackedColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.OpImage;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.operator.MosaicDescriptor;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.coverage.grid.GridEnvelope;
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
import org.geotools.geopkg.GeoPackage;
import org.geotools.geopkg.Tile;
import org.geotools.geopkg.TileEntry;
import org.geotools.geopkg.TileMatrix;
import org.geotools.geopkg.TileReader;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.CRS;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;

/**
 * GeoPackage Grid Reader (supports the GP mosaic datastore).
 *
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public class GeoPackageReader extends AbstractGridCoverage2DReader {

    /** The {@link Logger} for this {@link GeoPackageReader}. */
    private static final Logger LOGGER = Logging.getLogger(GeoPackageReader.class);

    protected static final int DEFAULT_TILE_SIZE = 256;

    protected static final int ZOOM_LEVEL_BASE = 2;

    protected File sourceFile;

    protected Map<String, TileEntry> tiles = new LinkedHashMap<>();

    GeoPackage file;

    public GeoPackageReader(Object source, Hints hints) throws IOException {
        coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(this.hints);

        sourceFile = GeoPackageFormat.getFileFromSource(source);
        file = new GeoPackage(sourceFile, null, null, true);
        for (TileEntry tile : file.tiles()) {
            tiles.put(tile.getTableName(), tile);
        }

        // have a sane default when hit with no name, useful in particular
        // when the geopackage only has one coverage
        coverageName = tiles.keySet().iterator().next();
        // add the overview count and allocate the
        final List<TileMatrix> tileMatricies = tiles.get(coverageName).getTileMatricies();
        this.numOverviews = tileMatricies.size() - 1;
        overViewResolutions = new double[numOverviews][2];
        // first tile matrix is the one with the lowest resolution, last one is native
        for (int i = 0; i < tileMatricies.size() - 1; i++) {
            final TileMatrix matrix = tileMatricies.get(i);
            overViewResolutions[tileMatricies.size() - i - 2] =
                    new double[] {matrix.getXPixelSize(), matrix.getYPixelSize()};
        }
    }

    @Override
    public Format getFormat() {
        return new GeoPackageFormat();
    }

    @Override
    protected boolean checkName(String coverageName) {
        Utilities.ensureNonNull("coverageName", coverageName);
        return tiles.keySet().contains(coverageName);
    }

    @Override
    public GeneralBounds getOriginalEnvelope(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }

        return new GeneralBounds(tiles.get(coverageName).getTileMatrixSetBounds());
    }

    @Override
    protected double[] getHighestRes(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }

        List<TileMatrix> matrices = tiles.get(coverageName).getTileMatricies();
        TileMatrix matrix = matrices.get(matrices.size() - 1);
        return new double[] {matrix.getXPixelSize(), matrix.getYPixelSize()};
    }

    @Override
    public GridEnvelope getOriginalGridRange(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }

        List<TileMatrix> matrices = tiles.get(coverageName).getTileMatricies();
        TileMatrix matrix = matrices.get(matrices.size() - 1);
        return new GridEnvelope2D(new Rectangle(
                matrix.getMatrixWidth() * matrix.getTileWidth(), matrix.getMatrixHeight() * matrix.getTileHeight()));
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem(String coverageName) {
        if (!checkName(coverageName)) {
            throw new IllegalArgumentException("The specified coverageName " + coverageName + "is not supported");
        }

        try {
            return CRS.decode("EPSG:" + tiles.get(coverageName).getSrid(), true);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String[] getGridCoverageNames() {
        return tiles.keySet().toArray(new String[tiles.size()]);
    }

    @Override
    public int getGridCoverageCount() {
        return tiles.size();
    }

    @Override
    public GridCoverage2D read(String coverageName, GeneralParameterValue... parameters)
            throws IllegalArgumentException, IOException {
        TileEntry entry = tiles.get(coverageName);
        RenderedImage image = null;
        ReferencedEnvelope resultEnvelope = null;

        CoordinateReferenceSystem crs = getCoordinateReferenceSystem(coverageName);

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
                                .transform(crs, true);
                    } catch (Exception e) {
                        requestedEnvelope = null;
                    }

                    dim = gg.getGridRange2D().getBounds();
                    continue;
                }
            }
        }

        // find the closest zoom based on horizontal resolution
        TileMatrix bestMatrix = null;
        if (requestedEnvelope != null && dim != null) {
            // requested res
            double horRes = requestedEnvelope.getSpan(0) / dim.getWidth(); // proportion of total width that is being
            // requested

            // loop over matrices
            double difference = Double.MAX_VALUE;
            for (TileMatrix matrix : entry.getTileMatricies()) {
                if (!matrix.hasTiles()) {
                    continue;
                }
                double newRes = matrix.getXPixelSize();
                double newDifference = Math.abs(horRes - newRes);
                if (newDifference < difference) {
                    difference = newDifference;
                    bestMatrix = matrix;
                }
            }
        }
        if (bestMatrix == null) {
            // pick the highest resolution, like in a geotiff with overviews
            double resolution = Double.POSITIVE_INFINITY;
            for (TileMatrix matrix : entry.getTileMatricies()) {
                if (!matrix.hasTiles()) {
                    continue;
                }
                double newRes = matrix.getXPixelSize();
                if (newRes < resolution) {
                    resolution = newRes;
                    bestMatrix = matrix;
                }
            }
        }

        if (bestMatrix == null) {
            // it means no level has tiles, return null, it's ok to do so
            return null;
        }

        Envelope entryBounds = entry.getTileMatrixSetBounds();
        double resX = bestMatrix.getXPixelSize() * bestMatrix.getTileWidth();
        double resY = bestMatrix.getYPixelSize() * bestMatrix.getTileHeight();
        /*
         * From the specification: "The tile coordinate (0,0) always refers to the tile in the upper left corner of the tile matrix at any zoom
         * level, regardless of the actual availability of that tile."
         * So remember the y axis goes from top to bottom, not the other way around
         */
        double offsetX = entryBounds.getMinX();
        double offsetY = entryBounds.getMaxY();

        // crop tiles to requested envelope if necessary
        int leftTile, bottomTile, rightTile, topTile;
        if (requestedEnvelope != null) {
            TileBoundsCalculator tileBoundsCalculator =
                    new TileBoundsCalculator(requestedEnvelope, resX, resY, offsetX, offsetY).invoke();
            leftTile = tileBoundsCalculator.getLeftTile();
            bottomTile = tileBoundsCalculator.getBottomTile();
            rightTile = tileBoundsCalculator.getRightTile();
            topTile = tileBoundsCalculator.getTopTile();
        } else {
            final double minX = entryBounds.getMinX();
            final double maxX = entryBounds.getMaxX();
            final double minY = entryBounds.getMinY();
            final double maxY = entryBounds.getMaxY();

            // cannot "round" here or half a tile in the requested area might be missing
            // TODO: the code could consider if the eventual extra tile introduced by
            // floor/ceil actually contributes at least one full pixel to the output, or
            // a significant part of it
            leftTile = (int) Math.floor((minX - offsetX) / resX);
            topTile = (int) Math.floor((offsetY - maxY) / resY);
            rightTile = (int) Math.ceil((maxX - offsetX) / resX);
            bottomTile = (int) Math.ceil((offsetY - minY) / resY);
        }

        try (TileReader it = file.reader(
                entry,
                bestMatrix.getZoomLevel(),
                bestMatrix.getZoomLevel(),
                leftTile,
                rightTile,
                topTile,
                bottomTile)) {
            /**
             * Composing the output is harder than it seems, GeoPackage does not mandate any uniformity in tiles, they
             * can be in different formats (a mix of PNG and JPEG) and can have different color models, thus a mix of
             * (possibly different) palettes, gray, RGB, RGBA. GDAL in particular defaults to generate a mix of PNG and
             * JPEG to generate the slow and large PNG format only when transparency is actually needed
             */
            List<ImageInTile> sources = new ArrayList<>();
            TileImageReader tileReader = new TileImageReader();

            while (it.hasNext()) {
                Tile tile = it.next();
                // recalculate the envelope we are actually returning (remember y axis is flipped)
                ReferencedEnvelope tileEnvelope = new ReferencedEnvelope( //
                        offsetX + tile.getColumn() * resX, //
                        offsetX + (tile.getColumn() + 1) * resX, //
                        offsetY - (tile.getRow() + 1) * resY, //
                        offsetY - tile.getRow() * resY,
                        crs);
                if (resultEnvelope == null) {
                    resultEnvelope = tileEnvelope;
                } else {
                    resultEnvelope.expandToInclude(tileEnvelope);
                }

                BufferedImage tileImage = tileReader.read(tile.getData());

                int posx = (tile.getColumn() - leftTile) * DEFAULT_TILE_SIZE;
                int posy = (tile.getRow() - topTile) * DEFAULT_TILE_SIZE;
                sources.add(new ImageInTile(tileImage, posx, posy));
            }
            it.close();

            if (sources.isEmpty()) {
                // no tiles
                return null;
            } else if (sources.size() == 1) {
                // one tile
                image = sources.get(0).image;
            } else {
                image = mosaicImages(sources);
            }
        }
        return coverageFactory.create(entry.getTableName(), image, resultEnvelope);
    }

    private RenderedImage mosaicImages(List<ImageInTile> sources) {
        if (uniformImages(sources.stream().map(it -> it.image).collect(Collectors.toList()))) {
            return mosaicUniformImages(sources);
        } else {
            return mosaicHeterogeneousImages(sources);
        }
    }

    /**
     * Fast lane mosaicker, basically builds an OpImage that returns translated versions of the source images, without
     * actually copying pixels around
     */
    @SuppressWarnings({"PMD.ReplaceVectorWithList", "PMD.UseArrayListInsteadOfVector"}) // old API asking for Vector
    private OpImage mosaicUniformImages(List<ImageInTile> sources) {
        // compute bounds
        int minx = sources.stream().mapToInt(it -> it.posx).min().getAsInt();
        int maxx = sources.stream()
                .mapToInt(it -> it.posx + it.image.getWidth())
                .max()
                .getAsInt();
        int miny = sources.stream().mapToInt(it -> it.posy).min().getAsInt();
        int maxy = sources.stream()
                .mapToInt(it -> it.posy + it.image.getHeight())
                .max()
                .getAsInt();
        int width = maxx - minx;
        int height = maxy - miny;

        // compute layout
        List<BufferedImage> sourceImages = sources.stream().map(it -> it.image).collect(Collectors.toList());
        ImageLayout il = new ImageLayout(sourceImages.get(0));
        il.setMinX(minx);
        il.setWidth(width);
        il.setHeight(height);
        il.setMinY(miny);
        il.setTileWidth(sourceImages.get(0).getWidth());
        il.setTileHeight(sourceImages.get(0).getHeight());

        // simple
        RenderingHints hints = new Hints(JAI.getDefaultInstance().getRenderingHints());
        hints.putAll(GeoTools.getDefaultHints());
        return new OpImage(new Vector<>(sourceImages), il, hints, false) {

            @Override
            public Raster computeTile(int tileX, int tileY) {
                int posx = tileX * tileWidth + tileGridXOffset;
                int posy = tileY * tileHeight + tileGridYOffset;
                ImageInTile candidate = sources.stream()
                        .filter(it -> it.posx == posx && it.posy == posy)
                        .findFirst()
                        .orElse(null);
                if (candidate != null) {
                    return candidate.image.getData().createTranslatedChild(posx, posy);
                }

                // not inside the available grid, build a white cell then
                WritableRaster dest = createWritableRaster(sampleModel, new Point(tileXToX(tileX), tileYToY(tileY)));
                BufferedImage bi = new BufferedImage(getColorModel(), dest, false, null);
                Graphics2D g2D = (Graphics2D) bi.getGraphics();
                g2D.setColor(Color.WHITE);
                g2D.fillRect(0, 0, bi.getWidth(), bi.getHeight());
                g2D.dispose();

                return dest;
            }

            @Override
            public Rectangle mapSourceRect(Rectangle sourceRect, int sourceIndex) {
                // should not really be used
                return sourceRect;
            }

            @Override
            public Rectangle mapDestRect(Rectangle destRect, int sourceIndex) {
                // should not really be used
                return destRect;
            }

            @Override
            @SuppressWarnings({"unchecked", "PMD.ReplaceVectorWithList"})
            public Vector<RenderedImage> getSources() {
                return super.getSources();
            }
        };
    }

    private RenderedImage mosaicHeterogeneousImages(List<ImageInTile> sources) {
        // at the time of writing, only JAI-EXT mosaic can handle a mix of different
        // color models, we need to use it explicitly
        final ParameterBlockJAI pb = new ParameterBlockJAI(new it.geosolutions.jaiext.mosaic.MosaicDescriptor());
        for (ImageInTile it : sources) {
            if (it.posx != 0 || it.posy != 0) {
                ImageWorker iw = new ImageWorker(it.image);
                iw.translate(it.posx, it.posy, Interpolation.getInstance(Interpolation.INTERP_NEAREST));
                RenderedImage translated = iw.getRenderedImage();
                pb.addSource(translated);
            } else {
                pb.addSource(it.image);
            }
        }
        pb.setParameter("mosaicType", MosaicDescriptor.MOSAIC_TYPE_OVERLAY);
        pb.setParameter("sourceAlpha", null);
        pb.setParameter("sourceROI", null);
        pb.setParameter("sourceThreshold", null);
        pb.setParameter("backgroundValues", new double[] {0});
        pb.setParameter("nodata", null);

        RenderingHints hints = new Hints(JAI.getDefaultInstance().getRenderingHints());
        hints.putAll(GeoTools.getDefaultHints());
        RenderedImage image = new MosaicRIF().create(pb, hints);
        return image;
    }

    @Override
    public GridCoverage2D read(GeneralParameterValue... parameters) throws IllegalArgumentException, IOException {
        return read(coverageName, parameters);
    }

    @Override
    public void dispose() {
        if (file != null) {
            file.close();
        }
    }

    /** Method object returning 4 separate params */
    private class TileBoundsCalculator {
        private Envelope requestedEnvelope;
        private double resX;
        private double resY;
        private double offsetX;
        private double offsetY;
        private int leftTile;
        private int bottomTile;
        private int rightTile;
        private int topTile;

        public TileBoundsCalculator(
                Envelope requestedEnvelope, double resX, double resY, double offsetX, double offsetY) {
            this.requestedEnvelope = requestedEnvelope;
            this.resX = resX;
            this.resY = resY;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public int getLeftTile() {
            return leftTile;
        }

        public int getBottomTile() {
            return bottomTile;
        }

        public int getRightTile() {
            return rightTile;
        }

        public int getTopTile() {
            return topTile;
        }

        public TileBoundsCalculator invoke() {
            // the requested bounds
            final double minX = requestedEnvelope.getMinX();
            final double maxX = requestedEnvelope.getMaxX();
            final double minY = requestedEnvelope.getMinY();
            final double maxY = requestedEnvelope.getMaxY();

            // cannot "round" here or half a tile in the requested area might be missing
            leftTile = (int) Math.floor((minX - offsetX) / resX);
            topTile = (int) Math.floor((offsetY - maxY) / resY);
            rightTile = (int) Math.ceil((maxX - offsetX) / resX);
            // but check if the extra tile is completely outside, and if so, remove
            if (offsetX + (rightTile * resX) > maxX) {
                rightTile -= 1;
            }
            bottomTile = (int) Math.ceil((offsetY - minY) / resY);
            if (offsetY - (bottomTile * resY) < minY) {
                bottomTile -= 1;
            }
            return this;
        }
    }

    /** Simple holder for tile information, the image and its position */
    private static class ImageInTile {
        BufferedImage image;
        int posx;
        int posy;

        public ImageInTile(BufferedImage image, int posX, int posY) {
            this.image = image;
            this.posx = posX;
            this.posy = posY;
        }
    }

    /** Returns true if the provided images are uniform color and sample model wise */
    private static boolean uniformImages(List<RenderedImage> sources) {
        final int numSources = sources.size();

        // get first image as reference
        RenderedImage first = sources.get(0);
        ColorModel firstColorModel = first.getColorModel();
        SampleModel firstSampleModel = first.getSampleModel();

        // starting point image layout
        ImageLayout result = new ImageLayout();
        result.setSampleModel(firstSampleModel);
        // easy case
        if (numSources == 1) {
            result.setColorModel(firstColorModel);
            return true;
        }

        // See if they all are the same
        int firstDataType = firstSampleModel.getDataType();
        int firstBands = firstSampleModel.getNumBands();
        int firstSampleSize = firstSampleModel.getSampleSize()[0];
        boolean hasIndexedColorModels = firstColorModel instanceof IndexColorModel;
        boolean hasComponentColorModels = firstColorModel instanceof ComponentColorModel;
        boolean hasPackedColorModels = firstColorModel instanceof PackedColorModel;
        boolean hasUnrecognizedColorModels =
                !hasComponentColorModels && !hasIndexedColorModels && !hasPackedColorModels;
        boolean hasUnsupportedTypes = false;
        int maxBands = firstBands;
        for (int i = 1; i < numSources; i++) {
            RenderedImage source = sources.get(i);
            SampleModel sourceSampleModel = source.getSampleModel();
            ColorModel sourceColorModel = source.getColorModel();
            int sourceBands = sourceSampleModel.getNumBands();
            int sourceDataType = sourceSampleModel.getDataType();
            if (sourceDataType == DataBuffer.TYPE_UNDEFINED) {
                hasUnsupportedTypes = true;
            }

            if (sourceBands > maxBands) {
                maxBands = sourceBands;
            }

            if (sourceColorModel instanceof IndexColorModel) {
                hasIndexedColorModels = true;
            } else if (sourceColorModel instanceof ComponentColorModel) {
                hasComponentColorModels = true;
            } else if (sourceColorModel instanceof PackedColorModel) {
                hasPackedColorModels = true;
            } else {
                hasUnrecognizedColorModels = true;
            }

            if (sourceDataType != firstDataType || sourceBands != firstBands) {
                return false;
            }

            for (int j = 0; j < sourceBands; j++) {
                if (sourceSampleModel.getSampleSize(j) != firstSampleSize) {
                    return false;
                }
            }
        }
        if (hasUnrecognizedColorModels || hasUnsupportedTypes) {
            return false;
        }

        // see how many types we're dealing with
        int colorModelsTypes =
                (hasIndexedColorModels ? 1 : 0) + (hasComponentColorModels ? 1 : 0) + (hasPackedColorModels ? 1 : 0);
        // if uniform, we have it easy
        if (colorModelsTypes > 1) {
            return false;
        }
        if (hasIndexedColorModels) {
            return hasUniformPalettes(sources);
        }
        return true;
    }

    private static boolean hasUniformPalettes(List<RenderedImage> sources) {
        // all indexed, but are the palettes the same?
        RenderedImage first = sources.get(0);
        IndexColorModel reference = (IndexColorModel) first.getColorModel();
        int mapSize = reference.getMapSize();
        byte[] reference_reds = new byte[mapSize];
        byte[] reference_greens = new byte[mapSize];
        byte[] reference_blues = new byte[mapSize];
        byte[] reference_alphas = new byte[mapSize];
        byte[] reds = new byte[mapSize];
        byte[] greens = new byte[mapSize];
        byte[] blues = new byte[mapSize];
        byte[] alphas = new byte[mapSize];
        reference.getReds(reference_reds);
        reference.getGreens(reference_greens);
        reference.getBlues(reference_blues);
        reference.getAlphas(reference_alphas);
        boolean uniformPalettes = true;
        final int numSources = sources.size();
        for (int i = 1; i < numSources; i++) {
            RenderedImage source = sources.get(i);

            IndexColorModel sourceColorModel = (IndexColorModel) source.getColorModel();

            // check the basics
            if (reference.getNumColorComponents() != sourceColorModel.getNumColorComponents()) {
                throw new IllegalArgumentException("Cannot mosaic togheter images with index "
                        + "color models having different numbers of color components:\n "
                        + reference
                        + "\n"
                        + sourceColorModel);
            }

            // if not the same color space, then we need to expand
            if (!reference.getColorSpace().equals(reference.getColorSpace())) {
                return false;
            }

            if (!sourceColorModel.equals(reference) || sourceColorModel.getMapSize() != mapSize) {
                uniformPalettes = false;
                break;
            }
            // the above does not compare the rgb(a) arrays, do it
            sourceColorModel.getReds(reds);
            sourceColorModel.getGreens(greens);
            sourceColorModel.getBlues(blues);
            sourceColorModel.getAlphas(alphas);
            if (!Arrays.equals(reds, reference_reds)
                    || !Arrays.equals(greens, reference_greens)
                    || !Arrays.equals(blues, reference_blues)
                    || !Arrays.equals(alphas, reference_alphas)) {
                uniformPalettes = false;
                break;
            }
        }
        return uniformPalettes;
    }
}
