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
/*
 * NOTICE OF RELEASE TO THE PUBLIC DOMAIN
 *
 * This work was created by employees of the USDA Forest Service's
 * Fire Science Lab for internal use.  It is therefore ineligible for
 * copyright under title 17, section 105 of the United States Code.  You
 * may treat it as you would treat any public domain work: it may be used,
 * changed, copied, or redistributed, with or without permission of the
 * authors, for free or for compensation.  You may not claim exclusive
 * ownership of this code because it is already owned by everyone.  Use this
 * software entirely at your own risk.  No warranty of any kind is given.
 *
 * A copy of 17-USC-105 should have accompanied this distribution in the file
 * 17USC105.html.  If not, you may access the law via the US Government's
 * public websites:
 *   - http://www.copyright.gov/title17/92chap1.html#105
 *   - http://www.gpoaccess.gov/uscode/  (enter "17USC105" in the search box.)
 */
package org.geotools.gce.geotiff;


import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataDecoder;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffMetadata2CRSAdapter;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.image.io.ImageIOExt;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.util.NumberRange;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * this class is responsible for exposing the data and the Georeferencing
 * metadata available to the Geotools library. This reader is heavily based on
 * the capabilities provided by the ImageIO tools and JAI libraries.
 * 
 * 
 * @author Bryce Nordgren, USDA Forest Service
 * @author Simone Giannecchini
 * @since 2.1
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/geotiff_new/src/main/java/org/geotools/gce/geotiff/GeoTiffReader.java $
 */
public class GeoTiffReader extends AbstractGridCoverage2DReader implements
		GridCoverageReader {
    /** Logger for the {@link GeoTiffReader} class. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeoTiffReader.class.toString());

    /**
     * Number of coverages for this reader is
     * 
     * @return the number of coverages for this reader.
     */
    @Override
    public int getGridCoverageCount() {
        return 1;
    }

    /** Adapter for the GeoTiff crs. */
    private GeoTiffMetadata2CRSAdapter gtcs;

    private double noData = Double.NaN;

    private RasterManager rasterManager;

    URL sourceURL;

    boolean expandMe;

    RasterLayout[] overViewLayouts;

    RasterLayout hrLayout;

    ImageTypeSpecifier baseImageType;
    
    File ovrSource;

    ImageInputStreamSpi ovrInStreamSPI = null;

    int extOvrImgChoice = -1;

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
    public GeoTiffReader(Object input) throws DataSourceException {
        this(input, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));

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
    public GeoTiffReader(Object input, Hints uHints) throws DataSourceException {
        super(input, uHints);

        //
        // Forcing longitude first since the geotiff specification seems to
        // assume that we have first longitude the latitude.
        //
        if (uHints != null) {
            // prevent the use from reordering axes
            this.hints.remove(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
            this.hints.add(new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));

        }

        //
        // Set the source being careful in case it is an URL pointing to a file
        //
        try {
            // setting source
            if (input instanceof URL) {
                final URL sourceURL = (URL) input;
                source = DataUtilities.urlToFile(sourceURL);
            }

            closeMe = true;
            // /////////////////////////////////////////////////////////////////////
            //
            // Get a stream in order to read from it for getting the basic
            // information for this coverage
            //
            // /////////////////////////////////////////////////////////////////////
            if ((source instanceof InputStream) || (source instanceof ImageInputStream)) {
                closeMe = false;
            }
            if (source instanceof ImageInputStream) {
                inStream = (ImageInputStream) source;
            } else {
                inStream = ImageIO.createImageInputStream(source);
            }
            if (inStream == null) {
                throw new IllegalArgumentException("No input stream for the provided source");
            }

            this.sourceURL = Utils.checkSource(source);
            // /////////////////////////////////////////////////////////////////////
            //
            // Informations about multiple levels and such
            //
            // /////////////////////////////////////////////////////////////////////
            checkForExternalOverviews();
            getHRInfo(this.hints);

            // /////////////////////////////////////////////////////////////////////
            //
            // Coverage name
            //
            // /////////////////////////////////////////////////////////////////////
            coverageName = source instanceof File ? ((File) source).getName() : "geotiff_coverage";
            final int dotIndex = coverageName.lastIndexOf('.');
            if (dotIndex != -1 && dotIndex != coverageName.length()) {
                coverageName = coverageName.substring(0, dotIndex);
            }

            // /////////////////////////////////////////////////////////////////////
            //
            // Freeing streams
            //
            // /////////////////////////////////////////////////////////////////////
            if (closeMe) {
                inStream.close();
            }
        } catch (IOException e) {
            throw new DataSourceException(e);
        } catch (TransformException e) {
            throw new DataSourceException(e);
        } catch (FactoryException e) {
            throw new DataSourceException(e);
        }

        rasterManager = new RasterManager(this);
    }

    private void checkForExternalOverviews() {
        if (!(source instanceof File)) {
            return;
        }
        File src = (File) source;
        ovrSource = new File(src.getParent(), src.getName() + ".ovr");
        if (!ovrSource.exists()) {
            return;
        }
        ovrInStreamSPI = ImageIOExt.getImageInputStreamSPI(ovrSource);
    }

    /**
     * 
     * @param hints
     * @throws IOException
     * @throws FactoryException
     * @throws TransformException
     * @throws MismatchedDimensionException
     * @throws DataSourceException
     */
    private void getHRInfo(Hints hints) throws IOException, FactoryException, TransformException, 
        MismatchedDimensionException, DataSourceException {
        // //
        //
        // Get a reader for this format
        //
        // //
        final ImageReader reader = Utils.TIFFREADERFACTORY.createReaderInstance();
        ImageReader ovrReader = null;
        ImageInputStream ovrStream = null;
        try {
            // //
            //
            // get the METADATA
            //
            // //
            reader.setInput(inStream);
    
            final IIOMetadata iioMetadata = reader.getImageMetadata(0);
            CoordinateReferenceSystem foundCrs = null;
            boolean useWorldFile = false;
            GeoTiffIIOMetadataDecoder metadata = null; 

            try {
                metadata = new GeoTiffIIOMetadataDecoder(iioMetadata);
                gtcs = new GeoTiffMetadata2CRSAdapter(hints);
                if (gtcs != null) {
                    foundCrs = gtcs.createCoordinateSystem(metadata);
                } else {
                    useWorldFile = true;
                }
    
                if (metadata.hasNoData()) {
                    noData = metadata.getNoData();
                }
            } catch (IllegalArgumentException iae) {
                useWorldFile = true;
            } catch (UnsupportedOperationException uoe) {
                useWorldFile = true;
            }

            // //
            //
            // get the CRS INFO
            //
            // //
            final Object tempCRS = this.hints.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
            if (tempCRS != null) {
                this.crs = (CoordinateReferenceSystem) tempCRS;
                LOGGER.log(Level.WARNING, "Using forced coordinate reference system " + crs.toWKT());
            } else {
                if (useWorldFile) {
                    foundCrs = Utils.getCRS(source);
                }
                crs = foundCrs;
            }
    
            if (crs == null) {
                throw new DataSourceException("Coordinate Reference System is not available");
            }
    
            // //
            //
            // get the dimension of the hr image and build the model as well as
            // computing the resolution
            // //
            numOverviews = reader.getNumImages(true) - 1;
            final int hrWidth = reader.getWidth(0);
            final int hrHeight = reader.getHeight(0);
            final int hrTileW = reader.getTileWidth(0);
            final int hrTileH = reader.getTileHeight(0);
            hrLayout = new RasterLayout(0, 0, hrWidth, hrHeight, 0, 0, hrTileW, hrTileH);
            final Rectangle actualDim = new Rectangle(0, 0, hrWidth, hrHeight);
            originalGridRange = new GridEnvelope2D(actualDim);
    
            if (!useWorldFile && gtcs != null) {
                this.raster2Model = GeoTiffMetadata2CRSAdapter.getRasterToModel(metadata);
            } else {
                this.raster2Model = Utils.parseWorldFile(source);
            }
    
            if (this.raster2Model == null) {
                throw new DataSourceException("Raster to Model Transformation is not available");
            }
    
            final AffineTransform tempTransform = new AffineTransform((AffineTransform) raster2Model);
            tempTransform.translate(-0.5, -0.5);
            originalEnvelope = CRS.transform(ProjectiveTransform
                    .create(tempTransform), new GeneralEnvelope(actualDim));
            originalEnvelope.setCoordinateReferenceSystem(crs);
    
            // ///
            //
            // setting the higher resolution available for this coverage
            //
            // ///
            highestRes = new double[2];
            highestRes[0] = XAffineTransform.getScaleX0(tempTransform);
            highestRes[1] = XAffineTransform.getScaleY0(tempTransform);
    
            if (ovrInStreamSPI != null) {
                ovrReader = Utils.TIFFREADERFACTORY.createReaderInstance();
                ovrStream = ovrInStreamSPI.createInputStreamInstance(ovrSource,
                        ImageIO.getUseCache(), ImageIO.getCacheDirectory());
                ovrReader.setInput(ovrStream);
                // this includes the real image as this is a image index, we need to add one.
                extOvrImgChoice = numOverviews + 1;
                numOverviews = numOverviews + ovrReader.getNumImages(true);
                if (numOverviews < extOvrImgChoice)
                    extOvrImgChoice = -1;
            }
            
            // //
            //
            // get information for the successive images
            //
            // //
    
            if (numOverviews >= 1) {
                overViewResolutions = new double[numOverviews][2];
                overViewLayouts = new RasterLayout[numOverviews];
                // Internal overviews start at 1, so lastInternalOverview matches numOverviews if no
                // external.
                int firstExternalOverview = extOvrImgChoice == -1 ? numOverviews : extOvrImgChoice - 1;
                double spanRes0 = highestRes[0] * this.originalGridRange.getSpan(0);
                double spanRes1 = highestRes[1] * this.originalGridRange.getSpan(1);
                for (int i = 0; i < firstExternalOverview; i++) {
                    final int w = reader.getWidth(i + 1);
                    final int h = reader.getHeight(i + 1);
                    final int tw = reader.getTileWidth(i + 1);
                    final int th = reader.getTileHeight(i + 1);
                    overViewResolutions[i][0] = spanRes0 / w; 
                    overViewResolutions[i][1] = spanRes1 / h;
                    overViewLayouts[i] = new RasterLayout(0, 0, w, h, 0, 0, tw, th);
                }
                for (int i = firstExternalOverview; i < numOverviews; i++) {
                    final int w = ovrReader.getWidth(i - firstExternalOverview);
                    final int h = ovrReader.getHeight(i - firstExternalOverview);
                    final int tw = ovrReader.getTileWidth(i - firstExternalOverview);
                    final int th = ovrReader.getTileHeight(i - firstExternalOverview);
                    overViewResolutions[i][0] = spanRes0 / w;
                    overViewResolutions[i][1] = spanRes1 / h;
                    overViewLayouts[i] = new RasterLayout(0, 0, w, h, 0, 0, tw, th);
                }
               
            } else {
                overViewResolutions = null;
            }
    
            // get sample image
            final ImageReadParam readParam = reader.getDefaultReadParam();
            readParam.setSourceRegion(new Rectangle(0, 0, 4, 4));
            final BufferedImage sampleImage = reader.read(0, readParam);
            baseImageType = new ImageTypeSpecifier(sampleImage);
            reader.dispose();
        } catch (Throwable e) {
            throw new DataSourceException(e);
        } finally {
            if (reader != null)
                try {
                    reader.dispose();
                } catch (Throwable t) {
                }
                
            if (ovrReader != null)
                try {
                    ovrReader.dispose();
                } catch (Throwable t) {
                }

            if (ovrStream != null)
                try {
                    ovrStream.close();
                } catch (Throwable t) {
                }

            if (inStream != null)
                try {
                    inStream.reset();
                } catch (Throwable t) {
                }

        }

    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
     */
    public Format getFormat() {
        return new GeoTiffFormat();
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#read(org.opengis.parameter.GeneralParameterValue[])
     */
    @Override
    public GridCoverage2D read(GeneralParameterValue[] params)
            throws IOException {

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Reading image from " + sourceURL.toString() + "\n"
                    + "Highest res " + highestRes[0] + " " + highestRes[1]);
        }

        final Collection<GridCoverage2D> response = rasterManager.read(params);
        if (response.isEmpty()) {
            throw new DataSourceException("Unable to create a coverage for this request ");
        } else {
            return response.iterator().next();
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
     *            contains the data for the coverage to create.
     * @param raster2Model
     *            is the {@link MathTransform} that maps from the raster space
     *            to the model space.
     * @return a {@link GridCoverage}
     * @throws IOException
     */
    protected final GridCoverage createCoverage(
            PlanarImage image, MathTransform raster2Model) throws IOException {

        // creating bands
        final SampleModel sm = image.getSampleModel();
        final ColorModel cm = image.getColorModel();
        final int numBands = sm.getNumBands();
        final GridSampleDimension[] bands = new GridSampleDimension[numBands];
        // setting bands names.

        Category noDataCategory = null;
        if (!Double.isNaN(noData)) {
            noDataCategory = new Category(
                    Vocabulary.formatInternational(VocabularyKeys.NODATA),
                    new Color[] { new Color(0, 0, 0, 0) }, NumberRange.create(
                            noData, noData), NumberRange.create(noData, noData));
        }

        for (int i = 0; i < numBands; i++) {
            final ColorInterpretation colorInterpretation = TypeMap.getColorInterpretation(cm, i);
            if (colorInterpretation == null) {
                throw new IOException("Unrecognized sample dimension type");
            }
            Category[] categories = null;
            if (noDataCategory != null) {
                categories = new Category[] { noDataCategory };
            }
            bands[i] = new GridSampleDimension(colorInterpretation.name(), categories, null)
                .geophysics(true);
        }
        // creating coverage
        if (raster2Model != null) {
            return coverageFactory.create(coverageName, image, crs, raster2Model, 
                    bands, null, null);
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
