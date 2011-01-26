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
package org.geotools.coverageio;

import it.geosolutions.imageio.stream.input.FileImageInputStreamExtImpl;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * A RasterLayerResponse. An instance of this class is produced everytime a
 * requestCoverage is called to a reader.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 */
@SuppressWarnings("deprecation")
class RasterLayerResponse {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.coverageio");

    /**
     * The GridCoverage produced after a {@link #compute()} method call
     */
    private GridCoverage gridCoverage;

    /** The {@link RasterLayerRequest} originating this response */
    private RasterLayerRequest originatingCoverageRequest;

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

    /**
     * Construct a {@code RasterLayerResponse} given a specific
     * {@link RasterLayerRequest}, a {@code GridCoverageFactory} to produce
     * {@code GridCoverage}s and an {@code ImageReaderSpi} to be used for
     * instantiating an Image Reader for a read operation,
     * 
     * @param request
     *                a {@link RasterLayerRequest} originating this response.
     * @param coverageFactory
     *                a {@code GridCoverageFactory} to produce a
     *                {@code GridCoverage} when calling the {@link #compute()}
     *                method.
     * @param readerSpi
     *                the Image Reader Service provider interface.
     */
    public RasterLayerResponse(RasterLayerRequest request,
            GridCoverageFactory coverageFactory, ImageReaderSpi readerSpi) {
        originatingCoverageRequest = request;
        hints = request.getHints();
        coverageEnvelope = request.getCoverageEnvelope();
        coverageCRS = request.getCoverageCRS();
        raster2Model = request.getRaster2Model();
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
     * @return the {@link RasterLayerRequest} originating this response.
     * 
     * @uml.property name="originatingCoverageRequest"
     */
    public RasterLayerRequest getOriginatingCoverageRequest() {
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
        if (originatingCoverageRequest.isEmptyRequest())
        	//something bad happened
            gridCoverage = null;
        else {
            final ImageReadParam imageReadParam = originatingCoverageRequest.getImageReadParam();
            final File input = originatingCoverageRequest.getInput();
            final boolean useMultithreading = originatingCoverageRequest.useMultithreading();
            final boolean newTransform = originatingCoverageRequest.isAdjustGridToWorldSet();
            final boolean useJAI = originatingCoverageRequest.useJAI();
            gridCoverage = createCoverage(input, imageReadParam, useJAI,useMultithreading, newTransform);
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
            final boolean useMultithreading, final boolean adjustGridToWorld)
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
        if (adjustGridToWorld) {
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
        final SampleModel sm=image.getSampleModel();
        final ColorModel cm=image.getColorModel();
		final int numBands = sm.getNumBands();
		final GridSampleDimension[] bands = new GridSampleDimension[numBands];
		// setting bands names.
		for (int i = 0; i < numBands; i++) {
		        final ColorInterpretation colorInterpretation=TypeMap.getColorInterpretation(cm, i);
		        if(colorInterpretation==null)
		               throw new IOException("Unrecognized sample dimension type");
			bands[i] = new GridSampleDimension(colorInterpretation.name()).geophysics(true);
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
    protected GridCoverage createCoverageFromImage(PlanarImage image)
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
        if (useJAI) {
            final ParameterBlock pbjImageRead = new ParameterBlock();
            pbjImageRead.add(new FileImageInputStreamExtImpl(input));
            pbjImageRead.add(0);
            pbjImageRead.add(Boolean.FALSE);
            pbjImageRead.add(Boolean.FALSE);
            pbjImageRead.add(Boolean.FALSE);
            pbjImageRead.add(null);
            pbjImageRead.add(null);
            pbjImageRead.add(imageReadParam);
            pbjImageRead.add(readerSpi.createReaderInstance());

            // Check if to use a simple JAI ImageRead operation or a
            // multithreaded one
            final String jaiOperation = useMultithreading ? GridCoverageUtilities.IMAGEREADMT: GridCoverageUtilities.IMAGEREAD;
            raster = JAI.create(jaiOperation, pbjImageRead, hints);
        } else {
            final ImageReader reader = readerSpi.createReaderInstance();
            try {
                reader.setInput(new FileImageInputStreamExtImpl(input), true, true);
                raster = PlanarImage.wrapRenderedImage(reader.read(0,imageReadParam));
            }
            finally {
            	if(reader!=null)
            		try {
            			reader.dispose();
            		}catch (Exception e) {
						if(LOGGER.isLoggable(Level.FINE))
							LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
					}
            }

            
        }
        return raster;
    }
}
