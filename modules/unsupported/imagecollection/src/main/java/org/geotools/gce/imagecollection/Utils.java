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

import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi;

import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
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
import javax.media.jai.RasterFactory;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.referencing.factory.epsg.CartesianAuthorityFactory;
import org.geotools.referencing.factory.epsg.DisplayCRSAuthorityFactory;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.Utilities;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Sparse utilities for the various classes. 
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @author Daniele Romagnoli, GeoSolutions S.A.S.
 * 
 */
class Utils {
    
    final static String FAKE_IMAGE_PATH = "$_!FAKE!_$";
    
    final static CoordinateReferenceSystem DISPLAY_CRS = DisplayCRSAuthorityFactory.DISPLAY;
    
    final static CoordinateReferenceSystem GENERIC2D_CRS = CartesianAuthorityFactory.GENERIC_2D;
    
    final static String SEPARATOR = File.separator; 
    
    final static String CONFIG_FILE = "config.properties"; 
    
    final static RenderedImage DEFAULT_IMAGE;
    
    final static TIFFImageReaderSpi TIFF_SPI = new TIFFImageReaderSpi();
    
    final static int IMAGE_EPSG = 404000;
    
    final static FilenameFilter FILE_FILTER = new FilenameFilter() {
        
        //TODO: ADD MORE
        public boolean accept(File dir, String name) {
            if (name.endsWith(".tif") || name.endsWith(".TIF")
                    || name.endsWith(".tiff") || name.endsWith(".TIFF")
                    || name.endsWith(".jpg") || name.endsWith(".JPG")
                    || name.endsWith(".jpeg") || name.endsWith(".JPEG")
                    || name.endsWith(".bmp") || name.endsWith(".BMP")
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
        final static String EPSG_CODE = "epsg";
        final static String TIME_BETWEEN_CHECKS = "timeBetweenChecks";
        final static String MAX_WIDTH = "maxWidth";
        final static String MAX_HEIGHT = "maxHeight";
        final static String ENVELOPE = "envelope";
        
    }
    
    static {
        final SampleModel sm = new BandedSampleModel(DataBuffer.TYPE_BYTE, 1, 1, 1);
        final ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        final WritableRaster raster = RasterFactory.createWritableRaster(sm, null);
        final BufferedImage sampleImage = new BufferedImage(cm, raster, false, null);
        DEFAULT_IMAGE = sampleImage;
    }
    
    final static AffineTransform IDENTITY = new AffineTransform(1, 0, 0, 1, -0.5, -0.5);
    
    final static AffineTransform IDENTITY_FLIP = new AffineTransform(1, 0, 0, -1, -0.5, -0.5);
    
    final static AffineTransform2D IDENTITY_2D = new AffineTransform2D(IDENTITY);
    
    final static AffineTransform2D IDENTITY_2D_FLIP = new AffineTransform2D(IDENTITY_FLIP);
    
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
    static float rationalTolerance = 0.000001F;

    final static String PATH_KEY = "PATH";

    ReadType DEFAULT_READ_TYPE = AbstractGridFormat.USE_JAI_IMAGEREAD
    .getDefaultValue() ? ReadType.JAI_IMAGEREAD : ReadType.DIRECT_READ;
}
