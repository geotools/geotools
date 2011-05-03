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
package org.geotools.gce.imagecollection;

import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.operation.MathTransform;

/**
 *
 */
public final class ImageCollectionReader extends AbstractGridCoverage2DReader implements GridCoverageReader {
    /** Logger for the {@link ImageCollectionReader} class. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageCollectionReader.class.toString());

    /**
     * Number of coverages for this reader is
     * 
     * @return the number of coverages for this reader.
     */
    @Override
    public int getGridCoverageCount() {
        return 1;
    }

    private RasterManager rasterManager;

    URL sourceURL;
    
    String parentPath;

//    RasterLayout[] overViewLayouts;

//    RasterLayout hrLayout;

    boolean expandMe;
    
    @Override
    public void dispose() {
        super.dispose();
        rasterManager.dispose();
    }

    /**
     * Let us retrieve the {@link GridCoverageFactory} that we want to use.
     * 
     * @return retrieves the {@link GridCoverageFactory} that we want to use.
     */
    GridCoverageFactory getGridCoverageFactory() {
        return coverageFactory;
    }

    /**
     * Creates a new instance of GeoTiffReader
     * 
     * @param input
     *            the GeoTiff file
     * @throws DataSourceException
     */
    public ImageCollectionReader(Object input) throws DataSourceException {
        this(input, null);

    }

    /**
     * Creates a new instance of GeoTiffReader
     * 
     * @param input
     *            the GeoTiff file
     * @param uHints
     *            user-supplied hints TODO currently are unused
     * @throws DataSourceException
     */
    public ImageCollectionReader(Object input, Hints uHints)
            throws DataSourceException {
        super(input, uHints);

//        //
//        // Forcing longitude first 
//        //
//        if (uHints != null) {
//            // prevent the use from reordering axes
//            this.hints.remove(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
//            this.hints.add(new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER,
//                    Boolean.TRUE));
//
//        }

        //
        // Set the source being careful in case it is an URL pointing to a file
        //
        File file = null;
        try {
//            // /////////////////////////////////////////////////////////////////////
//            //
//            // Get a stream in order to read from it for getting the basic
//            // information for this coverage
//            //
//            // /////////////////////////////////////////////////////////////////////
//            if ((source instanceof InputStream)
//                    || (source instanceof ImageInputStream))
//                closeMe = false;
//            if (source instanceof ImageInputStream)
//                inStream = (ImageInputStream) source;
//            else
//                inStream = ImageIO.createImageInputStream(source);
//            if (inStream == null)
//                throw new IllegalArgumentException(
//                        "No input stream for the provided source");

            this.sourceURL = Utils.checkSource(source);
            source = DataUtilities.urlToFile(sourceURL);
            if (source instanceof File){
                file = (File) source;
                parentPath = FilenameUtils.getFullPath(FilenameUtils.normalizeNoEndSeparator(file.getAbsolutePath())+Utils.SEPARATOR);
            }
            
            // /////////////////////////////////////////////////////////////////////
            //
            // Informations about multiple levels and such
            //
            // /////////////////////////////////////////////////////////////////////
//            getHRInfo(this.hints);
            originalGridRange = new GridEnvelope2D(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
            originalEnvelope = new GeneralEnvelope(new Rectangle2D.Double(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE));
            originalEnvelope.setCoordinateReferenceSystem(Utils.DEFAULT_IMAGE_CRS);
            crs = Utils.DEFAULT_IMAGE_CRS;
            
            // /////////////////////////////////////////////////////////////////////
            //
            // Coverage name
            //
            // /////////////////////////////////////////////////////////////////////
            //TODO: FIXME
            highestRes = new double[]{1.0, 1.0};
            coverageName = "sample_coverage"; //Take some naming from folders id
//            hrLayout = new RasterLayout(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
            
        } catch (IOException e) {
            throw new DataSourceException(e);
        }

        rasterManager = new RasterManager(this);
    }


    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
     */
    public Format getFormat() {
        return new ImageCollectionFormat();
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#read(org.opengis.parameter.GeneralParameterValue[])
     */
    @Override
    public GridCoverage2D read(GeneralParameterValue[] params)
            throws IOException {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Reading image from " + sourceURL.toString());
            LOGGER.fine(new StringBuffer("Highest res ").append(highestRes[0])
                    .append(" ").append(highestRes[1]).toString());
        }

        final Collection<GridCoverage2D> response = rasterManager.read(params);
        if (response.isEmpty())
            throw new DataSourceException("Unable to create a coverage for this request ");
        else
            return response.iterator().next();
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
     *            contains the data for the coverage to create.
     * @param raster2Model
     *            is the {@link MathTransform} that maps from the raster space
     *            to the model space.
     * @return a {@link GridCoverage}
     * @throws IOException
     */
    protected final GridCoverage createCoverage(PlanarImage image,
            MathTransform raster2Model) throws IOException {

        // creating bands
        final SampleModel sm = image.getSampleModel();
        final ColorModel cm = image.getColorModel();
        final int numBands = sm.getNumBands();
        final GridSampleDimension[] bands = new GridSampleDimension[numBands];
        // setting bands names.

        for (int i = 0; i < numBands; i++) {
            final ColorInterpretation colorInterpretation = TypeMap.getColorInterpretation(cm, i);
            if (colorInterpretation == null)
                throw new IOException("Unrecognized sample dimension type");
            Category[] categories = null;
            bands[i] = new GridSampleDimension(colorInterpretation.name(),
                    categories, null).geophysics(true);
        }
        // creating coverage
        if (raster2Model != null) {
            return coverageFactory.create(coverageName, image, crs,
                    raster2Model, bands, null, null);
        }
        return coverageFactory.create(coverageName, image, new GeneralEnvelope(
                originalEnvelope), bands, null, null);

    }

    /**
     * Package private accessor for {@link Hints}.
     * 
     * @return this {@link Hints} used by this reader.
     */
    Hints getHints() {
        return super.hints;
    }

    /**
     * Package private accessor for the highest resolution values.
     * 
     * @return the highest resolution values.
     */
    double[] getHighestRes() {
        return super.highestRes;
    }

    /**
     * 
     * @return
     */
    double[][] getOverviewsResolution() {
        return super.overViewResolutions;
    }

    int getNumberOfOverviews() {
        return super.numOverviews;
    }

    String getName() {
        return super.coverageName;
    }

}
