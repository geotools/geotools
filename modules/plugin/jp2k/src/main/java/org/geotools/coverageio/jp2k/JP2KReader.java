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
package org.geotools.coverageio.jp2k;

import it.geosolutions.imageio.plugins.jp2k.JP2KStreamMetadata;
import it.geosolutions.imageio.plugins.jp2k.box.UUIDBox;
import it.geosolutions.imageio.plugins.jp2k.box.UUIDBoxMetadataNode;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataDecoder;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffMetadata2CRSAdapter;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.PrjFileReader;
import org.geotools.data.WorldFileReader;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.opengis.coverage.grid.Format;
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
 * This class can read a JP2K data source and create a {@link GridCoverage2D}
 * from the data.
 * 
 * @author Daniele Romagnoli, GeoSolutions.
 * @author Simone Giannecchini (simboss), GeoSolutions
 */
public final class JP2KReader extends AbstractGridCoverage2DReader implements
        GridCoverageReader {

	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(JP2KReader.class);

	/** The system-dependent default name-separator character. */
    private final static char SEPARATOR = File.separatorChar;
	
    private final static short[] GEOJP2_UUID = new short[] { 0xb1, 0x4b, 0xf8, 0xbd, 0x08, 
        0x3d, 0x4b, 0x43, 0xa5, 0xae, 0x8c, 0xd7, 0xd5, 0xa6, 0xce, 0x03 };
    
    private final static short[] MSIG_WORLDFILEBOX_UUID = new short[] { 0x96, 0xa9, 0xf1, 0xf1,
        0xdc, 0x98, 0x40, 0x2d, 0xa7, 0xae, 0xd6, 0x8e, 0x34, 0x45, 0x18, 0x09 };

    private final static int WORLD_FILE_INTERPRETATION_PIXEL_CORNER = 1;
    
    /**
     * The base {@link GridRange} for the {@link GridCoverage2D} of this reader.
     */
    private GridEnvelope2D nativeGridRange = null;
    
    private GeneralEnvelope nativeEnvelope = null;
    
	ImageReaderSpi cachedSPI;
    
    /**
     * Creates a new instance of a {@link JP2KReader}. I assume nothing about
     * file extension.
     * 
     * @param input
     *                Source object for which we want to build an JP2KReader.
     * @throws DataSourceException
     */
    public JP2KReader(Object input) throws IOException {
        this(input, null);
    }

	/**
     * Setting Envelope, GridRange and CRS from the given {@code ImageReader}
     * 
     * @param reader
     *                the {@code ImageReader} from which to retrieve metadata
     *                (if available) for setting properties
     * @throws IOException
     */
    protected void setCoverageProperties(ImageReader reader) throws IOException {
        // //
        //
        // Getting stream metadata from the underlying layer
        //
        // //
        
        int hrWidth = reader.getWidth(0);
        int hrHeight = reader.getHeight(0);
        final Rectangle actualDim = new Rectangle(0, 0, hrWidth, hrHeight);
        this.nativeGridRange = new GridEnvelope2D(actualDim);
        if (this.crs == null) {
            parsePRJFile();
        }
        
        if (this.nativeEnvelope == null) {
        	parseWorldFile();
        }

        if (this.crs == null || this.nativeEnvelope == null) {
            final IIOMetadata metadata = reader.getStreamMetadata();
            checkUUIDBoxes(metadata);
        	
        }
        // //
        //
        // If no sufficient information have been found to set the
        // envelope, try other ways, such as looking for a WorldFile
        //
        // //
        if (this.nativeEnvelope == null) {
                throw new DataSourceException(
                        "Unavailable envelope for this coverage");
        }

        // setting the coordinate reference system for the envelope
        originalEnvelope = getCoverageEnvelope();
        originalEnvelope.setCoordinateReferenceSystem(crs);
        originalGridRange = getCoverageGridRange();

        // Additional settings due to "final" methods getOriginalXXX
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
    
    private boolean isGeoJP2(final byte[] id) {
    	return isSameUUID(id, GEOJP2_UUID);
    }
    
    private boolean isWorldBox(final byte[] id) {
    	return isSameUUID(id, MSIG_WORLDFILEBOX_UUID);
    }
    
    private boolean isSameUUID(final byte[] id, final short[] uuid) {
        for (int i = 0; i < uuid.length; i++) {
            if ((id[i] & 0xFF) != uuid[i])
                return false;
        }
        return true;
    }
    
    /**
     * Look for UUID boxes containing GeoJP2 Boxes / MSIG World Box
     * @param metadata 
     * @throws IOException
     */
    private void checkUUIDBoxes(final IIOMetadata metadata) throws IOException{
    	if (!(metadata instanceof JP2KStreamMetadata)) {
            if (LOGGER.isLoggable(Level.FINE))
            		LOGGER.fine("Metadata should be an instance of the expected class:"
                            + " JP2KStreamMetadata.");
            return;
        }
    	// //
        //
        // Looking for the UUIDBoxMetadataNode
        //
        // //
        final List<IIOMetadataNode> uuidBoxMetadataNodes = ((JP2KStreamMetadata) metadata).searchOccurrencesNode(UUIDBox.BOX_TYPE);
        UUIDBoxMetadataNode geoJP2uuid = null;
        UUIDBoxMetadataNode worldBoxuuid = null;
        if (uuidBoxMetadataNodes != null && !uuidBoxMetadataNodes.isEmpty()){
        	for (IIOMetadataNode node: uuidBoxMetadataNodes){
        		if (node instanceof UUIDBoxMetadataNode) {
                    final UUIDBoxMetadataNode uuid = (UUIDBoxMetadataNode) node;
                    final byte[] id = uuid.getUuid();
                    if (isGeoJP2(id)) {
                       geoJP2uuid = uuid;
                       continue;
                    }
                    
                	if (isWorldBox(id)){
                    	worldBoxuuid = uuid;
                    }
                }
        	}
        }

        if (geoJP2uuid!=null)
        	getGeoJP2(geoJP2uuid);
        
        // //
        //
        // Without a proper crs, the World Box is useless 
        //
        // //
        if (worldBoxuuid!=null && crs != null){
        	getWorldBox(worldBoxuuid);
        }
        
    }
    
    private void getWorldBox(final UUIDBoxMetadataNode uuid) throws IOException {
		
    	// //
    	//
    	// Parsing Header 
    	//
    	// //
    	final byte[] bb = uuid.getData();
        if (bb[0]!='M'||bb[1]!='S'||bb[2]!='I'||bb[3]!='G')
			 return;
		
        // Version number: not used
        // bb[4] bb[5]
        
        final int worldFileInterpretation = bb[6]; 

        // Not used.       
        // final int chunkNumber = bb[14];
        
        // //
        //
        // Parsing Chunk
        //
        // //
        final int ckIndex = 16;
        final int chunkIndex = bb[ckIndex];
        final long chunkLength = Utils.bytes2long(bb, ckIndex+2);
        
        if (chunkIndex!=0 && chunkLength!=48)
        	return;
        
        // //
        // Parsing the Grid to World transformation
        // //
        final double xScale = Utils.bytes2double(bb,ckIndex+6);
        final double xRotation = Utils.bytes2double(bb,ckIndex+14);
        final double yRotation = Utils.bytes2double(bb,ckIndex+22);
        final double yScale = Utils.bytes2double(bb,ckIndex+30);
        final double xUpperLeft = Utils.bytes2double(bb,ckIndex+38);
        final double yUpperLeft = Utils.bytes2double(bb,ckIndex+46);
        
        // Not used.       
        // final boolean footerOk = (bb[ckIndex + 54] == (byte)0xFF) && (bb[ckIndex + 55] == (byte)0x00);

        // //
        // Setting up the grid to world transformation
        // // 
        final AffineTransform tempTransform = new AffineTransform(
        		xScale, yRotation, xRotation,
        		yScale, xUpperLeft, yUpperLeft);

        // ////////////////////////////////////////////////////////////////////
        //
        // Quoting from 3.2.2.1 at:
        // http://www.lizardtech.com/support/kb/docs/geotiff_box.txt
        //
        // "This was instituted with version 1.03.11 (May 15, 2003) to signify that we
        //	clarified the definition of the georeferencing data and found out that that
        //	data represents the upper left corner of the upper left pixel, not the
        //	center as we had thought, so the [world chunk values are] not equal to the
        //	geotiff data, but is shifted by 0.5*scale to the center of the pixel."
        //
        // Finally note that:
        // If the world chunk is present, these values should override 
        // the corresponding values in the GeoTIFF box.
        //
        // ////////////////////////////////////////////////////////////////////
        if (worldFileInterpretation == WORLD_FILE_INTERPRETATION_PIXEL_CORNER){
        	AffineTransform transform = (AffineTransform) ProjectiveTransform.create(tempTransform);
        	final double tr = -PixelTranslation.getPixelTranslation(PixelInCell.CELL_CORNER);
        	transform.translate(tr, tr);
        	this.raster2Model = ProjectiveTransform.create(transform);
        } else {
        	this.raster2Model = ProjectiveTransform.create(tempTransform);
        	tempTransform.translate(-0.5, -0.5);
        }
       
        try {
        	final GeneralEnvelope envelope = CRS.transform(ProjectiveTransform.create(tempTransform),
        			new GeneralEnvelope(nativeGridRange));
        	envelope.setCoordinateReferenceSystem(crs);
        	this.nativeEnvelope = envelope;
        } catch (TransformException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE,"Unable to parse CRS from underlying TIFF", e);
        } catch (UnsupportedOperationException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE,"Unable to parse CRS from underlying TIFF due to an unsupported CRS", e);
        } 
        
    }

    /**
     * Get the degenerate GeoTIFF to obtain the related CoordinateReferenceSystem tags
     * @param uuid
     * @throws IOException
     */
	private void getGeoJP2(final UUIDBoxMetadataNode uuid) throws IOException {
		
		 CoordinateReferenceSystem coordinateReferenceSystem = null;
		 final ByteArrayInputStream inputStream = new ByteArrayInputStream(uuid.getData());
         final TIFFImageReader tiffreader = (TIFFImageReader) new TIFFImageReaderSpi().createReaderInstance();
         tiffreader.setInput(ImageIO.createImageInputStream(inputStream));
         final IIOMetadata tiffmetadata = tiffreader.getImageMetadata(0);
         try {
            final GeoTiffIIOMetadataDecoder metadataDecoder = new GeoTiffIIOMetadataDecoder(tiffmetadata);
            final GeoTiffMetadata2CRSAdapter adapter = new GeoTiffMetadata2CRSAdapter(hints);
             coordinateReferenceSystem = adapter.createCoordinateSystem(metadataDecoder);
             if (coordinateReferenceSystem != null) {
             	if (this.crs == null)
             		this.crs = coordinateReferenceSystem;
             }
             if (this.raster2Model == null){
                 this.raster2Model = GeoTiffMetadata2CRSAdapter.getRasterToModel(metadataDecoder);
                 final AffineTransform tempTransform = new AffineTransform((AffineTransform) raster2Model);
                 tempTransform.translate(-0.5, -0.5);
                 final GeneralEnvelope envelope = CRS.transform(ProjectiveTransform.create(tempTransform),
                         new GeneralEnvelope(nativeGridRange));
                 envelope.setCoordinateReferenceSystem(crs);
                 setCoverageEnvelope(envelope);
             }

         } catch (FactoryException e) {
             if (LOGGER.isLoggable(Level.FINE))
                 LOGGER.log(Level.FINE,
                         "Unable to parse CRS from underlying TIFF", e);
             coordinateReferenceSystem = null;
         } catch (TransformException e) {
             if (LOGGER.isLoggable(Level.FINE))
                 LOGGER.log(Level.FINE,
                         "Unable to parse CRS from underlying TIFF", e);
             coordinateReferenceSystem = null;
         } catch (UnsupportedOperationException e) {
             if (LOGGER.isLoggable(Level.FINE))
                 LOGGER.log(Level.FINE,
                         "Unable to parse CRS from underlying TIFF", e);
             coordinateReferenceSystem = null;
         } catch (IllegalArgumentException e) {
             if (LOGGER.isLoggable(Level.FINE))
                 LOGGER.log(Level.FINE,
                         "Unable to parse CRS from underlying TIFF", e);
             coordinateReferenceSystem = null;
         } finally {
             if (inputStream != null)
                 try {
                     inputStream.close();
                 } catch (IOException ioe) {
                     // Eat exception.
                 }
         }
		
	}

	/**
	 * Number of coverages for this reader is 1
	 * 
	 * @return the number of coverages for this reader.
	 */
	@Override
	public int getGridCoverageCount() {
		return 1;
	}

	/**
	 * Releases resources held by this reader.
	 * 
	 */
	@Override
	public synchronized void dispose() {
		super.dispose();
		rasterManager.dispose();
	}


	/**
	 * The source {@link URL} 
	 */
	URL sourceURL;

	boolean expandMe;

	private RasterManager rasterManager;

	private String parentPath;

    /**
     * Constructor.
     * 
     * @param source
     *            The source object.
     * @throws IOException
     * @throws UnsupportedEncodingException
     * 
     */
    public JP2KReader(Object source, Hints uHints) throws IOException {
        super(source,uHints);

        

        // /////////////////////////////////////////////////////////////////////
        //
        // Check source
        //
        // /////////////////////////////////////////////////////////////////////
        this.sourceURL = Utils.checkSource(source);
        if (this.sourceURL == null)
            throw new DataSourceException(
                    "This plugin accepts only File,  URL and String pointing to a file");

        final File inputFile = DataUtilities.urlToFile(sourceURL);
        if (inputFile == null)
            throw new DataSourceException("Unable to find a file for the provided source");
        parentPath = inputFile.getParent();
        ImageReader reader = null;
        final ImageInputStream stream = ImageIO.createImageInputStream(inputFile);
        if (cachedSPI == null) {
            reader = Utils.getReader(stream);
            if (reader != null)
                cachedSPI = reader.getOriginatingProvider();
        }

        if (reader == null)
            throw new DataSourceException("No reader found for that source " + sourceURL);
        reader.setInput(stream);

        coverageName = inputFile.getName();

        final int dotIndex = coverageName.lastIndexOf(".");
        coverageName = (dotIndex == -1) ? coverageName : coverageName.substring(0, dotIndex);

        // //
        //
        // get the crs if able to
        //
        // //
        final Object tempCRS = this.hints.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
        if (tempCRS != null) {
            this.crs = (CoordinateReferenceSystem) tempCRS;
            LOGGER.log(Level.WARNING, "Using forced coordinate reference system "+crs.toWKT());
        } else {

            setCoverageProperties(reader);

            if (crs == null) {
                throw new DataSourceException("Unable to find a CRS for this coverage, using a default one: "
                                + crs.toWKT());
            }
        }
        setResolutionInfo(reader);
        reader.dispose();

        // creating the raster manager
        rasterManager = new RasterManager(this);
    }

	/**
	 * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
	 */
	public Format getFormat() {
		return new JP2KFormat();
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageReader#read(org.opengis.parameter.GeneralParameterValue[])
	 */
	public GridCoverage2D read(GeneralParameterValue[] params) throws IOException {

		if (LOGGER.isLoggable(Level.FINE)) {
			LOGGER.fine("Reading image from " + sourceURL.toString());
			LOGGER.fine(new StringBuffer("Highest res ").append(highestRes[0])
					.append(" ").append(highestRes[1]).toString());
		}

		final Collection<GridCoverage2D> response = rasterManager.read(params);
		if(response.isEmpty())
			return null;
		else
			return response.iterator().next();
	}

	/**
	 * Package private accessor for {@link Hints}.
	 * 
	 * @return this {@link Hints} used by this reader.
	 */
	Hints getHints(){
		return super.hints;
	}
	
	/**
	 * Package private accessor for the highest resolution values.
	 * 
	 * @return the highest resolution values.
	 */
	double[] getHighestRes(){
		return super.highestRes;
	}
	
	/**
	 * 
	 * @return
	 */
	double[][] getOverviewsResolution(){
		return super.overViewResolutions;
	}
	
	int getNumberOfOverviews(){
		return super.numOverviews;
	}
	

    /** Package scope grid to world transformation accessor */
    MathTransform getRaster2Model() {
        return raster2Model;
    }
    
    /**
     * Let us retrieve the {@link GridCoverageFactory} that we want to use.
     * 
     * @return
     * 			retrieves the {@link GridCoverageFactory} that we want to use.
     */
    GridCoverageFactory getGridCoverageFactory(){
    	return coverageFactory;
    }

	String getName() {
		return super.coverageName;
	}
	
	
	/**
     * Gets the coordinate reference system that will be associated to the
     * {@link GridCoverage} by looking for a related PRJ.
	 * @throws UnsupportedEncodingException 
     */
    protected void parsePRJFile() throws UnsupportedEncodingException {
        String prjPath = new StringBuilder(parentPath).append(SEPARATOR)
                .append(coverageName).append(".prj").toString();

        // does it exist?
        final File prjFile = new File(prjPath);
        if (prjFile.exists()) {
            // it exists then we have top read it
            PrjFileReader projReader = null;
            FileInputStream instream = null;
            try {
            	instream=new FileInputStream(prjFile);
                final FileChannel channel = instream.getChannel();
                projReader = new PrjFileReader(channel);
                crs = projReader.getCoordinateReferenceSystem();
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
                    if (instream != null)
                        try {
                            instream.close();
                        } catch (IOException ioe) {
                            // warn about the error but proceed, it is not fatal
                            // we have at least the default crs to use
                            LOGGER.log(Level.FINE, ioe.getLocalizedMessage(),ioe);
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
                SEPARATOR).append(coverageName).toString();

        File file2Parse = null;
        boolean worldFileExists = false;
        
        // //
        //
        // Check for a world file with the format specific extension
        //
        // //
        file2Parse = new File(worldFilePath + ".j2w");
        worldFileExists = file2Parse.exists();
    
        // //
        //
        // Check for a world file with the default extension
        //
        // //
        if (!worldFileExists) {
            file2Parse = new File(worldFilePath + ".wld");
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
                final Envelope gridRange = new GeneralEnvelope(nativeGridRange);
                final GeneralEnvelope coverageEnvelope = CRS.transform(
                		tempTransform, gridRange);
                nativeEnvelope = coverageEnvelope;
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
     * Gets resolution information about the coverage itself.
     * 
     * @param reader
     *                an {@link ImageReader} to use for getting the resolution
     *                information.
     * @throws IOException
     * @throws TransformException
     * @throws DataSourceException
     */
    private void setResolutionInfo(ImageReader reader) throws IOException {
        // //
        //
        // get the dimension of the high resolution image and compute the
        // resolution
        //
        // //
        final Rectangle originalDim = new Rectangle(0, 0, reader.getWidth(0),
                reader.getHeight(0));

        if (nativeGridRange == null) {
        	setCoverageGridRange(new GridEnvelope2D(originalDim));
        }

        // //
        //
        // setting the higher resolution available for this coverage
        //
        // //
        highestRes = CoverageUtilities
                .getResolution((AffineTransform) raster2Model);
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(new StringBuffer("Highest Resolution = [").append(
                            highestRes[0]).append(",").append(highestRes[1])
                            .toString());
        numOverviews = 0;
		overViewResolutions = numOverviews >= 1 ? new double[numOverviews][2]: null;
    }
}

