/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageTypeSpecifier;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.util.NumberRange;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * Wraps metadata information for a raster dataset, whether it is composed of a single raster, or
 * it's raster catalog, and provides some conveinent methods to get to the raster metadata of it's
 * rasters and pyramid levels.
 *
 * <p>This is the single entry point to the metadata of a raster dataset. The associated classes
 * {@link RasterInfo} and {@link PyramidLevelInfo} are to be considered private to this class.
 *
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.4
 */
@SuppressWarnings({"nls"})
public final class RasterDatasetInfo {

    /** TRasterDatasetInfo the raster table we're pulling images from in this reader * */
    private String rasterTable = null;

    /**
     * raster column names on this raster. If there's more than one raster column (is this
     * possible?) then we just use the first one.
     */
    private String[] rasterColumns;

    /** Array holding information on each level of the pyramid in this raster. * */
    private List<RasterInfo> subRasterInfo;

    private GridEnvelope originalGridRange;

    private volatile List<GridSampleDimension> gridSampleDimensions;

    private final Map<Integer, ImageTypeSpecifier> renderedImageSpec =
            new HashMap<Integer, ImageTypeSpecifier>();

    /** @param rasterTable the rasterTable to set */
    void setRasterTable(String rasterTable) {
        this.rasterTable = rasterTable;
    }

    /** @return the raster table name */
    public String getRasterTable() {
        return rasterTable;
    }

    /** @param rasterColumns the rasterColumns to set */
    void setRasterColumns(String[] rasterColumns) {
        this.rasterColumns = rasterColumns;
    }

    /** @return the raster column names */
    public String[] getRasterColumns() {
        return rasterColumns;
    }

    /** @param pyramidInfo the pyramidInfo to set */
    public void setPyramidInfo(List<RasterInfo> pyramidInfo) {
        this.subRasterInfo = pyramidInfo;
    }

    public GridSampleDimension[] getGridSampleDimensions() {
        if (gridSampleDimensions == null) {
            synchronized (this) {
                if (gridSampleDimensions == null) {
                    gridSampleDimensions = buildSampleDimensions();
                }
            }
        }
        return gridSampleDimensions.toArray(new GridSampleDimension[getNumBands()]);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<GridSampleDimension> buildSampleDimensions() {

        final int numBands = getNumBands();
        List<GridSampleDimension> dimensions = new ArrayList<GridSampleDimension>(numBands);

        final Color transparent = new Color(0, 0, 0, 0);

        List<RasterBandInfo> bands = subRasterInfo.get(0).getBands();

        for (RasterBandInfo band : bands) {
            // use native cell type, in case no-data value has been computed to account for
            // sample depth promotion, we want to category to keep being the native range for
            // the values category
            final RasterCellType targetCellType = getNativeCellType();
            String bandName = band.getBandName();

            final double statsMin = band.getStatsMin();
            final double statsMax = band.getStatsMax();

            NumberRange<?> sampleValueRange;
            if (Double.isNaN(statsMin) || Double.isNaN(statsMax)) {
                sampleValueRange = targetCellType.getSampleValueRange();
            } else {
                sampleValueRange = NumberRange.create(statsMin, statsMax);
                Class elementClass = targetCellType.getSampleValueRange().getElementClass();
                sampleValueRange = sampleValueRange.castTo(elementClass);
            }

            final Color[] colorRange = null;

            final boolean geophysics = isGeoPhysics();

            Category valuesCat = new Category("values", colorRange, sampleValueRange);

            Category[] categories;
            if (geophysics) {
                double noDataValue = band.getNoDataValue().doubleValue();
                // same as Category.NODATA but for the actual nodata value instead of hardcoded to
                // zero
                Category nodataCat =
                        new Category(
                                Vocabulary.formatInternational(VocabularyKeys.NODATA),
                                transparent,
                                noDataValue);
                categories = new Category[] {valuesCat, nodataCat};
            } else {
                // do not build a nodata category. A nodata value that doesn't overlap the value
                // range couldn't be determined
                categories = new Category[] {valuesCat};
            }
            /*
             * if (band.isHasStats()) { //can't do this, get an exception telling categories
             * overlap.. so no real way to express the statistics, uh? Category catMin = new
             * Category("Min", null, band.getStatsMin()).geophysics(true); Category catMax = new
             * Category("Max", null, band.getStatsMin()).geophysics(true); Category catMean = new
             * Category("Mean", null, band.getStatsMin()).geophysics(true); Category catStdDev = new
             * Category("StdDev", null, band.getStatsMin()) .geophysics(true); categories = new
             * Category[] { valuesCat, nodataCat, catMin, catMax, catMean, catStdDev }; } else {
             * categories = new Category[] { valuesCat, nodataCat }; }
             */

            // .geophysics(false) because our sample model always corresponds to the packed view
            // (whether it matches the underlying sample depth or we're promoting in order to make
            // room for the nodata value).
            GridSampleDimension sampleDim = new GridSampleDimension(bandName, categories, null);

            dimensions.add(sampleDim);
        }
        return dimensions;
    }

    private boolean isGeoPhysics() {
        if (isColorMapped()) {
            return false;
        }
        return RasterUtils.isGeoPhysics(getNumBands(), getNativeCellType());
    }

    public int getNumBands() {
        return subRasterInfo.get(0).getNumBands();
    }

    public int getImageWidth() {
        final GridEnvelope originalGridRange = getOriginalGridRange();
        final int width = originalGridRange.getSpan(0);
        return width;
    }

    public int getImageHeight() {
        final GridEnvelope originalGridRange = getOriginalGridRange();
        final int height = originalGridRange.getSpan(1);
        return height;
    }

    /** @return the coverageCrs */
    public CoordinateReferenceSystem getCoverageCrs() {
        return subRasterInfo.get(0).getCoordinateReferenceSystem();
    }

    /**
     * @return the originalGridRange for the whole raster dataset, based on the first raster in the
     *     raster dataset
     */
    public GridEnvelope getOriginalGridRange() {
        if (originalGridRange == null) {
            final int rasterCount = getNumRasters();
            if (1 == rasterCount) {
                originalGridRange = getGridRange(0, 0);
                return originalGridRange;
            }

            final MathTransform modelToRaster;
            try {
                final MathTransform rasterToModel = getRasterToModel();
                modelToRaster = rasterToModel.inverse();
            } catch (NoninvertibleTransformException e) {
                throw new IllegalStateException("Can't create transform from model to raster");
            }

            int minx = Integer.MAX_VALUE;
            int miny = Integer.MAX_VALUE;
            int maxx = Integer.MIN_VALUE;
            int maxy = Integer.MIN_VALUE;

            for (int rasterN = 0; rasterN < rasterCount; rasterN++) {
                final GeneralEnvelope rasterEnvelope = getGridEnvelope(rasterN, 0);
                GeneralEnvelope rasterGridRangeInDataSet;
                try {
                    rasterGridRangeInDataSet = CRS.transform(modelToRaster, rasterEnvelope);
                } catch (NoninvertibleTransformException e) {
                    throw new IllegalArgumentException(e);
                } catch (TransformException e) {
                    throw new IllegalArgumentException(e);
                }

                minx = Math.min(minx, (int) Math.floor(rasterGridRangeInDataSet.getMinimum(0)));
                miny = Math.min(miny, (int) Math.floor(rasterGridRangeInDataSet.getMinimum(1)));
                maxx = Math.max(maxx, (int) Math.ceil(rasterGridRangeInDataSet.getMaximum(0)));
                maxy = Math.max(maxy, (int) Math.ceil(rasterGridRangeInDataSet.getMaximum(1)));
            }
            int width = maxx - minx;
            int height = maxy - miny;
            Rectangle range = new Rectangle(0, 0, width, height);
            originalGridRange = new GeneralGridEnvelope(range, 2);
        }
        return originalGridRange;
    }

    public MathTransform getRasterToModel() {
        return getRasterToModel(0, 0);
    }

    public MathTransform getRasterToModel(final int rasterIndex, final int pyramidLevel) {

        GeneralEnvelope levelEnvelope = getGridEnvelope(rasterIndex, pyramidLevel);
        GridEnvelope levelGridRange = getGridRange(rasterIndex, pyramidLevel);

        // create a raster to model transform, from this tile pixel space to the tile's geographic
        // extent
        GridToEnvelopeMapper geMapper = new GridToEnvelopeMapper(levelGridRange, levelEnvelope);
        geMapper.setPixelAnchor(PixelInCell.CELL_CORNER);

        MathTransform rasterToModel = geMapper.createTransform();
        return rasterToModel;
    }

    /** @return the originalEnvelope */
    public GeneralEnvelope getOriginalEnvelope(final PixelInCell pixelAnchor) {
        GeneralEnvelope env = null;
        if (1 == getNumRasters()) {
            env = getGridEnvelope(0, 0);
        } else {
            for (RasterInfo raster : subRasterInfo) {
                int rasterIndex = getRasterIndex(raster.getRasterId());
                GeneralEnvelope rasterEnvelope = getGridEnvelope(rasterIndex, 0);
                if (env == null) {
                    env = new GeneralEnvelope(rasterEnvelope);
                } else {
                    env.add(rasterEnvelope);
                }
            }
        }

        if (PixelInCell.CELL_CENTER.equals(pixelAnchor)) {
            double[] resolution = getResolution(0, 0);
            double deltaX = resolution[0] / 2;
            double deltaY = resolution[1] / 2;
            env.setEnvelope(
                    env.getMinimum(0) + deltaX,
                    env.getMinimum(1) + deltaY,
                    env.getMaximum(0) - deltaX,
                    env.getMaximum(1) - deltaY);
        }
        return env;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ArcSDE Raster: " + getRasterTable());
        sb.append(", Raster columns: ").append(Arrays.asList(getRasterColumns()));
        sb.append(", Num bands: ").append(getNumBands());
        sb.append(", Dimension: ").append(getImageWidth()).append("x").append(getImageHeight());
        sb.append(", Pixel type: ").append(getNativeCellType());
        sb.append(", Has Color Map: ").append(isColorMapped());
        for (int rasterIndex = 0; rasterIndex < getNumRasters(); rasterIndex++) {
            RasterInfo raster = getRasterInfo(rasterIndex);
            sb.append("\n ");
            sb.append(raster.toString());
        }
        return sb.toString();
    }

    public int getNumRasters() {
        return subRasterInfo.size();
    }

    public RasterBandInfo getBand(final int rasterIndex, final int bandIndex) {
        RasterInfo rasterInfo = getRasterInfo(rasterIndex);
        return rasterInfo.getBand(bandIndex);
    }

    public int getNumPyramidLevels(final int rasterIndex) {
        RasterInfo rasterInfo = getRasterInfo(rasterIndex);
        return rasterInfo.getNumLevels();
    }

    public GeneralEnvelope getGridEnvelope(final int rasterIndex, final int pyramidLevel) {
        PyramidLevelInfo level = getLevel(rasterIndex, pyramidLevel);
        return level.getSpatialExtent();
    }

    public GridEnvelope getGridRange(final int rasterIndex, final int pyramidLevel) {
        PyramidLevelInfo level = getLevel(rasterIndex, pyramidLevel);
        GridEnvelope levelRange = level.getGridEnvelope();
        return levelRange;
    }

    public int getNumTilesWide(int rasterIndex, int pyramidLevel) {
        PyramidLevelInfo level = getLevel(rasterIndex, pyramidLevel);
        return level.getNumTilesWide();
    }

    public int getNumTilesHigh(int rasterIndex, int pyramidLevel) {
        PyramidLevelInfo level = getLevel(rasterIndex, pyramidLevel);
        return level.getNumTilesHigh();
    }

    public int getTileWidth(final long rasterId) {
        return getTileDimension(rasterId).width;
    }

    public int getTileHeight(final long rasterId) {
        return getTileDimension(rasterId).height;
    }

    public Dimension getTileDimension(final long rasterId) {
        final int rasterIndex = getRasterIndex(rasterId);
        final RasterInfo rasterInfo = getRasterInfo(rasterIndex);
        return rasterInfo.getTileDimension();
    }

    public Dimension getTileDimension(int rasterIndex) {
        RasterInfo rasterInfo = getRasterInfo(rasterIndex);
        return rasterInfo.getTileDimension();
    }

    private PyramidLevelInfo getLevel(int rasterIndex, int pyramidLevel) {
        RasterInfo rasterInfo = getRasterInfo(rasterIndex);
        PyramidLevelInfo level = rasterInfo.getPyramidLevel(pyramidLevel);
        return level;
    }

    private RasterInfo getRasterInfo(int rasterIndex) {
        RasterInfo rasterInfo = subRasterInfo.get(rasterIndex);
        return rasterInfo;
    }

    public ImageTypeSpecifier getRenderedImageSpec(final long rasterId) {
        final int rasterIndex = getRasterIndex(rasterId);
        return getRenderedImageSpec(rasterIndex);
    }

    public ImageTypeSpecifier getRenderedImageSpec(final int rasterIndex) {
        if (!this.renderedImageSpec.containsKey(Integer.valueOf(rasterIndex))) {
            synchronized (this) {
                if (!this.renderedImageSpec.containsKey(Integer.valueOf(rasterIndex))) {
                    ImageTypeSpecifier imageTypeSpecifier;
                    imageTypeSpecifier =
                            RasterUtils.createFullImageTypeSpecifier(this, rasterIndex);
                    renderedImageSpec.put(Integer.valueOf(rasterIndex), imageTypeSpecifier);
                }
            }
        }
        return this.renderedImageSpec.get(Integer.valueOf(rasterIndex));
    }

    public IndexColorModel getColorMap(final int rasterIndex) {
        final RasterBandInfo bandOne = getBand(rasterIndex, 0);
        return bandOne.getColorMap();
    }

    public boolean isColorMapped() {
        RasterInfo rasterInfo = getRasterInfo(0);
        return rasterInfo.isColorMapped();
    }

    public RasterCellType getNativeCellType() {
        RasterInfo rasterInfo = getRasterInfo(0);
        return rasterInfo.getNativeCellType();
    }

    public RasterCellType getTargetCellType(final int rasterIndex) {
        RasterInfo rasterInfo = getRasterInfo(rasterIndex);
        return rasterInfo.getTargetCellType();
    }

    public RasterCellType getTargetCellType(final long rasterId) {
        final int rasterIndex = getRasterIndex(rasterId);
        return getTargetCellType(rasterIndex);
    }

    public Long getRasterId(final int rasterIndex) {
        final RasterInfo rasterInfo = getRasterInfo(rasterIndex);
        return rasterInfo.getRasterId();
    }

    public int getOptimalPyramidLevel(
            final int rasterIndex,
            final OverviewPolicy policy,
            final GeneralEnvelope requestedEnvelope,
            final GridEnvelope requestedDim) {

        final RasterInfo rasterInfo = getRasterInfo(rasterIndex);

        double[] requestedRes = new double[2];
        double reqSpanX = requestedEnvelope.getSpan(0);
        double reqSpanY = requestedEnvelope.getSpan(1);
        requestedRes[0] = reqSpanX / (double) requestedDim.getSpan(0);
        requestedRes[1] = reqSpanY / (double) requestedDim.getSpan(1);

        return rasterInfo.getOptimalPyramidLevel(policy, requestedRes);
    }

    public int getRasterIndex(Long rasterId) {
        int index = -1;
        for (RasterInfo p : subRasterInfo) {
            index++;
            if (rasterId.equals(p.getRasterId())) {
                return index;
            }
        }
        throw new IllegalArgumentException("rasterId: " + rasterId);
    }

    public double[] getResolution(int rasterN, int pyramidLevel) {
        RasterInfo rasterInfo = getRasterInfo(rasterN);
        double[] resolution = rasterInfo.getResolution(pyramidLevel);
        return resolution;
    }

    public Number getNoDataValue(final long rasterId, final int bandIndex) {
        final int rasterIndex = getRasterIndex(rasterId);
        return getNoDataValue(rasterIndex, bandIndex);
    }

    public Number getNoDataValue(final int rasterIndex, final int bandIndex) {
        RasterBandInfo band = getBand(rasterIndex, bandIndex);
        Number noDataValue = band.getNoDataValue();
        return noDataValue;
    }

    /**
     * @param rasterIndex the raster for which bands to return the no data values
     * @return the list of no data values, one per band for the raster at index {@code rasterIndex}
     */
    public List<Number> getNoDataValues(final int rasterIndex) {
        return getRasterInfo(rasterIndex).getNoDataValues();
    }

    /**
     * Returns the actual ArcSDE pyramid level for the given raster and internal pyramid level, in
     * order to take into acount the fact that if the raster is registered with "skipLevelOne", we
     * don't hold any information for the arcsde pyramid level 1 of this raster.
     */
    public int getArcSDEPyramidLevel(final Long rasterId, final int pyramidLevel) {
        if (pyramidLevel == 0) {
            return 0;
        }
        RasterInfo rasterInfo = getRasterInfo(getRasterIndex(rasterId));
        boolean skipLevelOne = rasterInfo.isSkipLevelOne();
        if (skipLevelOne) {
            return pyramidLevel + 1;
        }
        return pyramidLevel;
    }
}
