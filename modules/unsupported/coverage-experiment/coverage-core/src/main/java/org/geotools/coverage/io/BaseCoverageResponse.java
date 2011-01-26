/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

import it.geosolutions.imageio.stream.input.FileImageInputStreamExtImpl;

import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.measure.unit.Unit;
import javax.media.jai.IHSColorSpace;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.NumberRange;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * A BaseCoverageResponse. An instance of this class is produced everytime a
 * requestCoverage is called to a reader.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 */
class BaseCoverageResponse {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.imageio");

    /**
     * The GridCoverage produced after a {@link #compute()} method call
     */
    protected GridCoverage gridCoverage;

    /** The {@link BaseCoverageRequest} originating this response */
    protected BaseCoverageRequest originatingCoverageRequest;

    /** The readerSPI to be used for data read operations */
    private ImageReaderSpi readerSpi;

    /** The coverage factory producing a {@link GridCoverage} from an image */
    private GridCoverageFactory coverageFactory;

    /** The hints to be used to produce this coverage */
    private Hints hints;

    // ////////////////////////////////////////////////////////////////////////
    //
    // Information obtained by the coverageRequest instance
    //
    // ////////////////////////////////////////////////////////////////////////
    /** The coverage grid to world transformation */
    private MathTransform raster2Model;

    /** The base envelope related to the input coverage */
    private GeneralEnvelope coverageEnvelope;

    /** The CRS of the input coverage */
    private CoordinateReferenceSystem coverageCRS;

    /** The name of the input coverage */
    private String coverageName;

    private int imageIndex;

    private double[] scalingParameters;

    private double[] validRange;

    private double[] noDataValues;

    private Unit unit = null;

    private String longName;

    /**
     * Construct a {@code BaseCoverageResponse} given a specific
     * {@link BaseCoverageRequest}, a {@code GridCoverageFactory} to produce
     * {@code GridCoverage}s and an {@code ImageReaderSpi} to be used for
     * instantiating an Image Reader for a read operation,
     * 
     * @param request
     *                a {@link BaseCoverageRequest} originating this response.
     * @param coverageFactory
     *                a {@code GridCoverageFactory} to produce a
     *                {@code GridCoverage} when calling the {@link #compute()}
     *                method.
     * @param readerSpi
     *                the Image Reader Service provider interface.
     */
    public BaseCoverageResponse(BaseCoverageRequest request,
            GridCoverageFactory coverageFactory, ImageReaderSpi readerSpi) {
        originatingCoverageRequest = request;
        hints = request.getHints();
        coverageEnvelope = request.getCoverageEnvelope();
        coverageCRS = request.getCoverageCRS();
        raster2Model = request.getRaster2Model();
        imageIndex = request.getImageIndex();
        scalingParameters = request.getScaleAndOffset();
        validRange = request.getValidRange();
        noDataValues = request.getNoDataValues();
        longName = request.getLongName();
        unit = request.getUnit();
        this.coverageName = request.getCoverageName();
        this.coverageFactory = coverageFactory;
        this.readerSpi = readerSpi;
    }

    /**
     * @return the {@link GridCoverage} produced as computation of this response
     *         using the {@link #compute()} method.
     * @uml.property name="gridCoverage"
     */
    public GridCoverage getGridCoverage() {
        return gridCoverage;
    }

    /**
     * @return the {@link BaseCoverageRequest} originating this response.
     * 
     * @uml.property name="originatingCoverageRequest"
     */
    public BaseCoverageRequest getOriginatingCoverageRequest() {
        return originatingCoverageRequest;
    }

    /**
     * Compute the coverage request and produce a grid coverage which will be
     * returned by {@link #getGridCoverage()}. The produced grid coverage may
     * be {@code null} in case of empty request.
     * 
     * @throws IOException
     */
    public void compute() throws IOException {
        originatingCoverageRequest.prepare();
        boolean isEmptyRequest = originatingCoverageRequest.isEmptyRequest();

        if (isEmptyRequest)
            gridCoverage = null;
        else {
            ImageReadParam imageReadParam = originatingCoverageRequest
                    .getImageReadParam();
            File input = originatingCoverageRequest.getInput();
            boolean useMultithreading = originatingCoverageRequest
                    .useMultithreading();
            boolean newTransform = originatingCoverageRequest
                    .needTransformation();
            boolean useJAI = originatingCoverageRequest.useJAI();
            gridCoverage = createCoverage(input, imageReadParam, useJAI,
                    useMultithreading, newTransform);
        }
    }

    /**
     * This method creates the GridCoverage2D from the underlying file given a
     * specified envelope, and a requested dimension.
     * 
     * @param iUseJAI
     *                specify if the underlying read process should leverage on
     *                a JAI ImageRead operation or a simple direct call to the
     *                {@code read} method of a proper {@code ImageReader}.
     * @param useMultithreading
     *                specify if the underlying read process should use
     *                multithreading when a JAI ImageRead operation is requested
     * @param overviewPolicy
     *                the overview policy which need to be adopted
     * @return a {@code GridCoverage}
     * 
     * @throws java.io.IOException
     */
    private GridCoverage createCoverage(File input,
            ImageReadParam imageReadParam, final boolean useJAI,
            final boolean useMultithreading, final boolean newTransform)
            throws IOException {
        // ////////////////////////////////////////////////////////////////////
        //
        // Doing an image read for reading the coverage.
        //
        // ////////////////////////////////////////////////////////////////////
        final PlanarImage image = readRaster(input, useJAI, imageReadParam,
                useMultithreading);

        // /////////////////////////////////////////////////////////////////////
        //
        // Creating the coverage
        //
        // /////////////////////////////////////////////////////////////////////
        if (newTransform) {
            // I need to calculate a new transformation (raster2Model)
            // between the cropped image and the required envelope
            final int ssWidth = image.getWidth();
            final int ssHeight = image.getHeight();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Coverage read: width = " + ssWidth
                        + " height = " + ssHeight);
            }

            // //
            //
            // setting new coefficients to define a new affineTransformation
            // to be applied to the grid to world transformation
            // ------------------------------------------------------
            //
            // With respect to the original envelope, the obtained
            // planarImage needs to be rescaled and translated. The scaling
            // factors are computed as the ratio between the cropped source
            // region sizes and the read image sizes. The translate
            // settings are represented by the offsets of the source region.
            //
            // //
            final Rectangle sourceRegion = imageReadParam.getSourceRegion();
            final double scaleX = sourceRegion.width / (1.0 * ssWidth);
            final double scaleY = sourceRegion.height / (1.0 * ssHeight);
            final double translateX = sourceRegion.x;
            final double translateY = sourceRegion.y;
            return createCoverageFromImage(image, ConcatenatedTransform.create(
                    ProjectiveTransform.create(new AffineTransform(scaleX, 0,
                            0, scaleY, translateX, translateY)), raster2Model));
        } else {
            // In case of no transformation is required (As an instance,
            // when reading the whole image)
            return createCoverageFromImage(image);
        }
    }

    /**
     * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using
     * the {@link #raster2Model} that was provided for this coverage.
     * 
     * <p>
     * This method is vital when working with coverages that have a raster to
     * model transformation that is not a simple scale and translate.
     * 
     * @param image
     *                contains the data for the coverage to create.
     * @param raster2Model
     *                is the {@link MathTransform} that maps from the raster
     *                space to the model space.
     * @return a {@link GridCoverage}
     * @throws IOException
     */
    protected GridCoverage createCoverageFromImage(PlanarImage image,
            MathTransform raster2Model) throws IOException {
        // creating bands
        final int numBands = image.getSampleModel().getNumBands();
        final GridSampleDimension[] bands = new GridSampleDimension[numBands];

        // checking the names
        final ColorModel cm = image.getColorModel();
        final String[] names = new String[numBands];

        // in case of index color model we are already done.
        if (cm instanceof IndexColorModel) {
            names[0] = "index band";
        } else {
            // in case of multiband image we are not done yet.
            final ColorSpace cs = cm.getColorSpace();

            if (cs instanceof IHSColorSpace) {
                names[0] = "Intensity band";
                names[1] = "Hue band";
                names[2] = "Saturation band";
            } else {
                // not IHS, let's take the type
                final int type = cs.getType();

                switch (type) {
                case ColorSpace.CS_GRAY:
                case ColorSpace.TYPE_GRAY:
                    names[0] = "GRAY";

                    break;

                case ColorSpace.CS_sRGB:
                case ColorSpace.CS_LINEAR_RGB:
                case ColorSpace.TYPE_RGB:
                    names[0] = "RED";
                    names[1] = "GREEN";
                    names[2] = "BLUE";

                    break;

                case ColorSpace.TYPE_CMY:
                    names[0] = "CYAN";
                    names[1] = "MAGENTA";
                    names[2] = "YELLOW";

                    break;

                case ColorSpace.TYPE_CMYK:
                    names[0] = "CYAN";
                    names[1] = "MAGENTA";
                    names[2] = "YELLOW";
                    names[3] = "K";

                    break;
                }
            }
        }

        Unit unit = null;
        if (this.unit != null)
            unit = this.unit;

        // setting bands names.
        for (int i = 0; i < numBands; i++) {
            Category[] categories = null;
            Category values = null;
            Category nan = null;
            int cat = 0;

//            // TODO
//            // Actually, the underlying readers use fillValue as noData
//            // Is this correct?
//            if (noDataValues != null) {
//                final int size = noDataValues.length;
//                if (size == 1) {
//                    final double noData = noDataValues[0];
//                    if (!Double.isNaN(noData)) {
//                        nan = new Category(Vocabulary
//                                .formatInternational(VocabularyKeys.NODATA),
//                                new Color[] { new Color(0, 0, 0, 0) },
//                                NumberRange.create(0, 0), NumberRange.create(
//                                        noData, noData));
//                    }
//                }
//                // TODO: Handle more nodatavalues
//                cat++;
//            }
            if (validRange != null) {
                double min = validRange[0];
                double max = validRange[1];

                // TODO: Workaround to handle fillValue = valid min
                if (nan != null) {
                    if (noDataValues[0] == validRange[0])
                        min += Double.MIN_VALUE;
                }

                values = new Category("values", null, NumberRange.create(min,
                        max), scalingParameters[0], scalingParameters[1]);
                cat++;

            }
            if (cat > 0) {
                categories = new Category[cat];
                if (cat == 2) {
                    categories[0] = nan;
                    categories[1] = values;
                } else
                    categories[0] = nan == null ? values :nan ;
            }

            // categories[0] = values;
            // if (nan != null)
            // categories[1] = nan;
            //
            // final GridSampleDimension band = new
            // GridSampleDimension(longName,
            // categories, unit);
//            final GridSampleDimension band = new GridSampleDimension(longName,
//                    categories, unit);
            final GridSampleDimension band = new GridSampleDimension(names[i],
                    categories, unit);
            
            bands[i] = band;

        }

        // creating coverage
        if (raster2Model != null) {
            return coverageFactory.create(coverageName, image, coverageCRS,
                    raster2Model, bands, null, null);
        }

        return coverageFactory.create(coverageName, image, new GeneralEnvelope(
                coverageEnvelope), bands, null, null);
    }

    /**
     * Creates a {@link GridCoverage} for the provided {@link PlanarImage} using
     * the {@link #coverageEnvelope} that was provided for this coverage.
     * 
     * @param image
     *                contains the data for the coverage to create.
     * @return a {@link GridCoverage}
     * @throws IOException
     */
    protected final GridCoverage createCoverageFromImage(PlanarImage image)
            throws IOException {
        return createCoverageFromImage(image, null);
    }

    /**
     * Returns a {@code PlanarImage} given a set of parameter specifying the
     * type of read operation to be performed.
     * 
     * @param new
     *                FileImageInputStreamExtImplinput the input
     *                {@code ImageInputStream} to be used for reading the image.
     * @param useJAI
     *                {@code true} if we need to use a JAI ImageRead operation,
     *                {@code false} if we need a simple direct
     *                {@code ImageReader.read(...)} call.
     * @param imageReadParam
     *                an {@code ImageReadParam} specifying the read parameters
     * @param useMultithreading
     *                {@code true} if a JAI ImageRead operation is requested
     *                with support for multithreading. This parameter will be
     *                ignored if requesting a direct read operation.
     * @return the read {@code PlanarImage}
     * @throws IOException
     */
    protected PlanarImage readRaster(final File input, final boolean useJAI,
            final ImageReadParam imageReadParam, final boolean useMultithreading)
            throws IOException {
        PlanarImage raster;
        ImageReader reader;
        if (useJAI) {
            final ParameterBlock pbjImageRead = new ParameterBlock();
            pbjImageRead.add(new FileImageInputStreamExtImpl(input));
            pbjImageRead.add(imageIndex);
            pbjImageRead.add(Boolean.FALSE);
            pbjImageRead.add(Boolean.FALSE);
            pbjImageRead.add(Boolean.FALSE);
            pbjImageRead.add(null);
            pbjImageRead.add(null);
            pbjImageRead.add(imageReadParam);
            reader = readerSpi.createReaderInstance();
            pbjImageRead.add(reader);

            // Check if to use a simple JAI ImageRead operation or a
            // multithreaded one
            final String jaiOperation = useMultithreading ? "ImageReadMT"
                    : "ImageRead";
            raster = JAI.create(jaiOperation, pbjImageRead, hints);
        } else {
            reader = readerSpi.createReaderInstance();
            reader.setInput(new FileImageInputStreamExtImpl(input), true, true);
            raster = PlanarImage.wrapRenderedImage(reader.read(imageIndex,
                    imageReadParam));
            reader.dispose();
        }
        return raster;
    }
}
