package org.geotools.coverage.io.geotiff;


// JAI ImageIO Tools dependencies
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffException;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataDecoder;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffMetadata2CRSAdapter;
import org.geotools.data.DataSourceException;
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
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi;

/**
 * this class is responsible for exposing the data and the Georeferencing
 * metadata available to the Geotools library. This reader is heavily based on
 * the capabilities provided by the ImageIO tools and JAI libraries.
 * 
 * 
 * @author Bryce Nordgren, USDA Forest Service
 * @author Simone Giannecchini
 * @since 2.1
 * @source $URL:
 *         http://svn.geotools.org/geotools/branches/coverages_branch/trunk/gt/plugin/geotiff/src/org/geotools/gce/geotiff/GeoTiffReader.java $
 */
final class GeoTiffReader extends AbstractGridCoverage2DReader implements
		GridCoverageReader {

	@Override
	public int getGridCoverageCount() {
		return 1;
	}

	/** Logger for the {@link GeoTiffReader} class. */
	private Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeoTiffReader.class.toString());

	/** SPI for creating tiff readers in ImageIO tools */
	private final static TIFFImageReaderSpi readerSPI = new TIFFImageReaderSpi();
	
	/** noData value*/
	private double noData = Double.POSITIVE_INFINITY;

	/** Decoder for the GeoTiff metadata. */
	private GeoTiffIIOMetadataDecoder metadata;

	/** Adapter for the GeoTiff crs. */
	private GeoTiffMetadata2CRSAdapter gtcs;

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

		// /////////////////////////////////////////////////////////////////////
		//
		// Setting input
		//
		// /////////////////////////////////////////////////////////////////////
		if (input == null) {
			final IOException ex = new IOException(
					"GeoTiffReader:No source set to read this coverage.");
			throw new DataSourceException(ex);
		}
		// /////////////////////////////////////////////////////////////////////
		//
		// Set the source being careful in case it is an URL pointing to a file
		//
		// /////////////////////////////////////////////////////////////////////
		try {
			this.source = input;
			// setting source
			final URL sourceURL = (URL) input;
			if (sourceURL.getProtocol().equalsIgnoreCase("http")
					|| sourceURL.getProtocol().equalsIgnoreCase("ftp")) {
				try {
					source = sourceURL.openStream();
				} catch (IOException e) {
					new RuntimeException(e);
				}
			} else if (sourceURL.getProtocol().equalsIgnoreCase("file"))
				source = new File(URLDecoder.decode(sourceURL.getFile(),"UTF-8"));


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
			coverageName = source instanceof File ? ((File) source).getName()
					: "geotiff_coverage";
			final int dotIndex = coverageName.lastIndexOf('.');
			if (dotIndex != -1 && dotIndex != coverageName.length())
				coverageName = coverageName.substring(0, dotIndex);

			// /////////////////////////////////////////////////////////////////////
			// 
			// Freeing streams
			//
			// /////////////////////////////////////////////////////////////////////
			if (closeMe)// 
				inStream.close();
		} catch (IOException e) {
			throw new DataSourceException(e);
		} catch (TransformException e) {
			throw new DataSourceException(e);
		} catch (FactoryException e) {
			throw new DataSourceException(e);
		}
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
		// Get a reader for this formatr
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
		metadata = new GeoTiffIIOMetadataDecoder(iioMetadata);
		gtcs = (GeoTiffMetadata2CRSAdapter) GeoTiffMetadata2CRSAdapter.get(hints);
		if (metadata.hasNoData())
		    noData = metadata.getNoData();
		

		// //
		//
		// get the CRS INFO
		//
		// //
		final Object tempCRS = this.hints
				.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
		if (tempCRS != null) {
			this.crs = (CoordinateReferenceSystem) tempCRS;
			LOGGER.log(Level.WARNING, new StringBuffer(
					"Using forced coordinate reference system ").append(
					crs.toWKT()).toString());
		} else
			crs = gtcs.createCoordinateSystem(metadata);

		// //
		//
		// get the dimension of the hr image and build the model as well as
		// computing the resolution
		// //
		numOverviews = reader.getNumImages(true) - 1;
		int hrWidth = reader.getWidth(0);
		int hrHeight = reader.getHeight(0);
		final Rectangle actualDim = new Rectangle(0, 0, hrWidth, hrHeight);
		originalGridRange = new GridEnvelope2D(actualDim);

		this.raster2Model = gtcs.getRasterToModel(metadata);
		final AffineTransform tempTransform = new AffineTransform((AffineTransform) raster2Model);
		tempTransform.translate(-0.5, -0.5);
		originalEnvelope = CRS.transform(ProjectiveTransform.create(tempTransform), new GeneralEnvelope(actualDim));
		originalEnvelope.setCoordinateReferenceSystem(crs);

		// ///
		// 
		// setting the higher resolution avalaible for this coverage
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
			for (int i = 0; i < numOverviews; i++) {
				overViewResolutions[i][0] = (highestRes[0]*this.originalGridRange.getSpan(0))/reader.getWidth(i+1);
				overViewResolutions[i][1] = (highestRes[1]*this.originalGridRange.getSpan(1))/reader.getHeight(i+1);
			}
		} else
			overViewResolutions = null;
	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
	 */
	public Format getFormat() {
		return null;
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
	public GridCoverage read(GeneralParameterValue[] params) throws IOException {
		GeneralEnvelope requestedEnvelope = null;
		Rectangle dim = null;
		OverviewPolicy overviewPolicy=null;
		if (params != null) {
			// /////////////////////////////////////////////////////////////////////
			//
			// Checking params
			//
			// /////////////////////////////////////////////////////////////////////
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					final ParameterValue param = (ParameterValue) params[i];
					final String name = param.getDescriptor().getName().getCode();
					if (name.equals(
							AbstractGridFormat.READ_GRIDGEOMETRY2D.getName()
									.toString())) {
						final GridGeometry2D gg = (GridGeometry2D) param
								.getValue();
						requestedEnvelope = new GeneralEnvelope((Envelope) gg
								.getEnvelope2D());
						dim = gg.getGridRange2D().getBounds();
						continue;
					}
					if (name.equals(AbstractGridFormat.OVERVIEW_POLICY
							.getName().toString())) {
						overviewPolicy=(OverviewPolicy) param.getValue();
						continue;
					}					
				}
			}
		}
		// /////////////////////////////////////////////////////////////////////
		//
		// set params
		//
		// /////////////////////////////////////////////////////////////////////
		Integer imageChoice = new Integer(0);
		final ImageReadParam readP = new ImageReadParam();
		try {
			imageChoice = setReadParams(overviewPolicy, readP,
					requestedEnvelope, dim);
		} catch (TransformException e) {
			new DataSourceException(e);
		}

		// /////////////////////////////////////////////////////////////////////
		//
		// IMAGE READ OPERATION
		//
		// /////////////////////////////////////////////////////////////////////
//		final ImageReader reader = readerSPI.createReaderInstance();
//		final ImageInputStream inStream = ImageIO
//				.createImageInputStream(source);
//		reader.setInput(inStream);
		final Hints newHints = (Hints) hints.clone();
//		if (!reader.isImageTiled(imageChoice.intValue())) {
//			final Dimension tileSize = ImageUtilities.toTileSize(new Dimension(
//					reader.getWidth(imageChoice.intValue()), reader
//							.getHeight(imageChoice.intValue())));
//			final ImageLayout layout = new ImageLayout();
//			layout.setTileGridXOffset(0);
//			layout.setTileGridYOffset(0);
//			layout.setTileHeight(tileSize.height);
//			layout.setTileWidth(tileSize.width);
//			newHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
//		}
//		inStream.close();
//		reader.reset();
		final ParameterBlock pbjRead = new ParameterBlock();
		pbjRead.add(ImageIO.createImageInputStream(source));
		pbjRead.add(imageChoice);
		pbjRead.add(Boolean.FALSE);
		pbjRead.add(Boolean.FALSE);
		pbjRead.add(Boolean.FALSE);
		pbjRead.add(null);
		pbjRead.add(null);
		pbjRead.add(readP);
		pbjRead.add( readerSPI.createReaderInstance());
		final RenderedOp coverageRaster=JAI.create("ImageRead", pbjRead,
                        (RenderingHints) newHints);

		// /////////////////////////////////////////////////////////////////////
		//
		// BUILDING COVERAGE
		//
		// /////////////////////////////////////////////////////////////////////
                // I need to calculate a new transformation (raster2Model)
                // between the cropped image and the required
                // adjustedRequestEnvelope
                final int ssWidth = coverageRaster.getWidth();
                final int ssHeight = coverageRaster.getHeight();
                if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "Coverage read: width = " + ssWidth
                                        + " height = " + ssHeight);
                }

                // //
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
                // //
                final double scaleX = originalGridRange.getSpan(0) / (1.0 * ssWidth);
                final double scaleY = originalGridRange.getSpan(1) / (1.0 * ssHeight);
                final AffineTransform tempRaster2Model = new AffineTransform((AffineTransform) raster2Model);
                tempRaster2Model.concatenate(new AffineTransform(scaleX, 0, 0, scaleY, 0, 0));
                return createCoverage(coverageRaster, ProjectiveTransform.create((AffineTransform) tempRaster2Model));
	}

	/**
	 * Returns the geotiff metadata for this geotiff file.
	 * 
	 * @return the metadata
	 */
	public GeoTiffIIOMetadataDecoder getMetadata() {
		return metadata;
	}
	
	public String getCoverageName(){
		return coverageName;
	}

	@Override
	public String[] getMetadataNames() {
		return metadataNames().toArray(new String[0]);
	}
	/**
	 * Retrieve the getMetadataNames as a Set.
	 * <p>
	 * You can check this set for memebership; if a name
	 * is listed here it will be able to return a value
	 * for the getMetadataValue method.
	 * @return
	 */
	public Set<String> metadataNames(){
		HashSet<String> names = new HashSet<String>();
		
		return names;
	}
	/**
	 * Retrieve a metadata value for the provided name.
	 * <p>
	 * You can check the metadataNames for the set of valid
	 * names to use when calling this method.
	 */
	public String getMetadataValue(String name) {
		GeoTiffIIOMetadataDecoder decoder = getMetadata();

		return super.getMetadataValue(name);
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
        final SampleModel sm=image.getSampleModel();
        final ColorModel cm=image.getColorModel();
                final int numBands = sm.getNumBands();
                final GridSampleDimension[] bands = new GridSampleDimension[numBands];
                // setting bands names.
                
                Category nan = null;
                if (!Double.isInfinite(noData)){
                    nan = new Category(Vocabulary
                            .formatInternational(VocabularyKeys.NODATA), new Color[] { new Color(0, 0, 0, 0) }, NumberRange
                            .create(noData, noData), NumberRange
                            .create(noData, noData));
                }
                
                for (int i = 0; i < numBands; i++) {
                        final ColorInterpretation colorInterpretation=TypeMap.getColorInterpretation(cm, i);
                        if(colorInterpretation==null)
                               throw new IOException("Unrecognized sample dimension type");
                        Category[] categories = null;
                        if (nan!=null){
                            categories = new Category[]{nan};
                        }
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
}
