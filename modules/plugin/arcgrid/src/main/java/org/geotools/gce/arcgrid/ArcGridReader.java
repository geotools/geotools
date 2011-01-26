/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.gce.arcgrid;

import it.geosolutions.imageio.plugins.arcgrid.AsciiGridsImageMetadata;
import it.geosolutions.imageio.plugins.arcgrid.spi.AsciiGridsImageReaderSpi;
import it.geosolutions.imageio.utilities.ImageIOUtilities;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.measure.unit.Unit;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.PrjFileReader;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.image.ImageUtilities;
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
import org.opengis.referencing.operation.TransformException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.vividsolutions.jts.io.InStream;

/**
 * This class can read an arc grid data source (ArcGrid or GRASS ASCII) and
 * create a {@link GridCoverage2D} from the data.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.3.x
 */
public final class ArcGridReader extends AbstractGridCoverage2DReader implements
		GridCoverageReader {
	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.gce.arcgrid");

	/** Caches and ImageReaderSpi for an AsciiGridsImageReader. */
	private final static ImageReaderSpi readerSPI = new AsciiGridsImageReaderSpi();

	/** No data value for this dataset. */
	private double inNoData = Double.NaN;

	/**
	 * Creates a new instance of an ArcGridReader basing the decision on whether
	 * the file is compressed or not. I assume nothing about file extension.
	 * 
	 * @param input
	 *            Source object for which we want to build an ArcGridReader.
	 * @throws DataSourceException
	 */
	public ArcGridReader(Object input) throws DataSourceException {
		this(input, null);

	}

	/**
	 * Creates a new instance of an ArcGridReader basing the decision on whether
	 * the file is compressed or not. I assume nothing about file extension.
	 * 
	 * @param input
	 *            Source object for which we want to build an ArcGridReader.
	 * @param hints
	 *            Hints to be used by this reader throughout his life.
	 * @throws DataSourceException
	 */
	public ArcGridReader(Object input, final Hints hints)
			throws DataSourceException {
	    super(input,hints);
		// /////////////////////////////////////////////////////////////////////
		//
		// Checking input
		//
		// /////////////////////////////////////////////////////////////////////
		coverageName = "AsciiGrid";
		try {

			// /////////////////////////////////////////////////////////////////////
			//
			// Source management
			//
			// /////////////////////////////////////////////////////////////////////
			checkSource(input,hints);

			//
			// CRS
			//
		        final Object tempCRS = this.hints.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
		        if (tempCRS != null) {
		            this.crs=(CoordinateReferenceSystem) tempCRS;
		            LOGGER.log(Level.WARNING,"Using default coordinate reference system ");
		        } else			
		            getCoordinateReferenceSystem();

			// /////////////////////////////////////////////////////////////////////
			//
			// Reader and metadata
			//
			// /////////////////////////////////////////////////////////////////////
			// //
			//
			// Getting a reader for this format
			//
			// //
			final ImageReader reader = readerSPI.createReaderInstance();
			reader.setInput(inStream);

			// //
			//
			// Getting metadata
			//
			// //
			final Object metadata = reader.getImageMetadata(0);
			if (!(metadata instanceof AsciiGridsImageMetadata))
				throw new DataSourceException(
						"Unexpected error! Metadata are not of the expected class.");
			// casting the metadata
			final AsciiGridsImageMetadata gridMetadata = (AsciiGridsImageMetadata) metadata;

			// /////////////////////////////////////////////////////////////////////
			//
			// Envelope and other metadata
			//
			// /////////////////////////////////////////////////////////////////////
			parseMetadata(gridMetadata);

			// /////////////////////////////////////////////////////////////////////
			//
			// Informations about multiple levels and such
			//
			// /////////////////////////////////////////////////////////////////////
			getResolutionInfo(reader);

			// release the stream if we can.
			finalStreamPreparation();
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

	/**
	 * Close the {@link InStream} {@link ImageInputStream} if we open it up on
	 * purpose to read header info for this {@link AbstractGridCoverage2DReader}.
	 * If the stream cannot be closed, we just reset and mark it.
	 * 
	 * @throws IOException
	 */
	private void finalStreamPreparation() throws IOException {
		if (closeMe)
			inStream.close();
		else {
			inStream.reset();
			inStream.mark();
		}
	}

	/**
	 * Checks the input provided to this {@link ArcGridReader} and sets all the
	 * other objects and flags accordingly.
	 * 
	 * @param input
	 *            provided to this {@link ArcGridReader}.
	 * @param hints
	 *            Hints to be used by this reader throughout his life.
	 * 
	 * @throws UnsupportedEncodingException
	 * @throws DataSourceException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void checkSource(Object input, final Hints hints)
			throws UnsupportedEncodingException, DataSourceException,
			IOException, FileNotFoundException {
	
		closeMe = true;
		// //
		//
		// URL to File
		//
		// //
		// if it is a URL pointing to a File I convert it to a file,
		// otherwise, later on, I will try to get an inputstream out of it.
		if (input instanceof URL) {
			// URL that point to a file
			final URL sourceURL = ((URL) input);
			if (sourceURL.getProtocol().compareToIgnoreCase("file") == 0) {
				this.source = input = DataUtilities.urlToFile(sourceURL);

			}
		}

		// //
		//
		// File
		//
		// //
		if (input instanceof File) {
			final File sourceFile = (File) input;
			if (!sourceFile.exists() || sourceFile.isDirectory()
					|| !sourceFile.canRead())
				throw new DataSourceException("Provided file does not exist or is a directory or is not readable!");
			this.coverageName = sourceFile.getName();
			final int dotIndex = coverageName.indexOf(".");
			gzipped = coverageName.toLowerCase().endsWith("gz");
			coverageName = (dotIndex == -1) ? coverageName : coverageName
					.substring(0, dotIndex);
			if(gzipped)
                            inStream = ImageIO.createImageInputStream(new GZIPInputStream(
                                new FileInputStream(sourceFile)));	
			else{
			    inStreamSPI=ImageUtilities.getImageInputStreamSPI(sourceFile);
			    if (inStreamSPI == null)
	                        throw new DataSourceException(
	                                        "No input stream for the provided source");
			    inStream = inStreamSPI.createInputStreamInstance(sourceFile, ImageIO.getUseCache(), ImageIO.getCacheDirectory());
			}
		} else
		// //
		//
		// URL
		//
		// //
		if (input instanceof URL) {
			final URL tempURL = ((URL) input);
			input = tempURL.openConnection().getInputStream();
			GZIPInputStream gzInStream = null;
			try {
				gzInStream = new GZIPInputStream((InputStream) input);
				gzipped = false;
			} catch (Exception e) {
				gzipped = false;
			}
			input = tempURL.openConnection().getInputStream();
			inStream = gzipped ? ImageIO.createImageInputStream(gzInStream)
					: ImageIO.createImageInputStream(tempURL.openConnection()
							.getInputStream());
		} else
		// //
		//
		// InputStream
		//
		// //
		if (input instanceof InputStream) {
			closeMe = false;
			if (ImageIO.getUseCache())
				inStream = new FileCacheImageInputStream((InputStream) input,
						null);
			else
				inStream = new MemoryCacheImageInputStream((InputStream) input);
			// let's mark it
			inStream.mark();
		} else
		// //
		//
		// ImageInputStream
		//
		// //
		if (input instanceof ImageInputStream) {
			closeMe = false;
			inStream = (ImageInputStream) input;
			inStream.mark();
		} else
			throw new IllegalArgumentException("Unsupported input type");

		if (inStream == null)
			throw new DataSourceException(
					"No input stream for the provided source");
	}

	/**
	 * Gets resolution information about the coverage itself.
	 * 
	 * @param reader
	 *            an {@link ImageReader} to use for getting the resolution
	 *            information.
	 * @throws IOException
	 * @throws TransformException
	 */
	private void getResolutionInfo(ImageReader reader) throws IOException,
			TransformException {

		// //
		//
		// get the dimension of the hr image and build the model as well as
		// computing the resolution
		// //
		final Rectangle actualDim = new Rectangle(0, 0, reader.getWidth(0),reader.getHeight(0));
		originalGridRange = new GridEnvelope2D(actualDim);

		// ///
		//
		// setting the higher resolution avalaible for this coverage
		//
		// ///
		highestRes = getResolution(originalEnvelope, actualDim, crs);

	}

	/**
	 * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
	 */
	public Format getFormat() {
		return new ArcGridFormat();
	}

	/**
	 * Reads a {@link GridCoverage2D} possibly matching as close as possible the
	 * resolution computed by using the input params provided by using the
	 * parameters for this {@link #read(GeneralParameterValue[])}.
	 * 
	 * <p>
	 * To have an idea about the possible read parameters take a look at
	 * {@link AbstractGridFormat} class and {@link ArcGridFormat} class.
	 * 
	 * @param params
	 *            an array of {@link GeneralParameterValue} containing the
	 *            parameters to control this read process.
	 * 
	 * @return a {@link GridCoverage2D}.
	 * 
	 * @see AbstractGridFormat
	 * @see ArcGridFormat
	 * @see org.opengis.coverage.grid.GridCoverageReader#read(org.opengis.parameter.GeneralParameterValue[])
	 */
	@SuppressWarnings("unchecked")
	public GridCoverage2D read(GeneralParameterValue[] params)
			throws IllegalArgumentException, IOException {
		GeneralEnvelope readEnvelope = null;
		Rectangle requestedDim = null;
		OverviewPolicy overviewPolicy=null;
		if (params != null) {
			final int length = params.length;
			for (int i = 0; i < length; i++) {
				final ParameterValue param = (ParameterValue) params[i];
				final String name = param.getDescriptor().getName().getCode();
				if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString())) {
					final GridGeometry2D gg = (GridGeometry2D) param.getValue();
					readEnvelope = new GeneralEnvelope((Envelope) gg.getEnvelope2D());
					requestedDim = gg.getGridRange2D().getBounds();
					continue;
				}
				if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName().toString())) {
					overviewPolicy=(OverviewPolicy) param.getValue();
				}
			}
		}
		return createCoverage(readEnvelope, requestedDim, overviewPolicy);
	}

	/**
	 * This method creates the GridCoverage2D from the underlying file.
	 * 
	 * @param requestedDim
	 * @param readEnvelope
	 * 
	 * 
	 * @return a GridCoverage
	 * 
	 * @throws java.io.IOException
	 */
	private GridCoverage2D createCoverage(GeneralEnvelope requestedEnvelope,
			Rectangle requestedDim, OverviewPolicy overviewPolicy) throws IOException {

		if (!closeMe) {

			inStream.reset();
			inStream.mark();
		}
		// /////////////////////////////////////////////////////////////////////
		//
		// Doing an image read for reading the coverage.
		//
		// /////////////////////////////////////////////////////////////////////

		// //
		//
		// Setting subsampling factors with some checkings
		// 1) the subsampling factors cannot be zero
		// 2) the subsampling factors cannot be such that the w or h are zero
		//
		// //
		final ImageReadParam readP = new ImageReadParam();
		final Integer imageChoice;
		try {
			imageChoice = setReadParams(overviewPolicy, readP,
					requestedEnvelope, requestedDim);
		} catch (IOException e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return null;
		} catch (TransformException e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return null;
		}

		// //
		//
		// image and metadata
		//
		// //
		final ParameterBlock pbjImageRead = new ParameterBlock();
		// prepare input to handle possible parallelism between different
		// readers
		if (source instanceof File) {
			if (!gzipped){
			    if(inStreamSPI!=null)
			        pbjImageRead.add(inStreamSPI.createInputStreamInstance(source, ImageIO.getUseCache(), ImageIO.getCacheDirectory()    ));
			    else
			        pbjImageRead.add(ImageIO.createImageInputStream(source));
			}
			else
				pbjImageRead.add(ImageIO
						.createImageInputStream(new GZIPInputStream(
								new FileInputStream((File) source))));
		} else if (source instanceof ImageInputStream
				|| source instanceof InputStream)
			pbjImageRead.add(inStream);
		else if (source instanceof URL) {
			if (gzipped)
				ImageIO.createImageInputStream(new GZIPInputStream(
						((URL) source).openConnection().getInputStream()));
			else
				pbjImageRead.add(ImageIO.createImageInputStream(((URL) source)
						.openConnection().getInputStream()));

		}
		pbjImageRead.add(imageChoice);
		pbjImageRead.add(Boolean.FALSE);
		pbjImageRead.add(Boolean.FALSE);
		pbjImageRead.add(Boolean.FALSE);
		pbjImageRead.add(null);
		pbjImageRead.add(null);
		pbjImageRead.add(readP);
		pbjImageRead.add(readerSPI.createReaderInstance());
		final RenderedOp asciiCoverage = JAI.create("ImageRead", pbjImageRead,hints);

		// //
		//
		// Creating the coverage
		//
		// //
		try {

			
			// //
			//
			// Categories
			//
			//
			// //
			Unit<?> uom = null;
			final Category nan;
			if (Double.isNaN(inNoData)) {
				nan = new Category(Vocabulary
						.formatInternational(VocabularyKeys.NODATA), new Color(
						0, 0, 0, 0), 0);

			} else {
				nan = new Category(Vocabulary
						.formatInternational(VocabularyKeys.NODATA),
						new Color[] { new Color(0, 0, 0, 0) }, 
						NumberRange.create(0, 0),
						NumberRange.create(inNoData, inNoData));
	

			}


			//
			// Sample dimension
			//
		        final SampleModel sm = asciiCoverage.getSampleModel();
		        final ColorModel cm = asciiCoverage.getColorModel();
	                final ColorInterpretation colorInterpretation=TypeMap.getColorInterpretation(cm, 0);
	                if(colorInterpretation==null)
	                       throw new IOException("Unrecognized sample dimension type");
	                
			final GridSampleDimension band = new GridSampleDimension(
					coverageName, new Category[] { nan }, uom).geophysics(true);
			final Map<String, Double> properties = new HashMap<String, Double>();
			properties.put("GC_NODATA", new Double(inNoData));

			//
			// Coverage
			//
			return coverageFactory.create(
			        coverageName, 
			        asciiCoverage,
			        originalEnvelope, 
			        new GridSampleDimension[] { band },
			        null,
			        properties);

		} catch (NoSuchElementException e) {
			if (LOGGER.isLoggable(Level.SEVERE))
				LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new DataSourceException(e);
		}
	}

	/**
	 * This method is responsible for building up an envelope according to the
	 * definition of the crs. It assumes that X coordinate on the ascii grid
	 * itself maps to longitude and y coordinate maps to latitude.
	 * 
	 * @param gridMetadata
	 *            The {@link AsciiGridsImageMetadata} to parse.
	 * 
	 * @throws MismatchedDimensionException
	 */
	private void parseMetadata(AsciiGridsImageMetadata gridMetadata)
			throws MismatchedDimensionException {

		// getting metadata
		final Node root = gridMetadata
				.getAsTree("it.geosolutions.imageio.plugins.arcgrid.AsciiGridsImageMetadata_1.0");

		// getting Grid Properties
		Node child = root.getFirstChild();
		NamedNodeMap attributes = child.getAttributes();
		final boolean grass = attributes.getNamedItem("GRASS").getNodeValue()
				.equalsIgnoreCase("True");

		// getting Grid Properties
		child = child.getNextSibling();
		attributes = child.getAttributes();
		final int hrWidth = Integer.parseInt(attributes.getNamedItem("nColumns").getNodeValue());
		final int hrHeight = Integer.parseInt(attributes.getNamedItem("nRows").getNodeValue());
		originalGridRange = new GridEnvelope2D(new Rectangle(0, 0, hrWidth,hrHeight));
		final boolean pixelIsArea = AsciiGridsImageMetadata.RasterSpaceType.valueOf(attributes.getNamedItem("rasterSpaceType").getNodeValue()).equals(AsciiGridsImageMetadata.RasterSpaceType.PixelIsArea);
		if (!grass)
			inNoData = Double.parseDouble(attributes.getNamedItem("noDataValue").getNodeValue());

		// getting Envelope Properties
		child = child.getNextSibling();
		attributes = child.getAttributes();
		final double cellsizeX = Double.parseDouble(attributes.getNamedItem(
				"cellsizeX").getNodeValue());
		final double cellsizeY = Double.parseDouble(attributes.getNamedItem(
				"cellsizeY").getNodeValue());
		double xll = Double.parseDouble(attributes.getNamedItem("xll")
				.getNodeValue());
		double yll = Double.parseDouble(attributes.getNamedItem("yll")
				.getNodeValue());

		// /////////////////////////////////////////////////////////////////////
		//
		// OGC specifications says that PixelIsArea map a pixel to the corner
		// of the grid while PixelIsPoint map a pixel to the centre of the grid.
		//
		// /////////////////////////////////////////////////////////////////////
		if (!pixelIsArea) {
			final double correctionX = cellsizeX / 2d;
			final double correctionY = cellsizeY / 2d;
			xll -= correctionX;
			yll -= correctionY;
		}

		originalEnvelope = new GeneralEnvelope(new double[] { xll, yll },
				new double[] { xll + (hrWidth * cellsizeX),
						yll + (hrHeight * cellsizeY) });

		// setting the coordinate reference system for the envelope
		originalEnvelope.setCoordinateReferenceSystem(crs);

	}

	/**
	 * Gets the coordinate system that will be associated to the
	 * {@link GridCoverage}. The WGS84 coordinate system is used by default. It
	 * is worth to point out that when reading from a stream which is not
	 * connected to a file, like from an http connection (e.g. from a WCS) we
	 * cannot rely on receiving a prj file too. In this case the exchange of
	 * information about referencing should proceed the exchange of data thus I
	 * rely on this and I ask the user who's invoking the read operation to
	 * provide me a valid crs and envelope through read parameters.
	 * 
	 * @throws FactoryException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void getCoordinateReferenceSystem() throws FileNotFoundException,
			IOException {

		// check to see if there is a projection file
		if (source instanceof File
				|| (source instanceof URL && (((URL) source).getProtocol() == "file"))) {
			// getting name for the prj file
			final String sourceAsString;

			if (source instanceof File)
				sourceAsString = ((File) source).getAbsolutePath();
			else
				sourceAsString = ((URL) source).getFile();

			int index = sourceAsString.lastIndexOf(".");
			final StringBuffer prjFileName;
			if (index == -1)
				prjFileName = new StringBuffer(sourceAsString);
			else
				prjFileName = new StringBuffer(sourceAsString.substring(0,
						index));
			prjFileName.append(".prj");

			// does it exist?
			final File prjFile = new File(prjFileName.toString());
			if (prjFile.exists()) {
				// it exists then we have top read it
				PrjFileReader projReader = null;
				try {
					FileChannel channel = new FileInputStream(prjFile)
							.getChannel();
					projReader = new PrjFileReader(channel);
					crs = projReader.getCoordinateReferenceSystem();
				} catch (FileNotFoundException e) {
					// warn about the error but proceed, it is not fatal
					// we have at least the default crs to use
					LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
				} catch (IOException e) {
					// warn about the error but proceed, it is not fatal
					// we have at least the default crs to use
					LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
				} catch (FactoryException e) {
					// warn about the error but proceed, it is not fatal
					// we have at least the default crs to use
					LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
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
		if (crs == null) {
			crs = AbstractGridFormat.getDefaultCRS();
			LOGGER.info( "Unable to find crs, continuing with default CRS" );
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
}
