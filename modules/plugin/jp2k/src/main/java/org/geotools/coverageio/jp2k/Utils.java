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

import it.geosolutions.imageio.imageioimpl.imagereadmt.CloneableImageReadParam;
import it.geosolutions.imageio.imageioimpl.imagereadmt.DefaultCloneableImageReadParam;
import it.geosolutions.imageio.stream.input.FileImageInputStreamExt;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;
import org.opengis.geometry.BoundingBox;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.referencing.datum.PixelInCell;

/**
 * Sparse utilities for the various classes. I use them to extract
 * complex code from other places.
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @author Daniele Romagnoli, GeoSolutions S.A.S.
 */
class Utils {
	
	final static IOFileFilter FILEFILTER = createFilter();
	
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
			    	sourceURL =  DataUtilities.fileToURL(tempFile); 
				source = tempFile;
			}
		} else if (source instanceof FileImageInputStreamExt) {
            final File inputFile = ((FileImageInputStreamExt) source).getFile();
            source = inputFile;
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
	 * Builds a {@link ReferencedEnvelope} from a {@link GeographicBoundingBox}.
	 * This is useful in order to have an implementation of {@link BoundingBox}
	 * from a {@link GeographicBoundingBox} which strangely does implement
	 * {@link GeographicBoundingBox}.
	 * 
	 * @param geographicBBox
	 *            the {@link GeographicBoundingBox} to convert.
	 * @return an instance of {@link ReferencedEnvelope}.
	 */
	static ReferencedEnvelope getReferencedEnvelopeFromGeographicBoundingBox(
			final GeographicBoundingBox geographicBBox) {
		Utilities.ensureNonNull("GeographicBoundingBox", geographicBBox);
		return new ReferencedEnvelope(geographicBBox.getEastBoundLongitude(),
				geographicBBox.getWestBoundLongitude(), geographicBBox
						.getSouthBoundLatitude(), geographicBBox
						.getNorthBoundLatitude(), DefaultGeographicCRS.WGS84);
	}

	static ImageReadParam cloneImageReadParam(ImageReadParam param) {

		// The ImageReadParam passed in is non-null. As the
		// ImageReadParam class is not Cloneable, if the param
		// class is simply ImageReadParam, then create a new
		// ImageReadParam instance and set all its fields
		// which were set in param. This will eliminate problems
		// with concurrent modification of param for the cases
		// in which there is not a special ImageReadparam used.

		// Create a new ImageReadParam instance.
		CloneableImageReadParam newParam = new DefaultCloneableImageReadParam();

		// Set all fields which need to be set.

		// IIOParamController field.
		if (param.hasController()) {
			newParam.setController(param.getController());
		}

		// Destination fields.
		newParam.setDestination(param.getDestination());
		if (param.getDestinationType() != null) {
			// Set the destination type only if non-null as the
			// setDestinationType() clears the destination field.
			newParam.setDestinationType(param.getDestinationType());
		}
		newParam.setDestinationBands(param.getDestinationBands());
		newParam.setDestinationOffset(param.getDestinationOffset());

		// Source fields.
		newParam.setSourceBands(param.getSourceBands());
		newParam.setSourceRegion(param.getSourceRegion());
		if (param.getSourceMaxProgressivePass() != Integer.MAX_VALUE) {
	        newParam.setSourceProgressivePasses(
	            param.getSourceMinProgressivePass(),
	            param.getSourceNumProgressivePasses());
		}
		if (param.canSetSourceRenderSize()) {
			newParam.setSourceRenderSize(param.getSourceRenderSize());
		}
	    newParam.setSourceSubsampling(param.getSourceXSubsampling(),
	                                  param.getSourceYSubsampling(),
	                                  param.getSubsamplingXOffset(),
	                                  param.getSubsamplingYOffset());

		// Replace the local variable with the new ImageReadParam.
		return newParam;

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
//		inStream.mark();
		try {
			if (inStream instanceof FileImageInputStreamExt){
				final File file = ((FileImageInputStreamExt)inStream).getFile();
				if (FILEFILTER.accept(file))
					return JP2KFormatFactory.getCachedSpi().createReaderInstance();
			}
			return null;
			
		} catch (IOException e) {
			if (LOGGER.isLoggable(Level.WARNING))
					LOGGER.warning(e.getLocalizedMessage());
			return null;
		}
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
	 * Default priority for the underlying {@link Thread}.
	 */
	public static final int DEFAULT_PRIORITY = Thread.NORM_PRIORITY;

	/**
	 * Setup a double from an array of bytes.
	 * @param bytes
	 * @param start
	 * @return
	 */
	public static double bytes2double (final byte[] bytes, final int start) {
		int i = 0;
		final int length = 8;
		int count = 0;
		final byte[] tmp = new byte[length];
		for (i = start; i < (start + length); i++) {
			tmp[count] = bytes[i];
			count++;
		}
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 64; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return Double.longBitsToDouble(accum);
	}
	
	/**
	 * Setup a long from an array of bytes.
	 * @param bytes
	 * @param start
	 * @return
	 */
	public static long bytes2long (final byte[] bytes, final int start) {
		int i = 0;
		final int length = 4;
		int count = 0;
		final byte[] tmp = new byte[length];
		for (i = start; i < (start + length); i++) {
			tmp[count] = bytes[i];
			count++;
		}
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return accum;
	}
	
    private static IOFileFilter createFilter() {
        IOFileFilter fileFilter = Utils.includeFilters(
                FileFilterUtils.suffixFileFilter("jp2"), 
                FileFilterUtils.suffixFileFilter("JP2"), 
                FileFilterUtils.suffixFileFilter("j2c"),
                FileFilterUtils.suffixFileFilter("J2C"), 
                FileFilterUtils.suffixFileFilter("jpx"), 
                FileFilterUtils.suffixFileFilter("JPX"), 
                FileFilterUtils.suffixFileFilter("jp2k"), 
                FileFilterUtils.suffixFileFilter("JP2K"), 
                FileFilterUtils.nameFileFilter("jpeg2000"));
        return fileFilter;
    }

    static IOFileFilter excludeFilters(final IOFileFilter inputFilter, IOFileFilter... filters) {
        IOFileFilter retFilter = inputFilter;
        for (IOFileFilter filter : filters) {
            retFilter = FileFilterUtils.andFileFilter(retFilter, FileFilterUtils.notFileFilter(filter));
        }
        return retFilter;
    }

    static IOFileFilter includeFilters(final IOFileFilter inputFilter, IOFileFilter... filters) {
        IOFileFilter retFilter = inputFilter;
        for (IOFileFilter filter : filters) {
            retFilter = FileFilterUtils.orFileFilter(retFilter, filter);
        }
        return retFilter;
    }
}
