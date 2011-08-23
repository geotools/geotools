/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.geotiff;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.ROIShape;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.io.ImageIOExt;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.geometry.XRectangle2D;
import org.geotools.resources.image.ImageUtilities;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * A GranuleDescriptor is an elementar piece of data image, with its own
 * overviews and everything.
 * 
 * <p>
 * This class is responsible for caching the various size of the different
 * levels of each single rasterGranuleLoader.
 * 
 * <p>
 * Right now we are making the assumption that a single rasterGranuleLoader is
 * made a by a single file with embedded overviews, either explicit or intrinsic
 * through wavelets like MrSID, ECW or JPEG2000.
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @since 2.5.5
 */
class GranuleDescriptor {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(GranuleDescriptor.class);

    /**
     * This class represent an overview level in a single rasterGranuleLoader.
     * 
     * @author Simone Giannecchini, GeoSolutions S.A.S.
     * 
     */
    class GranuleOverviewLevelDescriptor {

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

        public GranuleOverviewLevelDescriptor(final double scaleX, final double scaleY, final int width, final int height) {
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.baseToLevelTransform = new AffineTransform2D(XAffineTransform.getScaleInstance(
                    scaleX, scaleY, 0, 0));

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
            buffer.append("Description of a rasterGranuleLoader level").append("\n")
                    .append("width:\t\t").append(width).append("\n").append("height:\t\t")
                    .append(height).append("\n").append("scaleX:\t\t").append(scaleX).append("\n")
                    .append("scaleY:\t\t").append(scaleY).append("\n")
                    .append("baseToLevelTransform:\t\t").append(baseToLevelTransform.toString())
                    .append("\n").append("gridToWorldTransform:\t\t")
                    .append(gridToWorldTransform.toString()).append("\n");
            return buffer.toString();
        }

    }

    /**
     * Simple placeholder class to store the result of a Granule Loading
     * which comprises of a raster as well as a {@link ROIShape} for its footprint.
     * 
     * @author Daniele Romagnoli, GeoSolutions S.A.S.
     * 
     */
    static class GranuleLoadingResult {

        RenderedImage raster;

        AffineTransform gridToWorld;

        public AffineTransform getGridToWorld() {
            return gridToWorld;
        }

        public RenderedImage getRaster() {
            return raster;
        }

        GranuleLoadingResult(RenderedImage raster, final AffineTransform gridToWorld) {
            this.raster = raster;
            this.gridToWorld = gridToWorld;
        }
    }

    ReferencedEnvelope granuleBBOX;

    File granuleFile;

    final Map<Integer, GranuleOverviewLevelDescriptor> granuleLevels = Collections
            .synchronizedMap(new HashMap<Integer, GranuleOverviewLevelDescriptor>());

    AffineTransform baseGridToWorld;

    public GranuleDescriptor(RasterManager rasterManager, final File granuleFile) {

        this.granuleBBOX = new ReferencedEnvelope(rasterManager.spatialDomainManager.coverageBBox);
        this.granuleFile = granuleFile;
        this.baseGridToWorld=new AffineTransform((AffineTransform) rasterManager.spatialDomainManager.coverageGridToWorld2D);

        // create the base grid to world transformation
        ImageInputStream inStream = null;
        ImageReader reader = null;
        try {
            //
            // get info about the raster we have to read
            //

            // get a stream
            inStream = ImageIOExt.createImageInputStream(granuleFile);
            if (inStream == null) {
                throw new IllegalArgumentException(
                        "Unable to get an input stream for the provided file "
                                + granuleFile.toString());
            }

            // get a reader
            reader = Utils.TIFFREADERFACTORY.createReaderInstance();
            reader.setInput(inStream);

            // get selected level and base level dimensions
            final Rectangle originalDimension = ImageUtilities.getDimension(0, inStream, reader);

            // add the base level
            this.granuleLevels.put(Integer.valueOf(0), new GranuleOverviewLevelDescriptor(1, 1, originalDimension.width,originalDimension.height));

        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(e);

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (Throwable e) {
                throw new IllegalArgumentException(e);
            } finally {
                if (reader != null)
                    reader.dispose();
            }
        }
    }

    
    public GranuleDescriptor(final BoundingBox granuleBBOX, final File granuleFile,
            final MathTransform gridToWorld) {

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
            inStream = ImageIOExt.createImageInputStream(granuleFile);
            if (inStream == null) {
                throw new IllegalArgumentException(
                        "Unable to get an input stream for the provided file "
                                + granuleFile.toString());
            }

            // get a reader
            reader = Utils.TIFFREADERFACTORY.createReaderInstance();
            reader.setInput(inStream);

            // get selected level and base level dimensions
            final Rectangle originalDimension = ImageUtilities.getDimension(0, inStream, reader);

            // build the g2W for this tile, in principle we should get it
            // somehow from the tile itself or from the index, but at the moment
            // we do not have such info, hence we assume that it is a simple
            // scale and translate
            if (gridToWorld == null) {
                final GridToEnvelopeMapper geMapper = new GridToEnvelopeMapper(new GridEnvelope2D(
                        originalDimension), granuleBBOX);
                // this is the default behavior but it is nice to write it down
                // anyway
                geMapper.setPixelAnchor(PixelInCell.CELL_CENTER);
                this.baseGridToWorld = geMapper.createAffineTransform();
            } else
                this.baseGridToWorld = (AffineTransform) gridToWorld;

            // add the base level
            this.granuleLevels.put(Integer.valueOf(0), new GranuleOverviewLevelDescriptor(1, 1, originalDimension.width,originalDimension.height));

        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(e);

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (Throwable e) {
                throw new IllegalArgumentException(e);
            } finally {
                if (reader != null)
                    reader.dispose();
            }
        }
    }

    public GranuleLoadingResult loadRaster(
            final ImageReadParam readParameters, 
            final int overviewIndex,
            final ReferencedEnvelope cropBBox, 
            final MathTransform2D requestedWorldToGrid,
            final RasterLayerRequest request, 
            final Dimension tileDimension) throws IOException {

        if (LOGGER.isLoggable(java.util.logging.Level.FINE)) {
            LOGGER.fine("Loading raster data for GranuleDescriptor " + this.toString());
        }
        ImageInputStream inStream = null;
        int imageChoice = overviewIndex;
        File file = granuleFile;

        final ReferencedEnvelope bbox = new ReferencedEnvelope(granuleBBOX);
        // intersection of this tile bound with the current crop bbox
        final ReferencedEnvelope intersection = new ReferencedEnvelope(bbox.intersection(cropBBox),
                cropBBox.getCoordinateReferenceSystem());

        ImageReader reader = null;
        try {
            //
            // get info about the raster we have to read
            //
            
            // EXTERNAL Overviews management
            if (request.rasterManager.parent.extOvrImgChoice >= 0 && overviewIndex >= request.rasterManager.parent.extOvrImgChoice) {
                file = request.rasterManager.parent.ovrSource;
                inStream = request.rasterManager.parent.ovrInStreamSPI.createInputStreamInstance(file, ImageIO.getUseCache(), ImageIO.getCacheDirectory());
                imageChoice = overviewIndex - request.rasterManager.parent.extOvrImgChoice;
            } else {
                inStream = ImageIOExt.createImageInputStream(file);    
            }
            
            if (inStream == null) {
                return null;
            }

            // get a reader
            reader = Utils.TIFFREADERFACTORY.createReaderInstance();
            reader.setInput(inStream);

            // get selected level and base level dimensions
            final GranuleOverviewLevelDescriptor selectedlevel = getLevel(reader,request, imageChoice, overviewIndex);

            // now create the crop grid to world which can be used to decide
            // which source area we need to crop in the selected level taking
            // into account the scale factors imposed by the selection of this
            // level together with the base level grid to world transformation
            final MathTransform2D cropGridToWorldCorner = (MathTransform2D) ProjectiveTransform.create(selectedlevel.gridToWorldTransform);
            final MathTransform2D cropWorldToGrid = cropGridToWorldCorner.inverse();

            // computing the crop source area which leaves straight into the
            // selected level raster space, NOTICE that at the end we need to
            // take into account the fact that we might also decimate therefore
            // we cannot just use the crop grid to world but we need to correct
            // it.
            final Rectangle sourceArea = CRS.transform(cropWorldToGrid, new GeneralEnvelope(intersection)).toRectangle2D().getBounds();
            XRectangle2D.intersect(sourceArea, selectedlevel.rasterDimensions, sourceArea);
            // make sure roundings don't bother us

            // is it empty??
            if (sourceArea.isEmpty()) {
                if (LOGGER.isLoggable(java.util.logging.Level.WARNING)) {
                    LOGGER.warning("Got empty area for rasterGranuleLoader " + this.toString()
                            + " with request " + request.toString());
                }
                return null;

            } else if (LOGGER.isLoggable(java.util.logging.Level.FINE)) {
                LOGGER.fine("Loading level " + overviewIndex + " with source region " + sourceArea);
            }
            // set the source region
            // readParameters.setSourceRegion(sourceAreaWithCollar);
            readParameters.setSourceRegion(sourceArea);

            // read
            RenderedImage raster = request.getReadType().read(readParameters, imageChoice, file, selectedlevel.rasterDimensions, tileDimension);
            if (raster == null) {
                return null;
            }
            try {
                raster.getWidth();
            } catch (Throwable e) {
                if (LOGGER.isLoggable(java.util.logging.Level.FINE)) {
                    LOGGER.log(java.util.logging.Level.FINE,
                            "Unable to load raster for rasterGranuleLoader " + this.toString()
                                    + " with request " + request.toString(), e);
                }
                return null;
            }

            // use fixed source area
            sourceArea.setRect(readParameters.getSourceRegion());

            //
            // setting new coefficients to define a new affineTransformation
            // to be applied to the grid to world transformation
            // -----------------------------------------------------------------------------------
            //
            // With respect to the original envelope, the obtained planarImage
            // needs to be rescaled. The scaling factors are computed as the
            // ratio between the cropped source region sizes and the read
            // image sizes.
            //
            // place it in the mosaic using the coords created above;
            double decimationScaleX = ((1.0 * sourceArea.width) / raster.getWidth());
            double decimationScaleY = ((1.0 * sourceArea.height) / raster.getHeight());
            final AffineTransform decimationScaleTranform = XAffineTransform.getScaleInstance(decimationScaleX, decimationScaleY);

            // keep into account translation to work into the selected level
            // raster space
            final AffineTransform afterDecimationTranslateTranform = XAffineTransform.getTranslateInstance(sourceArea.x, sourceArea.y);

            // now we need to go back to the base level raster space
            final AffineTransform backToBaseLevelScaleTransform =selectedlevel.getBaseToLevelTransform();
            
            // now create the overall transform
            final AffineTransform finalRaster2Model = new AffineTransform(baseGridToWorld);
            finalRaster2Model.concatenate(CoverageUtilities.CENTER_TO_CORNER);
            
            if(!XAffineTransform.isIdentity(backToBaseLevelScaleTransform, Utils.AFFINE_IDENTITY_EPS))
                    finalRaster2Model.concatenate(backToBaseLevelScaleTransform);
            if(!XAffineTransform.isIdentity(afterDecimationTranslateTranform, Utils.AFFINE_IDENTITY_EPS))
                    finalRaster2Model.concatenate(afterDecimationTranslateTranform);
            if(!XAffineTransform.isIdentity(decimationScaleTranform, Utils.AFFINE_IDENTITY_EPS))
                    finalRaster2Model.concatenate(decimationScaleTranform);
            
            // return raster + its own transformation
            return  new GranuleLoadingResult(raster, finalRaster2Model);
            

        } catch (IllegalStateException e) {
            if (LOGGER.isLoggable(java.util.logging.Level.WARNING)) {
                LOGGER.log(java.util.logging.Level.WARNING,
                        "Unable to load raster for rasterGranuleLoader " + this.toString()
                                + " with request " + request.toString(), e);
            }
            return null;
        } catch (org.opengis.referencing.operation.NoninvertibleTransformException e) {
            if (LOGGER.isLoggable(java.util.logging.Level.WARNING)) {
                LOGGER.log(java.util.logging.Level.WARNING,
                        "Unable to load raster for rasterGranuleLoader " + this.toString()
                                + " with request " + request.toString(), e);
            }
            return null;
        } catch (TransformException e) {
            if (LOGGER.isLoggable(java.util.logging.Level.WARNING)) {
                LOGGER.log(java.util.logging.Level.WARNING,
                        "Unable to load raster for rasterGranuleLoader " + this.toString()
                                + " with request " + request.toString(), e);
            }
            return null;
        } finally {

            if (inStream != null) {
                try {
                    inStream.close();
                } catch (Throwable t) {

                }
            }
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {

                }
            }
        }

    }

    private GranuleOverviewLevelDescriptor getLevel(ImageReader reader, RasterLayerRequest request,  int imageChoice, int overviewIndex) {
        synchronized (granuleLevels) {
            if (granuleLevels.containsKey(Integer.valueOf(overviewIndex))) {
                return granuleLevels.get(Integer.valueOf(overviewIndex));
            } else {
                // load level
                // create the base grid to world transformation
                try {
                    //
                    // get info about the raster we have to read
                    //
                    // get selected level and base level dimensions
                    final Rectangle levelDimension =  new Rectangle(0, 0, reader.getWidth(imageChoice), reader.getHeight(imageChoice));

                    final GranuleOverviewLevelDescriptor baseLevel = granuleLevels.get(0);
                    final double scaleX = baseLevel.width / (1.0 * levelDimension.width);
                    final double scaleY = baseLevel.height / (1.0 * levelDimension.height);

                    // add the base level
                    final GranuleOverviewLevelDescriptor newLevel = new GranuleOverviewLevelDescriptor(scaleX, scaleY, levelDimension.width,levelDimension.height);
                    this.granuleLevels.put(Integer.valueOf(overviewIndex), newLevel);
                    return newLevel;

                } catch (IllegalStateException e) {
                    throw new IllegalArgumentException(e);

                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                } 
            }

        }
    }

    @Override
    public String toString() {
        // build a decent representation for this level
        final StringBuilder buffer = new StringBuilder();
        buffer.append("Description of a rasterGranuleLoader ").append("\n").append("BBOX:\t\t")
                .append(granuleBBOX.toString()).append("file:\t\t").append(granuleFile)
                .append("gridToWorld:\t\t").append(baseGridToWorld);
        int i = 1;
        for (final GranuleOverviewLevelDescriptor granuleOverviewLevelDescriptor : granuleLevels.values()) {
            i++;
            buffer.append("Description of level ").append(i++).append("\n")
                    .append(granuleOverviewLevelDescriptor.toString()).append("\n");
        }
        return super.toString();
    }


    public BoundingBox getGranuleBBOX() {
    	return granuleBBOX;
    }

}
