/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderWriterSpi;
import javax.imageio.stream.FileCacheImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.geotools.resources.Classes;

import com.sun.media.imageioimpl.common.PackageUtil;

/**
 * Provides an alternative source of image input and output streams that uses optimized behavior.
 * <p>
 * Currently implemented optimizations:
 * <ul>
 * <li>wrap an OutputStream into a {@link MemoryCacheImageOutputStream} or a
 * {@link FileCacheImageOutputStream} based on a image size threshold</li>
 * </ul>
 * </p>
 * 
 * @author Andrea Aime - GeoSolutions
 * @since 2.7.2
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/coverage/src/main/java/org/geotools/image/io/ImageIOExt.java $
 */
public class ImageIOExt {

    static Long filesystemThreshold = null;

    static File cacheDirectory = null;

    /**
     * Builds a {@link ImageOutputStream} writing to <code>destination</code>, based on logic that
     * involves the image size
     * 
     * @param image
     *            the image to be written on the destination (can be null)
     * @param destination
     *            the destination
     * @return
     * @throws IOException
     */
    public static ImageOutputStream createImageOutputStream(RenderedImage image, Object destination)
            throws IOException {
        // already what we need?
        if (destination instanceof ImageOutputStream) {
            return (ImageOutputStream) destination;
        }

        // generate the ImageOutputStream
        if (destination instanceof OutputStream && filesystemThreshold != null && image != null) {
            OutputStream stream = (OutputStream) destination;

            // if going to wrap a output stream and we have a threshold set
            long imageSize = computeImageSize(image);
            if (imageSize > filesystemThreshold) {
                File cacheDirectory = getCacheDirectory();
                return new FileCacheImageOutputStream(stream, cacheDirectory);
            } else {
                return new MemoryCacheImageOutputStream(stream);
            }
        } else {
            return ImageIO.createImageOutputStream(destination);
        }
    }

    /**
     * Returns a {@link ImageOutputStream} suitable for writing on the specified <code>input</code>
     * 
     * @param destination
     * @return
     * @throws IOException
     */
    public static ImageInputStream createImageInputStream(Object input) throws IOException {
        return ImageIO.createImageInputStream(input);
    }

    /**
     * Returns the cache directory used by ImageIOExt, either the manually configured one, or the
     * result of calling {@link ImageIO#getCacheDirectory()}
     */
    public static File getCacheDirectory() {
        File cacheDir = cacheDirectory;
        if (cacheDir == null) {
            cacheDir = ImageIO.getCacheDirectory();
        }
        return cacheDir;
    }

    /**
     * Sets the directory where cache files are to be created. If set to null (the default value)
     * {@link ImageIO#getCacheDirectory()} will be used as the value
     * 
     * @param cacheDirectory
     *            a <code>File</code> specifying a directory.
     */
    public static void setCacheDirectory(File cache) {
        ImageIOExt.cacheDirectory = cache;
    }

    /**
     * The threshold at which the class will flip from {@link MemoryCacheImageOutputStream} to
     * {@link FileCacheImageOutputStream}. If the in memory, uncompressed image size is lower than
     * the threshold a {@link MemoryCacheImageOutputStream} will be returned, otherwise a
     * {@link FileCacheImageOutputStream} will be used instead
     * 
     * @return
     */
    public static Long getFilesystemThreshold() {
        return filesystemThreshold;
    }

    /**
     * Sets the memory/file usage threshold (or null to have the code fall back on ImageIO behavior)
     * 
     * @param filesystemThreshold
     * @see #getFilesystemThreshold()
     */
    public static void setFilesystemThreshold(Long filesystemThreshold) {
        ImageIOExt.filesystemThreshold = filesystemThreshold;
    }
    
    /**
     * Allows or disallows native acceleration for the specified image format. By default, the
     * image I/O extension for JAI provides native acceleration for PNG and JPEG. Unfortunatly,
     * those native codec has bug in their 1.0 version. Invoking this method will force the use
     * of standard codec provided in J2SE 1.4.
     * <p>
     * <strong>Implementation note:</strong> the current implementation assume that JAI codec
     * class name start with "CLib". It work for Sun's 1.0 implementation, but may change in
     * future versions. If this method doesn't recognize the class name, it does nothing.
     *
     * @param format The format name (e.g. "png").
     * @param category {@code ImageReaderSpi.class} to set the reader, or
     *        {@code ImageWriterSpi.class} to set the writer.
     * @param allowed {@code false} to disallow native acceleration.
     */
    public static synchronized <T extends ImageReaderWriterSpi> void allowNativeCodec(
            final String format, final Class<T> category, final boolean allowed)
    {
        T standard = null;
        T codeclib = null;
        final IIORegistry registry = IIORegistry.getDefaultInstance();
        for (final Iterator<T> it = registry.getServiceProviders(category, false); it.hasNext();) {
            final T provider = it.next();
            final String[] formats = provider.getFormatNames();
            for (int i=0; i<formats.length; i++) {
                if (formats[i].equalsIgnoreCase(format)) {
                    if (Classes.getShortClassName(provider).startsWith("CLib")) {
                        codeclib = provider;
                    } else {
                        standard = provider;
                    }
                    break;
                }
            }
        }
        if (standard!=null && codeclib!=null) {
            if (allowed) {
                registry.setOrdering(category, codeclib, standard);
            } else {
                registry.setOrdering(category, standard, codeclib);
            }
        }
    }
    
    /**
     * Get a proper {@link ImageInputStreamSpi} instance for the provided {@link Object} input without
     * trying to create an {@link ImageInputStream}.
     *  
     * @see #getImageInputStreamSPI(Object, boolean) 
     */
    public final static ImageInputStreamSpi getImageInputStreamSPI(final Object input) {
        return getImageInputStreamSPI(input, true);
    }

    /**
     * Get a proper {@link ImageInputStreamSpi} instance for the provided {@link Object} input.
     *   
     * @param input the input object for which we need to find a proper {@link ImageInputStreamSpi} instance
     * @param streamCreationCheck if <code>true</code>, when a proper {@link ImageInputStreamSpi} have been found 
     * for the provided input, use it to try creating an {@link ImageInputStream} on top of the input.  
     * 
     * @return an {@link ImageInputStreamSpi} instance.
     */
    public final static ImageInputStreamSpi getImageInputStreamSPI(final Object input, final boolean streamCreationCheck) {
    
        Iterator<ImageInputStreamSpi> iter;
        // Ensure category is present
        try {
            iter = IIORegistry.getDefaultInstance().getServiceProviders(ImageInputStreamSpi.class,
                    true);
        } catch (IllegalArgumentException e) {
            return null;
        }
    
        boolean usecache = ImageIO.getUseCache();
    
        ImageInputStreamSpi spi = null;
        while (iter.hasNext()) {
            spi = (ImageInputStreamSpi) iter.next();
            if (spi.getInputClass().isInstance(input)) {
                
                // Stream creation check
                if (streamCreationCheck){
                    ImageInputStream stream = null;
                    try {
                        stream = spi.createInputStreamInstance(input, usecache, ImageIO.getCacheDirectory());
                        break;
                    } catch (IOException e) {
                        return null;
                    } finally {
                        //Make sure to close the created stream
                        if (stream != null){
                            try {
                                stream.close();
                            } catch (Throwable t){
                                //eat exception
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        }
    
        return spi;
    }
    
    /**
     * Tells me whether or not the native libraries for JAI/ImageIO are active or not.
     * 
     * @return <code>false</code> in case the JAI/ImageIO native libs are not in the path, <code>true</code> otherwise.
     */
    public static boolean isCLibAvailable() {
        return PackageUtil.isCodecLibAvailable();
    }

    

    /**
     * Computes the image size based on the SampleModel and image dimension
     * 
     * @param image
     * @return
     */
    static long computeImageSize(RenderedImage image) {
        long bits = 0;
        final int bands = image.getSampleModel().getNumBands();
        for (int i = 0; i < bands; i++) {
            bits += image.getSampleModel().getSampleSize(i);
        }
        return (long) Math.ceil(bits / 8) * image.getWidth() * image.getHeight();
    }
}
