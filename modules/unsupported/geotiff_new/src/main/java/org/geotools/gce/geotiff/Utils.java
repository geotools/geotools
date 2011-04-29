/**
 * 
 */
package org.geotools.gce.geotiff;

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageMetadata;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageWriterSpi;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.Interpolation;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffConstants;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataEncoder;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.PrjFileReader;
import org.geotools.data.WorldFileReader;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Parent;
import org.jdom.input.DOMBuilder;
import org.jdom.output.DOMOutputter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;

import com.sun.media.jai.util.Rational;

/**
 * Sparse utilities for the various classes. I use them to extract
 * complex code from other places.
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * 
 */
class Utils {
    	
	/**
	 * {@link AffineTransform} that can be used to go from an image datum placed
	 * at the center of pixels to one that is placed at ULC.
	 */
	final static AffineTransform CENTER_TO_CORNER = AffineTransform
			.getTranslateInstance(PixelTranslation
					.getPixelTranslation(PixelInCell.CELL_CORNER),
					PixelTranslation
							.getPixelTranslation(PixelInCell.CELL_CORNER));
	/**
	 * {@link AffineTransform} that can be used to go from an image datum placed
	 * at the ULC corner of pixels to one that is placed at center.
	 */
	final static AffineTransform CORNER_TO_CENTER = AffineTransform
			.getTranslateInstance(-PixelTranslation
					.getPixelTranslation(PixelInCell.CELL_CORNER),
					-PixelTranslation
							.getPixelTranslation(PixelInCell.CELL_CORNER));


	static URL checkSource(Object source) throws MalformedURLException,
			DataSourceException {
		URL sourceURL = null;
		// /////////////////////////////////////////////////////////////////////
		//
		// Check source
		//
		// /////////////////////////////////////////////////////////////////////
		// if it is a URL or a String let's try to see if we can get a file to
		// check if we have to build the index
		if (source instanceof URL) {
			sourceURL = ((URL) source);
			source = DataUtilities.urlToFile(sourceURL);
		} else if (source instanceof String) {
			// is it a File?
			final String tempSource = (String) source;
			File tempFile = new File(tempSource);
			if (!tempFile.exists()) {
				// is it a URL
				try {
					sourceURL = new URL(tempSource);
					source = DataUtilities.urlToFile(sourceURL);
				} catch (MalformedURLException e) {
					sourceURL = null;
					source = null;
				}
			} else {
				sourceURL = tempFile.toURI().toURL();
				source = tempFile;
			}
		} 

		// at this point we have tried to convert the thing to a File as hard as
		// we could, let's see what we can do
		if (source instanceof File) {
			final File sourceFile = (File) source;
			if (!sourceFile.isDirectory())
				sourceURL = ((File) source).toURI().toURL();
		} else
			sourceURL = null;
		return sourceURL;
	}

	/**
	 * Look for an {@link ImageReader} instance that is able to read the
	 * provided {@link ImageInputStream}, which must be non null.
	 * 
	 * <p>
	 * In case no reader is found, <code>null</code> is returned.
	 * 
	 * @param inStream
	 *            an instance of {@link ImageInputStream} for which we need to
	 *            find a suitable {@link ImageReader}.
	 * @return a suitable instance of {@link ImageReader} or <code>null</code>
	 *         if one cannot be found.
	 */
	static ImageReader getReader(final ImageInputStream inStream) {
	        Utilities.ensureNonNull("inStream", inStream);
		// get a reader
		inStream.mark();
		final Iterator<ImageReader> readersIt = ImageIO
				.getImageReaders(inStream);
		if (!readersIt.hasNext()) {
			return null;
		}
		return readersIt.next();
	}

	/**
	 * Retrieves the dimensions of the {@link RenderedImage} at index
	 * <code>imageIndex</code> for the provided {@link ImageReader} and
	 * {@link ImageInputStream}.
	 * 
	 * <p>
	 * Notice that none of the input parameters can be <code>null</code> or a
	 * {@link NullPointerException} will be thrown. Morevoer the
	 * <code>imageIndex</code> cannot be negative or an
	 * {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param imageIndex
	 *            the index of the image to get the dimensions for.
	 * @param inStream
	 *            the {@link ImageInputStream} to use as an input
	 * @param reader
	 *            the {@link ImageReader} to decode the image dimensions.
	 * @return a {@link Rectangle} that contains the dimensions for the image at
	 *         index <code>imageIndex</code>
	 * @throws IOException
	 *             in case the {@link ImageReader} or the
	 *             {@link ImageInputStream} fail.
	 */
	static Rectangle getDimension(final int imageIndex,
			final ImageInputStream inStream, final ImageReader reader)
			throws IOException {
	        Utilities.ensureNonNull("inStream", inStream);
	        Utilities.ensureNonNull("reader", reader);
		if (imageIndex < 0)
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.INDEX_OUT_OF_BOUNDS_$1, imageIndex));
		inStream.reset();
		reader.setInput(inStream);
		return new Rectangle(0, 0, reader.getWidth(imageIndex), reader
				.getHeight(imageIndex));
	}

	/**
	 * Retrieves an {@link ImageInputStream} for the provided input {@link File}
	 * .
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	static ImageInputStream getInputStream(final File file) throws IOException {
		final ImageInputStream inStream = ImageIO.createImageInputStream(file);
		if (inStream == null)
			return null;
		return inStream;
	}

	/**
	 * Checks that the provided <code>dimensions</code> when intersected with
	 * the source region used by the provided {@link ImageReadParam} instance
	 * does not result in an empty {@link Rectangle}.
	 * 
	 * <p>
	 * Input parameters cannot be null.
	 * 
	 * @param readParameters
	 *            an instance of {@link ImageReadParam} for which we want to
	 *            check the source region element.
	 * @param dimensions
	 *            an instance of {@link Rectangle} to use for the check.
	 * @return <code>true</code> if the intersection is not empty,
	 *         <code>false</code> otherwise.
	 */
	static boolean checkEmptySourceRegion(final ImageReadParam readParameters,
			final Rectangle dimensions) {
	        Utilities.ensureNonNull("readDimension", dimensions);
	        Utilities.ensureNonNull("readP", readParameters);
		final Rectangle sourceRegion = readParameters.getSourceRegion();
		Rectangle.intersect(sourceRegion, dimensions, sourceRegion);
		if (sourceRegion.isEmpty())
			return true;
		readParameters.setSourceRegion(sourceRegion);
		return false;
	}

	/**
	 * Checks that a {@link File} is a real file, exists and is readable.
	 * 
	 * @param file
	 *            the {@link File} instance to check. Must not be null.
	 * 
	 * @return <code>true</code> in case the file is a real file, exists and is
	 *         readable; <code>false </code> otherwise.
	 */
	static boolean checkFileReadable(final File file) {
		if (LOGGER.isLoggable(Level.FINE)) {
			final StringBuilder builder = new StringBuilder();
			builder.append("Checking file:").append(
					FilenameUtils.getFullPath(file.getAbsolutePath())).append(
					"\n");
			builder.append("canRead:").append(file.canRead()).append("\n");
			builder.append("isHidden:").append(file.isHidden()).append("\n");
			builder.append("isFile").append(file.isFile()).append("\n");
			builder.append("canWrite").append(file.canWrite()).append("\n");
			LOGGER.fine(builder.toString());
		}
		if (!file.exists() || !file.canRead() || !file.isFile())
			return false;
		return true;
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

    static CoordinateReferenceSystem getCRS(Object source) {
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

    /** Logger for the {@link GeoTiffReader} class. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeoTiffReader.class.toString());


    /**
     * Creates image metadata which complies to the GeoTIFFWritingUtilities
     * specification for the given image writer, image type and
     * GeoTIFFWritingUtilities metadata.
     * 
     * @param writer
     *            the image writer, must not be null
     * @param type
     *            the image type, must not be null
     * @param geoTIFFMetadata
     *            the GeoTIFFWritingUtilities metadata, must not be null
     * @param params
     * @return the image metadata, never null
     * @throws IIOException
     *             if the metadata cannot be created
     */
    public final static IIOMetadata createGeoTiffIIOMetadata(
    		ImageWriter writer, ImageTypeSpecifier type,
    		GeoTiffIIOMetadataEncoder geoTIFFMetadata, ImageWriteParam params)
    		throws IIOException {
    	IIOMetadata imageMetadata = writer
    			.getDefaultImageMetadata(type, params);
    	imageMetadata = writer
    			.convertImageMetadata(imageMetadata, type, params);
    	org.w3c.dom.Element w3cElement = (org.w3c.dom.Element) imageMetadata
    			.getAsTree(GeoTiffConstants.GEOTIFF_IIO_METADATA_FORMAT_NAME);
    	final Element element = new DOMBuilder().build(w3cElement);
    
    	geoTIFFMetadata.assignTo(element);
    
    	final Parent parent = element.getParent();
    	parent.removeContent(element);
    
    	final Document document = new Document(element);
    
    	try {
    		final org.w3c.dom.Document w3cDoc = new DOMOutputter()
    				.output(document);
    		final IIOMetadata iioMetadata = new TIFFImageMetadata(
    				TIFFImageMetadata.parseIFD(w3cDoc.getDocumentElement()
    						.getFirstChild()));
    		imageMetadata = iioMetadata;
    	} catch (JDOMException e) {
    		throw new IIOException(
    				"Failed to set GeoTIFFWritingUtilities specific tags.", e);
    	} catch (IIOInvalidTreeException e) {
    		throw new IIOException(
    				"Failed to set GeoTIFFWritingUtilities specific tags.", e);
    	}
    
    	return imageMetadata;
    }

    /** factory for getting tiff writers. */
    final static TIFFImageWriterSpi TIFFWRITERFACTORY = new TIFFImageWriterSpi();
    /** SPI for creating tiff readers in ImageIO tools */
    final static TIFFImageReaderSpi TIFFREADERFACTORY = new TIFFImageReaderSpi();


    /** Move to base utils*/
    static Rectangle2D layoutHelper(RenderedImage source,
                                        float scaleX,
                                        float scaleY,
                                        float transX,
                                        float transY,
                                        Interpolation interp) {
    
    // Represent the scale factors as Rational numbers.
    	// Since a value of 1.2 is represented as 1.200001 which
    	// throws the forward/backward mapping in certain situations.
    	// Convert the scale and translation factors to Rational numbers
    	Rational scaleXRational = Rational.approximate(scaleX,Utils.rationalTolerance);
    	Rational scaleYRational = Rational.approximate(scaleY,Utils.rationalTolerance);
    
    	long scaleXRationalNum = (long) scaleXRational.num;
    	long scaleXRationalDenom = (long) scaleXRational.denom;
    	long scaleYRationalNum = (long) scaleYRational.num;
    	long scaleYRationalDenom = (long) scaleYRational.denom;
    
    	Rational transXRational = Rational.approximate(transX,Utils.rationalTolerance);
    	Rational transYRational = Rational.approximate(transY,Utils.rationalTolerance);
    
    	long transXRationalNum = (long) transXRational.num;
    	long transXRationalDenom = (long) transXRational.denom;
    	long transYRationalNum = (long) transYRational.num;
    	long transYRationalDenom = (long) transYRational.denom;
    
    	int x0 = source.getMinX();
    	int y0 = source.getMinY();
    	int w = source.getWidth();
    	int h = source.getHeight();
    
    	// Variables to store the calculated destination upper left coordinate
    	long dx0Num, dx0Denom, dy0Num, dy0Denom;
    
    	// Variables to store the calculated destination bottom right
    	// coordinate
    	long dx1Num, dx1Denom, dy1Num, dy1Denom;
    
    	// Start calculations for destination
    
    	dx0Num = x0;
    	dx0Denom = 1;
    
    	dy0Num = y0;
    	dy0Denom = 1;
    
    	// Formula requires srcMaxX + 1 = (x0 + w - 1) + 1 = x0 + w
    	dx1Num = x0 + w;
    	dx1Denom = 1;
    
    	// Formula requires srcMaxY + 1 = (y0 + h - 1) + 1 = y0 + h
    	dy1Num = y0 + h;
    	dy1Denom = 1;
    
    	dx0Num *= scaleXRationalNum;
    	dx0Denom *= scaleXRationalDenom;
    
    	dy0Num *= scaleYRationalNum;
    	dy0Denom *= scaleYRationalDenom;
    
    	dx1Num *= scaleXRationalNum;
    	dx1Denom *= scaleXRationalDenom;
    
    	dy1Num *= scaleYRationalNum;
    	dy1Denom *= scaleYRationalDenom;
    
    	// Equivalent to subtracting 0.5
    	dx0Num = 2 * dx0Num - dx0Denom;
    	dx0Denom *= 2;
    
    	dy0Num = 2 * dy0Num - dy0Denom;
    	dy0Denom *= 2;
    
    	// Equivalent to subtracting 1.5
    	dx1Num = 2 * dx1Num - 3 * dx1Denom;
    	dx1Denom *= 2;
    
    	dy1Num = 2 * dy1Num - 3 * dy1Denom;
    	dy1Denom *= 2;
    
    	// Adding translation factors
    
    	// Equivalent to float dx0 += transX
    	dx0Num = dx0Num * transXRationalDenom + transXRationalNum * dx0Denom;
    	dx0Denom *= transXRationalDenom;
    
    	// Equivalent to float dy0 += transY
    	dy0Num = dy0Num * transYRationalDenom + transYRationalNum * dy0Denom;
    	dy0Denom *= transYRationalDenom;
    
    	// Equivalent to float dx1 += transX
    	dx1Num = dx1Num * transXRationalDenom + transXRationalNum * dx1Denom;
    	dx1Denom *= transXRationalDenom;
    
    	// Equivalent to float dy1 += transY
    	dy1Num = dy1Num * transYRationalDenom + transYRationalNum * dy1Denom;
    	dy1Denom *= transYRationalDenom;
    
    	// Get the integral coordinates
    	int l_x0, l_y0, l_x1, l_y1;
    
    	l_x0 = Rational.ceil(dx0Num, dx0Denom);
    	l_y0 = Rational.ceil(dy0Num, dy0Denom);
    
    	l_x1 = Rational.ceil(dx1Num, dx1Denom);
    	l_y1 = Rational.ceil(dy1Num, dy1Denom);
    
    	// Set the top left coordinate of the destination
    	final Rectangle2D retValue= new Rectangle2D.Double();
    	retValue.setFrame(l_x0, l_y0, l_x1 - l_x0 + 1, l_y1 - l_y0 + 1);
    	return retValue;
    }

    /** Move to base utils*/
    static float rationalTolerance = 0.000001F;
}
