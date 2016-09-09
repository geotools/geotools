/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.maskband.DatasetLayout;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import it.geosolutions.imageioimpl.plugins.tiff.TiffDatasetLayoutImpl;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GroundControlPoints;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider.MaskInfo;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataDecoder;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffMetadata2CRSAdapter;
import org.geotools.coverage.grid.io.imageio.geotiff.TiePoint;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileGroupProvider.FileGroup;
import org.geotools.data.MapInfoFileReader;
import org.geotools.data.PrjFileReader;
import org.geotools.data.WorldFileReader;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.image.io.ImageIOExt;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.util.NumberRange;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
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
 */
public class GeoTiffReader extends AbstractGridCoverage2DReader implements GridCoverage2DReader {

	/** Logger for the {@link GeoTiffReader} class. */
	private Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeoTiffReader.class.toString());
	
	/** With this java switch I can control whether or not an external PRJ files takes precedence over the internal CRS definition*/
	public static final String OVERRIDE_CRS_SWITCH = "org.geotools.gce.geotiff.override.crs";
	
	/** With this java switch I can control whether or not an external PRJ files takes precedence over the internal CRS definition*/
	static boolean OVERRIDE_INNER_CRS=Boolean.valueOf(System.getProperty(GeoTiffReader.OVERRIDE_CRS_SWITCH, "True"));

	/** SPI for creating tiff readers in ImageIO tools */
	private final static TIFFImageReaderSpi READER_SPI = new TIFFImageReaderSpi();

	/** Adapter for the GeoTiff crs. */
	private GeoTiffMetadata2CRSAdapter gtcs;
	
	private double noData = Double.NaN;

        /** File containing image overviews */
        private File ovrSource;

        /** {@link ImageInputStreamSpi} for the file containing external overviews */
        private ImageInputStreamSpi ovrInStreamSPI = null;

        /** Image index of the overviews file */
        private int extOvrImgChoice = -1;

        /** {@link MaskOverviewProvider} instance used for handling internal/external Overviews*/
        private MaskOverviewProvider maskOvrProvider;

        /** Boolean indicating if {@link MaskOverviewProvider} is present*/
        private boolean hasMaskOvrProvider;

    /**
     * The ground control points, populated if there is no grid to world transformation
     */
    private GroundControlPoints gcps;

	/**
	 * Creates a new instance of GeoTiffReader
	 * 
	 * @param input
	 *            the GeoTiff file
	 * @throws DataSourceException
	 */
	public GeoTiffReader(Object input) throws DataSourceException {
		this(input, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER,Boolean.TRUE));

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
	    super(input,uHints);
               
		// /////////////////////////////////////////////////////////////////////
		//
		// Set the source being careful in case it is an URL pointing to a file
		//
		// /////////////////////////////////////////////////////////////////////
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
			if ((source instanceof InputStream)|| (source instanceof ImageInputStream))
				closeMe = false;
			if(source instanceof ImageInputStream )
				inStream = (ImageInputStream) source;
			else{

			    inStreamSPI = ImageIOExt.getImageInputStreamSPI(source);
			    if (inStreamSPI == null)
                                throw new IllegalArgumentException("No input stream for the provided source");
			    inStream = inStreamSPI.createInputStreamInstance(source, ImageIO.getUseCache(), ImageIO.getCacheDirectory());
			}
			if (inStream == null) {
                            // Try to figure out what went wrong and provide some info to the user.
                            if(source instanceof File) {
                                File f = (File) source;
                                if (!f.exists()) {
                                    throw new FileNotFoundException("File " + f.getAbsolutePath() + " does not exist.");
                                } else if (f.isDirectory()) {
                                    throw new IOException("File " + f.getAbsolutePath() + " is a directory.");
                                } else if(!f.canRead()) {
                                    throw new IOException("File " + f.getAbsolutePath() + " can not be read.");
                                }
                            }

                            // If we can't give anything more specific, throw the generic error.
                            throw new IllegalArgumentException("No input stream for the provided source");
                        }

			// /////////////////////////////////////////////////////////////////////
			//
			// Informations about multiple levels and such
			//
			// /////////////////////////////////////////////////////////////////////
			getHRInfo(this.hints);

			// /////////////////////////////////////////////////////////////////////
			// 
			// Coverage name
			//
			// /////////////////////////////////////////////////////////////////////
			coverageName = source instanceof File ? ((File) source).getName(): "geotiff_coverage";
			final int dotIndex = coverageName.lastIndexOf('.');
			if (dotIndex != -1 && dotIndex != coverageName.length())
				coverageName = coverageName.substring(0, dotIndex);

		} catch (IOException e) {
			throw new DataSourceException(e);
		}finally{
			// /////////////////////////////////////////////////////////////////////
			// 
			// Freeing streams
			//
			// /////////////////////////////////////////////////////////////////////
			if (closeMe&&inStream!=null)// 
				try{
					inStream.close();
				}catch (Throwable t) {
				}
		}
	}

    /**
     * Collect georeferencing information about this geotiff.
     * 
     * @param hints
     * @throws DataSourceException
     */
    private void getHRInfo(Hints hints) throws DataSourceException {
        ImageReader reader = null;
        ImageReader ovrReader = null;
        ImageInputStream ovrStream = null;
        try {
            // //
            //
            // Get a reader for this format
            //
            // //
            reader = READER_SPI.createReaderInstance();

            // //
            //
            // get the METADATA
            //
            // //
            inStream.mark();
            reader.setInput(inStream);
            final IIOMetadata iioMetadata = reader.getImageMetadata(0);
            final GeoTiffIIOMetadataDecoder metadata = new GeoTiffIIOMetadataDecoder(iioMetadata);
            gtcs = new GeoTiffMetadata2CRSAdapter(hints);
            
            // //
            //
            // get the CRS INFO
            //
            // //
            final Object tempCRS = this.hints.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
            if (tempCRS != null) {
                this.crs = (CoordinateReferenceSystem) tempCRS;
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Using forced coordinate reference system");
            } else {
            	
            	// check external prj first
            	crs = getCRS(source);
                                
            	// now, if we did not want to override the inner CRS or we did not have any external PRJ at hand
            	// let's look inside the geotiff
                if (!OVERRIDE_INNER_CRS || crs==null){
                	if(metadata.hasGeoKey()&& gtcs != null){
                	    crs = gtcs.createCoordinateSystem(metadata);
                	}
                }


            }

            // 
            // No data
            //
            if (metadata.hasNoData()){
                noData = metadata.getNoData();
            }
            
            // 
            // parse and set layout
            // 
            setLayout(reader);
            
            //
            // parse TIFF StreamMetadata
            //
            dtLayout = TiffDatasetLayoutImpl.parseLayout(reader.getStreamMetadata());

            // Creating a new OverviewsProvider instance
            File inputFile = null;
            if (source instanceof File) {
                inputFile = (File) source;
            } else if (source instanceof URL && (((URL) source).getProtocol() == "file")) {
                inputFile = DataUtilities.urlToFile((URL) source);
            }
            if (inputFile != null) {
                maskOvrProvider = new MaskOverviewProvider(dtLayout, inputFile);
                hasMaskOvrProvider = true;
            } else if (dtLayout != null && dtLayout.getExternalMasks() != null) {
                String path = dtLayout.getExternalMasks().getAbsolutePath();
                maskOvrProvider = new MaskOverviewProvider(dtLayout, new File(path.substring(0,
                        path.length() - 4)));
                hasMaskOvrProvider = true;
            }
            
            // //
            //
            // get the dimension of the hr image and build the model as well as
            // computing the resolution
            // //
            numOverviews = hasMaskOvrProvider ? maskOvrProvider.getNumOverviews() : dtLayout.getNumInternalOverviews();
            int hrWidth = reader.getWidth(0);
            int hrHeight = reader.getHeight(0);
            final Rectangle actualDim = new Rectangle(0, 0, hrWidth, hrHeight);
            originalGridRange = new GridEnvelope2D(actualDim);

            if (gtcs != null&& metadata!=null&& (metadata.hasModelTrasformation()||(metadata.hasPixelScales()&&metadata.hasTiePoints()))) {
                this.raster2Model = GeoTiffMetadata2CRSAdapter.getRasterToModel(metadata);
            } else {
            	// world file
                this.raster2Model = parseWorldFile(source);
                
                // now world file --> mapinfo?
                if (raster2Model == null) {
                    MapInfoFileReader mifReader = parseMapInfoFile(source);
                    if(mifReader!=null){
                        raster2Model = mifReader.getTransform();
                        crs = mifReader.getCRS();
                    }

                }
            }
            
            if (crs == null){
                if(LOGGER.isLoggable(Level.WARNING)){
                    LOGGER.warning("Coordinate Reference System is not available");
                }
                crs = AbstractGridFormat.getDefaultCRS();
            }

            if (this.raster2Model == null) {
                TiePoint[] modelTiePoints = metadata.getModelTiePoints();
                if (modelTiePoints != null && modelTiePoints.length > 1) {
                    // use a unit transform and expose the GCPs
                    gcps = new GroundControlPoints(Arrays.asList(modelTiePoints), crs);
                    raster2Model = ProjectiveTransform.create(new AffineTransform());
                    crs = AbstractGridFormat.getDefaultCRS();
                } else {
                    throw new DataSourceException("Raster to Model Transformation is not available");
                }
            }

            // create envelope using corner transformation
            final AffineTransform tempTransform = new AffineTransform(
                    (AffineTransform) raster2Model);
            tempTransform.concatenate(CoverageUtilities.CENTER_TO_CORNER);
            originalEnvelope = CRS.transform(ProjectiveTransform.create(tempTransform),
                    new GeneralEnvelope(actualDim));
            originalEnvelope.setCoordinateReferenceSystem(crs);

            // ///
            // 
            // setting the higher resolution available for this coverage
            //
            // ///
            highestRes = new double[2];
            highestRes[0] = XAffineTransform.getScaleX0(tempTransform);
            highestRes[1] = XAffineTransform.getScaleY0(tempTransform);

            // External Overview management
            if (maskOvrProvider != null) {
                extOvrImgChoice = maskOvrProvider.getNumExternalOverviews() > 0 ? maskOvrProvider
                        .getNumInternalOverviews() + 1 : -1;
            } else {
                File extOvrFile = dtLayout.getExternalOverviews();
                if (extOvrFile != null && extOvrFile.exists()) {
                    // Setting the overview file
                    ovrSource = extOvrFile;
                    ovrInStreamSPI = ImageIOExt.getImageInputStreamSPI(extOvrFile);
                    ovrReader = READER_SPI.createReaderInstance();
                    ovrStream = ovrInStreamSPI.createInputStreamInstance(extOvrFile,
                            ImageIO.getUseCache(), ImageIO.getCacheDirectory());
                    ovrReader.setInput(ovrStream);
                    // this includes the real image as this is a image index, we need to add one.
                    extOvrImgChoice = numOverviews + 1;
                    numOverviews = numOverviews + dtLayout.getNumExternalOverviews();
                    if (numOverviews < extOvrImgChoice)
                        extOvrImgChoice = -1;
                }
            }
            
            // //
            //
            // get information for the successive images
            //
            // //
            if (numOverviews >= 1) {
                overViewResolutions = new double[numOverviews][2];
                // Internal overviews start at 1, so lastInternalOverview matches numOverviews if no
                // external.
                int firstExternalOverview = extOvrImgChoice == -1 ? numOverviews
                        : extOvrImgChoice - 1;
                double spanRes0 = highestRes[0] * this.originalGridRange.getSpan(0);
                double spanRes1 = highestRes[1] * this.originalGridRange.getSpan(1);
                if (maskOvrProvider != null) {
                    overViewResolutions = maskOvrProvider
                            .getOverviewResolutions(spanRes0, spanRes1);
                } else {

                    for (int i = 0; i < firstExternalOverview; i++) {
                        // Setting the correct overview index
                        int overviewImageIndex = dtLayout.getInternalOverviewImageIndex(i + 1);
                        int index = overviewImageIndex >= 0 ? overviewImageIndex : 0;
                        overViewResolutions[i][0] = spanRes0 / reader.getWidth(index);
                        overViewResolutions[i][1] = spanRes1 / reader.getHeight(index);
                    }
                    for (int i = firstExternalOverview; i < numOverviews; i++) {
                        overViewResolutions[i][0] = spanRes0
                                / ovrReader.getWidth(i - firstExternalOverview);
                        overViewResolutions[i][1] = spanRes1
                                / ovrReader.getHeight(i - firstExternalOverview);
                    }
                }
            } else
                overViewResolutions = null;
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
	 * This method reads in the TIFF image, constructs an appropriate CRS,
	 * determines the math transform from raster to the CRS model, and
	 * constructs a GridCoverage.
	 * 
	 * @param params
	 *            currently ignored, potentially may be used for hints.
	 * 
	 * @return grid coverage represented by the image
	 * 
	 * @throws IOException
	 *             on any IO related troubles
	 */
	public GridCoverage2D read(GeneralParameterValue[] params) throws IOException {
		GeneralEnvelope requestedEnvelope = null;
		Rectangle dim = null;
		Color inputTransparentColor=null;
		OverviewPolicy overviewPolicy=null;
		int[] suggestedTileSize=null;
		if (params != null) {

			//
			// Checking params
			//
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					final ParameterValue param = (ParameterValue) params[i];
					final ReferenceIdentifier name = param.getDescriptor().getName();
					if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
						final GridGeometry2D gg = (GridGeometry2D) param.getValue();
						requestedEnvelope = new GeneralEnvelope((Envelope) gg.getEnvelope2D());
						dim = gg.getGridRange2D().getBounds();
						continue;
					}
					if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName())) {
						overviewPolicy=(OverviewPolicy) param.getValue();
						continue;
					}	
                    if (name.equals(AbstractGridFormat.INPUT_TRANSPARENT_COLOR.getName())) {
                        inputTransparentColor = (Color) param.getValue();
                        continue;
                    }	
                    if (name.equals(AbstractGridFormat.SUGGESTED_TILE_SIZE.getName())) {
                        String suggestedTileSize_= (String) param.getValue();
                        if(suggestedTileSize_!=null&&suggestedTileSize_.length()>0){
                            suggestedTileSize_=suggestedTileSize_.trim();
                            int commaPosition=suggestedTileSize_.indexOf(",");
                            if(commaPosition<0){
                                int tileDim=Integer.parseInt(suggestedTileSize_);
                                suggestedTileSize= new int[]{tileDim,tileDim};
                            } else {
                                int tileW=Integer.parseInt(suggestedTileSize_.substring(0,commaPosition));
                                int tileH=Integer.parseInt(suggestedTileSize_.substring(commaPosition+1));
                                suggestedTileSize= new int[]{tileW,tileH};
                            }
                        }
                        continue;
                    }                                               
				}
			}
		}

		//
		// set params
		//
		Integer imageChoice = new Integer(0);
		final ImageReadParam readP = new ImageReadParam();
		try {
			imageChoice = setReadParams(overviewPolicy, readP,requestedEnvelope, dim);
		} catch (TransformException e) {
			new DataSourceException(e);
		}

		//
		// IMAGE READ OPERATION
		//
        Hints newHints = null;
		if(suggestedTileSize!=null){
		    newHints= hints.clone();
            final ImageLayout layout = new ImageLayout();
            layout.setTileGridXOffset(0);
            layout.setTileGridYOffset(0);
            layout.setTileHeight(suggestedTileSize[1]);
            layout.setTileWidth(suggestedTileSize[0]);
            newHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
		}
		final ParameterBlock pbjRead = new ParameterBlock();
            // Image Index used for the Overview management
            if (maskOvrProvider != null) {
                if (maskOvrProvider.isExternalOverview(imageChoice)) {
                    pbjRead.add(maskOvrProvider.getExternalOverviewInputStreamSpi()
                            .createInputStreamInstance(maskOvrProvider.getOvrURL(),
                                    ImageIO.getUseCache(), ImageIO.getCacheDirectory()));
                } else {
                    pbjRead.add(maskOvrProvider.getInputStreamSpi().createInputStreamInstance(
                            maskOvrProvider.getFileURL(), ImageIO.getUseCache(),
                            ImageIO.getCacheDirectory()));
                }
                pbjRead.add(maskOvrProvider.getOverviewIndex(imageChoice));
            } else {
                if (extOvrImgChoice >= 0 && imageChoice >= extOvrImgChoice) {
                    pbjRead.add(ovrInStreamSPI.createInputStreamInstance(ovrSource,
                            ImageIO.getUseCache(), ImageIO.getCacheDirectory()));
                    pbjRead.add(imageChoice - extOvrImgChoice);
                } else {
                    if(inStream instanceof ImageInputStream && !closeMe) {
                        pbjRead.add(inStream);
                    }
                    else {
                        pbjRead.add(inStreamSPI != null ? inStreamSPI.createInputStreamInstance(source,
                                ImageIO.getUseCache(), ImageIO.getCacheDirectory()) : ImageIO
                                .createImageInputStream(source));
                    }
                    // Setting correct ImageChoice (taking into account overviews and masks)
                    int overviewImageIndex = dtLayout.getInternalOverviewImageIndex(imageChoice);
                    int index = overviewImageIndex >= 0 ? overviewImageIndex : 0;
                    pbjRead.add(index);
                }
            }
    	pbjRead.add(Boolean.FALSE);
		pbjRead.add(Boolean.FALSE);
		pbjRead.add(Boolean.FALSE);
		pbjRead.add(null);
		pbjRead.add(null);
		pbjRead.add(readP);
		pbjRead.add(READER_SPI.createReaderInstance());
		RenderedOp coverageRaster=JAI.create("ImageRead", pbjRead,newHints!=null?(RenderingHints) newHints:null);
	
        //
        // MASKING INPUT COLOR as indicated
        //
		if(inputTransparentColor!=null){
		    coverageRaster= new ImageWorker(coverageRaster).setRenderingHints(newHints).makeColorTransparent(inputTransparentColor).getRenderedOperation();
		}
		
        //
        // External/Internal Masking
        //
        // ROI definition
        ROI roi = null;
        // Using MaskOvrProvider
        if (hasMaskOvrProvider) {
            // Parameter definiton
            GridEnvelope ogr = getOriginalGridRange();
            Rectangle sourceRegion;
            if (readP.getSourceRegion() != null) {
                sourceRegion = readP.getSourceRegion();
            } else {
                sourceRegion = new Rectangle(ogr.getSpan(0), ogr.getSpan(1));
            }

            MaskInfo info = maskOvrProvider.getMaskInfo(imageChoice, sourceRegion, readP);
            if (info != null) {
                // Reading Mask
                RenderedOp roiRaster = readROIRaster(info.streamSpi,
                        DataUtilities.fileToURL(info.file), info.index, newHints,
                        info.readParameters);
                roi = MaskOverviewProvider.scaleROI(roiRaster, coverageRaster.getBounds());
            }
        }

        //
        // BUILDING COVERAGE
        //
        AffineTransform rasterToModel = getRescaledRasterToModel(coverageRaster);
        try {
            return createCoverage(coverageRaster, ProjectiveTransform.create(rasterToModel), roi);
        } catch (Exception e) {
            // dispose and close file
            ImageUtilities.disposePlanarImageChain(coverageRaster);

            // rethrow
            if (e instanceof DataSourceException) {
                throw (DataSourceException) e;
            }
            throw new DataSourceException(e);
        }
	}

    /**
     * General method for reading an input ROI Mask from a file
     * 
     * @return A {@link RenderedOp} representing the Raster ROI
     */
    private RenderedOp readROIRaster(ImageInputStreamSpi spi, URL inFile, int index,
            RenderingHints newHints, ImageReadParam readP) {
        // Raster initialization
        RenderedOp raster = null;
        try {
            // ParameterBlock creation
            ParameterBlock pb = new ParameterBlock();
            pb.add(spi.createInputStreamInstance(inFile, ImageIO.getUseCache(),
                    ImageIO.getCacheDirectory()));
            pb.add(index);
            pb.add(Boolean.FALSE);
            pb.add(Boolean.FALSE);
            pb.add(Boolean.FALSE);
            pb.add(null);
            pb.add(null);
            pb.add(readP);
            pb.add(READER_SPI.createReaderInstance());
            raster = JAI.create("ImageRead", pb, newHints != null ? (RenderingHints) newHints
                    : null);
        } catch (Exception e) {
            LOGGER.severe("Unable to read input Mask Band for coverage: " + coverageName);
        }

        return raster;
    }

    /**
     * Returns the geotiff metadata for this geotiff file.
     * 
     * @return the metadata
     */
    public GeoTiffIIOMetadataDecoder getMetadata() {
        GeoTiffIIOMetadataDecoder metadata = null;
        ImageReader reader = null;
        boolean closeMe = true;
        ImageInputStream stream = null;
        
        try {
            if ((source instanceof InputStream)|| (source instanceof ImageInputStream)){
                closeMe = false;
            }
            if (source instanceof ImageInputStream ) {
                    stream =(ImageInputStream) source;
            } else {
                inStreamSPI = ImageIOExt.getImageInputStreamSPI(source);
                if (inStreamSPI == null) {
                    throw new IllegalArgumentException("No input stream for the provided source");
                }
                stream = inStreamSPI.createInputStreamInstance(source, ImageIO.getUseCache(), ImageIO.getCacheDirectory());
            }
            if (stream == null) {
                    throw new IllegalArgumentException("No input stream for the provided source");
            }
            stream.mark();
            reader = READER_SPI.createReaderInstance();
            reader.setInput(stream);
            final IIOMetadata iioMetadata = reader.getImageMetadata(0);
            metadata = new GeoTiffIIOMetadataDecoder(iioMetadata);
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        } finally {
            if (reader != null)
                try {
                    reader.dispose();
                } catch (Throwable t) {
                }

            if (stream != null){
                try {
                    stream.reset();
                } catch (Throwable t) {
                }
                if (closeMe){
                    try {
                        stream.close();
                    } catch (Throwable t) {
                    }
                }
            }
        }
        return metadata;
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
	 * @param roi Optional ROI used as Mask
         * @return a {@link GridCoverage}
         * @throws IOException
         */
        protected final GridCoverage2D createCoverage(PlanarImage image, MathTransform raster2Model, ROI roi) throws IOException {

        //creating bands
        final SampleModel sm = image.getSampleModel();
        final ColorModel cm = image.getColorModel();
        final int numBands = sm.getNumBands();
        final GridSampleDimension[] bands = new GridSampleDimension[numBands];
        // setting bands names.
        
        Category noDataCategory = null;
        final Map<String, Object> properties = new HashMap<String, Object>();        
        if (!Double.isNaN(noData)){
            noDataCategory = new Category(Vocabulary
                    .formatInternational(VocabularyKeys.NODATA), new Color[] { new Color(0, 0, 0, 0) }, NumberRange
                    .create(noData, noData));
            CoverageUtilities.setNoDataProperty(properties, new Double(noData));
        }
        // Setting ROI Property
        if (roi != null) {
            image.setProperty("ROI", roi);
            CoverageUtilities.setROIProperty(properties, roi);
        }
        
        Set<String> bandNames = new HashSet<String>();
        for (int i = 0; i < numBands; i++) {
                final ColorInterpretation colorInterpretation=TypeMap.getColorInterpretation(cm, i);
                if(colorInterpretation==null)
                       throw new IOException("Unrecognized sample dimension type");
                Category[] categories = null;
                if (noDataCategory != null) {
                    categories = new Category[]{noDataCategory};
                }
                String bandName = colorInterpretation.name();
                // make sure we create no duplicate band names
                if(colorInterpretation == ColorInterpretation.UNDEFINED || bandNames.contains(bandName)) {
                    bandName = "Band" + (i + 1);
                } 
                bands[i] = new GridSampleDimension(bandName,categories,null);
        }
        // creating coverage
        if (raster2Model != null) {
                return coverageFactory.create(coverageName, image, crs,raster2Model, bands, null, properties);
        }
        return coverageFactory.create(coverageName, image, new GeneralEnvelope(originalEnvelope), bands, null, properties);

        }
	
	
        private CoordinateReferenceSystem getCRS(Object source) {
            CoordinateReferenceSystem crs = null;
        if (source instanceof File
                || (source instanceof URL && (((URL) source).getProtocol() == "file"))) {
            // getting name for the prj file
            final String sourceAsString;

            if (source instanceof File) {
                sourceAsString = ((File) source).getAbsolutePath();
            } else {
                String auth = ((URL) source).getAuthority();
                String path = ((URL) source).getPath();
                if (auth != null && !auth.equals("")) {
                    sourceAsString = "//" + auth + path;
                } else {
                    sourceAsString = path;
                }
            }

            final int index = sourceAsString.lastIndexOf(".");
            final String base=index>0?sourceAsString.substring(0, index)+".prj":sourceAsString+".prj";

            // does it exist?
            final File prjFile = new File(base.toString());
            if (prjFile.exists()) {
                // it exists then we have top read it
                PrjFileReader projReader = null;
                FileInputStream instream=null;
                try {
                	instream=new FileInputStream(prjFile);
                    final FileChannel channel = instream.getChannel();
                    projReader = new PrjFileReader(channel);
                    crs = projReader.getCoordinateReferenceSystem();
                } catch (FileNotFoundException e) {
                    // warn about the error but proceed, it is not fatal
                    // we have at least the default crs to use
                    LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
                } catch (IOException e) {
                    // warn about the error but proceed, it is not fatal
                    // we have at least the default crs to use
                    LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
                } catch (FactoryException e) {
                    // warn about the error but proceed, it is not fatal
                    // we have at least the default crs to use
                    LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
                } finally {
                    if (projReader != null)
                        try {
                            projReader.close();
                        } catch (IOException e) {
                            // warn about the error but proceed, it is not fatal
                            // we have at least the default crs to use
                            LOGGER .log(Level.FINE, e.getLocalizedMessage(),e);
                        }
                        
                        if (instream != null)
                            try {
                                instream.close();
                            } catch (IOException e) {
                                // warn about the error but proceed, it is not fatal
                                // we have at least the default crs to use
                                LOGGER.log(Level.FINE, e.getLocalizedMessage(),e);
                            }                        
                }

            }
        }
        return crs;
    }
        
        
    /**
     * @throws IOException
     */
    static MathTransform parseWorldFile(Object source) throws IOException {
        MathTransform raster2Model = null;

        // TODO: Add support for FileImageInputStreamExt
        // TODO: Check for WorldFile on URL beside the actual connection.
        if (source instanceof File) {
            final File sourceFile = ((File) source);
            String parentPath = sourceFile.getParent();
            String filename = sourceFile.getName();
            final int i = filename.lastIndexOf('.');
            filename = (i == -1) ? filename : filename.substring(0, i);
            
            // getting name and extension
            final String base = (parentPath != null) ? new StringBuilder(
                    parentPath).append(File.separator).append(filename)
                    .toString() : filename;

            // We can now construct the baseURL from this string.
            File file2Parse = new File(new StringBuilder(base).append(".wld")
                    .toString());

            if (file2Parse.exists()) {
                final WorldFileReader reader = new WorldFileReader(file2Parse);
                raster2Model = reader.getTransform();
            } else {
                // looking for another extension
                file2Parse = new File(new StringBuilder(base).append(".tfw")
                        .toString());

                if (file2Parse.exists()) {
                    // parse world file
                    final WorldFileReader reader = new WorldFileReader(
                            file2Parse);
                    raster2Model = reader.getTransform();
                }
            }
        }
        return raster2Model;
    }
    
    /**
     * @throws IOException
     */
    static MapInfoFileReader parseMapInfoFile(Object source) throws IOException {
        if (source instanceof File) {
            final File sourceFile = ((File) source);
            File file2Parse = getSibling(sourceFile, ".tab");

            if (file2Parse != null && file2Parse.exists()) {
                final MapInfoFileReader reader = new MapInfoFileReader(file2Parse);
                return reader;
            }
        }
        return null;
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

    @Override
    public GroundControlPoints getGroundControlPoints() {
        return gcps;
    }

    @Override
    protected List<FileGroup> getFiles() {
        File file = getSourceAsFile();
        if (file == null) {
            return null;
        }

        List<File> files = new ArrayList<>();
        // add all common sidecars
        addAllSiblings(file, files, ".prj", ".tab", ".wld", ".tfw");
        if (hasMaskOvrProvider) {
            DatasetLayout layout = maskOvrProvider.getLayout();
            addSiblings(files, layout.getExternalMaskOverviews(), layout.getExternalOverviews(),
                    layout.getExternalMasks());
        }
        return Collections.singletonList(new FileGroup(file, files, null));
    }
}
