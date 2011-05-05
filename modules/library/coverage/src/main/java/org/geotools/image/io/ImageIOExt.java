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

import javax.imageio.ImageIO;
import javax.imageio.stream.FileCacheImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

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
