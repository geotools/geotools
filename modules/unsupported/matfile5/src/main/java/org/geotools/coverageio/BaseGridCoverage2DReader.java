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

import it.geosolutions.imageio.imageioimpl.imagereadmt.ImageReadDescriptorMT;
import it.geosolutions.imageio.stream.input.FileImageInputStreamExt;
import it.geosolutions.imageio.stream.input.FileImageInputStreamExtImpl;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.media.jai.JAI;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.data.DefaultResourceInfo;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.PrjFileReader;
import org.geotools.data.ResourceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.data.WorldFileReader;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.CRS;
import org.geotools.resources.coverage.CoverageUtilities;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Base class for GridCoverage data access
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/matfile5/src/main/java/org/geotools/coverageio/BaseGridCoverage2DReader.java $
 */
public abstract class BaseGridCoverage2DReader extends AbstractGridCoverage2DReader implements GridCoverageReader {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.coverageio.gdal");
    
    /** registering ImageReadMT JAI operation (for multithread ImageRead) */
    static {
        try {
                ImageReadDescriptorMT.register(JAI.getDefaultInstance());
        }
        catch (Exception e) {
                if(LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
                }
    }

    /**
     * The format specific world file extension.
     */
    private final String worldFileExt;
    
    /** Caches an {@code ImageReaderSpi}. */
    private final ImageReaderSpi readerSPI;

    /**
     * Implement this method to setup the coverage properties (Envelope, CRS,
     * GridRange) using the provided {@code ImageReader}
     */
    protected abstract void setCoverageProperties(ImageReader reader)
            throws IOException;

    // ////////////////////////////////////////////////////////////////////////
    //  
    // Referencing related fields (CRS and Envelope)
    //  
    // ////////////////////////////////////////////////////////////////////////

    /** Envelope read from file */
    private GeneralEnvelope nativeEnvelope = null;

    /** The CRS for the coverage */
    private CoordinateReferenceSystem nativeCRS = null;

    // ////////////////////////////////////////////////////////////////////////
    //  
    // Data source properties and field for management
    //  
    // ////////////////////////////////////////////////////////////////////////

    /** Source given as input to the reader */
    private File inputFile = null;

    /** Coverage name */
    private String localCoverageName = "geotools_coverage";

    /**
     * The base {@link GridRange} for the {@link GridCoverage2D} of this reader.
     */
    private GridEnvelope2D nativeGridRange = null;

    /** Absolute path to the parent dir for this coverage. */
    private String parentPath;

	private ServiceInfo serviceInfo;

	private ResourceInfo resourceInfo;

    /**
     * Creates a new instance of a {@link BaseGridCoverage2DReader}. I assume
     * nothing about file extension.
     * 
     * @param input
     *                Source object for which we want to build a
     *                {@link BaseGridCoverage2DReader}.
     * @param hints
     *                Hints to be used by this reader throughout his life.
     * @param worldFileExtension
     *                the specific world file extension of the underlying format
     * @param formatSpecificSpi
     *                an instance of a proper {@code ImageReaderSpi}.
     * @throws DataSourceException
     */
    protected BaseGridCoverage2DReader(
    		final Object input, 
    		final Hints hints,
    		final String worldFileExtension,
            final ImageReaderSpi formatSpecificSpi) throws DataSourceException {
        super(input, hints);
        
    	ImageReader reader=null;
    	FileImageInputStreamExt fiis = null;
        try {
            

            readerSPI = formatSpecificSpi;
            worldFileExt = worldFileExtension;

            // //
            //
            // Source management
            //
            // //
            checkSource(input);
            
            // //
            //
            // Setting Envelope, GridRange and CRS
            //
            // //
            reader = readerSPI.createReaderInstance();
            fiis = new FileImageInputStreamExtImpl(inputFile);
            reader.setInput(fiis);
            setCoverageProperties(reader);

            // //
            //
            // Information about multiple levels and such
            //
            // //
            getResolutionInfo(reader);

        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);

            throw new DataSourceException(e);
        } catch (TransformException e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);

            throw new DataSourceException(e);
        }
        finally {

            // //
            //
            // Reset and dispose reader
            //
            // //
        	if(reader!=null) {
        		try {
        			reader.reset();
        		}catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
				}
        		
        		try {
        			reader.dispose();
        		}catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
				}        		
                
        	}
        	
        	if (fiis!=null){
        	    try {
                        fiis.close();
                    }catch (Exception e) {
                        if (LOGGER.isLoggable(Level.FINE))
                            LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                    }
                    
        	}
        }
    }

    /** Package scope highest resolution serviceInfo accessor */
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
     *                Actually supported input types for the underlying
     *                ImageIO-Ext GDAL framework are: {@code File}, {@code URL}
     *                pointing to a file and {@link FileImageInputStreamExt}
     * @param hints
     *                Hints to be used by this reader throughout his life.
     * 
     * @throws UnsupportedEncodingException
     * @throws DataSourceException
     * @throws IOException
     * @throws FileNotFoundException
     */
    private void checkSource(Object input) throws UnsupportedEncodingException,
            DataSourceException, IOException, FileNotFoundException {

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
                this.inputFile = new File(URLDecoder.decode(
                        sourceURL.getFile(), "UTF-8"));
                input = this.inputFile;
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
            final File sourceFile = (File) input;

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
            localCoverageName = sourceFile.getName();

            final int dotIndex = localCoverageName.lastIndexOf(".");
            localCoverageName = (dotIndex == -1) ? localCoverageName : localCoverageName
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
        final Rectangle originalDim = new Rectangle(0, 0, reader.getWidth(0),
                reader.getHeight(0));
        if (getCoverageGridRange() == null) {
            setCoverageGridRange(new GridEnvelope2D(originalDim));
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
        final RasterLayerRequest request = new RasterLayerRequest(params,this);
        

        // compute the request.
        return (GridCoverage2D) requestCoverage(request).getGridCoverage();
    }
    
    /**
     * Gets the coordinate reference system that will be associated to the
     * {@link GridCoverage} by looking for a related PRJ.
     */
    protected void parsePRJFile() {
        String prjPath = null;

        setCoverageCRS(null);
        prjPath = new StringBuilder(this.parentPath).append(File.separatorChar)
                .append(localCoverageName).append(".prj").toString();

        // read the prj serviceInfo from the file
        PrjFileReader projReader = null;

        try {
            final File prj = new File(prjPath);

            if (prj.exists()) {
                projReader = new PrjFileReader(new FileInputStream(prj)
                        .getChannel());
                setCoverageCRS(projReader.getCoordinateReferenceSystem());
            }
            // If some exception occurs, warn about the error but proceed
            // using a default CRS
        } catch (FileNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        } catch (FactoryException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        } finally {
            if (projReader != null) {
                try {
                    projReader.close();
                } catch (IOException e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * Checks whether a world file is associated with the data source. If found,
     * set a proper envelope.
     * 
     * @throws IllegalStateException
     * @throws IOException
     */
    protected void parseWorldFile() throws IOException {
        final String worldFilePath = new StringBuffer(this.parentPath).append(
                GridCoverageUtilities.SEPARATOR).append(localCoverageName).toString();

        File file2Parse = null;
        boolean worldFileExists = false;
        // //
        //
        // Check for a world file with the format specific extension
        //
        // //
        if (worldFileExt != null && worldFileExt.length() > 0) {
            file2Parse = new File(worldFilePath + worldFileExt);
            worldFileExists = file2Parse.exists();
        }

        // //
        //
        // Check for a world file with the default extension
        //
        // //
        if (!worldFileExists) {
            file2Parse = new File(worldFilePath + GridCoverageUtilities.DEFAULT_WORLDFILE_EXT);
            worldFileExists = file2Parse.exists();
        }

        if (worldFileExists) {
            final WorldFileReader reader = new WorldFileReader(file2Parse);
            raster2Model = reader.getTransform();
            
            // //
            //
            // In case we read from a real world file we have together the
            // envelope. World file transformation assumes to work in the
            // CELL_CENTER condition
            //
            // //
            MathTransform tempTransform = PixelTranslation.translate(raster2Model, PixelInCell.CELL_CENTER, PixelInCell.CELL_CORNER);
            try {
                final Envelope gridRange = new GeneralEnvelope(getCoverageGridRange().getBounds2D());
                final GeneralEnvelope coverageEnvelope = CRS.transform(
                                tempTransform, gridRange);
                setCoverageEnvelope(coverageEnvelope);
            } catch (TransformException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                }
            } catch (IllegalStateException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                }
            }
        }
    }

    /**
     * Information about this source. Subclasses should provide additional
     * format specific information.
     * 
     * @return ServiceInfo describing getSource().
     */
    @Override
    public synchronized ServiceInfo getInfo() {
    	if (serviceInfo!=null)
    		return new DefaultServiceInfo(this.serviceInfo);
    	
        DefaultServiceInfo localInfo = new DefaultServiceInfo();
        serviceInfo=localInfo;
        localInfo.setDescription(source.toString());

        if (source instanceof URL) {
            URL url = (URL) source;
            localInfo.setTitle(url.getFile());

            try {
                localInfo.setSource(url.toURI());
            } catch (URISyntaxException e) {
            }
        } else if (source instanceof File) {
            File file = (File) source;
            String filename = file.getName();

            if ((filename == null) || (filename.length() == 0)) {
                localInfo.setTitle(file.getName());
            }

            localInfo.setSource(file.toURI());
        }

        return new DefaultServiceInfo(localInfo);
    }
    
    protected void writePrj(final File outLocation, final String wkt) throws IOException {
        writePrj(new BufferedWriter(new FileWriter(outLocation), 4096), wkt);
    }

    private void writePrj(BufferedWriter writer, String wkt) {
        try {
            writer.write(wkt);
            close(writer);
            return;

        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE))
                LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            close(writer);
        }
    }

    private void close(BufferedWriter writer) {
        try {
            writer.close();
        } catch (Throwable t) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, t.getLocalizedMessage(), t);
        }
    }

    /**
     * Information about the named gridcoverage.
     * 
     * @param subname
     *                Name indicing grid coverage to describe
     * @return ResourceInfo describing grid coverage indicated
     */
    public synchronized ResourceInfo getInfo(String subname) {

        if (this.resourceInfo!=null)
    		return new DefaultResourceInfo(this.resourceInfo);
    	
        DefaultResourceInfo localInfo = new DefaultResourceInfo();
	resourceInfo=localInfo;    	
	localInfo.setName(subname);
        localInfo.setBounds(new ReferencedEnvelope(this.getOriginalEnvelope()));
        localInfo.setCRS(this.getCrs());
        localInfo.setTitle(subname);

        return new DefaultResourceInfo(this.resourceInfo);
    }

    /**
     * Returns a {@link RasterLayerResponse} from the specified
     * {@link RasterLayerRequest}.
     * 
     * @param request
     *                a previously set {@link RasterLayerRequest} defining a set of
     *                parameters to get a specific coverage
     * @return the computed {@code RasterLayerResponse}
     * @todo Future versions may cache requestes<->responses using hashing
     */
    private RasterLayerResponse requestCoverage(RasterLayerRequest request) {
        final RasterLayerResponse response = new RasterLayerResponse(request,
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
     * @param nativeCRS
     *                the nativeCRS to set
     */
    protected void setCoverageCRS(CoordinateReferenceSystem coverageCRS) {
        this.nativeCRS = coverageCRS;
    }

    /**
     * @return the nativeCRS
     */
    protected CoordinateReferenceSystem getCoverageCRS() {
        return nativeCRS;
    }

    /**
     * @param nativeEnvelope
     *                the nativeEnvelope to set
     */
    protected void setCoverageEnvelope(GeneralEnvelope coverageEnvelope) {
        this.nativeEnvelope = coverageEnvelope;
    }

    /**
     * @return the nativeEnvelope
     */
    protected GeneralEnvelope getCoverageEnvelope() {
        return nativeEnvelope;
    }

    /**
     * @param nativeGridRange
     *                the nativeGridRange to set
     */
    protected void setCoverageGridRange(GridEnvelope2D coverageGridRange) {
        this.nativeGridRange = coverageGridRange;
    }

    /**
     * @return the nativeGridRange
     */
    protected GridEnvelope2D getCoverageGridRange() {
        return nativeGridRange;
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
        return localCoverageName;
    }

    /**
     * @return the gridCoverage count
     */
    @Override
    public int getGridCoverageCount() {
        return 1;
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#hasMoreGridCoverages()
     */
    @Override
    public boolean hasMoreGridCoverages() {
        return false;
    }
    
}
