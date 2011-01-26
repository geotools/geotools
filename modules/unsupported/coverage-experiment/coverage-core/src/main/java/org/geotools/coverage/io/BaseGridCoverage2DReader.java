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

import it.geosolutions.imageio.core.CoreCommonImageMetadata;
import it.geosolutions.imageio.imageioimpl.imagereadmt.ImageReadDescriptorMT;
import it.geosolutions.imageio.ndplugin.BaseImageMetadata;
import it.geosolutions.imageio.stream.input.FileImageInputStreamExt;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.measure.unit.Unit;
import javax.media.jai.JAI;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.io.util.Utilities;
import org.geotools.data.DataSourceException;
import org.geotools.data.DefaultResourceInfo;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.ResourceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.SpatioTemporalImageReader;
import org.geotools.resources.coverage.CoverageUtilities;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridRange;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Base class for GridCoverage data access
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 * 
 */
@SuppressWarnings("deprecation")
public abstract class BaseGridCoverage2DReader extends
        AbstractGridCoverage2DReader implements GridCoverageReader {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(BaseGridCoverage2DReader.class.toString());

    /** registering ImageReadMT JAI operation (for multithreaded ImageRead) */
    static {
        ImageReadDescriptorMT.register(JAI.getDefaultInstance());
    }

    final static protected boolean INFO_DEBUG = Boolean
            .getBoolean("org.geotools.imageio.debug");

    private int imageIndex = 0;

    /** Caches an {@code ImageReaderSpi}. */
    private final ImageReaderSpi readerSPI;

    private double[] scaleAndOffset;

    private double[] validRange;

    private double[] noDataValues;

    private String longName;

    private Unit<?> unit;

    private int gridCoverageCount;

    /**
     * Implement this method to setup the coverage properties (Envelope,
     * CoordinateReferenceSystem, GridRange) using the provided
     * {@code ImageReader}
     */
    protected abstract void setCoverageProperties(ImageReader reader)
            throws IOException;

    // ////////////////////////////////////////////////////////////////////////
    //  
    // Referencing related fields (CoordinateReferenceSystem and Envelope)
    //  
    // ////////////////////////////////////////////////////////////////////////

    /** Envelope read from file */
    private GeneralEnvelope coverageEnvelope = null;

    /** The CoordinateReferenceSystem for the coverage */
    private CoordinateReferenceSystem coverageCRS = null;

    // ////////////////////////////////////////////////////////////////////////
    //  
    // Data source properties and field for management
    //  
    // ////////////////////////////////////////////////////////////////////////

    /** Source given as input to the reader */
    private File inputFile = null;

    /** Coverage name */
    private String coverageName = "geotools_coverage";

    /**
     * The base {@link GridRange} for the {@link GridCoverage2D} of this reader.
     */
    private GeneralGridEnvelope coverageGridRange = null;

    /** Absolute path to the parent dir for this coverage. */
    private String parentPath;

    /**
     * Creates a new instance of a {@link BaseGridCoverage2DReader}. I assume
     * nothing about file extension.
     * 
     * @param input
     *                Source object for which we want to build a
     *                {@link BaseGridCoverage2DReader}.
     * @param hints
     * 
     * Hints to be used by this reader throughout his life.
     * @param formatSpecificSpi
     *                an instance of a proper {@code ImageReaderSpi}.
     * @throws DataSourceException
     */
    protected BaseGridCoverage2DReader(Object input, final Hints hints,
            final ImageReaderSpi formatSpecificSpi) throws DataSourceException {

        try {
            // //
            //
            // managing hints
            //
            // //
            if (this.hints == null)
                this.hints = new Hints();

            if (hints != null)
                this.hints.add(hints);
            this.coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(this.hints);
            readerSPI = formatSpecificSpi;


            // //
            //
            // Source management
            //
            // //
            checkSource(input);
            final ImageReader reader = readerSPI.createReaderInstance();
            reader.setInput(inputFile);

            // //
            //
            // Setting Envelope, GridRange and CoordinateReferenceSystem
            //
            // //
            setCoverageProperties(reader);
            if (getCoverageCRS() == null) {
                LOGGER.info("crs not found, proceeding with EPSG:4326");
                setCoverageCRS(AbstractGridFormat.getDefaultCRS());
            }

            if (getCoverageEnvelope() == null) {
                throw new DataSourceException(
                        "Unavailable envelope for this coverage");

            }

            // Additional settings due to "final" methods getOriginalXXX
            originalEnvelope = getCoverageEnvelope();
            originalGridRange = getCoverageGridRange();
            crs = getCoverageCRS();

            // //
            //
            // Information about multiple levels and such
            //
            // //
            getResolutionInfo(reader);

            // //
            //
            // Reset and dispose reader
            //
            // //
            reader.reset();
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);

            throw new DataSourceException(e);
        } catch (TransformException e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);

            throw new DataSourceException(e);
        }
    }

    /** Package scope highest resolution info accessor */
    double[] getHighestRes() {
        return highestRes;
    }

    /** Package scope hints accessor */
    Hints getHints() {
        return hints;
    }

    /** Package scope grid to world transformation accessor */
    MathTransform getRaster2Model() {
        return raster2Model;
    }

    /**
     * Checks the input provided to this {@link BaseGridCoverage2DReader} and
     * sets all the other objects and flags accordingly.
     * 
     * @param input
     *                provided to this {@link BaseGridCoverage2DReader}.
     * 
     * @param hints
     *                Hints to be used by this reader throughout his life.
     * 
     * @throws UnsupportedEncodingException
     * @throws DataSourceException
     * @throws IOException
     * @throws FileNotFoundException
     */
    protected void checkSource(Object input)
            throws UnsupportedEncodingException, DataSourceException,
            IOException, FileNotFoundException {
        if (input == null) {
            final DataSourceException ex = new DataSourceException(
                    "No source set to read this coverage.");

            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }

            throw ex;
        }

        this.source = null;

        // //
        //
        // URL to FIle
        //
        // //
        // if it is an URL pointing to a File I convert it to a file.
        if (input instanceof URL) {
            // URL that point to a file
            final URL sourceURL = ((URL) input);
            this.source = sourceURL;
            if (sourceURL.getProtocol().compareToIgnoreCase("file") == 0) {
                final File fileCheck = new File(URLDecoder.decode(sourceURL
                        .getFile(), "UTF-8"));
                final String path = fileCheck.getAbsolutePath();
                final int imageSpecifierIndex = path.lastIndexOf(":");
                final File file;
                if (imageSpecifierIndex > 1
                        && imageSpecifierIndex > path.indexOf(":")) {
                    file = new File(path.substring(0, imageSpecifierIndex));
                    try {
                        imageIndex = Integer.parseInt(path.substring(
                                imageSpecifierIndex + 1, path.length()));
                    } catch (NumberFormatException nfe) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER
                                    .log(Level.WARNING,
                                            "Unable to parse the specified 0-based imageIndex");
                        }
                    }

                } else
                    file = fileCheck;
                inputFile = file;
                input = file;
            } else {
                throw new IllegalArgumentException("Unsupported input type");
            }
        }

        if (input instanceof FileImageInputStreamExt) {
            if (source == null) {
                source = input;
            }

            inputFile = ((FileImageInputStreamExt) input).getFile();
            input = inputFile;
        }

        // //
        //        
        // File
        //        
        // //
        if (input instanceof File) {
            final File fileCheck = (File) input;
            final String path = fileCheck.getAbsolutePath();
            final int imageSpecifierIndex = path.lastIndexOf(":");
            final File file;
            if (imageSpecifierIndex > 1
                    && imageSpecifierIndex > path.indexOf(":")) {
                file = new File(path.substring(0, imageSpecifierIndex));
                try {
                    imageIndex = Integer.parseInt(path.substring(
                            imageSpecifierIndex + 1, path.length()));
                } catch (NumberFormatException nfe) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER
                                .log(Level.WARNING,
                                        "Unable to parse the specified 0-based imageIndex");
                    }
                }

            } else
                file = fileCheck;

            final File sourceFile = file;

            if (source == null) {
                source = sourceFile;
            }

            if (inputFile == null) {
                inputFile = sourceFile;
            }

            if (!sourceFile.exists() || sourceFile.isDirectory()
                    || !sourceFile.canRead()) {
                throw new DataSourceException(
                        "Provided file does not exist or is a directory or is not readable!");
            }

            this.parentPath = sourceFile.getParent();
            coverageName = sourceFile.getName();

            final int dotIndex = coverageName.indexOf(".");
            coverageName = (dotIndex == -1) ? coverageName : coverageName
                    .substring(0, dotIndex);
        } else {
            throw new IllegalArgumentException("Unsupported input type");
        }
    }

    /**
     * Gets resolution information about the coverage itself.
     * 
     * @param reader
     *                an {@link ImageReader} to use for getting the resolution
     *                information.
     * @throws IOException
     * @throws TransformException
     * @throws DataSourceException
     */
    private void getResolutionInfo(ImageReader reader) throws IOException,
            TransformException {
        // //
        //
        // get the dimension of the high resolution image and compute the
        // resolution
        //
        // //
        final Rectangle originalDim = new Rectangle(0, 0, reader
                .getWidth(imageIndex), reader.getHeight(imageIndex));

        if (getCoverageGridRange() == null) {
            setCoverageGridRange(new GeneralGridEnvelope(originalDim));
        }

        // ///
        //
        // setting the higher resolution available for this coverage
        //
        // ///
        highestRes = CoverageUtilities
                .getResolution((AffineTransform) raster2Model);

        if (LOGGER.isLoggable(Level.FINE))
            LOGGER
                    .fine(new StringBuffer("Highest Resolution = [").append(
                            highestRes[0]).append(",").append(highestRes[1])
                            .toString());
    }

    /**
     * Returns a {@link GridCoverage} from this reader in compliance with the
     * specified parameters.
     * 
     * @param params
     *                a {@code GeneralParameterValue} array to customize the
     *                read operation.
     */
    public GridCoverage2D read(GeneralParameterValue[] params)
            throws IllegalArgumentException, IOException {

        // Setup a new coverage request
        final BaseCoverageRequest request = new BaseCoverageRequest(params, this);

        // compute the request.
        GridCoverage2D gc = ((GridCoverage2D) (requestCoverage(request)
                .getGridCoverage()));
        if (validRange != null)
            return gc.view(ViewType.GEOPHYSICS);
        return gc;
    }

    /**
     * Information about this source. Subclasses should provide additional
     * format specific information.
     * 
     * @return ServiceInfo describing getSource().
     */
    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription(source.toString());

        if (source instanceof URL) {
            URL url = (URL) source;
            info.setTitle(url.getFile());

            try {
                info.setSource(url.toURI());
            } catch (URISyntaxException e) {
            }
        } else if (source instanceof File) {
            File file = (File) source;
            String filename = file.getName();

            if ((filename == null) || (filename.length() == 0)) {
                info.setTitle(file.getName());
            }

            info.setSource(file.toURI());
        }

        return info;
    }

    /**
     * Information about the named gridcoverage.
     * 
     * @param subname
     *                Name indicing grid coverage to describe
     * @return ResourceInfo describing grid coverage indicated
     */
    public ResourceInfo getInfo(String subname) {
        DefaultResourceInfo info = new DefaultResourceInfo();
        info.setName(subname);
        info.setBounds(new ReferencedEnvelope(this.getOriginalEnvelope()));
        info.setCRS(this.getCrs());
        info.setTitle(subname);

        return info;
    }

    /**
     * Returns a {@link BaseCoverageResponse} from the specified
     * {@link BaseCoverageRequest}.
     * 
     * @param request
     *                a previously set {@link BaseCoverageRequest} defining a set of
     *                parameters to get a specific coverage
     * @return the computed {@code BaseCoverageResponse}
     * @todo Future versions may cache requests<->responses using hashing
     */
    private BaseCoverageResponse requestCoverage(BaseCoverageRequest request) {
        final BaseCoverageResponse response = new BaseCoverageResponse(request,
                coverageFactory, readerSPI);
        try {
            response.compute();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return null;
        }
        return response;
    }

    /**
     * @param coverageCRS
     *                the coverageCRS to set
     */
    protected void setCoverageCRS(CoordinateReferenceSystem coverageCRS) {
        this.coverageCRS = coverageCRS;
    }

    /**
     * @return the coverageCRS
     */
    protected CoordinateReferenceSystem getCoverageCRS() {
        return coverageCRS;
    }

    /**
     * @param coverageEnvelope
     *                the coverageEnvelope to set
     */
    protected void setCoverageEnvelope(GeneralEnvelope coverageEnvelope) {
        this.coverageEnvelope = coverageEnvelope;
    }

    /**
     * @return the coverageEnvelope
     */
    protected GeneralEnvelope getCoverageEnvelope() {
        return coverageEnvelope;
    }

    /**
     * @param coverageGridRange
     *                the coverageGridRange to set
     */
    protected void setCoverageGridRange(GeneralGridEnvelope coverageGridRange) {
        this.coverageGridRange = coverageGridRange;
    }

    /**
     * @return the coverageGridRange
     */
    protected GeneralGridEnvelope getCoverageGridRange() {
        return coverageGridRange;
    }

    /**
     * @return the input file
     */
    protected File getInputFile() {
        return inputFile;
    }

    /**
     * @return the coverage name
     */
    public String getCoverageName() {
        return coverageName;
    }

    protected void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    protected void setCoverageName(String coverageName) {
        this.coverageName = coverageName;
    }

    protected String getParentPath() {
        return parentPath;
    }

    protected void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    protected ImageReaderSpi getReaderSPI() {
        return readerSPI;
    }

    protected int getImageIndex() {
        return imageIndex;
    }

    protected String getLongName() {
        return longName;
    }

    protected void setLongName(String longName) {
        this.longName = longName;
    }

    protected double[] getNoDataValues() {
        return noDataValues != null ? noDataValues.clone() : null;
    }

    protected void setNoDataValues(double[] noDataValues) {
        this.noDataValues = noDataValues;
    }

    protected double[] getScaleAndOffset() {
        return scaleAndOffset != null ? scaleAndOffset.clone() : null;
    }

    protected void setScaleAndOffset(double[] scaleAndOffset) {
        this.scaleAndOffset = scaleAndOffset;
    }

    protected double[] getValidRange() {
        return validRange != null ? validRange.clone() : null;
    }

    protected void setValidRange(double[] validRange) {
        this.validRange = validRange;
    }

    protected void setGridCoverageCount(int gridCoverageCount) {
        this.gridCoverageCount = gridCoverageCount;
    }

    /**
     * Note: although this method returns the number of datasets available by
     * means of the underlying imageReader, use a single Reader for a single
     * dataset.
     * 
     */
    public int getGridCoverageCount() {
        return gridCoverageCount;
    }

    protected Unit<?> getUnit() {
        return unit;
    }

    protected void setUnit(Unit<?> unit) {
        this.unit = unit;
    }

    /**
     * @param spatioTemporalReader
     * @throws IOException
     * 
     * @todo: TODO XXX When defining new Geotools reader, fill these properties
     *        using the spatioTemporal metadata instance, getting values from
     *        the Band node instead of querying the commonMetadata
     *        object
     */
    protected void setInternalParameters(
            SpatioTemporalImageReader spatioTemporalReader) throws IOException {
        final int imageIndex = getImageIndex();
        IIOMetadata metadata = spatioTemporalReader.getImageMetadata(imageIndex);
        double scale = 1.0;
        double offset = 0.0;
        double[] validRange = null;
        double[] noDataValues = null;
        if (metadata != null && metadata instanceof BaseImageMetadata) {
        	BaseImageMetadata commonMetadata = (BaseImageMetadata) metadata;
            try {
                scale = commonMetadata.getScale(0);
                offset = commonMetadata.getOffset(0);
            } catch (IllegalArgumentException iae) {
                // TODO: no scale and offset are available
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Using default scale and offset values");
                }
            }
            setScaleAndOffset(new double[] { scale, offset });

            try {
                double max = commonMetadata.getMaximum(0);
                double min = commonMetadata.getMinimum(0);
                validRange = new double[] { min, max };
            } catch (IllegalArgumentException iae) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("ValidRange values not found");
                }
            }
            if (validRange != null)
                setValidRange(validRange);
            try {
                double noData = commonMetadata.getNoDataValue(0);
                noDataValues = new double[] { noData };
            } catch (IllegalArgumentException iae) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("NoData values not found");
                }
            }
            if (noDataValues != null)
                setNoDataValues(noDataValues);
            setLongName(commonMetadata.getDatasetName());
        }

    }

    protected void setCommonMetadata(IIOMetadata metadata) {
        if (metadata instanceof CoreCommonImageMetadata) {
            Node root = metadata.getAsTree(CoreCommonImageMetadata.nativeMetadataFormatName);

            Node commonChild = root.getFirstChild();
            while (!commonChild.getNodeName().equalsIgnoreCase("RasterDimensions"))
                commonChild = commonChild.getNextSibling();

            NamedNodeMap attributes = commonChild.getAttributes();
            // //
            //
            // 1) Grid
            //
            // //
            if (getCoverageGridRange() == null) {
                final String width = attributes.getNamedItem("width").getNodeValue();
                final String height = attributes.getNamedItem("height").getNodeValue();
                if (Utilities.ensureValidString(width,height)) {
                    final int w = Integer.parseInt(width);
                    final int h = Integer.parseInt(height);
                    setCoverageGridRange(new GeneralGridEnvelope(new Rectangle(0, 0, w, h)));
                }
            }
        }
    }
}
