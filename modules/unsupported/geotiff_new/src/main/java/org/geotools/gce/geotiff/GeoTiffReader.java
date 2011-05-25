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

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataDecoder;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffMetadata2CRSAdapter;
import org.geotools.data.DataSourceException;
import org.geotools.data.PrjFileReader;
import org.geotools.data.WorldFileReader;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/geotiff_new/src/main/java/org/geotools/gce/geotiff/GeoTiffReader.java $
 */
@SuppressWarnings("deprecation")
public final class GeoTiffReader extends AbstractGridCoverage2DReader implements
		GridCoverageReader {

        
    
	/**
	 * Number of coverages for this reader is 1
	 * 
	 * @return the number of coverages for this reader.
	 */
	@Override
	public int getGridCoverageCount() {
		return 1;
	}

	/** Logger for the {@link GeoTiffReader} class. */
	private Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeoTiffReader.class.toString());

	/** SPI for creating tiff readers in ImageIO tools */
	private final static TIFFImageReaderSpi readerSPI = new TIFFImageReaderSpi();

	/** Decoder for the GeoTiff metadata. */
	private GeoTiffIIOMetadataDecoder metadata;

	/** Adapter for the GeoTiff crs. */
	private GeoTiffMetadata2CRSAdapter gtcs;
	
	private double noData = Double.NaN;
	
	private RasterManager rasterManager;

    URL sourceURL;
    
    boolean expandMe;

	RasterLayout[] overViewLayouts;

	RasterLayout hrLayout;

	@Override
        public void dispose() {
            super.dispose();
            rasterManager.dispose();
        }

    /**
	 * Let us retrieve the {@link GridCoverageFactory} that we want to use.
	 * 
	 * @return
	 *                  retrieves the {@link GridCoverageFactory} that we want to use.
	 */
	 GridCoverageFactory getGridCoverageFactory(){
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
		// /////////////////////////////////////////////////////////////////////
		// 
		// Forcing longitude first since the geotiff specification seems to
		// assume that we have first longitude the latitude.
		//
		// /////////////////////////////////////////////////////////////////////
		if (hints == null)
			this.hints= new Hints();	
		if (uHints != null) {
			// prevent the use from reordering axes
			uHints.remove(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
			this.hints.add(uHints);
			this.hints.add(new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER,Boolean.TRUE));
			
		}
		this.coverageFactory= CoverageFactoryFinder.getGridCoverageFactory(this.hints);
		coverageName = "geotiff_coverage";


		//
		// Seting input
		//
		if (input == null) {

			final IOException ex = new IOException("GeoTiffReader:No source set to read this coverage.");
			throw new DataSourceException(ex);
		} 

		//
		// Set the source being careful in case it is an URL pointing to a file
		//
		try {
			this.source = input;
			// setting source
			if (input instanceof URL) {
				final URL sourceURL = (URL) input;
				if (sourceURL.getProtocol().equalsIgnoreCase("http")
						|| sourceURL.getProtocol().equalsIgnoreCase("ftp")) {
					try {
						source = sourceURL.openStream();
					} catch (IOException e) {
						new RuntimeException(e);
					}
				} else if (sourceURL.getProtocol().equalsIgnoreCase("file"))
					source = new File(URLDecoder.decode(sourceURL.getFile(),
							"UTF-8"));
			}

			closeMe = true;
			// /////////////////////////////////////////////////////////////////////
			//
			// Get a stream in order to read from it for getting the basic
			// information for this coverage
			//
			// /////////////////////////////////////////////////////////////////////
			if ((source instanceof InputStream)
					|| (source instanceof ImageInputStream))
				closeMe = false;
			if(source instanceof ImageInputStream )
				inStream=(ImageInputStream) source;
			else
				inStream = ImageIO.createImageInputStream(source);
			if (inStream == null)
				throw new IllegalArgumentException(
						"No input stream for the provided source");
			
			this.sourceURL=Utils.checkSource(source);
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

			// /////////////////////////////////////////////////////////////////////
			// 
			// Freeing streams
			//
			// /////////////////////////////////////////////////////////////////////
			if (closeMe)
				inStream.close();
		} catch (IOException e) {
			throw new DataSourceException(e);
		} catch (TransformException e) {
			throw new DataSourceException(e);
		} catch (FactoryException e) {
			throw new DataSourceException(e);
		}

		rasterManager = new RasterManager(this);
	}

	/**
	 * 
	 * @param hints
	 * @throws IOException
	 * @throws FactoryException
	 * @throws GeoTiffException
	 * @throws TransformException
	 * @throws MismatchedDimensionException
	 * @throws DataSourceException
	 */
	private void getHRInfo(Hints hints) throws IOException, FactoryException,
			GeoTiffException, TransformException, MismatchedDimensionException,
			DataSourceException {
		// //
		//
		// Get a reader for this format
		//
		// //
		final ImageReader reader = readerSPI.createReaderInstance();

		// //
		//
		// get the METADATA
		//
		// //
		reader.setInput(inStream);
		final IIOMetadata iioMetadata = reader.getImageMetadata(0);
		CoordinateReferenceSystem foundCrs = null;
		boolean useWorldFile = false;
		
		try{
		    metadata = new GeoTiffIIOMetadataDecoder(iioMetadata);
		    gtcs = (GeoTiffMetadata2CRSAdapter) GeoTiffMetadata2CRSAdapter.get(hints);
		    if (gtcs != null)
		    	foundCrs = gtcs.createCoordinateSystem(metadata);
		    else 
		        useWorldFile = true;
		    
		    if (metadata.hasNoData())
	                    noData = metadata.getNoData();
		}catch (IllegalArgumentException iae){
		    useWorldFile = true;
		}catch (UnsupportedOperationException uoe){
		    useWorldFile = true;
		}
		
		
		// //
		//
		// get the CRS INFO
		//
		// //
		final Object tempCRS = this.hints
				.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
		if (tempCRS != null) {
			this.crs = (CoordinateReferenceSystem) tempCRS;
			LOGGER.log(Level.WARNING, new StringBuilder(
					"Using forced coordinate reference system ").append(
					crs.toWKT()).toString());
		} else{
		    if (useWorldFile)
		        foundCrs = getCRS(source);
		    crs = foundCrs;
		}
		
		if (crs == null){
                    throw new DataSourceException ("Coordinate Reference System is not available");
        }
			

		// //
		//
		// get the dimension of the hr image and build the model as well as
		// computing the resolution
		// //
		numOverviews = reader.getNumImages(true) - 1;
		final int hrWidth = reader.getWidth(0);
		final int hrHeight = reader.getHeight(0);
		final int hrTileW= reader.getTileWidth(0);
		final int hrTileH= reader.getTileHeight(0);
		hrLayout= new RasterLayout(0,0,hrWidth,hrHeight,0,0,hrTileW,hrTileH);
		final Rectangle actualDim = new Rectangle(0, 0, hrWidth, hrHeight);
		originalGridRange = new GridEnvelope2D(actualDim);

		if (!useWorldFile && gtcs != null) {
		    this.raster2Model = gtcs.getRasterToModel(metadata);
        }
        else {
            this.raster2Model = parseWorldFile(source);
        }
		
		if (this.raster2Model == null){
                    throw new DataSourceException ("Raster to Model Transformation is not available");
        }
		
		final AffineTransform tempTransform = new AffineTransform((AffineTransform) raster2Model);
		tempTransform.translate(-0.5, -0.5);
		originalEnvelope = CRS.transform(ProjectiveTransform.create(tempTransform), new GeneralEnvelope(actualDim));
		originalEnvelope.setCoordinateReferenceSystem(crs);

		// ///
		// 
		// setting the higher resolution available for this coverage
		//
		// ///
		highestRes = new double[2];
		highestRes[0]=XAffineTransform.getScaleX0(tempTransform);
		highestRes[1]=XAffineTransform.getScaleY0(tempTransform);

		// //
		//
		// get information for the successive images
		//
		// //
		
		if (numOverviews >= 1) {
			overViewResolutions = new double[numOverviews][2];
			overViewLayouts= new RasterLayout[numOverviews];
			for (int i = 0; i < numOverviews; i++) {
				//getting raster layout
				final int width=reader.getWidth(i+1);
				final int height=reader.getHeight(i+1);
				final int tileW=reader.getTileWidth(i+1);
				final int tileH=reader.getTileHeight(i+1);
				
				overViewLayouts[i]= new RasterLayout(0,0,width,height,0,0,tileW,tileH);
				
				//computing resolutions
				overViewResolutions[i][0] = (highestRes[0]*hrWidth)/width;
				overViewResolutions[i][1] = (highestRes[1]*hrHeight)/height;
			}
		} else
			overViewResolutions = null;
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
	 */
	public Format getFormat() {
		return new GeoTiffFormat();
	}

//	/**
//	 * This method reads in the TIFF image, constructs an appropriate CRS,
//	 * determines the math transform from raster to the CRS model, and
//	 * constructs a GridCoverage.
//	 * 
//	 * @param params
//	 *            currently ignored, potentially may be used for hints.
//	 * 
//	 * @return grid coverage represented by the image
//	 * 
//	 * @throws IOException
//	 *             on any IO related troubles
//	 */
//	public GridCoverage read(GeneralParameterValue[] params) throws IOException {
//		GeneralEnvelope requestedEnvelope = null;
//		Rectangle dim = null;
//		OverviewPolicy overviewPolicy=null;
//		if (params != null) {
//			// /////////////////////////////////////////////////////////////////////
//			//
//			// Checking params
//			//
//			// /////////////////////////////////////////////////////////////////////
//			if (params != null) {
//				for (int i = 0; i < params.length; i++) {
//					final ParameterValue param = (ParameterValue) params[i];
//					final String name = param.getDescriptor().getName().getCode();
//					if (name.equals(
//							AbstractGridFormat.READ_GRIDGEOMETRY2D.getName()
//									.toString())) {
//						final GridGeometry2D gg = (GridGeometry2D) param
//								.getValue();
//						requestedEnvelope = new GeneralEnvelope((Envelope) gg
//								.getEnvelope2D());
//						dim = gg.getGridRange2D().getBounds();
//						continue;
//					}
//					if (name.equals(AbstractGridFormat.OVERVIEW_POLICY
//							.getName().toString())) {
//						overviewPolicy=(OverviewPolicy) param.getValue();
//						continue;
//					}					
//				}
//			}
//		}
//		// /////////////////////////////////////////////////////////////////////
//		//
//		// set params
//		//
//		// /////////////////////////////////////////////////////////////////////
//		Integer imageChoice = new Integer(0);
//		final ImageReadParam readP = new ImageReadParam();
//		try {
//			imageChoice = setReadParams(overviewPolicy, readP,
//					requestedEnvelope, dim);
//		} catch (TransformException e) {
//			new DataSourceException(e);
//		}
//
//		// /////////////////////////////////////////////////////////////////////
//		//
//		// IMAGE READ OPERATION
//		//
//		// /////////////////////////////////////////////////////////////////////
////		final ImageReader reader = readerSPI.createReaderInstance();
////		final ImageInputStream inStream = ImageIO
////				.createImageInputStream(source);
////		reader.setInput(inStream);
//		final Hints newHints = (Hints) hints.clone();
////		if (!reader.isImageTiled(imageChoice.intValue())) {
////			final Dimension tileSize = ImageUtilities.toTileSize(new Dimension(
////					reader.getWidth(imageChoice.intValue()), reader
////							.getHeight(imageChoice.intValue())));
////			final ImageLayout layout = new ImageLayout();
////			layout.setTileGridXOffset(0);
////			layout.setTileGridYOffset(0);
////			layout.setTileHeight(tileSize.height);
////			layout.setTileWidth(tileSize.width);
////			newHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
////		}
////		inStream.close();
////		reader.reset();
//		final ParameterBlock pbjRead = new ParameterBlock();
//		pbjRead.add(ImageIO.createImageInputStream(source));
//		pbjRead.add(imageChoice);
//		pbjRead.add(Boolean.FALSE);
//		pbjRead.add(Boolean.FALSE);
//		pbjRead.add(Boolean.FALSE);
//		pbjRead.add(null);
//		pbjRead.add(null);
//		pbjRead.add(readP);
//		pbjRead.add( readerSPI.createReaderInstance());
//		final RenderedOp coverageRaster=JAI.create("ImageRead", pbjRead,
//                        (RenderingHints) newHints);
//
//		// /////////////////////////////////////////////////////////////////////
//		//
//		// BUILDING COVERAGE
//		//
//		// /////////////////////////////////////////////////////////////////////
//                // I need to calculate a new transformation (raster2Model)
//                // between the cropped image and the required
//                // adjustedRequestEnvelope
//                final int ssWidth = coverageRaster.getWidth();
//                final int ssHeight = coverageRaster.getHeight();
//                if (LOGGER.isLoggable(Level.FINE)) {
//                        LOGGER.log(Level.FINE, "Coverage read: width = " + ssWidth
//                                        + " height = " + ssHeight);
//                }
//
//                // //
//                //
//                // setting new coefficients to define a new affineTransformation
//                // to be applied to the grid to world transformation
//                // -----------------------------------------------------------------------------------
//                //
//                // With respect to the original envelope, the obtained planarImage
//                // needs to be rescaled. The scaling factors are computed as the
//                // ratio between the cropped source region sizes and the read
//                // image sizes.
//                //
//                // //
//                final double scaleX = originalGridRange.getLength(0) / (1.0 * ssWidth);
//                final double scaleY = originalGridRange.getLength(1) / (1.0 * ssHeight);
//                final AffineTransform tempRaster2Model = new AffineTransform((AffineTransform) raster2Model);
//                tempRaster2Model.concatenate(new AffineTransform(scaleX, 0, 0, scaleY, 0, 0));
//                return createCoverage(coverageRaster, ProjectiveTransform.create((AffineTransform) tempRaster2Model));
//
//
//	}
	
	/**
         * @see org.opengis.coverage.grid.GridCoverageReader#read(org.opengis.parameter.GeneralParameterValue[])
         */
	@Override
        public GridCoverage2D read(GeneralParameterValue[] params) throws IOException {

                if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Reading image from " + sourceURL.toString());
                        LOGGER.fine(new StringBuffer("Highest res ").append(highestRes[0])
                                        .append(" ").append(highestRes[1]).toString());
                }

                final Collection<GridCoverage2D> response = rasterManager.read(params);
                if(response.isEmpty())
                        throw new DataSourceException("Unable to create a coverage for this request ");
                else
                        return response.iterator().next();
        }

	/**
	 * Returns the geotiff metadata for this geotiff file.
	 * 
	 * @return the metadata
	 */
	public GeoTiffIIOMetadataDecoder getMetadata() {
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
                
                Category noDataCategory = null;
                if (!Double.isNaN(noData)){
                    noDataCategory = new Category(Vocabulary
                            .formatInternational(VocabularyKeys.NODATA), new Color[] { new Color(0, 0, 0, 0) }, NumberRange
                            .create(noData, noData), NumberRange
                            .create(noData, noData));
                }
                
                for (int i = 0; i < numBands; i++) {
                        final ColorInterpretation colorInterpretation=TypeMap.getColorInterpretation(cm, i);
                        if(colorInterpretation==null)
                               throw new IOException("Unrecognized sample dimension type");
                        Category[] categories = null;
                        if (noDataCategory != null)
                            categories = new Category[]{noDataCategory};
                        bands[i] = new GridSampleDimension(colorInterpretation.name(),categories,null).geophysics(true);
                }
                // creating coverage
                if (raster2Model != null) {
                        return coverageFactory.create(coverageName, image, crs,
                                        raster2Model, bands, null, null);
                }
                return coverageFactory.create(coverageName, image, new GeneralEnvelope(
                                originalEnvelope), bands, null, null);

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
            final StringBuilder base = new StringBuilder(sourceAsString
                    .substring(0, index)).append(".prj");

            // does it exist?
            final File prjFile = new File(base.toString());
            if (prjFile.exists()) {
                // it exists then we have top read it
                PrjFileReader projReader = null;
                try {
                    final FileChannel channel = new FileInputStream(prjFile)
                            .getChannel();
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
                            LOGGER
                                    .log(Level.SEVERE, e.getLocalizedMessage(),
                                            e);
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
    
    String getName() {
        return super.coverageName;
    }

}
