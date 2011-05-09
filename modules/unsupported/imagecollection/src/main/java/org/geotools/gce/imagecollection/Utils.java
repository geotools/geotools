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
package org.geotools.gce.imagecollection;

import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BandedSampleModel;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.Interpolation;
import javax.media.jai.RasterFactory;

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.referencing.factory.epsg.DisplayCRSAuthorityFactory;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.sun.media.jai.util.Rational;

/**
 * Sparse utilities for the various classes. 
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @author Daniele Romagnoli, GeoSolutions S.A.S.
 * 
 */
class Utils {
    
    final static String DEFAULT_IMAGE_PATH = "$_DEFAULT_$";
    
    final static CoordinateReferenceSystem DEFAULT_IMAGE_CRS = DisplayCRSAuthorityFactory.DISPLAY;
    
    final static String SEPARATOR = File.separator; 
    
    final static String CONFIG_FILE = "config.properties"; 
    
    final static RenderedImage DEFAULT_IMAGE;
    
    final static FilenameFilter FILE_FILTER = new FilenameFilter() {
        
        //TODO: ADD MORE
        public boolean accept(File dir, String name) {
            if (name.endsWith(".tif") || name.endsWith(".TIF")
                    || name.endsWith(".tiff") || name.endsWith(".TIFF")
                    || name.endsWith(".jpg") || name.endsWith(".JPG")
                    || name.endsWith(".jpeg") || name.endsWith(".JPEG")
                    || name.endsWith(".png") || name.endsWith(".PNG")) {
                return true;
            } else {
                final String fullPath = dir.getAbsolutePath() + SEPARATOR + name;
                final File file = new File(fullPath);
                if (file.isDirectory()){
                    return true;
                }
            }
            return false;
        }
    };
    
    /**
     * Recursively get a fileList from the specified startingDir, using the provided {@link FilenameFilter},
     * optionally stop at the first occurrence if the {@code stopAtFirst} flag is {@code true}.
     * @param startingDir
     * @param filter
     * @param stopAtFirst
     * @return
     * @throws FileNotFoundException
     */
    static List<File> getFileList(final File startingDir, final FilenameFilter fileFilter, final boolean stopAtFirst)
            throws FileNotFoundException {
        List<File> result = new ArrayList<File>();
        File[] filesAndDirs = startingDir.listFiles(fileFilter);
        List<File> filesDirs = Arrays.asList(filesAndDirs);
        for (File file : filesDirs) {
            if (!file.isFile()) {
                // must be a directory
                // recursive call!
                List<File> deeperList = getFileList(file, fileFilter, stopAtFirst);
                result.addAll(deeperList);
                if (stopAtFirst && !result.isEmpty()) {
                    return result;
                }
            } else {
                result.add(file); 
                if (stopAtFirst) {
                    return result;
                }
            }
        }
        return result;
    }
    
    static class ImageCollectionProperties {
        final static String COVERAGE_NAME = "coverageName";
        final static String DEFAULT_PATH = "defaultPath";
        final static String EXPAND_RGB = "expand";
        
    }
    
    static {
        final SampleModel sm = new BandedSampleModel(DataBuffer.TYPE_BYTE, 1, 1, 1);
        final ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        final WritableRaster raster = RasterFactory.createWritableRaster(sm, null);
        final BufferedImage sampleImage = new BufferedImage(cm, raster, false, null);
        DEFAULT_IMAGE = sampleImage;
    }
    
    final static AffineTransform IDENTITY = new AffineTransform(1, 0, 0, 1, -0.5, -0.5);
    
    final static AffineTransform2D IDENTITY_2D = new AffineTransform2D(IDENTITY);
    
    static URL checkSource(Object source) throws MalformedURLException, DataSourceException {
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
            if (sourceFile.isDirectory())
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
        final Iterator<ImageReader> readersIt = ImageIO.getImageReaders(inStream);
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
        return new Rectangle(0, 0, reader.getWidth(imageIndex),
                reader.getHeight(imageIndex));
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
            builder.append("Checking file:")
                    .append(FilenameUtils.getFullPath(file.getAbsolutePath()))
                    .append("\n");
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

    /** Logger for the {@link ImageCollectionReader} class. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(ImageCollectionReader.class.toString());

    /** Move to base utils */
    static Rectangle2D layoutHelper(RenderedImage source, float scaleX,
            float scaleY, float transX, float transY, Interpolation interp) {

        // Represent the scale factors as Rational numbers.
        // Since a value of 1.2 is represented as 1.200001 which
        // throws the forward/backward mapping in certain situations.
        // Convert the scale and translation factors to Rational numbers
        Rational scaleXRational = Rational.approximate(scaleX,
                Utils.rationalTolerance);
        Rational scaleYRational = Rational.approximate(scaleY,
                Utils.rationalTolerance);

        long scaleXRationalNum = (long) scaleXRational.num;
        long scaleXRationalDenom = (long) scaleXRational.denom;
        long scaleYRationalNum = (long) scaleYRational.num;
        long scaleYRationalDenom = (long) scaleYRational.denom;

        Rational transXRational = Rational.approximate(transX,
                Utils.rationalTolerance);
        Rational transYRational = Rational.approximate(transY,
                Utils.rationalTolerance);

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
        final Rectangle2D retValue = new Rectangle2D.Double();
        retValue.setFrame(l_x0, l_y0, l_x1 - l_x0 + 1, l_y1 - l_y0 + 1);
        return retValue;
    }

    /** Move to base utils */
    static float rationalTolerance = 0.000001F;
}
