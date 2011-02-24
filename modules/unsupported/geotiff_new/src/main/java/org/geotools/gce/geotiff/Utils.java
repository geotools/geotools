/**
 * 
 */
package org.geotools.gce.geotiff;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;
import org.opengis.referencing.datum.PixelInCell;

/**
 * Sparse utilities for the various classes. I use them to extract
 * complex code from other places.
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * 
 */
class Utils {
	   
	/**
	 * Logger.
	 */
	final static Logger LOGGER = org.geotools.util.logging.Logging
			.getLogger(Utils.class.toString());
	
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

	/**
	 * Default path behavior with respect to absolute paths.
	 */
	static final boolean DEFAULT_PATH_BEHAVIOR = false;

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
}
