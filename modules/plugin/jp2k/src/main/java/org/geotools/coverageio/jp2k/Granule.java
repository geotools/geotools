/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverageio.jp2k;

import it.geosolutions.imageio.utilities.ImageIOUtilities;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.ImageLayout;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.PixelTranslation;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.util.XRectangle2D;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ImageUtilities;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.ProjectiveTransform;

/**
 * A granule is an elementar piece of data image, with its own overviews and everything.
 *
 * <p>This class is responsible for caching the various size of the different levels of each single granule.
 *
 * <p>Right now we are making the assumption that a single granule is made by a single file with embedded overviews,
 * either explicit or intrinsic through wavelets like MrSID, ECW or JPEG2000.
 *
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @author Daniele Romagnoli, GeoSolutions S.A.S.
 */
class Granule {

    static final double EPS = 10E-6;

    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(Granule.class);

    /**
     * This class represent an overview level in a single granule.
     *
     * @author Simone Giannecchini, GeoSolutions S.A.S.
     */
    class Level {

        final double scaleX;

        final double scaleY;

        final int width;

        final int height;

        final AffineTransform2D baseToLevelTransform;

        final AffineTransform2D gridToWorldTransform;

        final Rectangle rasterDimensions;

        public AffineTransform getBaseToLevelTransform() {
            return baseToLevelTransform;
        }

        public double getScaleX() {
            return scaleX;
        }

        public double getScaleY() {
            return scaleY;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public Level(final double scaleX, final double scaleY, final int width, final int height) {
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.baseToLevelTransform = new AffineTransform2D(XAffineTransform.getScaleInstance(scaleX, scaleY, 0, 0));

            final AffineTransform gridToWorldTransform_ = new AffineTransform(baseToLevelTransform);
            gridToWorldTransform_.preConcatenate(CoverageUtilities.CENTER_TO_CORNER);
            gridToWorldTransform_.preConcatenate(baseGridToWorld);
            this.gridToWorldTransform = new AffineTransform2D(gridToWorldTransform_);
            this.width = width;
            this.height = height;
            this.rasterDimensions = new Rectangle(0, 0, width, height);
        }

        public Rectangle getBounds() {
            return (Rectangle) rasterDimensions.clone();
        }

        public AffineTransform2D getGridToWorldTransform() {
            return gridToWorldTransform;
        }

        @Override
        public String toString() {
            // build a decent representation for this level
            final StringBuilder buffer = new StringBuilder();
            buffer.append("Description of a granule level").append("\n");
            buffer.append("width:\t\t").append(width).append("\n");
            buffer.append("height:\t\t").append(height).append("\n");
            buffer.append("scaleX:\t\t").append(scaleX).append("\n");
            buffer.append("scaleY:\t\t").append(scaleY).append("\n");
            buffer.append("baseToLevelTransform:\t\t")
                    .append(baseToLevelTransform.toString())
                    .append("\n");
            buffer.append("gridToWorldTransform:\t\t")
                    .append(gridToWorldTransform.toString())
                    .append("\n");
            return buffer.toString();
        }
    }

    ReferencedEnvelope granuleBBOX;

    File granuleFile;

    final Map<Integer, Level> granuleLevels = Collections.synchronizedMap(new HashMap<>());

    AffineTransform baseGridToWorld;

    ImageReaderSpi cachedSPI;

    @SuppressWarnings("PMD.UseTryWithResources") // the image input stream might be null
    public Granule(BoundingBox granuleBBOX, File granuleFile) {
        super();
        this.granuleBBOX = ReferencedEnvelope.reference(granuleBBOX);
        this.granuleFile = granuleFile;

        // create the base grid to world transformation
        ImageInputStream inStream = null;
        ImageReader reader = null;
        try {
            //
            // get info about the raster we have to read
            //

            // get a stream
            inStream = Utils.getInputStream(granuleFile);
            if (inStream == null)
                throw new IllegalArgumentException(
                        "Unable to get an input stream for the provided file " + granuleFile.getAbsolutePath());

            // get a reader and try to cache the relevant SPI
            if (cachedSPI == null) {
                reader = Utils.getReader(inStream);
                if (reader != null) cachedSPI = reader.getOriginatingProvider();
            } else reader = cachedSPI.createReaderInstance();
            if (reader == null)
                throw new IllegalArgumentException(
                        "Unable to get an ImageReader for the provided file " + granuleFile.getAbsolutePath());

            // get selected level and base level dimensions
            final Rectangle originalDimension = ImageUtilities.getDimension(0, inStream, reader);

            // build the g2W for this tile, in principle we should get it
            // somehow from the tile itself or from the index, but at the moment
            // we do not have such info, hence we assume that it is a simple
            // scale and translate
            final GridToEnvelopeMapper geMapper =
                    new GridToEnvelopeMapper(new GridEnvelope2D(originalDimension), granuleBBOX);
            geMapper.setPixelAnchor(PixelInCell.CELL_CENTER); // this is the default behavior but it is nice to write
            // it down anyway
            this.baseGridToWorld = geMapper.createAffineTransform();

            // add the base level
            this.granuleLevels.put(
                    Integer.valueOf(0), new Level(1, 1, originalDimension.width, originalDimension.height));

        } catch (IllegalStateException | IOException e) {
            throw new IllegalArgumentException(e);

        } finally {
            try {
                if (inStream != null) inStream.close();
            } catch (Throwable e) {
                throw new IllegalArgumentException(e);
            } finally {
                if (reader != null) reader.dispose();
            }
        }
    }

    @SuppressWarnings("PMD.UseTryWithResources") // the image input stream might be null
    public RenderedImage loadRaster(
            final ImageReadParam readParameters,
            final int imageIndex,
            final ReferencedEnvelope cropBBox,
            final MathTransform2D worldToGrid,
            final RasterLayerRequest request,
            final Dimension tileDimension)
            throws IOException {

        if (LOGGER.isLoggable(java.util.logging.Level.FINE))
            LOGGER.fine("Loading raster data for granule " + this.toString());

        final ReferencedEnvelope bbox = new ReferencedEnvelope(granuleBBOX);
        // intersection of this tile bound with the current crop bbox
        final ReferencedEnvelope intersection =
                new ReferencedEnvelope(bbox.intersection(cropBBox), cropBBox.getCoordinateReferenceSystem());

        ImageInputStream inStream = null;
        ImageReader reader = null;
        try {
            //
            // get info about the raster we have to read
            //

            // get a stream
            inStream = Utils.getInputStream(granuleFile);
            if (inStream == null) return null;

            // get a reader and try to cache the relevant SPI
            if (cachedSPI == null) {
                reader = Utils.getReader(inStream);
                if (reader != null) cachedSPI = reader.getOriginatingProvider();
            } else reader = cachedSPI.createReaderInstance();
            if (reader == null) {
                if (LOGGER.isLoggable(java.util.logging.Level.WARNING))
                    LOGGER.warning("Unable to get reader for granule "
                            + this.toString()
                            + " with request "
                            + request.toString());
                return null;
            }

            // get selected level and base level dimensions
            final Level selectedlevel = getLevel(imageIndex);

            // now create the crop grid to world which can be used to decide
            // which source area we need to crop in the selected level taking
            // into account the scale factors imposed by the selection of this
            // level together with the base level grid to world transformation
            MathTransform2D cropWorldToGrid = (MathTransform2D) PixelTranslation.translate(
                            ProjectiveTransform.create(selectedlevel.gridToWorldTransform),
                            PixelInCell.CELL_CENTER,
                            PixelInCell.CELL_CORNER)
                    .inverse();

            // computing the crop source area which leaves straight into the
            // selected level raster space, NOTICE that at the end we need to
            // take into account the fact that we might also decimate therefore
            // we cannot just use the crop grid to world but we need to correct
            // it.
            final Rectangle sourceArea = CRS.transform(cropWorldToGrid, new GeneralBounds(intersection))
                    .toRectangle2D()
                    .getBounds();
            XRectangle2D.intersect(
                    sourceArea, selectedlevel.rasterDimensions, sourceArea); // make sure roundings don't bother us
            // is it empty??
            if (sourceArea.isEmpty()) {
                if (LOGGER.isLoggable(java.util.logging.Level.FINE))
                    LOGGER.fine(
                            "Got empty area for granule " + this.toString() + " with request " + request.toString());
                return null;

            } else if (LOGGER.isLoggable(java.util.logging.Level.FINE))
                LOGGER.fine(new StringBuffer("Loading level ")
                        .append(imageIndex)
                        .append(" with source region ")
                        .append(sourceArea)
                        .toString());
            final int ssx = readParameters.getSourceXSubsampling();
            final int ssy = readParameters.getSourceYSubsampling();
            final int newSubSamplingFactor = ImageIOUtilities.getSubSamplingFactor2(ssx, ssy);
            if (newSubSamplingFactor != 0) {
                readParameters.setSourceSubsampling(newSubSamplingFactor, newSubSamplingFactor, 0, 0);
            }

            // set the source region
            readParameters.setSourceRegion(sourceArea);
            final RenderedImage raster;
            try {
                // read
                raster = request.getReadType()
                        .read(
                                readParameters,
                                imageIndex,
                                granuleFile,
                                selectedlevel.rasterDimensions,
                                tileDimension,
                                cachedSPI);
                if (raster == null) return null;
            } catch (Throwable e) {
                if (LOGGER.isLoggable(java.util.logging.Level.FINE))
                    LOGGER.log(
                            java.util.logging.Level.FINE,
                            "Unable to load raster for granule "
                                    + this.toString()
                                    + " with request "
                                    + request.toString(),
                            e);
                return null;
            }

            // use fixed source area
            sourceArea.setRect(readParameters.getSourceRegion());

            //
            // setting new coefficients to define a new affineTransformation
            // to be applied to the grid to world transformation
            // -----------------------------------------------------------------------------------
            // With respect to the original envelope, the obtained planarImage
            // needs to be rescaled. The scaling factors are computed as the
            // ratio between the cropped source region sizes and the read
            // image sizes.
            //
            // place it in the dest image using the coords created above;
            double decimationScaleX = 1.0 * sourceArea.width / raster.getWidth();
            double decimationScaleY = 1.0 * sourceArea.height / raster.getHeight();
            final AffineTransform decimationScaleTranform =
                    XAffineTransform.getScaleInstance(decimationScaleX, decimationScaleY);

            // keep into account translation  to work into the selected level raster space
            final AffineTransform afterDecimationTranslateTranform =
                    XAffineTransform.getTranslateInstance(sourceArea.x, sourceArea.y);

            // now we need to go back to the base level raster space
            final AffineTransform backToBaseLevelScaleTransform = selectedlevel.baseToLevelTransform;

            // now create the overall transform
            final AffineTransform finalRaster2Model = new AffineTransform(baseGridToWorld);
            finalRaster2Model.concatenate(CoverageUtilities.CENTER_TO_CORNER);
            if (!XAffineTransform.isIdentity(backToBaseLevelScaleTransform, EPS))
                finalRaster2Model.concatenate(backToBaseLevelScaleTransform);
            if (!XAffineTransform.isIdentity(afterDecimationTranslateTranform, EPS))
                finalRaster2Model.concatenate(afterDecimationTranslateTranform);
            if (!XAffineTransform.isIdentity(decimationScaleTranform, EPS))
                finalRaster2Model.concatenate(decimationScaleTranform);

            // keep into account translation factors to place this tile
            finalRaster2Model.preConcatenate((AffineTransform) worldToGrid);

            final InterpolationNearest nearest = new InterpolationNearest();
            // paranoiac check to avoid that JAI freaks out when computing its internal layouT on
            // images that are too small
            Rectangle2D finalLayout = ImageUtilities.layoutHelper(
                    raster,
                    (float) finalRaster2Model.getScaleX(),
                    (float) finalRaster2Model.getScaleY(),
                    (float) finalRaster2Model.getTranslateX(),
                    (float) finalRaster2Model.getTranslateY(),
                    nearest);
            if (finalLayout.isEmpty()) {
                if (LOGGER.isLoggable(java.util.logging.Level.FINE))
                    LOGGER.fine("Unable to create a granule " + this.toString() + " due to jai scale bug");
                return null;
            }

            // apply the affine transform  conserving indexed color model
            final RenderingHints localHints = new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE);
            if (XAffineTransform.isIdentity(finalRaster2Model, 10E-6)) return raster;
            else {
                //
                // In case we are asked to use certain tile dimensions we tile
                // also at this stage in case the read type is Direct since
                // buffered images comes up untiled and this can affect the
                // performances of the subsequent affine operation.
                //
                final Dimension tileDimensions = request.getTileDimensions();
                if (tileDimensions != null && request.getReadType().equals(ReadType.DIRECT_READ)) {
                    final ImageLayout layout = new ImageLayout();
                    layout.setTileHeight(tileDimensions.width).setTileWidth(tileDimensions.height);
                    localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
                }
                ImageWorker worker = new ImageWorker(raster).setRenderingHints(localHints);
                worker.affine(finalRaster2Model, nearest, null);
                return worker.getRenderedImage();
            }
        } catch (IllegalStateException | TransformException e) {
            if (LOGGER.isLoggable(java.util.logging.Level.WARNING))
                LOGGER.log(
                        java.util.logging.Level.WARNING,
                        "Unable to load raster for granule " + this.toString() + " with request " + request.toString(),
                        e);
            return null;
        } finally {
            try {
                if (inStream != null) inStream.close();
            } finally {
                if (reader != null) reader.dispose();
            }
        }
    }

    @SuppressWarnings("PMD.UseTryWithResources") // the image input stream might be null
    public Level getLevel(final int index) {
        synchronized (granuleLevels) {
            if (granuleLevels.containsKey(Integer.valueOf(index))) return granuleLevels.get(Integer.valueOf(index));
            else {
                // load level
                // create the base grid to world transformation
                ImageInputStream inStream = null;
                ImageReader reader = null;
                try {
                    //
                    // get info about the raster we have to read
                    //

                    // get a stream
                    inStream = Utils.getInputStream(granuleFile);
                    if (inStream == null) throw new IllegalArgumentException();

                    // get a reader and try to cache the relevant SPI
                    if (cachedSPI == null) {
                        reader = Utils.getReader(inStream);
                        if (reader != null) cachedSPI = reader.getOriginatingProvider();
                    } else reader = cachedSPI.createReaderInstance();
                    if (reader == null)
                        throw new IllegalArgumentException(
                                "Unable to get an ImageReader for the provided file " + granuleFile.getAbsolutePath());

                    // get selected level and base level dimensions
                    final Rectangle levelDimension = ImageUtilities.getDimension(index, inStream, reader);
                    final Level baseLevel = granuleLevels.get(0);
                    final double scaleX = baseLevel.width / (1.0 * levelDimension.width);
                    final double scaleY = baseLevel.height / (1.0 * levelDimension.height);

                    // add the base level
                    final Level newLevel = new Level(scaleX, scaleY, levelDimension.width, levelDimension.height);
                    this.granuleLevels.put(Integer.valueOf(index), newLevel);
                    return newLevel;

                } catch (IllegalStateException | IOException e) {
                    throw new IllegalArgumentException(e);

                } finally {
                    try {
                        if (inStream != null) inStream.close();
                    } catch (Throwable e) {
                        throw new IllegalArgumentException(e);
                    } finally {
                        if (reader != null) reader.dispose();
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        // build a decent representation for this level
        final StringBuilder buffer = new StringBuilder();
        buffer.append("Description of a granule ").append("\n");
        buffer.append("BBOX:\t\t").append(granuleBBOX.toString());
        buffer.append("file:\t\t").append(granuleFile);
        buffer.append("gridToWorld:\t\t").append(baseGridToWorld);
        int i = 1;
        for (final Level level : granuleLevels.values()) {
            i++;
            buffer.append("Description of level ").append(i++).append("\n");
            buffer.append(level.toString()).append("\n");
        }
        return super.toString();
    }
}
